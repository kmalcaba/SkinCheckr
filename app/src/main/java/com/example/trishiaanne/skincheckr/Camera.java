package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.trishiaanne.skincheckr.imgProcessing.ImageProcessing;

public class Camera extends AppCompatActivity {

    private ImageView imageView;
    private Button takePhoto, importPhoto;

    File photoFile = null;
    Bitmap importedImage = null;

    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_IMPORT_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        takePhoto = findViewById(R.id.takePhoto);
        importPhoto = findViewById(R.id.importPhoto);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the Capture image function with save functionality
                captureImage();
            }
        });

        importPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(fromGallery, REQUEST_IMPORT_PHOTO);
            }
        });
    }

    private void captureImage() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile();
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        displayMessage(getBaseContext(),"File path successfully generated!" + photoFile.getAbsolutePath()); //for debugging
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.trishiaanne.skincheckr.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    displayMessage(getBaseContext(),ex.getMessage());
                }


            }else
            {
                displayMessage(getBaseContext(),"Null");
            }
        }

    private File createImageFile() throws IOException {
        //Create an image File name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Display messages, for debugging
    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //Save the directory in this string variable
                    String picDirectory = photoFile.getAbsolutePath();
                    Bitmap capturedImage = BitmapFactory.decodeFile(picDirectory);
                    imageView.setImageBitmap(capturedImage);
                    //Pass the image to Image Processing and ImageProcessing UNIT
                    Intent passValue = new Intent(Camera.this, ImageProcessing.class);
                    //Intent history = new Intent(Camera.this, History.class);
                    //startActivity(history);
                    passValue.putExtra("path_value", photoFile.getAbsolutePath());
                    startActivity(passValue);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri importedImageURI = imageReturnedIntent.getData();
                    try {
                        importedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), importedImageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Intent passImportedImage = new Intent(Camera.this, ImageProcessing.class);
                    imageView.setImageBitmap(importedImage);
                }
                break;
        }
    }
}
