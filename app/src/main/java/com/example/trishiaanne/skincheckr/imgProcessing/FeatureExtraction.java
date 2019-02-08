package com.example.trishiaanne.skincheckr.imgProcessing;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

class FeatureExtraction {

    private final int GRAY_LEVEL = 8;

    private final double[][] grayLevelMatrix;

    private Bitmap image;

    private double contrast;
    private double homogeneity;
    private double entropy;
    private double energy;
    private double mean;
    private double variance;
    private double correlation;

    FeatureExtraction(Bitmap image) {
        //initialize matrix size
        this.image = image;
        grayLevelMatrix = new double[this.image.getWidth()][this.image.getHeight()];
    }

    void extract() {
        this.createMatrix();

        //0 angle
        double[][] cm0 = createCoOccurrenceMatrix(0);
        double[][] cm0Norm = normalizeMatrix(add(cm0, transposeMatrix(cm0)));

        //45 angle
        double[][] cm45 = createCoOccurrenceMatrix(45);
        double[][] cm45Norm = normalizeMatrix(add(cm45, transposeMatrix(cm45)));

        //90 angle
        double[][] cm90 = createCoOccurrenceMatrix(90);
        double[][] cm90Norm = normalizeMatrix(add(cm90, transposeMatrix(cm90)));

        //135 angle
        double[][] cm135 = createCoOccurrenceMatrix(135);
        double[][] cm135Norm = normalizeMatrix(add(cm135, transposeMatrix(cm135)));

        //average of the four orientations
        this.contrast = (calcContrast(cm0Norm) + calcContrast(cm45Norm) + calcContrast(cm90Norm) + calcContrast(cm135Norm)) / 4;
        this.homogeneity = (calcHomogeneity(cm0Norm) + calcHomogeneity(cm45Norm) + calcHomogeneity(cm90Norm) + calcHomogeneity(cm135Norm)) / 4;
        this.entropy = (calcEntropy(cm0Norm) + calcEntropy(cm45Norm) + calcEntropy(cm90Norm) + calcEntropy(cm135Norm)) / 4;
        this.energy = (calcEnergy(cm0Norm) + calcEnergy(cm45Norm) + calcEnergy(cm90Norm) + calcEnergy(cm135Norm)) / 4;
        this.mean = (calcMean(cm0Norm) + calcMean(cm45Norm) + calcMean(cm90Norm) + calcMean(cm135Norm)) / 4;
        this.variance = (calcVariance(cm0Norm) + calcVariance(cm45Norm) + calcVariance(cm90Norm) + calcVariance(cm135Norm)) / 4;
        this.correlation = (calcCorrelation(cm0Norm) + calcCorrelation(cm45Norm) + calcCorrelation(cm90Norm) + calcCorrelation(cm135Norm)) / 4;
    }

    private void createMatrix() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                //get the image's colors
                int rgb = image.getPixel(i, j);
                int r = Color.red(rgb);
                int g = Color.green(rgb);
                int b = Color.blue(rgb);
                //convert to grayscale
                int grayscale = (r + g + b) / 3;

                grayLevelMatrix[i][j] = (double) grayscale * GRAY_LEVEL / 255;
            }
        }
    }

    private double[][] createCoOccurrenceMatrix(int angle) {
        //distance = 1
        double[][] temp = new double[GRAY_LEVEL + 1][GRAY_LEVEL + 1];
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
                            temp[(int)grayLevelMatrix[i][j]][(int)grayLevelMatrix[i][j + 1]]++;
                            break;
                        case 45:
                            temp[(int)grayLevelMatrix[i][j]][(int)grayLevelMatrix[i - 1][j + 1]]++;
                            break;
                        case 90:
                            temp[(int)grayLevelMatrix[i][j]][(int)grayLevelMatrix[i - 1][j]]++;
                            break;
                        case 135:
                            temp[(int)grayLevelMatrix[i][j]][(int)grayLevelMatrix[i - 1][j - 1]]++;
                            break;
                    }
                }
            }
        }

        return temp;
    }

    private double[][] transposeMatrix(double[][] grayLevelMatrix) {
        double[][] temp = new double[grayLevelMatrix[0].length][grayLevelMatrix.length];
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp[j][i] = grayLevelMatrix[i][j];
            }
        }

        return temp;
    }

    //add the matrix to its transpose to make it symmetrical
    private double[][] add(double[][] m2, double[][] m1) {
        double[][] temp = new double[m1[0].length][m1.length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                temp[j][i] = m1[i][j] + m2[i][j];
            }
        }

        return temp;
    }

    private double getTotal(double [][] m) {
        double temp = 0;
        for (double[] m1 : m) {
            for (double m2: m1) {
                temp += m2;
            }
        }

        return temp;
    }

    //normalizing involves dividing by the sum of values
    private double[][] normalizeMatrix(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        double total = getTotal(m);
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
    private double calcContrast(double[][] m) {
        double temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp += m[i][j] * Math.pow(i - j, 2);
            }
        }
        return temp;
    }

    //weights are the inverse of the contrast weight
    private double calcHomogeneity(double [][] m) {
        double temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp += m[i][j] / (1 + Math.pow(i - j, 2));
            }
        }
        return temp;
    }

    private double calcEntropy(double [][] m) {
        double temp = 0;
        for (double[] m1 : m) {
            for (double m2 : m1) {
                if (m2 != 0) {
                    temp += (m2 * Math.log10(m2)) * -1;
                }
            }
        }
        return temp;
    }

    private double calcEnergy(double [][] m) {
        double temp = 0;
        for (double[] m1 : m) {
            for (int j = 0; j < m[0].length; j++) {
                temp += Math.pow(m1[j], 2);
            }
        }
        return temp;
    }

    private double calcMean(double [][] m) {
        double temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp += m[i][j] * i;
            }
        }
        return temp;
    }

    //standard deviation
    private double calcVariance(double [][] m) {
        double temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp += m[i][j] * Math.pow(i - mean, 2);
            }
        }

        //temp = Math.sqrt(temp);
        return temp;
    }

    private double calcCorrelation(double [][] m) {
        double temp = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                //temp += m[i][j] * ( ((i-mean)*(j-mean)) / (Math.sqrt(Math.pow(variance,2))) );
                temp += m[i][j] * (((i - mean) * (j - mean)) / (variance));
            }
        }
        return temp;
    }

    double getContrast() {
        return contrast;
    }

    double getHomogeneity() {
        return homogeneity;
    }

    double getEntropy() {
        return entropy;
    }

    double getEnergy() {
        return energy;
    }

    double getMean() {
        return mean;
    }

    double getVariance() {
        return variance;
    }

    double getCorrelation() {
        return correlation;
    }
}
