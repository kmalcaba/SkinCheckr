package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    private static final String TAG = "";
    private DrawerLayout mDrawerLayout;

    private ArrayList<String> dImgName = new ArrayList<>();
    private ArrayList<Bitmap> dImg = new ArrayList<>();
    private ArrayList<String> dImgSummary = new ArrayList<>();
    private ArrayList<Integer> top3 = new ArrayList<>();
    private ArrayList<String> label = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        top3.add(1);
        top3.add(10);
        top3.add(0);
        top3.add(5);
        top3.add(9);
        top3.add(6);
        top3.add(3);
        top3.add(4);
        top3.add(2);
        top3.add(7);
        top3.add(4);
        top3.add(8);

        displayToolbar();

        for (int x : top3) {
            initImageBitmaps(x);
        }
    }

    private void initImageBitmaps(Integer x) {
        switch (x) {
            case 0:
                Bitmap atopic = BitmapFactory.decodeResource(getResources(), R.drawable.atopic_sample);
                dImg.add(atopic);
                dImgName.add("Atopic Dermatitis");
                dImgSummary.add("Atopic dermatitis (eczema) is a condition that makes your skin red and itchy. It's common in children but can occur at any age.");
                label.add("Click image for more information about Atopic dermatitis.");
                initRecyclerView();
                break;
            case 1:
                Bitmap contact = BitmapFactory.decodeResource(getResources(), R.drawable.contact_sample);
                dImg.add(contact);
                dImgName.add("Contact Dermatitis");
                dImgSummary.add("Contact dermatitis is a red, itchy rash caused by direct contact with a substance or an allergic reaction to it.");
                label.add("Click image for more information about Contact dermatitis.");
                initRecyclerView();
                break;
            case 2:
                Bitmap dys = BitmapFactory.decodeResource(getResources(), R.drawable.dys_sample);
                dImg.add(dys);
                dImgName.add("Dyshidrotic eczema");
                dImgSummary.add("Dyshidrotic eczema, or dyshidrosis, is a skin condition in which blisters develop on the soles of your feet and/or the palms of your hands.");
                label.add("Click image for more information about Dyshidrotic eczema.");
                initRecyclerView();
                break;
            case 3:
                Bitmap intertrigo = BitmapFactory.decodeResource(getResources(), R.drawable.intertrigo_sample);
                dImg.add(intertrigo);
                dImgName.add("Intertrigo");
                dImgSummary.add("Intertrigo (intertriginous dermatitis) is an inflammatory condition of skin folds, induced or aggravated by heat, moisture, maceration, friction, and lack of air circulation.");
                label.add("Click image for more information about Intertrigo.");
                initRecyclerView();
                break;
            case 4:
                Bitmap melanoma = BitmapFactory.decodeResource(getResources(), R.drawable.melanoma_sample);
                dImg.add(melanoma);
                dImgName.add("Melanoma");
                dImgSummary.add("Melanoma, also known as malignant melanoma, is a type of cancer that develops from the pigment-containing cells known as melanocytes.");
                label.add("Click image for more information about Melanoma.");
                initRecyclerView();
                break;
            case 5:
                Bitmap pity = BitmapFactory.decodeResource(getResources(), R.drawable.pity_sample);
                dImg.add(pity);
                dImgName.add("Pityriasis versicolor");
                dImgSummary.add("Pityriasis versicolor, sometimes called tinea versicolor, is a common fungal infection that causes small patches of skin to become scaly and discoloured.");
                label.add("Click image for more information about Pityriasis versicolor.");
                initRecyclerView();
                break;
            case 6:
                Bitmap psor = BitmapFactory.decodeResource(getResources(), R.drawable.psor_sample);
                dImg.add(psor);
                dImgName.add("Psoriasis");
                dImgSummary.add("Psoriasis is an immune-mediated disease that causes raised, red, scaly patches to appear on the skin.");
                label.add("Click image for more information about Psoriasis.");
                initRecyclerView();
                break;
            case 7:
                Bitmap corpo = BitmapFactory.decodeResource(getResources(), R.drawable.corp_sample);
                dImg.add(corpo);
                dImgName.add("Tinea corporis (Ringworm)");
                dImgSummary.add("Ringworm is a common fungal skin infection otherwise known as tinea");
                label.add("Click image for more information about Tinea corporis.");
                initRecyclerView();
                break;
            case 8:
                Bitmap pedis = BitmapFactory.decodeResource(getResources(), R.drawable.pedis_sample);
                dImg.add(pedis);
                dImgName.add("Tinea corporis");
                dImgSummary.add("Athlete's foot — also called tinea pedis — is a contagious fungal infection that affects the skin on the feet.");
                label.add("Click image for more information about Tinea pedis.");
                initRecyclerView();
                break;
            case 9:
                Bitmap benign = BitmapFactory.decodeResource(getResources(), R.drawable.benign_sample);
                dImg.add(benign);
                dImgName.add("Benign mole");
                dImgSummary.add("Benign pigmented moles made of melanocytes are defined as those lesions which do not produce any harmful effects.");
                label.add("Click image for more information about Benign mole.");
                initRecyclerView();
                break;
            case 10:
                Bitmap skin = BitmapFactory.decodeResource(getResources(), R.drawable.skin_sample);
                dImg.add(skin);
                dImgName.add("Skin");
                dImgSummary.add("The skin is the largest organ of the body, with a total area of about 20 square feet.");
                label.add("Click image for more information about Skin.");
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
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dImgName, dImg, dImgSummary, label);
        recyclerView.setAdapter(adapter);

    }

    private void displayToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESULTS");
        toolbar.setTitleTextColor(0xFFFFFFFF);
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
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
