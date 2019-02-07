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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);

        itch = findViewById(R.id.itchTextview);
        scale = findViewById(R.id.scalingTextview);
        burn = findViewById(R.id.burningTextview);
        sweat = findViewById(R.id.sweatingTextview);
        crust = findViewById(R.id.crustingTextview);

        Intent intent = getIntent();
        int itching = intent.getIntExtra("itch", 0);
        int scaling = intent.getIntExtra("scaling", 0);
        int burning = intent.getIntExtra("burn", 0);
        int sweating = intent.getIntExtra("sweat", 0);
        int crusting = intent.getIntExtra("crust", 0);

        itch.setText("Itching " + String.valueOf(itching));
        scale.setText("Scaling " + String.valueOf(scaling));
        burn.setText("Burning " + String.valueOf(burning));
        sweat.setText("Sweat " + String.valueOf(sweating));
        crust.setText("Crusting " + String.valueOf(crusting));
    }
}
