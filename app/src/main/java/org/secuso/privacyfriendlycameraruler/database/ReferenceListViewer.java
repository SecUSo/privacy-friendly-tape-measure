/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.database;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.secuso.privacyfriendlycameraruler.R;

import java.util.ArrayList;

/**
 * Activity for displaying user defined reference objects. Uses the activity_reference_list and
 * fetches the user defined references from the database.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 18.02.17.
 */

public class ReferenceListViewer extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_list);

        ListView mListView = (ListView) findViewById(R.id.reference_viewer_list);
        PFASQLiteHelper dbHelper = new PFASQLiteHelper(getBaseContext());
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
