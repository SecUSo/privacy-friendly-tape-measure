package org.secuso.privacyfriendlycameraruler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class WelcomeDialog extends DialogFragment {

    boolean closeDialog = false;
    Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = inflater.inflate(R.layout.welcome_dialog, null);
        builder.setView(rootView);

        builder.setIcon(R.mipmap.icon_drawer);
        builder.setTitle(getActivity().getString(R.string.welcome));

        final View rootViewFinal = rootView;

        Button okayButton = (Button) rootView.findViewById(R.id.okayButton);
        Button helpButton = (Button) rootView.findViewById(R.id.helpButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox readCheckBox = (CheckBox) rootViewFinal.findViewById(R.id.readCheckBox);
                if (!readCheckBox.isChecked()) {
                    Toast.makeText(activity.getBaseContext(), getString(R.string.disclaimer_check_toast), Toast.LENGTH_LONG).show();
                } else {
                    dismiss();
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox readCheckBox = (CheckBox) rootViewFinal.findViewById(R.id.readCheckBox);
                if (!readCheckBox.isChecked()) {
                    Toast.makeText(activity.getBaseContext(), getString(R.string.disclaimer_check_toast), Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getActivity(), HelpActivity.class);
                    getActivity().startActivity(i);
                    dismiss();
                }
            }
        });

        return builder.create();
    }
}