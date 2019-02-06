package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button button;
    Button logBtn;
    private EditText enterEmail, enterPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnLogin;
    TextView btnSignup;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        /*if (auth.getCurrentUser() == null) {
            //if the user is not logged in
            startActivity(new Intent(Login.this, Main_2.class));
            finish();
        }*/
        setContentView(R.layout.activity_login);

        enterEmail = (EditText) findViewById(R.id.emailAdd);
        enterPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (TextView) findViewById(R.id.signUp);
        btnLogin = (Button) findViewById(R.id.logBtn);

        auth = FirebaseAuth.getInstance();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = enterEmail.getText().toString();
                final String password = enterPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

             //   progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        progressBar.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "User does not exists! Check your email or password!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(Login.this, Main_2.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                });

            }
    public void onClick(View v) {
        text = (TextView) findViewById(R.id.signUp);
        Intent myIntent = new Intent(Login.this, SignUp.class);
        startActivity(myIntent);
    }


}
