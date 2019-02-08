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
<<<<<<< HEAD
                Intent userIntent = new Intent(MainActivity.this, Result.class);
=======
                Intent userIntent = new Intent(MainActivity.this, UserCam.class);
>>>>>>> eb1d58627593fc2127b419256a5817b3e685b3e5
                startActivity(userIntent);
            }
        });
    }
}
