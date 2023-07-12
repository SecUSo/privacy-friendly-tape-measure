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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlycameraruler.R;

/**
 * Dialog fragment for editing a user defined reference object. Uses the edit_dialog layout and
 * gives functionality to the buttons defined there.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 18.02.17.
 */

public class EditDialog extends DialogFragment {
    UserDefinedReferences reference;
    private EditText nameInput;
    private EditText sizeInput;
    private RadioButton btnLine;
    private RadioButton btnCircle;
    private RadioButton btnTetragon;
    private TextView unitsText;
    private TextView sizeDiscr;
    String uom;

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(rootView);

        builder.setTitle(getActivity().getString(R.string.edit_dialog_title));

        Button okayButton = (Button) rootView.findViewById(R.id.edit_confirm_button);
        Button cancelButton = (Button) rootView.findViewById(R.id.edit_cancel_button);
        Button deleteButton = (Button) rootView.findViewById(R.id.edit_delete_button);
        btnLine = (RadioButton) rootView.findViewById(R.id.rdbtn_line);
        btnCircle = (RadioButton) rootView.findViewById(R.id.rdbtn_circle);
        btnTetragon = (RadioButton) rootView.findViewById(R.id.rdbtn_tetragon);
        nameInput = (EditText) rootView.findViewById(R.id.name_input);
        sizeInput = (EditText) rootView.findViewById(R.id.size_input);
        unitsText = (TextView) rootView.findViewById(R.id.units);
        sizeDiscr = (TextView) rootView.findViewById(R.id.size_discription);
        RadioGroup shapeGroup = (RadioGroup) rootView.findViewById(R.id.shape_rdbtn_group);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        final PFASQLiteHelper dbHelper = new PFASQLiteHelper(getActivity().getBaseContext());

        uom = prefs.getString("pref_units_of_measurement", "mm");
        if (uom.equals("in")) {
            unitsText.setText(R.string.calibrationInch);
        }

        if (reference.getUDR_ACTIVE()) {
            nameInput.setText(reference.getUDR_NAME());
            float s = reference.getUDR_SIZE();
            if (uom.equals("in")) {
                s /= 25.4;
            }
            String shape = reference.getUDR_SHAPE();
            switch (shape) {
                case "line":
                    btnLine.setChecked(true);
                    break;
                case "circle":
                    btnCircle.setChecked(true);
                    sizeDiscr.setText(R.string.diameter);
                    break;
                case "tetragon":
                    btnTetragon.setChecked(true);
                    sizeDiscr.setText(R.string.area);
                    unitsText.setText(unitsText.getText() + "²");
                    if (uom.equals("in")) {
                        s /= 25.4;
                    }
                    break;
            }
            sizeInput.setText("" + s);
        }

        shapeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (btnLine.isChecked()) {
                    sizeDiscr.setText(R.string.edit_size);
                    if (uom.equals("mm")) {
                        unitsText.setText(R.string.calibrationMM);
                    } else {
                        unitsText.setText(R.string.calibrationInch);
                    }
                } else if (btnCircle.isChecked()) {
                    sizeDiscr.setText(R.string.diameter);
                    if (uom.equals("mm")) {
                        unitsText.setText(R.string.calibrationMM);
                    } else {
                        unitsText.setText(R.string.calibrationInch);
                    }
                } else if (btnTetragon.isChecked()) {
                    sizeDiscr.setText(R.string.area);
                    unitsText.setText(unitsText.getText()+"²");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Closes the dialog without any changes to the database.
             * @param v view
             */
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sets all values of the user defined reference object to zero values and updates the
             * corresponding database entry.
             * @param v view
             */
            @Override
            public void onClick(View v) {
                reference.setUDR_ACTIVE(false);
                reference.setUDR_NAME("");
                reference.setUDR_SIZE(0);
                reference.setUDR_SHAPE("");
                dbHelper.updateUserDefRef(reference);
                Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_delete_done), Toast.LENGTH_LONG).show();
                dismiss();
                getActivity().recreate();
            }
        });

        okayButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks if the input of all fields is valid, enters the new or updated reference
             * object into the database and sets it active.
             * @param v clicked view
             */
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                float size = sizeInput.getText().toString().isEmpty()?-1f:Float.parseFloat(sizeInput.getText().toString());
                String shape = "";

                if (btnLine.isChecked()) {
                    shape = "line";
                } else if (btnCircle.isChecked()) {
                    shape = "circle";
                } else if (btnTetragon.isChecked()) {
                    shape = "tetragon";
                }

                if (name.isEmpty() || size <= 0 || shape.isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_input_failure), Toast.LENGTH_LONG).show();
                } else {
                    reference.setUDR_ACTIVE(true);
                    reference.setUDR_NAME(name);
                    reference.setUDR_SHAPE(shape);
                    if (uom.equals("in")) {
                        size *= 25.4;
                        if (shape.equals("tetragon")){
                            size *= 25.4;
                        }
                    }
                    Log.d("SIZE", size+"");
                    reference.setUDR_SIZE(size);
                    dbHelper.updateUserDefRef(reference);
                    Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_edit_done), Toast.LENGTH_LONG).show();
                    dismiss();
                    getActivity().recreate();
                }
            }
        });

        return builder.create();
    }
}
