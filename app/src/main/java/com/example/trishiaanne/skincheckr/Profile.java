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
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(uid);
        DatabaseReference fname = rootRef.child("fname");
        final DatabaseReference lname = rootRef.child("lname");

        // getting info in /Users/uid/fname
        fname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {
                lname.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        displayName.setText("Hi " + dataSnapshot1.getValue(String.class)
                                + " " + dataSnapshot2.getValue(String.class) + "!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
