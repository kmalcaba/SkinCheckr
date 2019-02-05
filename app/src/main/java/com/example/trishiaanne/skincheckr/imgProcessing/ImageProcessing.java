package com.example.trishiaanne.skincheckr.imgProcessing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 *
 * @author Kirsten A. Malcaba
 */
public class ImageProcessing extends AppCompatActivity{
    int days;
    int itch;
    int scale;
    int burn;
    int sweat;
    int crust;
    int bleed;
    int disease;
    String path="";


    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        path = i.getStringExtra("path_value");
        displayMessage(getBaseContext(),"PATH: " + path);
        Log.i("PATH: ",path);

    }
    /*
        String path_value = Intent.getIntentOld("")
        //img pro
        //median filter - noise reduction
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
