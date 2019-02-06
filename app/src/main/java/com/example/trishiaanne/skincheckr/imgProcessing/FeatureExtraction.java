package com.example.trishiaanne.skincheckr.imgProcessing;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import java.io.IOException;

public class FeatureExtraction {

    private final Bitmap image;
    private final int[][] grayLevelMatrix;
    private final int grayLevel;
    private double contrast;
    private double homogeneity;
    private double entropy;
    private double energy;
    private double dissimilarity;
    private double mean;
    private double variance;
    private double correlation;

    public FeatureExtraction(Bitmap image, int grayLevel) throws IOException {
        this.image = image;
        this.grayLevel = grayLevel;
        //initialize matrix size
        grayLevelMatrix = new int[this.image.getWidth()][this.image.getHeight()];
    }

    public void extract() {
        this.createMatrix();

        //0 angle
        int[][] cm0 = createCoOccurrenceMatrix(0);
        double[][] cm0Norm = normalizeMatrix(add(cm0, transposeMatrix(cm0)));

        //45 angle
        int[][] cm45 = createCoOccurrenceMatrix(45);
        double[][] cm45Norm = normalizeMatrix(add(cm45, transposeMatrix(cm45)));

        //90 angle
        int[][] cm90 = createCoOccurrenceMatrix(90);
        double[][] cm90Norm = normalizeMatrix(add(cm90, transposeMatrix(cm90)));

        //135 angle
        int[][] cm135 = createCoOccurrenceMatrix(135);
        double[][] cm135Norm = normalizeMatrix(add(cm135, transposeMatrix(cm135)));

        //average of the four orientations
        this.contrast = (double) (calcContrast(cm0Norm) + calcContrast(cm45Norm) + calcContrast(cm90Norm) + calcContrast(cm135Norm)) / 4;
        this.homogeneity = (double) (calcHomogenity(cm0Norm) + calcHomogenity(cm45Norm) + calcHomogenity(cm90Norm) + calcHomogenity(cm135Norm)) / 4;
        this.entropy = (double) (calcEntropy(cm0Norm) + calcEntropy(cm45Norm) + calcEntropy(cm90Norm) + calcEntropy(cm135Norm)) / 4;
        this.energy = (double) (calcEnergy(cm0Norm) + calcEnergy(cm45Norm) + calcEnergy(cm90Norm) + calcEnergy(cm135Norm)) / 4;
        this.dissimilarity = (double) (calcDissimilarity(cm0Norm) + calcDissimilarity(cm45Norm) + calcDissimilarity(cm90Norm) + calcDissimilarity(cm135Norm)) / 4;
        this.mean = (double) (calcMean(cm0Norm) + calcMean(cm45Norm) + calcMean(cm90Norm) + calcMean(cm135Norm)) / 4;
        this.variance = (double) (calcVariance(cm0Norm) + calcVariance(cm45Norm) + calcVariance(cm90Norm) + calcVariance(cm135Norm)) / 4;
        this.correlation = (double) (calcCorrelation(cm0Norm) + calcCorrelation(cm45Norm) + calcCorrelation(cm90Norm) + calcCorrelation(cm135Norm)) / 4;
    }

    private void createMatrix() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                //get the image's colors
                int rgb = image.getPixel(i, j);
                int newRed = Color.red(rgb);
                int newGreen = Color.green(rgb);
                int newBlue = Color.blue(rgb);
                //convert to grayscale
                int grayscale = (newRed + newGreen + newBlue) / 3;

                //idk what this is for?
                if (grayLevel > 0 && grayLevel < 255) {
                    grayLevelMatrix[i][j] = grayscale * grayLevel / 255;
                } else {
                    grayLevelMatrix[i][j] = grayscale;
                }
            }
        }
    }

    //grayLevel = 8 bits
    //GLCM is created by making a scaled version of the image
    //the img is scaled to 8 gray-levels
    private int[][] createCoOccurrenceMatrix(int angle) {
        //distance = 1
        int[][] temp = new int[grayLevel + 1][grayLevel + 1];
        int startRow = 0;
        int startColumn = 0;
        int endColumn = 0;

        boolean isValidAngle = true;

        //determining the end column depending on the angle
        switch (angle) {
            case 0:
                startRow = 0;
                startColumn = 0;
                // ??????????????????
                endColumn = grayLevelMatrix[0].length - 2;
                break;
            case 45:
                startRow = 1;
                startColumn = 0;
                endColumn = grayLevelMatrix[0].length - 2;
                break;
            case 90:
                startRow = 1;
                startColumn = 0;
                endColumn = grayLevelMatrix[0].length - 1;
                break;
            case 135:
                startRow = 1;
                startColumn = 1;
                endColumn = grayLevelMatrix[0].length - 1;
                break;
            default:
                isValidAngle = false;
                break;
        }

        // wtf is thissssssssss
        if (isValidAngle) {
            for (int i = startRow; i < grayLevelMatrix.length; i++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    switch (angle) {
                        case 0:
                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i][j + 1]]++;
                            break;
                        case 45:
                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j + 1]]++;
                            break;
                        case 90:
                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j]]++;
                            break;
                        case 135:
                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j - 1]]++;
                            break;
                    }
                }
            }
        }

        return temp;
    }

    private int[][] transposeMatrix(int[][] m) {
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }

        return temp;
    }

    //add the matrix to its transpose to make it symmetrical
    private int[][] add(int[][] m2, int[][] m1) {
        int[][] temp = new int[m1[0].length][m1.length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                temp[j][i] = m1[i][j] + m2[i][j];
            }
        }

        return temp;
    }

    private int getTotal(int[][] m) {
        int temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp += m[i][j];
            }
        }

        return temp;
    }

    //normalizing involves dividing by the sum of values
    private double[][] normalizeMatrix(int[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        int total = getTotal(m);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = (double) m[i][j] / total;
            }
        }

        return temp;
    }

    /*

        STATISTICAL FEATURES

     */
    //multiply each cell by the weight = (x-y)^2
    //x is the cell's row and y is the cell's column
    //add all values
    private double calcContrast(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * Math.pow(i - j, 2);
            }
        }
        return temp;
    }

    //weights are the inverse of the contrast weight
    private double calcHomogenity(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] / (1 + Math.pow(i - j, 2));
            }
        }
        return temp;
    }

    private double calcEntropy(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    temp += (matrix[i][j] * Math.log10(matrix[i][j])) * -1;
                }
            }
        }
        return temp;
    }

    private double calcEnergy(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += Math.pow(matrix[i][j], 2);
            }
        }
        return temp;
    }

    //weights increase linearly
    private double calcDissimilarity(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * Math.abs(i - j);
            }
        }
        return temp;
    }

    private double calcMean(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * i;
            }
        }
        return temp;
    }

    //standard deviation
    private double calcVariance(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * Math.pow(i - mean, 2);
            }
        }

        //temp = Math.sqrt(temp);
        return temp;
    }

    private double calcCorrelation(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                //temp += matrix[i][j] * ( ((i-mean)*(j-mean)) / (Math.sqrt(Math.pow(variance,2))) );
                temp += matrix[i][j] * (((i - mean) * (j - mean)) / (variance));
            }
        }
        return temp;
    }

    public double getContrast() {
        return contrast;
    }

    public double getHomogeneity() {
        return homogeneity;
    }

    public double getEntropy() {
        return entropy;
    }

    public double getEnergy() {
        return energy;
    }

    public double getDissimilarity() {
        return dissimilarity;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double getCorrelation() {
        return correlation;
    }
}
