package com.example.trishiaanne.skincheckr;

import android.support.annotation.NonNull;
import com.example.trishiaanne.skincheckr.R;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class UserCam extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercam);

        dl = (DrawerLayout) findViewById(R.id.navView);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.navView);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.editProfile:
                        Toast.makeText(UserCam.this, "Profile", Toast.LENGTH_SHORT).show();
                    case R.id.records:
                        Toast.makeText(UserCam.this, "Records", Toast.LENGTH_SHORT).show();
                    case R.id.signout:
                        Toast.makeText(UserCam.this, "Sign Out", Toast.LENGTH_SHORT).show();
                    default:
                        return true;

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}

