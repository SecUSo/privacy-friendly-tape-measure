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
 * Created by roberts on 18.02.17.
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
