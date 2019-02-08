package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity {

    private RadioGroup group;
    private RadioButton radioButton;
    private Button btnDisplay;

    private Button button;

    private SeekBar seek;
    private SeekBar seek1;
    private SeekBar seek2;
    private SeekBar seek3;
    private SeekBar seek4;

    private TextView textview;
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private TextView textview4;

    private EditText editText;

    // Values containers
    private int days = 0;
    private int itch = 0;
    private int scaling = 0;
    private int burn = 0;
    private int sweat = 0;
    private int crust = 0;
    private int bleeding = 0;

    private float[][] inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final int max = 10;

        /*inputs = null;
        Object [] objectArray = (Object[]) getIntent().getExtras().getSerializable("inputs");
        if(inputs!=null) {
            inputs = new float[1][objectArray.length + 7];
            for (int i = 0; i < objectArray.length; i++) {
                inputs[0][i] = (float) objectArray[i];
            }
        }*/

        //createRadioButtons();

        editText = findViewById(R.id.editDays);

        seek = findViewById(R.id.itchSeekbar);
        seek1 = findViewById(R.id.scalingSeekbar);
        seek2 = findViewById(R.id.burningSeekbar);
        seek3 = findViewById(R.id.sweatingSeekbar);
        seek4 = findViewById(R.id.crustingSeekbar);

        textview = findViewById(R.id.itchProgress);
        textview1 = findViewById(R.id.scalingProgress);
        textview2 = findViewById(R.id.burningProgress);
        textview3 = findViewById(R.id.sweatingProgress);
        textview4 = findViewById(R.id.crustingProgress);

        button = (Button) findViewById(R.id.resultButton);
        group = (RadioGroup) findViewById(R.id.radioButtonChoices);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = group.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if (editText.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Days is empty", Toast.LENGTH_LONG).show();
                } else if (group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Bleeding is empty", Toast.LENGTH_LONG).show();
                } else {
                    if (radioButton.getText().equals("Yes")) {
                        bleeding = 1;
                    } else {
                        bleeding = 0;
                    }
                    Intent myIntent = new Intent(History.this, ReviewHistory.class);
                    myIntent.putExtra("days", days);
                    myIntent.putExtra("itch", itch + 1);
                    myIntent.putExtra("scale", scaling + 1);
                    myIntent.putExtra("burn", burn + 1);
                    myIntent.putExtra("sweat", sweat + 1);
                    myIntent.putExtra("crust", crust + 1);
                    myIntent.putExtra("bleed", bleeding);
                    startActivity(myIntent);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().matches("")) {

                } else {
                    days = Integer.valueOf(editText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Itchiness Seekbar
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview.setText("Itchiness: " + String.valueOf(i + 1) + "/" + String.valueOf(max));
                itch = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Scaling/Peeling
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview1.setText("Scaling/Peeling: " + String.valueOf(i + 1) + "/" + String.valueOf(max));
                scaling = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Burning
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview2.setText("Burning: " + String.valueOf(i + 1) + "/" + String.valueOf(max));
                burn = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Sweating
        seek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview3.setText("Sweating: " + String.valueOf(i + 1) + "/" + String.valueOf(max));
                sweat = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Crusting
        seek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview4.setText("Crusting: " + String.valueOf(i + 1) + "/" + String.valueOf(max));
                crust = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /*private void createRadioButtons() {
        RadioGroup group = findViewById(R.id.radioButtonChoices);

        String[] stringPanels = getResources().getStringArray(R.array.radio_buttons_choices);

        for(int i = 0; i < stringPanels.length; i++){
            String stringPanel = stringPanels[i];
            RadioButton button = new RadioButton(this);
            button.setText(stringPanel);
            group.addView(button);
        }

    }*/
}
