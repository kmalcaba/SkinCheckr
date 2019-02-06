package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ProgressBar;


public class SignUp extends AppCompatActivity {
    Button singUp;
    Button button;
    private EditText firstn, lastn, email_id, pw;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firstn = (EditText) findViewById(R.id.fname);
        lastn = (EditText) findViewById(R.id.lname);
        email_id = (EditText) findViewById(R.id.emailAddress);
        pw = (EditText) findViewById(R.id.pass);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //findViewById(R.id.sign).setOnClickListener();

    }
        @Override
        protected void onStart () {
            super.onStart();
            if (mAuth.getCurrentUser() != null) {
            }
        }

        private void registerUser () {
            final String fname = firstn.getText().toString().trim();
            final String lname = lastn.getText().toString().trim();
            final String email = email_id.getText().toString().trim();
            final String password = pw.getText().toString().trim();


            if (fname.isEmpty()) {
                firstn.setError(getString(R.string.input_fname));
                firstn.requestFocus();
                return;
            }

            if (lname.isEmpty()) {
                lastn.setError(getString(R.string.input_lname));
                lastn.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                email_id.setError(getString(R.string.input_email));
                email_id.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_id.setError(getString(R.string.input_validm));
                email_id.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                pw.setError(getString(R.string.input_password));
                pw.requestFocus();
                return;
            }

            if (password.length() < 6) {
                pw.setError(getString(R.string.input_plength));
                pw.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                User user = new User(
                                        fname,
                                        lname,
                                        email,
                                        password
                                );
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, getString(R.string.reg_success), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(SignUp.this, getString(R.string.reg_unsuc), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                //Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, Login.class));
                                finish();
                            }
                        }
                    });
        }

        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.signupBtn:
                    registerUser();
                    break;

            }
        }
    }



       /* button = (Button) findViewById(R.id.SignupBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignUp.this, Login.class);
                startActivity(myIntent);
            }
        });

    }*/


