package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Profile extends AppCompatActivity {

    private TextView displayName;

    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private DatabaseReference checkUser;
    private DatabaseReference databaseReference;
    private ArrayList<UploadResult> uploadResults;
    private Button viewTimeline;

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

        //getting the disease name and date from database
        recyclerView = findViewById(R.id.recordRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadResults = new ArrayList<>();
        checkUser = FirebaseDatabase.getInstance().getReference("user_result");

        //compare the current user to the result_images userID variable
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadResult uploadResult = postSnapshot.getValue(UploadResult.class);
                    uploadResults.add(uploadResult);
                }
                recordAdapter = new RecordAdapter(Profile.this, uploadResults);
                recyclerView.setAdapter(recordAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
