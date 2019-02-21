package com.example.trishiaanne.skincheckr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;
import com.example.trishiaanne.skincheckr.dermaSearch.Finder;

import java.util.List;

public class Derma extends AppCompatActivity {
    Spinner spinner;
    private List<Dermatologist> dermaList;
    Finder f;
    RecyclerView recyclerView;
    DermaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derma);

        f = new Finder(2);
        initDermaList();

        spinner = findViewById(R.id.spinner_sort);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: //sort by distance
                        //TODO: Sort by distance
                        adapter.notifyDataSetChanged();
                        break;
                    case 1: //sort by price low to high
                        f.findByFee(0);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2: //sort by price high to low
                        f.findByFee(1);
                        adapter.notifyDataSetChanged();
                        break;
                    case 3: //sort by years of experience
                        f.findByYear();
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initDermaList() {
        dermaList = f.getDermaList();

        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.derma_recycler);
        adapter = new DermaAdapter(dermaList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

