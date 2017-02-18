package org.secuso.privacyfriendlycameraruler.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.tutorial.DisclaimerDialog;

import java.util.ArrayList;

/**
 * Created by roberts on 18.02.17.
 */

public class ReferenceListViewer extends AppCompatActivity {

    private ListView mListView;
    private PFASQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_list);

        mListView = (ListView) findViewById(R.id.reference_viewer_list);
        dbHelper = new PFASQLiteHelper(getBaseContext());
        final ArrayList<UserDefinedReferences> uDefRefs = dbHelper.getAllUDefRef();
        String[] listItems = new String[uDefRefs.size()];
        for(int i = 0; i < uDefRefs.size(); i++){
            UserDefinedReferences uDefRef = uDefRefs.get(i);
            listItems[i] = uDefRef.getUDR_ACTIVE()?uDefRef.getUDR_NAME():getString(R.string.list_item)+" "+i;
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDefinedReferences selectedReference = uDefRefs.get(position);
                FragmentManager fm = getSupportFragmentManager();
                EditDialog editDialog = new EditDialog();
                editDialog.reference = selectedReference;
                editDialog.show(fm, "EditDialog");
            }

        });
    }

}
