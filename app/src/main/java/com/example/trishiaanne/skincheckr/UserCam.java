package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    private ImageView userImageView;
    private Button userTakePhoto, userImportPhoto;

    private DrawerLayout mDrawerLayout;

    File photoFile = null;
    String importedFile = null;

    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_IMPORT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercam);

        userImageView = findViewById(R.id.userImageView);
        userTakePhoto= findViewById(R.id.userTakePhoto);
        userImportPhoto= findViewById(R.id.userImportPhoto);

        userTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the Capture image function with save functionality
                captureImage();
            }
        });

        userImportPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImage();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.editProfile:
                                Toast.makeText(UserCam.this, "Profile", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(UserCam.this, EditProfile.class);
                                startActivity(i);
                                break;
                            case R.id.records:
                                Toast.makeText(UserCam.this, "Records", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.signout:
                                Toast.makeText(UserCam.this, "Sign Out", Toast.LENGTH_SHORT).show();
                                if (id == R.id.signout) {
                                    logout();
                                }
                            default:
                                return true;
                        }
                        return true;
                    }
                }
        );
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {

                photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),"File path successfully generated!" + photoFile.getAbsolutePath()); //for debugging
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

    private void importImage() {
        //Allow storage access
        if(ContextCompat.checkSelfPermission(UserCam.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserCam.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(UserCam.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        } else {
            //Permission granted
        }
        Intent fromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(fromGallery, REQUEST_IMPORT_PHOTO);
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
                    userImageView.setImageBitmap(capturedImage);

                    //Pass the captured image to Image Processing and GLCM UNIT
                    Intent passCapturedImage = new Intent(UserCam.this, ImageProcessing.class);
                    passCapturedImage.putExtra("capture_value", photoFile.getAbsolutePath());
                    startActivity(passCapturedImage);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri importedImageURI = imageReturnedIntent.getData();
                    importedFile = getRealPathFromURI(getBaseContext(), importedImageURI);
                    /*try {
                        importedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), importedImageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    Bitmap importedImage = BitmapFactory.decodeFile(importedFile);
                    userImageView.setImageBitmap(importedImage);
                    //Pass the imported image to Image Processing and GLCM UNIT
                    Intent passImportedImage = new Intent(UserCam.this, ImageProcessing.class);
                    passImportedImage.putExtra("import_value", importedFile);
                    startActivity(passImportedImage);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout () {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }
}
