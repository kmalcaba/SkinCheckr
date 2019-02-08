package com.example.trishiaanne.skincheckr.imgProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.History;
import com.example.trishiaanne.skincheckr.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Kirsten A. Malcaba
 */
public class ImageProcessing extends AppCompatActivity{
    String capturePath="";
    String importPath="";
    private Bitmap chosenImage;


    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        ImageView imageView = findViewById(R.id.imageView2);
        Button confirmPhoto = findViewById(R.id.imgProButton);

        Intent i = getIntent();
        //check if passed key is capture or import image
        if(i.getExtras().containsKey("capture_value")) {//captured image
            capturePath = i.getStringExtra("capture_value");
            //displayMessage(getBaseContext(),"CAPTURE PATH: " + capturePath); //for debugging
            Log.i("PATH: ",capturePath);
            chosenImage = BitmapFactory.decodeFile(capturePath);
            imageView.setImageBitmap(chosenImage);
        } else {//imported image
            importPath = i.getStringExtra("import_value");
            //displayMessage(getBaseContext(),"IMPORT PATH: " + importPath); //for debugging
            Log.i("PATH: ",importPath);

            chosenImage = BitmapFactory.decodeFile(importPath);
            imageView.setImageBitmap(chosenImage);
        }

        /*

                IMAGE PROCESSING

         */

        confirmPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long startTime = SystemClock.uptimeMillis();

                Bitmap img = chosenImage.copy(chosenImage.getConfig(), chosenImage.isMutable());

                //Median Filtering
                Bitmap med = MedianFilter.filter(img);

//                //Otsu's Method of Thresholding
                Otsu o = new Otsu(med, img);
                int threshold = o.getThreshold();
                Log.d("Threshold: ", Integer.toString(threshold));
                Bitmap thresh = o.applyThreshold();
                Bitmap dilate = o.dilateImage(thresh);
                Bitmap mask = o.applyMask(dilate);

                //Feature Extraction
                FeatureExtraction fe = new FeatureExtraction(mask);
                fe.extract();

                long endTime = SystemClock.uptimeMillis();
                Log.d("SkinCheckr:", "Timecost to run image processing: " + Long.toString((endTime - startTime)/1000));
//                Log.d("Contrast: ", String.valueOf(fe.getContrast()));
//                Log.d("Correlation: ", String.valueOf(fe.getCorrelation()));
//                Log.d("Energy: ", String.valueOf(fe.getEnergy()));
//                Log.d("Entropy: ", String.valueOf(fe.getEntropy()));
//                Log.d("Homogeneity: ", String.valueOf(fe.getHomogeneity()));
//                Log.d("Mean: ", String.valueOf(fe.getMean()));
//                Log.d("Variance: ", String.valueOf(fe.getVariance()));

                ArrayList<Float> inputs = new ArrayList<>();
                inputs.add((float) fe.getContrast());
                inputs.add((float) fe.getCorrelation());
                inputs.add((float) fe.getEnergy());
                inputs.add((float) fe.getEntropy());
                inputs.add((float) fe.getHomogeneity());
                inputs.add((float) fe.getMean());
                inputs.add((float) fe.getVariance());

                float [][] arrayInputs = new float[1][14];
                for(int i = 0; i < inputs.size(); i++) {
                    arrayInputs[0][i] = inputs.get(i);
                }


                Intent intent = new Intent(ImageProcessing.this, History.class);
                startActivity(intent);
            }
        });
    }
}
