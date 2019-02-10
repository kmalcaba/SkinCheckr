package com.example.trishiaanne.skincheckr.imgProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Kirsten A. Malcaba
 */
public class ImageProcessing extends AppCompatActivity{
    private String capturePath;
    private String importPath;
    private Bitmap chosenImage;
    private String chosenImagePath;
    private static int TYPE_OF_USER;

    private ImageView imageView;
    private Button confirmPhoto;

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        imageView = findViewById(R.id.imageView2);
        confirmPhoto = findViewById(R.id.imgProButton);

        //get the user type
        TYPE_OF_USER = getIntent().getExtras().getInt("user_type");
        getImage();

        /*

                IMAGE PROCESSING

         */

        confirmPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = SystemClock.uptimeMillis();
                //change dimensions
                Bitmap original = BitmapFactory.decodeFile(chosenImagePath);
                Bitmap resized = Bitmap.createScaledBitmap(original, 770, 504, false);

                //Compress image
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG,100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                Log.e("Original   dimensions", original.getWidth()+" "+original.getHeight());
                Log.e("Compressed dimensions", decoded.getWidth()+" "+decoded.getHeight());

                Bitmap img = decoded.copy(decoded.getConfig(), decoded.isMutable());

                //Median Filtering
//                Bitmap med = MedianFilter.filter(img);

//                //Otsu's Method of Thresholding
                Otsu o = new Otsu(img, img);
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

                float [] arrayInputs = new float[14];
                arrayInputs[0] = (float) fe.getContrast();
                arrayInputs[1] = (float) fe.getCorrelation();
                arrayInputs[2] = (float) fe.getEnergy();
                arrayInputs[3] = (float) fe.getEntropy();
                arrayInputs[4] = (float) fe.getHomogeneity();
                arrayInputs[5] = (float) fe.getMean();
                arrayInputs[6] = (float) fe.getVariance();

                displayMessage(getApplicationContext(), "Image processing complete");
                Intent intent = new Intent(ImageProcessing.this, History.class);
                intent.putExtra("features", arrayInputs);
                intent.putExtra("image_path", chosenImagePath);
                intent.putExtra("user_type", TYPE_OF_USER);
                startActivity(intent);
            }
        });
    }

    private void getImage() {
        Intent i = getIntent();
        //check if passed key is capture or import image
        if(i.getExtras().containsKey("capture_value")) {//captured image
            capturePath = getIntent().getStringExtra("capture_value");
            //displayMessage(getBaseContext(),"CAPTURE PATH: " + capturePath); //for debugging
            Log.i("PATH: ",capturePath);
            chosenImage = BitmapFactory.decodeFile(capturePath);
            imageView.setImageBitmap(chosenImage);
            chosenImagePath = capturePath;
        } else {//imported image
            importPath = i.getStringExtra("import_value");
            //displayMessage(getBaseContext(),"IMPORT PATH: " + importPath); //for debugging
            Log.i("PATH: ",importPath);

            chosenImage = BitmapFactory.decodeFile(importPath);
            imageView.setImageBitmap(chosenImage);
            chosenImagePath = importPath;
        }
    }
}
