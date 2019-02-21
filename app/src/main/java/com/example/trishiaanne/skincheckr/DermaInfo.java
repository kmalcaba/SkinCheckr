package com.example.trishiaanne.skincheckr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;

public class DermaInfo extends AppCompatActivity {

    Dermatologist derma;
    TextView dermaName;
    TextView dermaYear;

    RecyclerView recyclerView;
    ClinicListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derma_info);

        dermaName = findViewById(R.id.derma_info_name);
        dermaYear = findViewById(R.id.derma_info_year);

        Bundle data = getIntent().getExtras();
        derma = new Dermatologist();
        if (data != null) {
            derma = getIntent().getParcelableExtra("derma");
            dermaName.setText(derma.getName());
            if (derma.getYears() == 0)
                dermaYear.setText("");
            else
                dermaYear.setText(derma.getYears() + " years of experience");


            initClinicList();

        } else {
            Toast.makeText(this, "No object found", Toast.LENGTH_LONG).show();
        }


    }

    private void initClinicList() {
        recyclerView = findViewById(R.id.clinic_recycler);
        adapter = new ClinicListAdapter(derma.getClinics(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
