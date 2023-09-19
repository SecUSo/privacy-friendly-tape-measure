package org.secuso.privacyfriendlycameraruler.backup

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.JsonReader
import android.util.Log
import androidx.annotation.NonNull
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil
import org.secuso.privacyfriendlybackup.api.backup.FileUtil
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer
import org.secuso.privacyfriendlycameraruler.database.PFASQLiteHelper
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.exitProcess


class BackupRestorer : IBackupRestorer {
    @Throws(IOException::class)
    private fun readDatabase(reader: JsonReader, context: Context) {
        reader.beginObject()
        val n1: String = reader.nextName()
        if (n1 != "version") {
            throw RuntimeException("Unknown value $n1")
        }
        val version: Int = reader.nextInt()
        val n2: String = reader.nextName()
        if (n2 != "content") {
            throw RuntimeException("Unknown value $n2")
        }

        Log.d(TAG, "Restoring database...")
        val restoreDatabaseName = "restoreDatabase"

        // delete if file already exists
        val restoreDatabaseFile = context.getDatabasePath(restoreDatabaseName)
        if (restoreDatabaseFile.exists()) {
            DatabaseUtil.deleteRoomDatabase(context, restoreDatabaseName)
        }

        // create new restore database
        val db = DatabaseUtil.getSupportSQLiteOpenHelper(context, restoreDatabaseName, version).writableDatabase

        db.beginTransaction()
        db.version = version

        Log.d(TAG, "Copying database contents...")
        DatabaseUtil.readDatabaseContent(reader, db)
        Log.d(TAG, "succesfully read database")
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()

        reader.endObject()

        // copy file to correct location
        val actualDatabaseFile = context.getDatabasePath(PFASQLiteHelper.DATABASE_NAME)

        DatabaseUtil.deleteRoomDatabase(context, PFASQLiteHelper.DATABASE_NAME)

        FileUtil.copyFile(restoreDatabaseFile, actualDatabaseFile)
        Log.d(TAG, "Database Restored")

        // delete restore database
        DatabaseUtil.deleteRoomDatabase(context, restoreDatabaseName)
    }

    @Throws(IOException::class)
    private fun readPreferences(reader: JsonReader, preferences: SharedPreferences.Editor) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            Log.d("preference", name)
            when (name) {
                "pref_leftruler",
                "pref_units_of_measurement",
                "pref_rightruler" -> preferences.putString(name, reader.nextString())
                "pref_type_selection" -> preferences.putStringSet(name, readPreferenceSet(reader))
                else -> throw RuntimeException("Unknown preference $name")
            }
        }
        reader.endObject()
    }

    private fun readPreferenceSet(reader: JsonReader): Set<String> {
        val preferenceSet = mutableSetOf<String>()

        reader.beginArray()
        while (reader.hasNext()) {
            preferenceSet.add(reader.nextString());
        }
        reader.endArray()
        return preferenceSet
    }

    override fun restoreBackup(context: Context, restoreData: InputStream): Boolean {
        return try {
            val isReader = InputStreamReader(restoreData)
            val reader = JsonReader(isReader)
            val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()

            // START
            reader.beginObject()
            while (reader.hasNext()) {
                val type: String = reader.nextName()
                when (type) {
                    "database" -> readDatabase(reader, context)
                    "preferences" -> readPreferences(reader, preferences)
                    else -> throw RuntimeException("Can not parse type $type")
                }
            }
            reader.endObject()
            preferences.commit()

            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        const val TAG = "PFABackupRestorer"
    }
}