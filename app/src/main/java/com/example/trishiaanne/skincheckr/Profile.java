package com.example.trishiaanne.skincheckr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends AppCompatActivity {

    private TextView displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        displayName = findViewById(R.id.firstName);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // firebase path: skinchekr/Users/uid/fname
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference uidRef = rootRef.child(uid);
        DatabaseReference fileref = uidRef.child("fname");

        // getting info in /Users/uid/fname
        fileref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayName.setText("Hi " + dataSnapshot.getValue(String.class) + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
