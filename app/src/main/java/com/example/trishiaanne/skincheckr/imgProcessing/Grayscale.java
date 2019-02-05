package com.example.trishiaanne.skincheckr.imgProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

public class Grayscale {

    public static int[][] imgToGrayPixels(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] grayscale = new int[width][height];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                //get the image's colors
                int rgb = image.getPixel(i, j);
                int newRed = Color.red(rgb);
                int newGreen = Color.green(rgb);
                int newBlue = Color.blue(rgb);
                //convert to grayscale
                int gray = (newRed + newGreen + newBlue) / 3;

                grayscale[i][j] = gray;
            }
        }

        return grayscale;
    }

    public static Bitmap toGray(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap grayscale = Bitmap.createBitmap(width, height, image.getConfig());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                int avg = (r + g + b) / 3;

                //replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                //int gray = (newRed + newGreen + newBlue) / 3;
                grayscale.setPixel(i, j, p);
            }
        }

        return grayscale;
    }
}
