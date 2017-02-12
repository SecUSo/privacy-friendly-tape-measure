package org.secuso.privacyfriendlycameraruler.tutorial;

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

import org.secuso.privacyfriendlycameraruler.HelpActivity;
import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.cameraruler.CameraActivity;

public class DisclaimerDialog extends DialogFragment {

    Activity activity;
    private PrefManager prefManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        prefManager = new PrefManager(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = inflater.inflate(R.layout.disclaimer_dialog, null);
        builder.setView(rootView);

        builder.setIcon(R.mipmap.icon_drawer);
        builder.setTitle(getActivity().getString(R.string.disclaimer_dialog_title));

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
                    prefManager.setFirstTimeLaunch(false);
                    startActivity(new Intent(getActivity().getBaseContext(), CameraActivity.class));
                    getActivity().finish();
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
                    prefManager.setFirstTimeLaunch(false);
                    startActivity(new Intent(getActivity().getBaseContext(), HelpActivity.class));
                    dismiss();
                }
            }
        });

        return builder.create();
    }
}