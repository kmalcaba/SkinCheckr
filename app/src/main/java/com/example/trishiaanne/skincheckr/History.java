package com.example.trishiaanne.skincheckr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class History extends AppCompatActivity {
    private Button button;

    private SeekBar seek;
    private SeekBar seek1;
    private SeekBar seek2;
    private SeekBar seek3;
    private SeekBar seek4;

    private TextView textview;
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private TextView textview4;

    // Values containers
    private int itch = 1;
    private int scaling = 1;
    private int burn = 1;
    private int sweat = 1;
    private int crust = 1;
    private int bleeding = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final int max = 10;

        seek = findViewById(R.id.itchSeekbar);
        seek1 = findViewById(R.id.scalingSeekbar);
        seek2 = findViewById(R.id.burningSeekbar);
        seek3 = findViewById(R.id.sweatingSeekbar);
        seek4 = findViewById(R.id.crustingSeekbar);

        textview = findViewById(R.id.itchProgress);
        textview1 = findViewById(R.id.scalingProgress);
        textview2 = findViewById(R.id.burningProgress);
        textview3 = findViewById(R.id.sweatingProgress);
        textview4 = findViewById(R.id.crustingProgress);

        button = (Button) findViewById(R.id.reviewHistory);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(History.this, ReviewHistory.class);
                myIntent.putExtra("itch", itch + 1);
                myIntent.putExtra("scaling", scaling + 1);
                myIntent.putExtra("burn", burn + 1);
                myIntent.putExtra("sweat", sweat + 1);
                myIntent.putExtra("crust", crust + 1);
                myIntent.putExtra("bleeding", bleeding + 1);
                startActivity(myIntent);
            }
        });

        // Itchiness Seekbar
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview.setText("Itchiness: " + String.valueOf(i+1) + "/" + String.valueOf(max));
                itch = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Scaling/Peeling
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview1.setText("Scaling/Peeling: " + String.valueOf(i+1) + "/" + String.valueOf(max));
                scaling = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Burning
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview2.setText("Burning: " + String.valueOf(i+1) + "/" + String.valueOf(max));
                burn = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Sweating
        seek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview3.setText("Sweating: " + String.valueOf(i+1) + "/" + String.valueOf(max));
                sweat = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Crusting
        seek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textview4.setText("Crusting: " + String.valueOf(i+1) + "/" + String.valueOf(max));
                crust = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
