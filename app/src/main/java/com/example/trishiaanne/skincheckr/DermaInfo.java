package com.example.trishiaanne.skincheckr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;

import java.util.ArrayList;
import java.util.List;

public class DermaInfo extends AppCompatActivity {

    Dermatologist derma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derma_info);

        Bundle data = getIntent().getExtras();
        derma = new Dermatologist();
        if (data != null) {
            derma = data.getParcelable("derma");
        } else {
            Toast.makeText(this, "No object found", Toast.LENGTH_LONG).show();
        }


    }
}
