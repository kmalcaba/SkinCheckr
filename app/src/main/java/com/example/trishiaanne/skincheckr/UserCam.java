package com.example.trishiaanne.skincheckr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.trishiaanne.skincheckr.imgProcessing.ImageProcessing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class UserCam extends AppCompatActivity {
    private ImageView userImageView;
    private Button userTakePhoto, userImportPhoto;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    File photoFile = null;
    String importedFile = null;

    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_IMPORT_PHOTO = 1;
    private static final int STORAGE_REQUEST = 1;
    private static final int TYPE_OF_USER = 1; //registered_user

    Uri file;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercam);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
        getSupportActionBar().setTitle(null);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.profile:
                                startActivity(new Intent(UserCam.this, Profile.class));
                                break;
                            case R.id.uv:
                                startActivity(new Intent(UserCam.this, Uv.class));
                                break;
                            case R.id.derma:
                                Toast.makeText(UserCam.this, "Find Nearby Dermatologist", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UserCam.this, Derma.class));
                                break;
                            case R.id.signout:
                                Toast.makeText(UserCam.this, "Signed Out", Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(UserCam.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent fromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(fromGallery, REQUEST_IMPORT_PHOTO);
        } else {
            requestStorage();
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

    private void requestStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Storage access required")
                    .setMessage("Allow SkinCheckr to access your storage")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(UserCam.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(UserCam.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_REQUEST) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent fromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(fromGallery, REQUEST_IMPORT_PHOTO);
            }
        } else {
            displayMessage(getApplicationContext(), "Permission DENIED");
        }
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
                    file = Uri.parse(picDirectory);

                    StorageReference ref = storageReference.child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());

                    ref.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(UserCam.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(UserCam.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                    //Pass the captured image to Image Processing and GLCM UNIT
                    Intent passCapturedImage = new Intent(UserCam.this, ImageProcessing.class);
                    passCapturedImage.putExtra("capture_value", photoFile.getAbsolutePath());
                    passCapturedImage.putExtra("user_type", TYPE_OF_USER);
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
                    passImportedImage.putExtra("user_type", TYPE_OF_USER);
                    startActivity(passImportedImage);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (mToggle.onOptionsItemSelected(item)) {
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

    /*private void profile () {
        Intent intent = new Intent(UserCam.this, Profile.class);
        startActivity(intent);
    }

    private void uv () {
        Intent intent = new Intent(UserCam.this, Uv.class);
        startActivity(intent);
    }
    private void derma () {
        Intent intent = new Intent(UserCam.this, Derma.class);
        startActivity(intent);
    }*/

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
    //disable back button
    @Override
    public void onBackPressed() {
        if (TYPE_OF_USER == 1) {

        } else {
            super.onBackPressed();
        }
    }
}
