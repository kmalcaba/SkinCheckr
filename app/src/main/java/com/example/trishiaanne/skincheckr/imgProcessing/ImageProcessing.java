package com.example.trishiaanne.skincheckr.imgProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
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
                Bitmap original = BitmapFactory.decodeFile(chosenImagePath);

                //change dimensions
                final int maxWidth = 640;
                int outWidth;
                int outHeight;
                int inWidth = original.getWidth();
                int inHeight = original.getHeight();

                Bitmap img;

                if(original.getWidth() > 640) {
                    if (inWidth > inHeight) {
                        outWidth = maxWidth;
                        outHeight = (inHeight * maxWidth) / inWidth;
                    } else {
                        outHeight = maxWidth;
                        outWidth = (inWidth * maxWidth) / inHeight;
                    }

                    Bitmap resized = Bitmap.createScaledBitmap(original, outWidth, outHeight, false);
                    MediaStore.Images.Media.insertImage(getContentResolver(), resized, "RESIZED_IMG", "SAMPLE");

                    //Compress image
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    Log.e("Original   dimensions", original.getWidth() + " " + original.getHeight());
                    Log.e("Compressed dimensions", decoded.getWidth() + " " + decoded.getHeight());

                    img = decoded.copy(decoded.getConfig(), decoded.isMutable());
                } else {
                    img = original.copy(original.getConfig(), original.isMutable());
                }

                //Median Filtering
                Bitmap med = MedianFilter.filter(img);

//                //Otsu's Method of Thresholding
                Otsu o = new Otsu(med, img);
                int threshold = o.getThreshold();
                Log.d("Threshold: ", Integer.toString(threshold));
                Bitmap thresh = o.applyThreshold();
                Bitmap dilate = o.dilateImage(thresh);
                Bitmap mask = o.applyMask(dilate);

                MediaStore.Images.Media.insertImage(getContentResolver(), mask, "THRESHOLDED_IMG", "SAMPLE");
                //Feature Extraction
                FeatureExtraction fe = new FeatureExtraction(mask);
                fe.extract();

                long endTime = SystemClock.uptimeMillis();
                long duration = (endTime - startTime)/1000;
                Log.d("SkinCheckr:", "Timecost to run image processing: " + Long.toString(duration));

                float [] arrayInputs = new float[19];
                arrayInputs[0] = (float) fe.getASM();
                arrayInputs[1] = (float) fe.getContrast();
                arrayInputs[2] = (float) fe.getCorrelation();
                arrayInputs[3] = (float) fe.getHomogeneity();
                arrayInputs[4] = (float) fe.getSumAvg();
                arrayInputs[5] = (float) fe.getSumVariance();
                arrayInputs[6] = (float) fe.getSumEntropy();
                arrayInputs[7] = (float) fe.getEntropy();
                arrayInputs[8] = (float) fe.getDiffVariance();
                arrayInputs[9] = (float) fe.getDiffEntropy();
                arrayInputs[10] = (float) fe.getInfoMeasure1();
                arrayInputs[11] = (float) fe.getInfoMeasure2();

                Log.d("ASM:", String.valueOf(fe.getASM()));
                Log.d("Contrast: ", String.valueOf(fe.getContrast()));
                Log.d("Correlation: ", String.valueOf(fe.getCorrelation()));
                Log.d("Homogeneity:", String.valueOf(fe.getHomogeneity()));
                Log.d("SumAvg:", String.valueOf(fe.getSumAvg()));
                Log.d("SumVariance:", String.valueOf(fe.getSumVariance()));
                Log.d("SumEntropy:", String.valueOf(fe.getSumEntropy()));
                Log.d("Entropy:", String.valueOf(fe.getEntropy()));
                Log.d("DiffVariance:", String.valueOf(fe.getDiffVariance()));
                Log.d("DiffEntropy:", String.valueOf(fe.getDiffEntropy()));
                Log.d("InfoMeasure1:", String.valueOf(fe.getInfoMeasure1()));
                Log.d("InfoMeasure2:", String.valueOf(fe.getInfoMeasure2()));

                displayMessage(getApplicationContext(), "Image processing completed in " + duration + " seconds");
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
