package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewHistory extends AppCompatActivity {

    private TextView itch;
    private TextView scale;
    private TextView burn;
    private TextView sweat;
    private TextView crust;
    private TextView days;

    private float [][] inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);

        itch = findViewById(R.id.itchTextview);
        scale = findViewById(R.id.scalingTextview);
        burn = findViewById(R.id.burningTextview);
        sweat = findViewById(R.id.sweatingTextview);
        crust = findViewById(R.id.crustingTextview);
        days = findViewById(R.id.daysTextview);

        Intent intent = getIntent();
        int daysSymptom = intent.getIntExtra("days", 1);
        int itching = intent.getIntExtra("itch", 1);
        int scaling = intent.getIntExtra("scaling", 1);
        int burning = intent.getIntExtra("burn", 1);
        int sweating = intent.getIntExtra("sweat", 1);
        int crusting = intent.getIntExtra("crust", 1);

        inputs = null;
        Object [] objectArray = (Object[]) getIntent().getExtras().getSerializable("inputs");
        if(inputs!=null) {
            inputs = new float[1][objectArray.length + 7];
            for (int i = 0; i < objectArray.length; i++) {
                inputs[0][i] = (float) objectArray[i];
            }
        }

        days.setText("Days: " + String.valueOf(daysSymptom));
        itch.setText("Itching " + String.valueOf(itching));
        scale.setText("Scaling " + String.valueOf(scaling));
        burn.setText("Burning " + String.valueOf(burning));
        sweat.setText("Sweat " + String.valueOf(sweating));
        crust.setText("Crusting " + String.valueOf(crusting));
    }
}
