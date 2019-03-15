package com.example.trishiaanne.skincheckr;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ImageClassifier implements Classifier {

    private static final String MODEL_PATH = "retrained_new.pb";
    private static final String LABEL_PATH = "retrained_labels.txt";
    private static final int RESULTS_TO_SHOW = 3;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";
    private static final String [] OUTPUTS = {OUTPUT_NAME};

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final float THRESHOLD = 0.1f;

    private TensorFlowInferenceInterface inferenceInterface;

    private ImageClassifier() {}

    private static List<String> labelList;
    private float[] outputs = null;
    private int[] intValues = null;
    private float[] floatValues = null;

    public static Classifier create(Activity a, AssetManager assetManager){
        ImageClassifier imageClassifier = new ImageClassifier();
        labelList = loadLabelList(a);

        imageClassifier.inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_PATH);

        final Operation o = imageClassifier.inferenceInterface.graphOperation(OUTPUT_NAME);
        final int numClasses = (int) o.output(0).shape().size(1);
        Log.i("Classifier: ", "Read " + labelList.size() + " labels, output layer size is " + numClasses);

        imageClassifier.intValues = new int[INPUT_SIZE * INPUT_SIZE];
        imageClassifier.floatValues = new float [INPUT_SIZE * INPUT_SIZE * 3];
        imageClassifier.outputs = new float[numClasses];
        return imageClassifier;
    }

    private static List<String> loadLabelList(Activity activity) {
        List<String> labelList = new ArrayList<String>();
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
            String line;
            while ((line = b.readLine()) != null)
                labelList.add(line);
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labelList;
    }


    @Override
    public List<Recognition> recognizeImage(Bitmap bitmap) {

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for(int i = 0; i < intValues.length; i++) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
            floatValues[i * 3 + 2] = ((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
        }

        inferenceInterface.feed(INPUT_NAME, floatValues, 1, INPUT_SIZE, INPUT_SIZE, 3);
        inferenceInterface.run(OUTPUTS, true);
        inferenceInterface.fetch(OUTPUT_NAME, outputs);

        PriorityQueue<Recognition> predictions = new PriorityQueue<>(
                RESULTS_TO_SHOW, new Comparator<Recognition>() {
            @Override
            public int compare(Recognition lhs, Recognition rhs) {
                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
            }
        }
        );

        for(int i = 0; i < outputs.length; i++) {
            if(outputs[i] > THRESHOLD) {
                predictions.add(new Recognition(
                        "" + i, labelList.size() > i ? labelList.get(i) : "unknown", outputs[i], null
                ));
            }
        }

        final ArrayList<Recognition> topResults = new ArrayList<>();
        int size = Math.min(predictions.size(), RESULTS_TO_SHOW);
        for (int i = 0; i < size; i++) {
            topResults.add(predictions.poll());
        }

        return topResults;
    }

    @Override
    public List<Recognition> recognizeSkin(float [] inputs) {
        return null;
    }

    @Override
    public void enableStatLogging(boolean debug) {

    }

    @Override
    public String getStatString() {
        return inferenceInterface.getStatString();
    }

    @Override
    public void close() {
        inferenceInterface.close();
        inferenceInterface = null;
    }
}
