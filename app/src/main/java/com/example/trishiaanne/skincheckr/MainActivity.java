package com.example.trishiaanne.skincheckr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.trishiaanne.skincheckr.diseaseProfile.atopic_profile;

public class MainActivity extends AppCompatActivity {
    Button guestButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guestButton = (Button) findViewById(R.id.guestBtn);
        loginButton = (Button) findViewById(R.id.loginBtn);

        guestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(MainActivity.this, Camera.class);
                startActivity(guestIntent);
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(MainActivity.this, atopic_profile.class);
                startActivity(userIntent);
            }
        });
    }
}
