package com.example.trishiaanne.skincheckr;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import com.example.trishiaanne.skincheckr.R;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Model {
    Activity a;
    //protected Interpreter tflite = new Interpreter(loadModelFile(a));
    //to put in an activity file
    //then run with tflite.run(inputs, outputs)

    public Model(Activity activity) {
        a = activity;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
