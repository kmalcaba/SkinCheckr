package com.example.trishiaanne.skincheckr;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth;
=======
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Profile2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
<<<<<<< HEAD
    public String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
=======
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private StorageReference storage;
    private DatabaseReference database;

  //  private Uri imgURI;
    private String imagePath;
    private ArrayList<String> dImgName = new ArrayList<>();
    private static final String TAG = "";

    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile2);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
<<<<<<< HEAD
=======
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Profile2.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
<<<<<<< HEAD
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void uploadFile() {
        storage = FirebaseStorage.getInstance().getReference("uploads");
        database = FirebaseDatabase.getInstance().getReference("uploads");

        if (mImageUri != null) {
            final StorageReference imagesReference = storage.child(String.valueOf(System.currentTimeMillis()));
            imagesReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
=======


            //orig  Picasso.with(this).load(mImageUri).into(mImageView);
            Picasso.get().load(mImageUri).into(mImageView);

            //File file=new File(mImageUri.getPath());
           // Picasso.get().load(file).into(mImageView);
           // Picasso.with(getActivity()).load(file).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //final StorageReference storageRef = storage.getReference();


/*        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(Profile2.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                    taskSnapshot.getStorage().getDownloadUrl().toString());
                            // taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }*/

            storage = FirebaseStorage.getInstance().getReference("uploads");
            database = FirebaseDatabase.getInstance().getReference("uploads");

            if (mImageUri != null) {
                final StorageReference imagesReference = storage.child(String.valueOf(System.currentTimeMillis()));
                imagesReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imagesReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d(TAG, "DOWNLOAD URI: " + downloadUri.toString());

                            Calendar dateToday = Calendar.getInstance();
                            String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(dateToday.getTime());

<<<<<<< HEAD
                            UploadResult uploadResult = new UploadResult("INTERTRIGO", downloadUri.toString(), currentDate, uid);
=======
                            UploadResult uploadResult = new UploadResult("INTERTRIGO", downloadUri.toString(), currentDate);
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1
                            String uploadID = database.push().getKey();
                            database.child(uploadID).setValue(uploadResult);
                        } else {

                        }
                    }
                });
            }
    }


<<<<<<< HEAD
    private void openImagesActivity () {
        Intent intent = new Intent(Profile2.this, ImagesActivity.class);
        startActivity(intent);
        // startActivity(new Intent(Profile2.this, ImagesActivity.class));
=======
        private void openImagesActivity () {
            Intent intent = new Intent(Profile2.this, ImagesActivity.class);
            startActivity(intent);
            // startActivity(new Intent(Profile2.this, ImagesActivity.class));
>>>>>>> 8a91583c6f432b93fa01ec27560176d9410b6be1
        }
    }