package com.example.trishiaanne.skincheckr.imgProcessing;

/**
 * Created by RenzAlbandia on 2/6/2019.
 */

import android.graphics.Bitmap;

public class FeatureExtraction {

    private final int GRAY_LEVEL = 32;
    private final int GRAY_RANGE = 256;

    private final Bitmap image;
    private final double[][] grayLevelMatrix;
    private final int distance = 1;
    private final int[] grayValue;

    public static byte[] imageArray;

    private double grayscale;
    private double meanGray;
    private double [] histogram;

    private double contrast;
    private double homogeneity;
    private double entropy;
    private double energy;
    private double dissimilarity;
    private double mean;
    private double variance;
    private double correlation;

    public FeatureExtraction(Bitmap image) {
        this.image = image;
        //initialize matrix size
//        grayLevelMatrix = new int[this.image.getWidth()][this.image.getHeight()];
        grayLevelMatrix = new double[GRAY_LEVEL][GRAY_LEVEL];
        grayValue = new int[image.getWidth() * image.getHeight()];
        grayscale = (double) GRAY_RANGE / (double) GRAY_LEVEL;
        histogram = new double[GRAY_RANGE];
    }

    private void calculateGrayValues() {
        final int size = grayValue.length;
        double graySum = 0;
        for (int i = 0; i < size; i++) {
            int gray = imageArray[i]&0xff;
            graySum += gray;
            grayValue[i] = (int) (gray / grayscale);
            assert grayValue[i] >= 0 : grayValue[i] + " > 0 violated";
            histogram[gray]++;
        }
        for (int j = 0; j < histogram.length; j++)
            histogram[j] = histogram[j] / size;
        meanGray = Math.floor(graySum / size / grayscale) * grayscale;
    }


    public void extract() {
        calculateGrayValues();
        normalizeMatrix();

        contrast = calcContrast();
        homogeneity = calcHomogenity();
        entropy = calcEntropy();
        energy = calcEnergy();
        mean = calcMean();
        variance = calcVariance();
        correlation = calcCorrelation();


//        this.createMatrix();
//
//        //0 angle
//        int[][] cm0 = createCoOccurrenceMatrix(0);
//        double[][] cm0Norm = normalizeMatrix(add(cm0, transposeMatrix(cm0)));
//
//        //45 angle
//        int[][] cm45 = createCoOccurrenceMatrix(45);
//        double[][] cm45Norm = normalizeMatrix(add(cm45, transposeMatrix(cm45)));
//
//        //90 angle
//        int[][] cm90 = createCoOccurrenceMatrix(90);
//        double[][] cm90Norm = normalizeMatrix(add(cm90, transposeMatrix(cm90)));
//
//        //135 angle
//        int[][] cm135 = createCoOccurrenceMatrix(135);
//        double[][] cm135Norm = normalizeMatrix(add(cm135, transposeMatrix(cm135)));
//
//        //average of the four orientations
//        this.contrast = (double) (calcContrast(cm0Norm) + calcContrast(cm45Norm) + calcContrast(cm90Norm) + calcContrast(cm135Norm)) / 4;
//        this.homogeneity = (double) (calcHomogenity(cm0Norm) + calcHomogenity(cm45Norm) + calcHomogenity(cm90Norm) + calcHomogenity(cm135Norm)) / 4;
//        this.entropy = (double) (calcEntropy(cm0Norm) + calcEntropy(cm45Norm) + calcEntropy(cm90Norm) + calcEntropy(cm135Norm)) / 4;
//        this.energy = (double) (calcEnergy(cm0Norm) + calcEnergy(cm45Norm) + calcEnergy(cm90Norm) + calcEnergy(cm135Norm)) / 4;
//        this.dissimilarity = (double) (calcDissimilarity(cm0Norm) + calcDissimilarity(cm45Norm) + calcDissimilarity(cm90Norm) + calcDissimilarity(cm135Norm)) / 4;
//        this.mean = (double) (calcMean(cm0Norm) + calcMean(cm45Norm) + calcMean(cm90Norm) + calcMean(cm135Norm)) / 4;
//        this.variance = (double) (calcVariance(cm0Norm) + calcVariance(cm45Norm) + calcVariance(cm90Norm) + calcVariance(cm135Norm)) / 4;
//        this.correlation = (double) (calcCorrelation(cm0Norm) + calcCorrelation(cm45Norm) + calcCorrelation(cm90Norm) + calcCorrelation(cm135Norm)) / 4;
    }
//
//    private void createMatrix() {
//        for (int i = 0; i < image.getWidth(); i++) {
//            for (int j = 0; j < image.getHeight(); j++) {
//                //get the image's colors
//                int rgb = image.getPixel(i, j);
//                int r = (rgb >> 16) & 0xff;
//                int g = (rgb >> 8) & 0xff;
//                int b = rgb & 0xff;
////                int newRed = Color.red(rgb);
////                int newGreen = Color.green(rgb);
////                int newBlue = Color.blue(rgb);
//                //convert to grayscale
//                int grayscale = (r + g + b) / 3;
//
//                grayLevelMatrix[i][j] = (double) grayscale * GRAY_LEVEL / 255;
//            }
//        }
//    }
//
//    //GRAY_LEVEL = 8 bits
//    //GLCM is created by making a scaled version of the image
//    //the img is scaled to 8 gray-levels
//    private int[][] createCoOccurrenceMatrix(int angle) {
//        //distance = 1
//        int[][] temp = new int[GRAY_LEVEL + 1][GRAY_LEVEL + 1];
//        int startRow = 0;
//        int startColumn = 0;
//        int endColumn = 0;
//
//        boolean isValidAngle = true;
//
//        //determining the end column depending on the angle
//        switch (angle) {
//            case 0:
//                startRow = 0;
//                startColumn = 0;
//                // ??????????????????
//                endColumn = grayLevelMatrix[0].length - 2;
//                break;
//            case 45:
//                startRow = 1;
//                startColumn = 0;
//                endColumn = grayLevelMatrix[0].length - 2;
//                break;
//            case 90:
//                startRow = 1;
//                startColumn = 0;
//                endColumn = grayLevelMatrix[0].length - 1;
//                break;
//            case 135:
//                startRow = 1;
//                startColumn = 1;
//                endColumn = grayLevelMatrix[0].length - 1;
//                break;
//            default:
//                isValidAngle = false;
//                break;
//        }
//
//        // wtf is thissssssssss
//        if (isValidAngle) {
//            for (int i = startRow; i < grayLevelMatrix.length; i++) {
//                for (int j = startColumn; j <= endColumn; j++) {
//                    switch (angle) {
//                        case 0:
//                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i][j + 1]]++;
//                            break;
//                        case 45:
//                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j + 1]]++;
//                            break;
//                        case 90:
//                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j]]++;
//                            break;
//                        case 135:
//                            temp[grayLevelMatrix[i][j]][grayLevelMatrix[i - 1][j - 1]]++;
//                            break;
//                    }
//                }
//            }
//        }
//
//        return temp;
//    }
//
//    private double[][] transposeMatrix(double[][] grayLevelMatrix) {
//        double[][] temp = new double[grayLevelMatrix[0].length][grayLevelMatrix.length];
//        for (int i = 0; i < grayLevelMatrix.length; i++) {
//            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
//                temp[j][i] = grayLevelMatrix[i][j];
//            }
//        }
//
//        return temp;
//    }
//
//    //add the matrix to its transpose to make it symmetrical
//    private int[][] add(int[][] m2, int[][] m1) {
//        int[][] temp = new int[m1[0].length][m1.length];
//        for (int i = 0; i < m1.length; i++) {
//            for (int j = 0; j < m1[0].length; j++) {
//                temp[j][i] = m1[i][j] + m2[i][j];
//            }
//        }
//
//        return temp;
//    }
//
    private double getTotal() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += grayLevelMatrix[i][j];
            }
        }

        return temp;
    }

    //normalizing involves dividing by the sum of values
    private double[][] normalizeMatrix() {
        double[][] temp = new double[grayLevelMatrix[0].length][grayLevelMatrix.length];
        double total = getTotal();
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp[j][i] = (double) grayLevelMatrix[i][j] / total;
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
    private double calcContrast() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += grayLevelMatrix[i][j] * Math.pow(i - j, 2);
            }
        }
        return temp;
    }

    //weights are the inverse of the contrast weight
    private double calcHomogenity() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += grayLevelMatrix[i][j] / (1 + Math.pow(i - j, 2));
            }
        }
        return temp;
    }

    private double calcEntropy() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                if (grayLevelMatrix[i][j] != 0) {
                    temp += (grayLevelMatrix[i][j] * Math.log10(grayLevelMatrix[i][j])) * -1;
                }
            }
        }
        return temp;
    }

    private double calcEnergy() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += Math.pow(grayLevelMatrix[i][j], 2);
            }
        }
        return temp;
    }

    //weights increase linearly
//    private double calcDissimilarity(double[][] matrix) {
//        double temp = 0;
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[0].length; j++) {
//                temp += matrix[i][j] * Math.abs(i - j);
//            }
//        }
//        return temp;
//    }

    private double calcMean() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += grayLevelMatrix[i][j] * i;
            }
        }
        return temp;
    }

    //standard deviation
    private double calcVariance() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                temp += grayLevelMatrix[i][j] * Math.pow(i - mean, 2);
            }
        }

        //temp = Math.sqrt(temp);
        return temp;
    }

    private double calcCorrelation() {
        double temp = 0;
        for (int i = 0; i < grayLevelMatrix.length; i++) {
            for (int j = 0; j < grayLevelMatrix[0].length; j++) {
                //temp += grayLevelMatrix[i][j] * ( ((i-mean)*(j-mean)) / (Math.sqrt(Math.pow(variance,2))) );
                temp += grayLevelMatrix[i][j] * (((i - mean) * (j - mean)) / (variance));
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
