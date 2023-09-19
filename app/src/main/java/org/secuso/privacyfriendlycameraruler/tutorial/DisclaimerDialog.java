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

package org.secuso.privacyfriendlycameraruler.tutorial;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import org.secuso.privacyfriendlycameraruler.HelpActivity;
import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.cameraruler.CameraActivity;

/**
 * Dialog fragment displaying the disclaimer in disclaimer_dialog layout. Requires a checkbox to be
 * checked before it can be dismissed.
 *
 * @author Roberts Kolosovs
 */
public class DisclaimerDialog extends DialogFragment {
    private PrefManager prefManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        prefManager = new PrefManager(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View rootView = inflater.inflate(R.layout.disclaimer_dialog, null);
        builder.setView(rootView);

        builder.setIcon(R.mipmap.launchericon_tapemeasure);
        builder.setTitle(getActivity().getString(R.string.disclaimer_dialog_title));

        final View rootViewFinal = rootView;

        Button okayButton = (Button) rootView.findViewById(R.id.okayButton);
        Button helpButton = (Button) rootView.findViewById(R.id.helpButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox readCheckBox = (CheckBox) rootViewFinal.findViewById(R.id.readCheckBox);
                if (!readCheckBox.isChecked()) {
                    Toast.makeText(getActivity().getBaseContext(), getString(R.string.disclaimer_check_toast), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity().getBaseContext(), getString(R.string.disclaimer_check_toast), Toast.LENGTH_LONG).show();
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