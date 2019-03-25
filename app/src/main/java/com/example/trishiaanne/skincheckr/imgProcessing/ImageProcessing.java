package com.example.trishiaanne.skincheckr.imgProcessing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.Camera;
import com.example.trishiaanne.skincheckr.Classifier;
import com.example.trishiaanne.skincheckr.GuestResult;
import com.example.trishiaanne.skincheckr.History;
import com.example.trishiaanne.skincheckr.ImageClassifier;
import com.example.trishiaanne.skincheckr.MainActivity;
import com.example.trishiaanne.skincheckr.R;
import com.example.trishiaanne.skincheckr.Result;
import com.example.trishiaanne.skincheckr.ReviewHistory;
import com.example.trishiaanne.skincheckr.SkinClassifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirsten A. Malcaba
 */
public class ImageProcessing extends AppCompatActivity {
    private String capturePath;
    private String importPath;
    private Bitmap chosenImage;
    private String chosenImagePath;
    private static int TYPE_OF_USER;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private ImageView imageView;
    private Button confirmPhoto;

    private ArrayList<String> diagnosed = new ArrayList<>();
    private ArrayList<String> percentage = new ArrayList<>();

    ProgressDialog progressDialog;

    Classifier imageClassifier;

//    float[] inputs;

    boolean isAccurate = true;

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        imageClassifier = null;
        if(chosenImage != null) {
            chosenImage.recycle();
            chosenImage = null;
        }
        chosenImagePath = null;

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        imageView = findViewById(R.id.imageView2);
        confirmPhoto = findViewById(R.id.imgProButton);

        //get the user type
        TYPE_OF_USER = getIntent().getExtras().getInt("user_type");

        getImage();

        /*

                IMAGE PROCESSING

         */

        confirmPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Load().execute();
            }
        });
    }

    public void processImage(Bitmap img) {
        final List<Classifier.Recognition> results = imageClassifier.recognizeImage(img);
        for (Classifier.Recognition r : results) {
            Log.i("Results: ", r.getTitle() + " " + (r.getConfidence() * 100));
            diagnosed.add(r.getTitle().toLowerCase());
            percentage.add((r.getConfidence()).toString());
        }
    }

    class Load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ImageProcessing.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            Bitmap original = BitmapFactory.decodeFile(chosenImagePath);
            imageClassifier = ImageClassifier.create(ImageProcessing.this, getAssets());


            long startTime = SystemClock.uptimeMillis();

            //change dimensions
            final int maxWidth = 640;
            int outWidth;
            int outHeight;
            int inWidth = original.getWidth();
            int inHeight = original.getHeight();

            Bitmap img;

            if (original.getWidth() > maxWidth) {
                if (inWidth > inHeight) {
                    outWidth = maxWidth;
                    outHeight = (inHeight * maxWidth) / inWidth;
                } else {
                    outHeight = maxWidth;
                    outWidth = (inWidth * maxWidth) / inHeight;
                }

                Bitmap resized = Bitmap.createScaledBitmap(original, outWidth, outHeight, false);
                MediaStore.Images.Media.insertImage(getContentResolver(), resized, "RESIZED_IMG", "SAMPLE");

                //Compress image
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                Log.e("Original   dimensions", original.getWidth() + " " + original.getHeight());
                Log.e("Compressed dimensions", decoded.getWidth() + " " + decoded.getHeight());

                img = decoded.copy(decoded.getConfig(), decoded.isMutable());
            } else {
                img = original.copy(original.getConfig(), original.isMutable());
            }

//                Bitmap file = Bitmap.createScaledBitmap(original, 224, 224, false);

            ByteArrayOutputStream origStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 99, origStream);

            int[] rgbBytes = new int[img.getWidth() * img.getHeight()];
            img.getPixels(rgbBytes, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

            Bitmap croppedImg = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888);
            Bitmap rgbFrameBitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

            frameToCropTransform = getTransformationMatrix(img.getWidth(), img.getHeight(), 224, 224, true);
            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);

            rgbFrameBitmap.setPixels(rgbBytes, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
            final Canvas canvas = new Canvas(croppedImg);
            canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            croppedImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

//                //Otsu's Method of Thresholding
            Otsu o = new Otsu(img, img);
//                int threshold = o.getThreshold();
//                Log.d("Threshold: ", Integer.toString(threshold));
            Bitmap thresh = o.applyThreshold();
            Bitmap dilate = o.dilateImage(thresh);
            Bitmap mask = o.applyMask(dilate);

            //saved thresholded image
//                MediaStore.Images.Media.insertImage(getContentResolver(), mask, "THRESHOLDED_IMG", "SAMPLE");

            long endTime = SystemClock.uptimeMillis();
            long duration = (endTime - startTime) / 1000;
            Log.d("SkinCheckr:", "Timecost to run image processing: " + Long.toString(duration));

            processImage(croppedImg);

            original.recycle();
            original = null;
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
            progressDialog = null;
            isAccurate = Float.parseFloat(percentage.get(0)) >= 0.70;

            if (diagnosed.get(0).equals("non skin")) {
                displayMessage(getApplicationContext(), "Not a skin image! Try again");

                startActivity(new Intent(ImageProcessing.this, Camera.class));
                finish();
            } else if (isAccurate) {
                if (TYPE_OF_USER == 0) { //if guest
                    displayMessage(getApplicationContext(), "User type is GUEST = " + TYPE_OF_USER);
                    Intent guestIntent = new Intent(ImageProcessing.this, GuestResult.class);
                    guestIntent.putExtra("image_path", chosenImagePath);
                    guestIntent.putStringArrayListExtra("result", diagnosed);
                    guestIntent.putExtra("percentage", percentage);

                    startActivity(guestIntent);
                    finish();
                } else { //if registered user
                    displayMessage(getApplicationContext(), "User type is REGISTERED USER = " + TYPE_OF_USER);
                    Intent registeredUserIntent = new Intent(ImageProcessing.this, Result.class);
                    registeredUserIntent.putExtra("image_path", chosenImagePath);
                    registeredUserIntent.putStringArrayListExtra("result", diagnosed);
                    registeredUserIntent.putExtra("percentage", percentage);

                    startActivity(registeredUserIntent);
                    finish();
                }
            } else if (!isAccurate) {
                Intent intent = new Intent(ImageProcessing.this, History.class);
                intent.putExtra("image_path", chosenImagePath);
                intent.putExtra("user_type", TYPE_OF_USER);
                intent.putStringArrayListExtra("result", diagnosed);
                intent.putExtra("percentage", percentage);

                startActivity(intent);
                finish();
            }

        }
    }

    public static Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.

        final int inWidth = srcWidth;
        final int inHeight = srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        return matrix;
    }

    private void getImage() {
        Intent i = getIntent();
        //check if passed key is capture or import image
        if (i.getExtras().containsKey("capture_value")) {//captured image
            capturePath = getIntent().getStringExtra("capture_value");
            //displayMessage(getBaseContext(),"CAPTURE PATH: " + capturePath); //for debugging
            Log.i("PATH: ", capturePath);
            chosenImage = BitmapFactory.decodeFile(capturePath);
            imageView.setImageBitmap(chosenImage);
            chosenImagePath = capturePath;
        } else {//imported image
            importPath = i.getStringExtra("import_value");
            //displayMessage(getBaseContext(),"IMPORT PATH: " + importPath); //for debugging
            Log.i("PATH: ", importPath);

            chosenImage = BitmapFactory.decodeFile(importPath);
            imageView.setImageBitmap(chosenImage);
            chosenImagePath = importPath;
        }
    }
}
