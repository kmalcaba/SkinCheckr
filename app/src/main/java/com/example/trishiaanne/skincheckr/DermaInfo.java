package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;
import com.google.firebase.auth.FirebaseAuth;

public class DermaInfo extends AppCompatActivity {

    Dermatologist derma;
    TextView dermaName;
    TextView dermaYear;

    RecyclerView recyclerView;
    ClinicListAdapter adapter;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derma_info);

        dermaName = findViewById(R.id.derma_info_name);
        dermaYear = findViewById(R.id.derma_info_year);

        Bundle data = getIntent().getExtras();
        derma = new Dermatologist();
        if (data != null) {
            derma = getIntent().getParcelableExtra("derma");
            dermaName.setText(derma.getName());
            if (derma.getYears() == 0)
                dermaYear.setText("");
            else
                dermaYear.setText(derma.getYears() + " years of experience");


            initClinicList();

        } else {
            Toast.makeText(this, "No object found", Toast.LENGTH_LONG).show();
        }


    }

    private void displayToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
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
                                startActivity(new Intent(DermaInfo.this, Profile.class));
                                break;
                            case R.id.uv:
                                startActivity(new Intent(DermaInfo.this, Uv.class));
                                break;
                            case R.id.derma:
//                                Toast.makeText(DermaInfo.this, "Find Nearby Dermatologist", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DermaInfo.this, Derma.class));
                                break;
                                //TODO: Update menus
//                            case R.id.editProfile:
//                                Toast.makeText(Result.this, "Profile", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(Result.this, EditProfile.class);
//                                startActivity(i);
//                                break;
//                            case R.id.records:
//                                Toast.makeText(Result.this, "Records", Toast.LENGTH_SHORT).show();
//                                break;
                            case R.id.signout:
                                Toast.makeText(DermaInfo.this, "Sign Out", Toast.LENGTH_SHORT).show();
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

    private void initClinicList() {
        recyclerView = findViewById(R.id.clinic_recycler);
        adapter = new ClinicListAdapter(derma.getClinics(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
