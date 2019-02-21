package com.example.trishiaanne.skincheckr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Result extends AppCompatActivity {
    private static final String TAG = "";
    private DrawerLayout mDrawerLayout;
    private ImageView skin_img;
    private String imagePath;
    private TextView labelDiag;
    private ActionBarDrawerToggle mToggle;

    private ArrayList<String> dImgName = new ArrayList<>();
    private ArrayList<Bitmap> dImg = new ArrayList<>();
    private ArrayList<String> dImgSummary = new ArrayList<>();
    private ArrayList<String> diagnosed = new ArrayList<>();
    private ArrayList<String> label = new ArrayList<>();
    private ArrayList<String> percentage = new ArrayList<>();

    private StorageReference storage;
    private DatabaseReference database;

    private Uri imgURI;

    public String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        skin_img = findViewById(R.id.display_diagnosed);
        imagePath = getIntent().getStringExtra("image_path");
        Bitmap skin = BitmapFactory.decodeFile(imagePath);
        diagnosed = getIntent().getStringArrayListExtra("result");
        labelDiag = findViewById(R.id.diagnosis_label);
        percentage = getIntent().getStringArrayListExtra("percentage");

        skin_img.setImageBitmap(skin);

        uploadImage();
        displayToolbar();

        for (String x : diagnosed) {
            String y = percentage.get(diagnosed.indexOf(x));
            initImageBitmaps(x,y);
        }

        int diagnosedCounter = diagnosed.size();
        if (diagnosedCounter == 1) {
            labelDiag.setText("TOP DIAGNOSIS:");
        } else {
            labelDiag.setText("TOP " + diagnosedCounter + " DIAGNOSIS:");
        }
    }

    private void uploadImage() {
        storage = FirebaseStorage.getInstance().getReference(uid);
        database = FirebaseDatabase.getInstance().getReference("result_images");

        File f = new File(imagePath);
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
                        int diseaseID=0;
                        switch (dImgName.get(0).toLowerCase()) {
                            case "atopic dermatitis":
                                diseaseID = 0;
                                break;
                            case "contact dermatitis":
                                diseaseID = 1;
                                break;
                            case "dyshidrotic eczema":
                                diseaseID = 2;
                                break;
                            case "intertrigo":
                                diseaseID = 3;
                                break;
                            case "melanoma":
                                diseaseID = 4;
                                break;
                            case "pityriasis versicolor":
                                diseaseID = 5;
                                break;
                            case "psoriasis":
                                diseaseID = 6;
                                break;
                            case "tinea corporis":
                                diseaseID = 7;
                                break;
                            case "tinea pedis":
                                diseaseID = 8;
                                break;
                            case "benign mole":
                                diseaseID = 9;
                                break;
                            case "skin":
                                diseaseID = 10;
                                break;

<<<<<<< HEAD
                        }
                        UploadResult uploadResult = new UploadResult(diseaseID, downloadUri.toString(), currentDate);
=======
                        UploadResult uploadResult = new UploadResult(dImgName.get(0), downloadUri.toString(), currentDate, uid);
>>>>>>> ce44be6a07288c736e2d1711f4f62ba8875a6c7f
                        String uploadID = database.push().getKey();
                        database.child(uploadID).setValue(uploadResult);
                    } else {
                        displayMessage(getApplicationContext(), "FAILED TO UPLOAD!");
                    }
                }
            });
        }
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
                dImgSummary.add("Ringworm is a common fungal skin infection otherwise known as tinea");
                label.add("Click image for more information about Tinea corporis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "tinea pedis":
                Bitmap pedis = BitmapFactory.decodeResource(getResources(), R.drawable.pedis_sample);
                dImg.add(pedis);
                dImgName.add("Tinea pedis");
                dImgSummary.add("Athlete's foot — also called tinea pedis — is a contagious fungal infection that affects the skin on the feet.");
                label.add("Click image for more information about Tinea pedis.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "benign mole":
                Bitmap benign = BitmapFactory.decodeResource(getResources(), R.drawable.benign_sample);
                dImg.add(benign);
                dImgName.add("Benign mole");
                dImgSummary.add("Benign pigmented moles made of melanocytes are defined as those lesions which do not produce any harmful effects.");
                label.add("Click image for more information about Benign mole.");
                percentage.add(y);
                initRecyclerView();
                break;
            case "skin":
                Bitmap skin = BitmapFactory.decodeResource(getResources(), R.drawable.skin_sample);
                dImg.add(skin);
                dImgName.add("Healthy Skin");
                dImgSummary.add("The skin is the largest organ of the body, with a total area of about 20 square feet.");
                label.add("Click image for more information about Skin.");
                percentage.add(y);
                initRecyclerView();
                break;
            default:
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

    private void displayToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESULTS");
        toolbar.setTitleTextColor(0xFFFFFFFF);

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
                                startActivity(new Intent(Result.this, Profile.class));
                                break;
                            case R.id.uv:
                                startActivity(new Intent(Result.this, Uv.class));
                                break;
                            case R.id.derma:
                                Toast.makeText(Result.this, "Find Nearby Dermatologist", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Result.this, Derma.class));
                                break;
//                            case R.id.editProfile:
//                                Toast.makeText(Result.this, "Profile", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(Result.this, EditProfile.class);
//                                startActivity(i);
//                                break;
//                            case R.id.records:
//                                Toast.makeText(Result.this, "Records", Toast.LENGTH_SHORT).show();
//                                break;
                            case R.id.signout:
                                Toast.makeText(Result.this, "Sign Out", Toast.LENGTH_SHORT).show();
                                if (id == R.id.signout) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            default:
                                return true;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

//    //disable back button
//    @Override
//    public void onBackPressed() {
//    }
}
