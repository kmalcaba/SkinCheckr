package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import com.example.trishiaanne.skincheckr.R;
import com.example.trishiaanne.skincheckr.imgProcessing.ImageProcessing;
import com.google.firebase.auth.FirebaseAuth;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserCam extends AppCompatActivity {
    private ImageView imageView;
    private Button takePhoto, importPhoto;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    File photoFile = null;
    Bitmap importedImage = null;

    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_IMPORT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        dl = (DrawerLayout) findViewById(R.id.navView);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.navView);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.editProfile:
                        Toast.makeText(UserCam.this, "Profile", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UserCam.this, EditProfile.class);
                        startActivity(i);
                    case R.id.records:
                        Toast.makeText(UserCam.this, "Records", Toast.LENGTH_SHORT).show();
                    case R.id.signout:
                        Toast.makeText(UserCam.this, "Sign Out", Toast.LENGTH_SHORT).show();
                        if (id == R.id.signout) {
                            logout();
                        }
                    default:
                        return true;
                }
            }
        });

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
                    Intent passValue = new Intent(UserCam.this, ImageProcessing.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
