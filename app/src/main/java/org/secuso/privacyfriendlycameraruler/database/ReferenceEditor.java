package org.secuso.privacyfriendlycameraruler.database;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.secuso.privacyfriendlycameraruler.R;

/**
 * This activity is responsible for editing user defined references.
 *
 * Created by roberts on 16.02.17.
 */

public class ReferenceEditor extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_editor);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Button okBTN = (Button) findViewById(R.id.edit_confirm_button);
        final EditText idInput = (EditText) findViewById(R.id.id_input);
        final EditText nameInput = (EditText) findViewById(R.id.name_input);
        final RadioGroup shapeRBtnGroup = (RadioGroup) findViewById(R.id.shape_rdbtn_group);
        final EditText sizeInput = (EditText) findViewById(R.id.size_input);
        final PFASQLiteHelper dbHelper = new PFASQLiteHelper(getBaseContext());

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(idInput.getText().toString());
                String name = nameInput.getText().toString();
                int rbtnIndex = shapeRBtnGroup.getCheckedRadioButtonId();
                float size = Float.parseFloat(sizeInput.getText().toString());
                String shape = "";
                System.out.println("Shape index: "+rbtnIndex);

                if(rbtnIndex!=-1){
                    View radioButton = shapeRBtnGroup.findViewById(rbtnIndex);
                    int radioId = shapeRBtnGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) shapeRBtnGroup.getChildAt(radioId);
                    shape = ((String) btn.getText()).toLowerCase();
                }

                UserDefinedReferences udf;
                if (id >=0 && id <= 9) {
                    if (name.isEmpty() || size <= 0 || shape.isEmpty()) {
                        udf = new UserDefinedReferences(id);
                        Toast.makeText(getBaseContext(), "Deactivated user defined reference object "+id, Toast.LENGTH_LONG).show();
                    } else {
                        udf = new UserDefinedReferences(id, name, shape, size);
                        Toast.makeText(getBaseContext(), "Set user defined reference object "+id, Toast.LENGTH_LONG).show();
                    }
                    dbHelper.updateUserDefRef(udf);
                } else {
                    Toast.makeText(getBaseContext(), "ID must be between 0 and 9", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}
