package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        displayName = findViewById(R.id.firstName);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // firebase path: skinchekr/Users/uid/fname
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(uid).child("fname");

        // getting info in /Users/uid/fname
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayName.setText("Hi " + dataSnapshot.getValue(String.class) + "!");
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
        checkUser = FirebaseDatabase.getInstance().getReference("result_images");

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

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Profile2.class));
            }
        });
    }
}
