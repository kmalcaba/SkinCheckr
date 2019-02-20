package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class GuestResult extends AppCompatActivity {
    private static final String TAG = "";
    private DrawerLayout mDrawerLayout;
    private ImageView skin_img;
    private String imagePath;
    private TextView labelDiag;
    private Bitmap skin;

    private Uri imgURI;
    private File f;

    private StorageReference storage;
    private DatabaseReference database;

    private ArrayList<String> dImgName = new ArrayList<>();
    private ArrayList<Bitmap> dImg = new ArrayList<>();
    private ArrayList<String> dImgSummary = new ArrayList<>();
    private ArrayList<String> diagnosed = new ArrayList<>();
    private ArrayList<String> label = new ArrayList<>();
    private ArrayList<String> percentage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_result);

        labelDiag = findViewById(R.id.diagnosis_label);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESULTS");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_check_black_24dp);

        skin_img = findViewById(R.id.display_diagnosed);
        imagePath = getIntent().getStringExtra("image_path");
        skin = BitmapFactory.decodeFile(imagePath);
        diagnosed = getIntent().getStringArrayListExtra("result");
        percentage = getIntent().getStringArrayListExtra("percentage");

        skin_img.setImageBitmap(skin);

        for (String x : diagnosed) {
            String y = percentage.get(diagnosed.indexOf(x));
            initImageBitmaps(x,y);
        }

        int diagnosedCounter = diagnosed.size();
        if (diagnosedCounter == 1) {
            labelDiag.setText("TOP DIAGNOSIS:");
        }else { labelDiag.setText("TOP " + diagnosedCounter + " DIAGNOSIS:");}
    }

    private void initImageBitmaps(String x, String y) {
        switch (x) {
            case "atopic dermatitis":
                Bitmap atopic = BitmapFactory.decodeResource(getResources(), R.drawable.atopic_sample);
                dImg.add(atopic);
                dImgName.add("Atopic Dermatitis");
                dImgSummary.add("Atopic dermatitis (eczema) is a condition that makes your skin red and itchy. It's common in children but can occur at any age.");
                label.add("Click image for more information about Atopic dermatitis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "contact dermatitis":
                Bitmap contact = BitmapFactory.decodeResource(getResources(), R.drawable.contact_sample);
                dImg.add(contact);
                dImgName.add("Contact Dermatitis");
                dImgSummary.add("Contact dermatitis is a red, itchy rash caused by direct contact with a substance or an allergic reaction to it.");
                label.add("Click image for more information about Contact dermatitis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "dyshidrotic eczema":
                Bitmap dys = BitmapFactory.decodeResource(getResources(), R.drawable.dys_sample);
                dImg.add(dys);
                dImgName.add("Dyshidrotic eczema");
                dImgSummary.add("Dyshidrotic eczema, or dyshidrosis, is a skin condition in which blisters develop on the soles of your feet and/or the palms of your hands.");
                label.add("Click image for more information about Dyshidrotic eczema.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "intertrigo":
                Bitmap intertrigo = BitmapFactory.decodeResource(getResources(), R.drawable.intertrigo_sample);
                dImg.add(intertrigo);
                dImgName.add("Intertrigo");
                dImgSummary.add("Intertrigo (intertriginous dermatitis) is an inflammatory condition of skin folds, induced or aggravated by heat, moisture, maceration, friction, and lack of air circulation.");
                label.add("Click image for more information about Intertrigo.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "melanoma":
                Bitmap melanoma = BitmapFactory.decodeResource(getResources(), R.drawable.melanoma_sample);
                dImg.add(melanoma);
                dImgName.add("Melanoma");
                dImgSummary.add("Melanoma, also known as malignant melanoma, is a type of cancer that develops from the pigment-containing cells known as melanocytes.");
                label.add("Click image for more information about Melanoma.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "pityriasis versicolor":
                Bitmap pity = BitmapFactory.decodeResource(getResources(), R.drawable.pity_sample);
                dImg.add(pity);
                dImgName.add("Pityriasis versicolor");
                dImgSummary.add("Pityriasis versicolor, sometimes called tinea versicolor, is a common fungal infection that causes small patches of skin to become scaly and discoloured.");
                label.add("Click image for more information about Pityriasis versicolor.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "psoriasis":
                Bitmap psor = BitmapFactory.decodeResource(getResources(), R.drawable.psor_sample);
                dImg.add(psor);
                dImgName.add("Psoriasis");
                dImgSummary.add("Psoriasis is an immune-mediated disease that causes raised, red, scaly patches to appear on the skin.");
                label.add("Click image for more information about Psoriasis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "tinea corporis":
                Bitmap corpo = BitmapFactory.decodeResource(getResources(), R.drawable.corp_sample);
                dImg.add(corpo);
                dImgName.add("Tinea corporis");
                dImgSummary.add("Also known as Ringworm is a common fungal skin disease");
                label.add("Click image for more information about Tinea corporis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "tinea pedis":
                Bitmap pedis = BitmapFactory.decodeResource(getResources(), R.drawable.pedis_sample);
                dImg.add(pedis);
                dImgName.add("Tinea pedis");
                dImgSummary.add("Also known as Athlete's foot is a contagious fungal infection that affects the skin on the feet.");
                label.add("Click image for more information about Tinea pedis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "benign mole":
                Bitmap benign = BitmapFactory.decodeResource(getResources(), R.drawable.benign_sample);
                dImg.add(benign);
                dImgName.add("Benign mole");
                dImgSummary.add("Don't worry this is just a benign mole, not a Melanoma.");
                label.add("Click image for more information about Benign mole.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "skin":
                Bitmap skin = BitmapFactory.decodeResource(getResources(), R.drawable.skin_sample);
                dImg.add(skin);
                dImgName.add("Healthy Skin");
                dImgSummary.add("Congratulations! You have a healthy skin.");
                label.add("Click image for more information about Skin.");
                percentage.add(y);
                initRecyclerView();
                break;
        }

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dImgName, percentage, dImg, dImgSummary, label);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("Save record? ")
                        .setMessage("Do you want to save this record? If you want to save, you will be redirected to our sign up page.")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent toSign = new Intent(GuestResult.this, SignUp.class);
                                startActivity(toSign);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                uploadImage();
                                Intent out = new Intent(GuestResult.this, MainActivity.class);
                                startActivity(out);
                            }
                        })
                        .create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadImage() {
        storage = FirebaseStorage.getInstance().getReference("result_images");
        database = FirebaseDatabase.getInstance().getReference("result_images");

        f = new File(imagePath);
        Log.d(TAG, "Original Image Path: " + imagePath);

        imgURI = Uri.fromFile(f);
        Log.d(TAG, "URI Image Path: " + imgURI.getPath());

        if (imgURI != null) {
            final StorageReference imagesReference = storage.child(String.valueOf(System.currentTimeMillis()));
            imagesReference.putFile(imgURI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imagesReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.d(TAG, "DOWNLOAD URI: " + downloadUri.toString());

                        Calendar dateToday = Calendar.getInstance();
                        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(dateToday.getTime());

                        UploadResult uploadResult = new UploadResult(dImgName.get(0), downloadUri.toString(), currentDate);
                        String uploadID = database.push().getKey();
                        database.child(uploadID).setValue(uploadResult);
                    } else {
                        displayMessage(getApplicationContext(), "FAILED TO UPLOAD!");
                    }
                }
            });
        }
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

//    //disable back button
//    @Override
//    public void onBackPressed() {
//    }

}
