package com.example.trishiaanne.skincheckr.imgProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorLong;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

class Grayscale {

    static Bitmap toGray(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap grayscale = Bitmap.createBitmap(width, height, image.getConfig());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);

                int a = Color.alpha(p);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                //calculate average
                int avg = (r + g + b) / 3;

                //replace RGB value with avg
//                p = Color.argb(a, (avg << 16), (avg << 8), avg);
                p = Color.argb(a, r, g, b);
                grayscale.setPixel(i, j, p);
            }
        }

        return grayscale;
    }
}
