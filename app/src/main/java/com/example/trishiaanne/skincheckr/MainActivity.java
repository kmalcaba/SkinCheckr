package com.example.trishiaanne.skincheckr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.guestBtn);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Camera.class);
                startActivity(myIntent);
            }
        });

        button = (Button) findViewById(R.id.loginBtn);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Login.class);
                startActivity(myIntent);
            }
        });
    }
}
