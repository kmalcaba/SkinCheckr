package com.example.trishiaanne.skincheckr.imgProcessing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.History;
import com.example.trishiaanne.skincheckr.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Kirsten A. Malcaba
 */
public class ImageProcessing extends AppCompatActivity{
    String capturePath="";
    String importPath="";
    private Bitmap chosenImage;
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

        Intent i = getIntent();
        //check if passed key is capture or import image
        if(i.getExtras().containsKey("capture_value")) {//captured image
            capturePath = i.getStringExtra("capture_value");
            displayMessage(getBaseContext(),"CAPTURE PATH: " + capturePath);
            Log.i("PATH: ",capturePath);
            chosenImage = BitmapFactory.decodeFile(capturePath);
            imageView.setImageBitmap(chosenImage);
        } else {//imported image
            importPath = i.getStringExtra("import_value");
            displayMessage(getBaseContext(),"IMPORT PATH: " + importPath);
            Log.i("PATH: ",importPath);
            //Allow storage access
            if(ContextCompat.checkSelfPermission(ImageProcessing.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ImageProcessing.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(ImageProcessing.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            } else {
                //Permission granted
            }
            chosenImage = BitmapFactory.decodeFile(importPath);
            imageView.setImageBitmap(chosenImage);
        }
        //img pro
        confirmPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageProcessing.this, History.class);
                startActivity(intent);
//                Bitmap med = MedianFilter.filter(chosenImage);
//                //otsu's method thresholding
//                Otsu o = new Otsu(med,chosenImage);
//                int threshold = o.getThreshold();
//                displayMessage(getBaseContext(),"Threshold: " + threshold);
//                Bitmap thresh = o.applyThreshold();
//                Bitmap dilate = o.dilateImage(thresh);
//                Bitmap mask = o.applyMask(dilate);
//
//
//                //Feature Extraction
//                FeatureExtraction fe = new FeatureExtraction();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                mask.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//                FeatureExtraction.imageArray = new byte[]{};
//                FeatureExtraction.imageArray = stream.toByteArray();


//                FeatureExtraction fe = null;
//                try {
//                    fe = new FeatureExtraction(mask);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                fe.extract();
//                Log.d("Contrast: ", String.valueOf(fe.getContrast()));
//                Log.d("Correlation: ", String.valueOf(fe.getCorrelation()));
//                Log.d("Energy: ", String.valueOf(fe.getEnergy()));
//                Log.d("Entropy: ", String.valueOf(fe.getEntropy()));
//                Log.d("Homogeneity: ", String.valueOf(fe.getHomogeneity()));
//                Log.d("Mean: ", String.valueOf(fe.getMean()));
//                Log.d("Variance: ", String.valueOf(fe.getVariance()));
//
//                displayMessage(getBaseContext(), "Contrast: " + fe.getContrast() +
//                "\nCorrelation: " + fe.getCorrelation() +
//                "\nEnergy: " + fe.getEnergy() +
//                "\nEntropy: " + fe.getEntropy() +
//                "\nHomogeneity: " + fe.getHomogeneity() +
//                "\nMean: " + fe.getMean() +
//                "\nVariance: " + fe.getVariance());
            }
        });
    }
    /*
        String path_value = Intent.getIntentOld("")
        File f = new File("test/20.jpg");
            BufferedImage img = ImageIO.read(f);

            //img pro
            //median filter - noise reduction
            BufferedImage med = MedianFilter.filter(img);
        BufferedImage med = MedianFilter.filter(img);
        //otsu's method thresholding
        Otsu o = new Otsu(med, img); //if mask
//      Otsu o = new Otsu(med); //if binary img only
        int threshold = o.getThreshold();
        System.out.println("Threshold: " + threshold);
        BufferedImage thresh = o.applyThreshold();
        BufferedImage dilate = o.dilateImage(thresh);BufferedImage mask = o.applyMask(dilate); //replace white with orig img

            //ImageProcessing
            FeatureExtraction fe = new FeatureExtraction(mask, 8);
            fe.extract();

            System.out.println("Contrast: " + fe.getContrast());
            System.out.println("Correlation: " + fe.getCorrelation());
            System.out.println("Energy: " + fe.getEnergy());
            System.out.println("Entropy: " + fe.getEntropy());
            System.out.println("Homogeneity: " + fe.getHomogeneity());
            System.out.println("Mean: " + fe.getMean());
            System.out.println("Variance: " + fe.getVariance());
*/
}
