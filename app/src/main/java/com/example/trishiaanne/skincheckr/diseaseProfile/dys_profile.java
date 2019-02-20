package com.example.trishiaanne.skincheckr.diseaseProfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.trishiaanne.skincheckr.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;
import java.io.InputStream;

public class dys_profile extends AppCompatActivity {
    private ViewPager viewPager;
    private int skinDisease = 2;
    private HtmlTextView profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dys);

        viewPager = findViewById(R.id.image_slider);
        profile = findViewById(R.id.causes);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, skinDisease);
        viewPager.setAdapter(viewPagerAdapter);

        String text="";
        try {
            InputStream inputStream = getAssets().open("dys.html");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile.setHtml(text, new HtmlHttpImageGetter(profile));
    }
}
