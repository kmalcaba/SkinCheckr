package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private ImageView skin_img;
    private String imagePath;
    private static int TYPE_OF_USER;

    private float[] inputs;

    private ArrayList<String> diagnosed = new ArrayList<>();
    private ArrayList<String> percentage = new ArrayList<>();

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
        skin_img = findViewById(R.id.imageView4);

        final Intent intent = getIntent();
        int daysSymptom = intent.getIntExtra("days", 1);
        int itching = intent.getIntExtra("itch", 1);
        int scaling = intent.getIntExtra("scale", 1);
        int burning = intent.getIntExtra("burn", 1);
        int sweating = intent.getIntExtra("sweat", 1);
        int crusting = intent.getIntExtra("crust", 1);
        int bleeding = intent.getIntExtra("bleed", 0);
        inputs = intent.getFloatArrayExtra("features");
        imagePath = intent.getStringExtra("image_path");
        TYPE_OF_USER = intent.getExtras().getInt("user_type");

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage();
                if (TYPE_OF_USER == 0) { //if guest
                    displayMessage(getApplicationContext(), "User type is GUEST = " + TYPE_OF_USER);
                    Intent guestIntent = new Intent (ReviewHistory.this, GuestResult.class);
                    guestIntent.putExtra("image_path", imagePath);
                    guestIntent.putStringArrayListExtra("result", diagnosed);
                    guestIntent.putExtra("percentage", percentage);
                    startActivity(guestIntent);
                } else { //if registered user
                    displayMessage(getApplicationContext(), "User type is REGISTERED USER = " + TYPE_OF_USER);
                    Intent registeredUserIntent = new Intent(ReviewHistory.this, Result.class);
                    registeredUserIntent.putExtra("image_path", imagePath);
                    registeredUserIntent.putStringArrayListExtra("result", diagnosed);
                    registeredUserIntent.putExtra("percentage", percentage);
                    startActivity(registeredUserIntent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bitmap skin = BitmapFactory.decodeFile(imagePath);
        skin_img.setImageBitmap(skin);

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

        inputs[12] = (float) daysSymptom;
        inputs[13] = (float) itching;
        inputs[14] = (float) scaling;
        inputs[15] = (float) burning;
        inputs[16] = (float) sweating;
        inputs[17] = (float) crusting;
        inputs[18] = (float) bleeding;

        classifier = ImageClassifier.create(this, getAssets());
    }

    public void processImage() {
        final long startTime = SystemClock.uptimeMillis();
        final List<Classifier.Recognition> results = classifier.recognizeImage(inputs);
        final long lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
        Log.i("Detect: %s", Long.toString(lastProcessingTimeMs));
        for (Classifier.Recognition r : results) {
            Log.i("Results: ", r.getTitle() + " " + (r.getConfidence()*100));
            diagnosed.add(r.getTitle().toLowerCase());
            percentage.add((r.getConfidence()).toString());
        }
        classifier.close();
    }

    //Disable back button
    @Override
    public void onBackPressed() {
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }
}
