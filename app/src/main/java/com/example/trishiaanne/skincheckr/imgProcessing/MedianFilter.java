package com.example.trishiaanne.skincheckr.imgProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

public class MedianFilter {

    private static int[] createArray(Bitmap image, int x, int y) {
        int h = image.getHeight();
        int w = image.getWidth();

        int window = 5;

        int xmin = x - window / 2;
        int xmax = x + window / 2;
        int ymin = y - window / 2;
        int ymax = y + window / 2;

        //edge cases
        if (xmin < 0)
            xmin = 0;
        if (xmax > (w - 1))
            xmax = w - 1;
        if (ymin < 0)
            ymin = 0;
        if (ymax > (h - 1))
            ymax = h - 1;

        int arrSize = (xmax - xmin + 1) * (ymax - ymin + 1);
        int[] array = new int[arrSize];

        //get neighboring pixel values
        int k = 0;
        for (int i = xmin; i <= xmax; i++) {
            for (int j = ymin; j <= ymax; j++) {
                array[k] = image.getPixel(i, j);
                k++;
            }
        }

        return array;
    }

    public static Bitmap filter(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] m;
        //Bitmap filteredImg = new Bitmap(width, height, BufferedImage.TYPE_INT_RGB);
        Bitmap filteredImg = Bitmap.createBitmap(width, height, image.getConfig());
        image = Grayscale.toGray(image);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                m = createArray(image, i, j);
                int[] red = new int[m.length];
                int[] green = new int[m.length];
                int[] blue = new int[m.length];
                int r, g, b;

                for (int k = 0; k < m.length; k++) {
                    red[k] = Color.red(m[k]);
                    green[k] = Color.green(m[k]);
                    blue[k] = Color.blue(m[k]);
                }

                Arrays.sort(red);
                Arrays.sort(green);
                Arrays.sort(blue);

                r = red[m.length / 2];
                g = green[m.length / 2];
                b = blue[m.length / 2];

                int rgb = Color.rgb(r, g, b);
                filteredImg.setPixel(i, j, rgb);
            }
        }

        return filteredImg;
    }

}
