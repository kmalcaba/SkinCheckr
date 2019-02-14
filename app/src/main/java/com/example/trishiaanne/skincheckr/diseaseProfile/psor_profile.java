package com.example.trishiaanne.skincheckr.diseaseProfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.trishiaanne.skincheckr.R;

public class psor_profile extends AppCompatActivity {
    private ViewPager viewPager;
    private int skinDisease = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_psor);

        viewPager = findViewById(R.id.image_slider);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, skinDisease);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
