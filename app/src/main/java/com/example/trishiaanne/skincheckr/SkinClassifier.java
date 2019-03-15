package com.example.trishiaanne.skincheckr;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SkinClassifier implements Classifier {
    private static final String MODEL_PATH = "skin.pb";
    private static final String LABEL_PATH = "skin_labels.txt";
    private static final int RESULTS_TO_SHOW = 2;
    private static final String INPUT_NAME = "input_input_2";
    private static final String OUTPUT_NAME = "output_2/Softmax";
    private static final String [] OUTPUTS = {OUTPUT_NAME};

    private static final int INPUT_SIZE = 3;
    private static final float THRESHOLD = 0.1f;

    private TensorFlowInferenceInterface inferenceInterface;

    private SkinClassifier() {}

    private static List<String> labelList;
    private float [] outputs = null;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels
            = new PriorityQueue<>(
            RESULTS_TO_SHOW, new Comparator<Map.Entry<String, Float>>() {
        @Override
        public int compare(Map.Entry<String, Float> t1, Map.Entry<String, Float> t2) {
            return (t1.getValue().compareTo(t2.getValue()));
        }
    });

    public static Classifier create(Activity a, AssetManager assetManager){
        SkinClassifier skinClassifier = new SkinClassifier();
        labelList = loadLabelList(a);

        skinClassifier.inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_PATH);

        final Operation o = skinClassifier.inferenceInterface.graphOperation(OUTPUT_NAME);
        final int numClasses = (int) o.output(0).shape().size(1);
        Log.i("Classifier: ", "Read " + labelList.size() + " labels, output layer size is " + numClasses);

        skinClassifier.outputs = new float[numClasses];
        return skinClassifier;
    }

    private static List<String> loadLabelList(Activity activity) {
        List<String> labelList = new ArrayList<String>();
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
            String line = "";
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
        return null;
    }

    @Override
    public List<Recognition> recognizeSkin(float [] inputs) {
        inferenceInterface.feed(INPUT_NAME, inputs, 1, INPUT_SIZE);
        inferenceInterface.run(OUTPUTS);
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
    public void enableStatLogging(boolean debug) {

    }

    @Override
    public String getStatString() {
        return null;
    }

    @Override
    public void close() {

    }
}
