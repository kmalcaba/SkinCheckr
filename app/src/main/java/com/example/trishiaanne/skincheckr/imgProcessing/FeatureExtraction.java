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
    private double ASM;

    private double meanX, meanY;
    private double varianceX, varianceY;
    private double correlation;

    private double sumAvg;
    private double sumVariance;
    private double sumEntropy;

    private double diffVariance;
    private double diffEntropy;

    private double infoMeasure1;
    private double infoMeasure2;


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
        this.ASM = (calcASM(cm0Norm) + calcASM(cm45Norm) + calcASM(cm90Norm) + calcASM(cm135Norm)) / 4;

        this.meanX = (calcMeanX(cm0Norm) + calcMeanX(cm45Norm) + calcMeanX(cm90Norm) + calcMeanX(cm135Norm)) / 4;
        this.meanY = (calcMeanY(cm0Norm) + calcMeanY(cm45Norm) + calcMeanY(cm90Norm) + calcMeanY(cm135Norm)) / 4;
        this.varianceX = (calcVarianceX(cm0Norm) + calcVarianceX(cm45Norm) + calcVarianceX(cm90Norm) + calcVarianceX(cm135Norm)) / 4;
        this.varianceY = (calcVarianceY(cm0Norm) + calcVarianceY(cm45Norm) + calcVarianceY(cm90Norm) + calcVarianceY(cm135Norm)) / 4;
        this.correlation = (calcCorrelation(cm0Norm) + calcCorrelation(cm45Norm) + calcCorrelation(cm90Norm) + calcCorrelation(cm135Norm)) / 4;

        this.sumAvg = (calcSumAvg(cm0Norm, calcSumXY(cm0Norm)) + calcSumAvg(cm45Norm, calcSumXY(cm45Norm))
                + calcSumAvg(cm90Norm, calcSumXY(cm90Norm)) + calcSumAvg(cm135Norm, calcSumXY(cm135Norm))) / 4;
        this.sumEntropy = (calcSumEntropy(cm0Norm, calcSumXY(cm0Norm)) + calcSumEntropy(cm45Norm, calcSumXY(cm45Norm))
                + calcSumEntropy(cm90Norm, calcSumXY(cm90Norm)) + calcSumEntropy(cm135Norm, calcSumXY(cm135Norm))) / 4;
        this.sumVariance = (calcSumVariance(cm0Norm, calcSumXY(cm0Norm)) + calcSumVariance(cm45Norm, calcSumXY(cm45Norm))
                + calcSumVariance(cm90Norm, calcSumXY(cm90Norm)) + calcSumVariance(cm135Norm, calcSumXY(cm135Norm))) / 4;

        this.diffEntropy = (calcDiffEntropy(cm0Norm, calcDiffXY(cm0Norm)) + calcDiffEntropy(cm45Norm, calcDiffXY(cm45Norm))
                + calcDiffEntropy(cm90Norm, calcDiffXY(cm90Norm)) + calcDiffEntropy(cm135Norm, calcDiffXY(cm135Norm))) / 4;
        this.diffVariance = (calcDiffVariance(cm0Norm, calcDiffXY(cm0Norm)) + calcDiffVariance(cm45Norm, calcDiffXY(cm45Norm))
                + calcDiffVariance(cm90Norm, calcDiffXY(cm90Norm)) + calcDiffVariance(cm135Norm, calcDiffXY(cm135Norm))) / 4;

        this.infoMeasure1 = (calcInfoMeasure1(cm0Norm, calcSumX(cm0Norm), calcSumY(cm0Norm))
                + calcInfoMeasure1(cm45Norm, calcSumX(cm45Norm), calcSumY(cm45Norm))
                + calcInfoMeasure1(cm90Norm, calcSumX(cm90Norm), calcSumY(cm90Norm))
                + calcInfoMeasure1(cm135Norm, calcSumX(cm135Norm), calcSumY(cm135Norm))) / 4;
        this.infoMeasure2 = (calcInfoMeasure2(cm0Norm, calcSumX(cm0Norm), calcSumY(cm0Norm))
                + calcInfoMeasure2(cm45Norm, calcSumX(cm45Norm), calcSumY(cm45Norm))
                + calcInfoMeasure2(cm90Norm, calcSumX(cm90Norm), calcSumY(cm90Norm))
                + calcInfoMeasure2(cm135Norm, calcSumX(cm135Norm), calcSumY(cm135Norm))) / 4;
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
    private double calcContrast(double[][] matrix) {
        double temp = 0;
        int n = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
//                temp += matrix[i][j] * Math.pow(i - j, 2);
                n = Math.abs(i - j);
                temp += Math.pow(n, 2) * matrix[i][j];
            }
        }
        return temp;
    }

    //weights are the inverse of the contrast weight
    //aka inverse difference moment
    private double calcHomogeneity(double[][] matrix) {
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
                    temp += (matrix[i][j] * Math.log10(matrix[i][j]));
                }
            }
        }

        temp *= -1;
        return temp;
    }

    //aka angular second moment
    private double calcASM(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += Math.pow(matrix[i][j], 2);
            }
        }
        return temp;
    }

    private double calcMeanX(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * i;
            }
        }
        return temp;
    }

    private double calcMeanY(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * j;
            }
        }
        return temp;
    }

    //standard deviation
    private double calcVarianceX(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * Math.pow(i - meanX, 2);
            }
        }

        temp = Math.sqrt(temp);
        return temp;
    }

    private double calcVarianceY(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp += matrix[i][j] * Math.pow(j - meanY, 2);
            }
        }

        temp = Math.sqrt(temp);
        return temp;
    }

    private double calcCorrelation(double[][] matrix) {
        double temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                //temp += matrix[i][j] * ( ((i-mean)*(j-mean)) / (Math.sqrt(Math.pow(variance,2))) );
                temp += matrix[i][j] * (((i - meanX) * (j - meanY)) / (varianceX * varianceY));
            }
        }
        return temp;
    }

    private double[] calcSumX(double[][] matrix) {
        double sum[] = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum[i] += matrix[i][j];
            }
        }
        return sum;
    }

    private double[] calcSumY(double[][] matrix) {
        double sum[] = new double[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum[j] += matrix[j][i];
            }
        }
        return sum;
    }

    private double[] calcSumXY(double[][] matrix) {
        double[] sum = new double[matrix.length * 2];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum[i + j] += matrix[i][j];
            }
        }

        return sum;
    }

    private double[] calcDiffXY(double[][] matrix) {
        double[] sum = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum[Math.abs(i - j)] += matrix[i][j];
            }
        }

        return sum;
    }

    private double calcSumAvg(double[][] matrix, double[] sumXY) {
        double temp = 0;
        for (int i = 0; i < sumXY.length; i++) {
            temp += i * sumXY[i];
        }
        return temp;
    }

    private double calcSumVariance(double[][] matrix, double[] sumXY) {
        double temp = 0;
        for (int i = 0; i < sumXY.length; i++) {
            if (sumXY[i] == 0) {
                continue;
            } else {
                temp += Math.pow(i - sumEntropy, 2) * sumXY[i];
            }
        }
        return temp;
    }

    private double calcSumEntropy(double[][] matrix, double[] sumXY) {
        double temp = 0;
        for (int i = 0; i < sumXY.length; i++) {
            if (sumXY[i] == 0) {
                continue;
            } else {
                temp += sumXY[i] * Math.log10(sumXY[i]);
            }
        }

        temp *= -1;
        return temp;
    }

    private double calcDiffVariance(double[][] matrix, double[] diffXY) {
        double temp = 0;
        for (int i = 0; i < diffXY.length; i++) {
            if (diffXY[i] == 0) {
                continue;
            } else {
                temp += Math.pow(i - diffEntropy, 2) * Math.abs(diffXY[i]);
            }
        }
        return temp;
    }

    private double calcDiffEntropy(double[][] matrix, double[] diffXY) {
        double temp = 0;
        for (int i = 0; i < diffXY.length; i++) {
            if (diffXY[i] == 0) {
                continue;
            } else {
                temp += diffXY[i] * Math.log10(diffXY[i]);
            }
        }

        temp *= -1;
        return temp;
    }

    private double calcEntropyP(double[] p) {
        double temp = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] == 0) {
                continue;
            } else {
                temp += p[i] * Math.log10(p[i]) * -1;
            }
        }
        return temp;
    }

    private double calcInfoMeasure1(double[][] matrix, double[] sumX, double[] sumY) {
        double HXY1 = 0;
        double HX = calcEntropyP(sumX);
        double HY = calcEntropyP(sumY);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (sumX[i] == 0 || sumY[j] == 0) {
                    continue;
                } else {
                    HXY1 += matrix[i][j] * Math.log10(sumX[i] * sumY[j]);
                }
            }
        }

        return (entropy - HXY1) / Math.max(HX, HY);
    }

    private double calcInfoMeasure2(double[][] matrix, double[] sumX, double[] sumY) {
        double HXY2 = 0, temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (sumX[i] == 0 || sumY[j] == 0) {
                    continue;
                } else {
                    HXY2 += sumX[i] * sumY[j] * Math.log10(sumX[i] * sumY[j]);
                }
            }
        }
        HXY2 *= -1;

        return Math.sqrt(1 - Math.exp(-2 * (HXY2 - entropy)));
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

    public double getASM() {
        return ASM;
    }

    public double getCorrelation() {
        return correlation;
    }

    /**
     * @return the sumAvg
     */
    public double getSumAvg() {
        return sumAvg;
    }

    /**
     * @return the sumVariance
     */
    public double getSumVariance() {
        return sumVariance;
    }

    /**
     * @return the sumEntropy
     */
    public double getSumEntropy() {
        return sumEntropy;
    }

    /**
     * @return the diffVariance
     */
    public double getDiffVariance() {
        return diffVariance;
    }

    /**
     * @return the diffEntropy
     */
    public double getDiffEntropy() {
        return diffEntropy;
    }

    /**
     * @return the infoMeasure1
     */
    public double getInfoMeasure1() {
        return infoMeasure1;
    }

    /**
     * @return the infoMeasure2
     */
    public double getInfoMeasure2() {
        return infoMeasure2;
    }
}
