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
    private TextView bleed;
    private ImageView skin_img;
    private String imagePath;
    private static int TYPE_OF_USER;

    private ArrayList<String> diagnosed = new ArrayList<>();
    ArrayList<String> newDx = new ArrayList<>();

    private Classifier imgClassifier;

    int[] inputs;

    int itching, scaling, burning, sweating, crusting, bleeding;
    float max;

    Bitmap skin;
    Bitmap skin1;

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
        bleed = findViewById(R.id.bleedingTextview);
        skin_img = findViewById(R.id.imageView4);

        final Intent intent = getIntent();
        itching = intent.getIntExtra("itch", 1);
        scaling = intent.getIntExtra("scale", 1);
        burning = intent.getIntExtra("burn", 1);
        sweating = intent.getIntExtra("sweat", 1);
        crusting = intent.getIntExtra("crust", 1);
        bleeding = intent.getIntExtra("bleed", 0);
        imagePath = intent.getStringExtra("image_path");
        TYPE_OF_USER = intent.getExtras().getInt("user_type");
        diagnosed = intent.getStringArrayListExtra("diagnosed");
        byte[] byteArray = getIntent().getByteArrayExtra("image");

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = narrowDownResults();
                if(max > 3) {
                    newDx.add(diagnosed.get(idx));
                    diagnosed.clear();
                    diagnosed.addAll(newDx);
                }

                if (TYPE_OF_USER == 0) { //if guest
                    displayMessage(getApplicationContext(), "User type is GUEST = " + TYPE_OF_USER);
                    Intent guestIntent = new Intent(ReviewHistory.this, GuestResult.class);
                    guestIntent.putExtra("image_path", imagePath);
                    guestIntent.putStringArrayListExtra("result", diagnosed);

                    startActivity(guestIntent);
                    finish();
                } else { //if registered user
                    displayMessage(getApplicationContext(), "User type is REGISTERED USER = " + TYPE_OF_USER);
                    Intent registeredUserIntent = new Intent(ReviewHistory.this, Result.class);
                    registeredUserIntent.putExtra("image_path", imagePath);
                    registeredUserIntent.putStringArrayListExtra("result", diagnosed);

                    startActivity(registeredUserIntent);
                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        skin = BitmapFactory.decodeFile(imagePath);
//        skin1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        skin_img.setImageBitmap(skin);
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

        imgClassifier = ImageClassifier.create(this, getAssets());
    }

    int narrowDownResults() {
        float[] i = new float[3];
        int j = 0;

        for (String dx : diagnosed) {
            switch (dx) {
                case "atopic dermatitis":
                    if (itching >= 7)
                        i[j]++;
                    else if (itching == 6)
                        i[j] += 0.5;
                    else if (itching == 5)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning == 1)
                        i[j]++;
                    if (sweating == 1)
                        i[j]++;
                    if (crusting >= 5 && crusting <= 8)
                        i[j]++;
                    else if (crusting == 4 || crusting == 9)
                        i[j] += 0.5;
                    else if (crusting == 3 || crusting == 10)
                        i[j] += 0.25;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "contact dermatitis":
                    if (itching >= 7)
                        i[j]++;
                    else if (itching == 6)
                        i[j] += 0.5;
                    else if (itching == 5)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning >= 5 && burning <= 8)
                        i[j]++;
                    else if (burning == 4 || burning == 9)
                        i[j] += 0.5;
                    else if (burning == 3 || burning == 10)
                        i[j] += 0.25;
                    if (sweating == 1)
                        i[j]++;
                    if (crusting >= 5 && crusting <= 8)
                        i[j]++;
                    else if (crusting == 4 || crusting == 9)
                        i[j] += 0.5;
                    else if (crusting == 3 || crusting == 10)
                        i[j] += 0.25;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "dyshidrotic eczema":
                    if (itching >= 7)
                        i[j]++;
                    else if (itching == 6)
                        i[j] += 0.5;
                    else if (itching == 5)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning >= 5 && burning <= 8)
                        i[j]++;
                    else if (burning == 4 || burning == 9)
                        i[j] += 0.5;
                    else if (burning == 3 || burning == 10)
                        i[j] += 0.25;
                    if (sweating >= 7)
                        i[j]++;
                    else if (sweating == 6)
                        i[j] += 0.5;
                    else if (sweating == 5)
                        i[j] += 0.25;
                    if (crusting <= 5)
                        i[j]++;
                    else if (crusting == 6)
                        i[j] += 0.5;
                    else if (crusting == 7)
                        i[j] += 0.25;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "intertrigo":
                    if (itching >= 5)
                        i[j]++;
                    else if (itching == 4)
                        i[j] += 0.5;
                    else if (itching == 3)
                        i[j] += 0.25;
                    if (scaling >= 2 && scaling <= 5)
                        i[j]++;
                    else if (scaling == 1 || scaling == 6)
                        i[j] += 0.5;
                    else if (scaling == 7)
                        i[j] += 0.25;
                    if (burning >= 7)
                        i[j]++;
                    else if (burning == 6)
                        i[j] += 0.5;
                    else if (burning == 5)
                        i[j] += 0.25;
                    if (sweating >= 7)
                        i[j]++;
                    else if (sweating == 6)
                        i[j] += 0.5;
                    else if (sweating == 5)
                        i[j] += 0.25;
                    if (crusting >= 5 && crusting <= 8)
                        i[j]++;
                    else if (crusting == 4 || crusting == 9)
                        i[j] += 0.5;
                    else if (crusting == 3 || crusting == 10)
                        i[j] += 0.25;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "melanoma":
                    if (itching >= 5)
                        i[j]++;
                    else if (itching == 4)
                        i[j] += 0.5;
                    else if (itching == 3)
                        i[j] += 0.25;
                    if (scaling >= 2 && scaling <= 5)
                        i[j]++;
                    else if (scaling == 1 || scaling == 6)
                        i[j] += 0.5;
                    else if (scaling == 7)
                        i[j] += 0.25;
                    if (burning >= 2 && burning <= 5)
                        i[j]++;
                    else if (burning == 1 || burning == 6)
                        i[j] += 0.5;
                    else if (burning == 7)
                        i[j] += 0.25;
                    if (sweating >= 2)
                        i[j]++;
                    else if (sweating == 1)
                        i[j] += 0.5;
                    if (crusting >= 2 && crusting <= 5)
                        i[j]++;
                    else if (crusting == 1 || crusting == 6)
                        i[j] += 0.5;
                    else if (crusting == 7)
                        i[j] += 0.25;
                    if (bleeding == 1)
                        i[j]++;
                    break;
                case "pityriasis versicolor":
                    if (itching >= 2 && itching <= 5)
                        i[j]++;
                    else if (itching == 1 || itching == 6)
                        i[j] += 0.5;
                    else if (itching == 7)
                        i[j] += 0.25;
                    if (scaling >= 2 && scaling <= 5)
                        i[j]++;
                    else if (scaling == 1 || scaling == 6)
                        i[j] += 0.5;
                    else if (scaling == 7)
                        i[j] += 0.25;
                    if (burning == 1)
                        i[j]++;
                    if (sweating == 1)
                        i[j]++;
                    if (crusting == 1)
                        i[j]++;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "psoriasis":
                    if (itching >= 5)
                        i[j]++;
                    else if (itching == 4)
                        i[j] += 0.5;
                    else if (itching == 3)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning >= 5 && burning <= 8)
                        i[j]++;
                    else if (burning == 4 || burning == 9)
                        i[j] += 0.5;
                    else if (burning == 3 || burning == 10)
                        i[j] += 0.25;
                    if (sweating >= 2 && sweating <= 5)
                        i[j]++;
                    else if (sweating == 1 || sweating == 6)
                        i[j] += 0.5;
                    else if (sweating == 7)
                        i[j] += 0.25;
                    if (crusting >= 5)
                        i[j]++;
                    else if (crusting == 4)
                        i[j] += 0.5;
                    else if (crusting == 3)
                        i[j] += 0.25;
                    if (bleeding == 1)
                        i[j]++;
                    break;
                case "tinea corporis":
                    if (itching >= 5 && itching <= 8)
                        i[j]++;
                    else if (itching == 4 || itching == 9)
                        i[j] += 0.5;
                    else if (itching == 3 || itching == 10)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning >= 2 && burning <= 5)
                        i[j]++;
                    else if (burning == 1 || burning == 6)
                        i[j] += 0.5;
                    else if (burning == 7)
                        i[j] += 0.25;
                    if (sweating == 1)
                        i[j]++;
                    if (crusting >= 5)
                        i[j]++;
                    else if (crusting == 4)
                        i[j] += 0.5;
                    else if (crusting == 3)
                        i[j] += 0.25;
                    if (bleeding == 0)
                        i[j]++;
                    break;
                case "tinea pedis":
                    if (itching >= 7)
                        i[j]++;
                    else if (itching == 6)
                        i[j] += 0.5;
                    else if (itching == 5)
                        i[j] += 0.25;
                    if (scaling >= 5)
                        i[j]++;
                    else if (scaling == 4)
                        i[j] += 0.5;
                    else if (scaling == 3)
                        i[j] += 0.25;
                    if (burning >= 5)
                        i[j]++;
                    else if (burning == 4)
                        i[j] += 0.5;
                    else if (burning == 3)
                        i[j] += 0.25;
                    if (sweating >= 7)
                        i[j]++;
                    else if (sweating == 6)
                        i[j] += 0.5;
                    else if (sweating == 5)
                        i[j] += 0.25;
                    if (crusting >= 5)
                        i[j]++;
                    else if (crusting == 4)
                        i[j] += 0.5;
                    else if (crusting == 3)
                        i[j] += 0.25;
                    if (bleeding == 1)
                        i[j]++;
                    break;
                case "benign mole":
                    if (itching <= 5)
                        i[j]++;
                    if (scaling == 1)
                        i[j]++;
                    if (burning == 1)
                        i[j]++;
                    if (sweating <= 5)
                        i[j]++;
                    if (crusting == 1)
                        i[j]++;
                    if(bleeding == 0)
                        i[j]++;
                    break;
                case "skin":
                    if (itching <= 5)
                        i[j]++;
                    if (scaling == 1)
                        i[j]++;
                    if (burning == 1)
                        i[j]++;
                    if (sweating <= 5)
                        i[j]++;
                    if (crusting == 1)
                        i[j]++;
                    if(bleeding == 0)
                        i[j]++;
                    break;
            }
            j++;
        }

        max = i[0];
        int idx = 0;

        for(int k = 0; k < 3; k++) {
            Log.d("ImageProccesing", "Index: " + idx + "; Value: " + i[k]);
            if(max <= i[k]) {
                idx = k;
                max = i[k];
            }
        }

        Log.d("ImageProcessing", "Max of correct symptoms: " + max + "; Index: " + idx);

        return idx;
    }

    //Disable back button
    @Override
    public void onBackPressed() {
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }
}
