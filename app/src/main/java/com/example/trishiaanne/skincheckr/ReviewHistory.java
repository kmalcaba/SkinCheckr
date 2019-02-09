package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ReviewHistory extends AppCompatActivity {

    private Button result;
    private Button back;

    private TextView itch;
    private TextView scale;
    private TextView burn;
    private TextView sweat;
    private TextView crust;
    private TextView days;
    private TextView bleed;

    private float[] inputs;

    private Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);

        result = findViewById(R.id.resultButton);
        back = findViewById(R.id.backButton);

        itch = findViewById(R.id.itchTextview);
        scale = findViewById(R.id.scalingTextview);
        burn = findViewById(R.id.burningTextview);
        sweat = findViewById(R.id.sweatingTextview);
        crust = findViewById(R.id.crustingTextview);
        days = findViewById(R.id.daysTextview);
        bleed = findViewById(R.id.bleedingTextview);

        Intent intent = getIntent();
        int daysSymptom = intent.getIntExtra("days", 1);
        int itching = intent.getIntExtra("itch", 1);
        int scaling = intent.getIntExtra("scale", 1);
        int burning = intent.getIntExtra("burn", 1);
        int sweating = intent.getIntExtra("sweat", 1);
        int crusting = intent.getIntExtra("crust", 1);
        int bleeding = intent.getIntExtra("bleed", 0);
        inputs = intent.getFloatArrayExtra("features");


        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage();
                startActivity(new Intent(ReviewHistory.this, Result.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        days.setText("Days: " + String.valueOf(daysSymptom));
        itch.setText("Itching: " + String.valueOf(itching) + "/10");
        scale.setText("Scaling/Peeling: " + String.valueOf(scaling) + "/10");
        burn.setText("Burning: " + String.valueOf(burning) + "/10");
        sweat.setText("Sweating: " + String.valueOf(sweating) + "/10");
        crust.setText("Crusting: " + String.valueOf(crusting) + "/10");
        if (bleeding == 1) {
            bleed.setText("Bleeding: " + "Yes");
        } else {
            bleed.setText("Bleeding: " + "No");
        }

        inputs[7] = (float) daysSymptom;
        inputs[8] = (float) itching;
        inputs[9] = (float) scaling;
        inputs[10] = (float) burning;
        inputs[11] = (float) sweating;
        inputs[12] = (float) crusting;
        inputs[13] = (float) bleeding;

        classifier = ImageClassifier.create(this, getAssets());
    }

    public void processImage() {
        final long startTime = SystemClock.uptimeMillis();
        final List<Classifier.Recognition> results = classifier.recognizeImage(inputs);
        final long lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
        Log.i("Detect: %s", Long.toString(lastProcessingTimeMs));
        for (Classifier.Recognition r : results) {
            Log.i("Results: ", r.getTitle());
        }
        classifier.close();
    }
}
