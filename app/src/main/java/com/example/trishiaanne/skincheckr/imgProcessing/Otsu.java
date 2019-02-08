package com.example.trishiaanne.skincheckr.imgProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

public class Otsu {
    private final int[][] pixels;
    private final Bitmap origImage;
    private int threshold;

    public Otsu(Bitmap image) {
        origImage = Bitmap.createBitmap(image);
        pixels = createMatrix(image);
        threshold(pixels);
    }

    Otsu(Bitmap grayImg, Bitmap srcImg) {
        origImage = srcImg.copy(srcImg.getConfig(), srcImg.isMutable());
        pixels = createMatrix(grayImg);

        threshold(pixels);
    }

    private int[][] createMatrix(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] matrix = new int[width][height];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int rgb = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
                matrix[i][j] = rgb / (65536);
            }
        }

        return matrix;
    }

    private void threshold(int[][] pixels) {
        // create a histogram out of the pixels
        int[] hist = histogram(pixels);

        // get sum of all pixel intensities
        int sum = sumIntensities(hist);

        // perform Otsu's method
        calcThreshold(hist, pixels.length * pixels[0].length, sum);
    }

    private int[] histogram(int[][] pixels) {
        int[] hist = new int[256];

        for (int[] pixel : pixels) {
            for (int pix: pixel) {
                hist[pixel[pix]]++;
            }
        }

        return hist;
    }

    private int sumIntensities(int[] hist) {
        int sum = 0;
        for (int i = 0; i < hist.length; i++) {
            sum += i * hist[i];
        }
        return sum;
    }

    private void calcThreshold(int[] hist, int N, int sum) {
        double variance;                       // objective function to maximize
        double bestVariance = Double.NEGATIVE_INFINITY;

        double mean_bg = 0;
        double weight_bg = 0;

        double mean_fg = (double) sum / (double) N;     // mean of population
        double weight_fg = N;                           // weight of population

        double diff_means;

        // loop through all candidate thresholds
        int t = 0;
        while (t < (255)) {
            // calculate variance
            diff_means = mean_fg - mean_bg;
            variance = weight_bg * weight_fg * diff_means * diff_means;

            // store best threshold
            if (variance > bestVariance) {
                bestVariance = variance;
                threshold = t;
            }

            // go to next candidate threshold
            while (t < 255 && hist[t] == 0) {
                t++;
            }

            mean_bg = (mean_bg * weight_bg + hist[t] * t) / (weight_bg + hist[t]);
            mean_fg = (mean_fg * weight_fg - hist[t] * t) / (weight_fg - hist[t]);
            weight_bg += hist[t];
            weight_fg -= hist[t];
            t++;
        }

        //threshold manipulation
        //increases the threshold if it is too small
        //a small threshold does not capture many details
        if (threshold <= 10) {
            threshold += 100;
        } else if (threshold <= 50) {
            threshold += 50;
        } else if (threshold <= 100) {
            threshold *= 1.5;
        }
    }

    Bitmap applyThreshold() {
        Bitmap img = Bitmap.createBitmap(pixels.length, pixels[0].length, origImage.getConfig());
        int white = Color.rgb(255, 255, 255);
        int center = pixels[(pixels.length - 1) / 2][(pixels[0].length - 1) / 2] - 10;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {

                if (center <= threshold) {
                    if (pixels[i][j] >= threshold)
                        img.setPixel(i, j, 0); //background
                    else
                        img.setPixel(i, j, white); //foreground
                } else {
                    //foreground
                    if (pixels[i][j] >= threshold)
                        img.setPixel(i, j, white); // foreground
                    else
                        img.setPixel(i, j, 0); //background
                }
            }
        }

        return img;
    }

    Bitmap dilateImage(Bitmap binaryImg) {
        Bitmap img = Bitmap.createBitmap(pixels.length, pixels[0].length, origImage.getConfig());
        int white = Color.rgb(255, 255, 255);

        for (int i = 0; i < binaryImg.getWidth(); i++) {
            for (int j = 0; j < binaryImg.getHeight(); j++) {
                if (binaryImg.getPixel(i, j) == white) {
                    img.setPixel(i, j, white);
                    if (i > 1) {
                        img.setPixel(i - 2, j, white);
                    }
                    if (j > 1) {
                        img.setPixel(i, j - 2, white);
                    }
                    if (i + 2 < pixels.length) {
                        img.setPixel(i + 2, j, white);
                    }
                    if (j + 2 < pixels[0].length) {
                        img.setPixel(i, j + 2, white);
                    }
                } else {
                    img.setPixel(i, j, 0);
                }
            }
        }

        return img;
    }

    Bitmap applyMask(Bitmap mask) {
        Bitmap img = Bitmap.createBitmap(origImage.getWidth(), origImage.getHeight(), origImage.getConfig());
        int white = Color.rgb(255, 255, 255);

        for (int i = 0; i < mask.getWidth(); i++) {
            for (int j = 0; j < mask.getHeight(); j++) {
                if (mask.getPixel(i, j) == white) {
//                  original color threshold
                    img.setPixel(i, j, origImage.getPixel(i, j));
                }
            }
        }

        return img;
    }

    int getThreshold(){
        return threshold;
    }

}
