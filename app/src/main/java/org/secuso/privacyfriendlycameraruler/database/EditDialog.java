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

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(rootView);

        builder.setTitle(getActivity().getString(R.string.edit_dialog_title));

        final View rootViewFinal = rootView;

        Button okayButton = (Button) rootView.findViewById(R.id.edit_confirm_button);
        Button cancelButton = (Button) rootView.findViewById(R.id.edit_cancel_button);
        Button deleteButton = (Button) rootView.findViewById(R.id.edit_delete_button);
        btnLine = (RadioButton) rootView.findViewById(R.id.rdbtn_line);
        btnCircle = (RadioButton) rootView.findViewById(R.id.rdbtn_circle);
        btnTetragon = (RadioButton) rootView.findViewById(R.id.rdbtn_tetragon);
        nameInput = (EditText) rootView.findViewById(R.id.name_input);
        sizeInput = (EditText) rootView.findViewById(R.id.size_input);
        final PFASQLiteHelper dbHelper = new PFASQLiteHelper(getActivity().getBaseContext());

        if (reference.getUDR_ACTIVE()) {
            nameInput.setText(reference.getUDR_NAME());
            sizeInput.setText("" + reference.getUDR_SIZE());
            String shape = reference.getUDR_SHAPE();
            RadioButton btn;
            if (shape.equals("line")) {
                btnLine.setChecked(true);
            } else if (shape.equals("circle")) {
                btnCircle.setChecked(true);
            } else if (shape.equals("tetragon")) {
                btnTetragon.setChecked(true);
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
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
