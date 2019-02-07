package com.example.trishiaanne.skincheckr;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ImageClassifier {

    private static final String TAG = "SkinCheckr";
    private static final String MODEL_PATH = "model.tflite";
    private static final String LABEL_PATH = "labels.txt";
    private static final int RESULTS_TO_SHOW = 3;

    private Interpreter tflite;

    private List<String> labelList;
    private float[][] outputs = null;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels
            = new PriorityQueue<>(
            RESULTS_TO_SHOW, new Comparator<Map.Entry<String, Float>>() {
        @Override
        public int compare(Map.Entry<String, Float> t1, Map.Entry<String, Float> t2) {
            return (t1.getValue().compareTo(t2.getValue()));
        }
    });

    public ImageClassifier(Activity activity) throws IOException {
        tflite = new Interpreter(loadModelFile(activity));
        labelList = loadLabelList(activity);
        outputs = new float[1][labelList.size()];
    }

    private List<String> loadLabelList(Activity activity) {
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

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float[][] getOutputs(float [][] inputs) {
        tflite.run(inputs, outputs);
        return outputs;
    }

    public void close() {
        tflite.close();
        tflite = null;
    }

    private String printTopLabels() {
        for (int i = 0; i < labelList.size(); i++) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<String, Float>(labelList.get(i), outputs[0][i])
            );
            if (sortedLabels.size() > RESULTS_TO_SHOW)
                sortedLabels.poll();
        }
        String topLabels = "";
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLabels = String.format("\n%s: %4.2f %s", label.getKey(), label.getValue()) + topLabels;
        }
        return topLabels;
    }
}
