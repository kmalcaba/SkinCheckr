package com.example.trishiaanne.skincheckr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    TextView fname;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        fname = findViewById(R.id.fname);
        reference = database.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                String firstname = dataSnapshot.child("fname").getValue(String.class);
                fname.setText("Name: " + user.getFname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User n = new User();
        if (user != null) {
            String fname = n.getFname();
            String email = n.getEmail();
           // Uri photoUrl = user.getPhotoUrl();

            String uid = user.getUid();
            fname = uid;
        }*/



    }
}
