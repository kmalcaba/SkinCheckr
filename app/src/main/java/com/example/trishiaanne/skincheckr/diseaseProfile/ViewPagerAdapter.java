package com.example.trishiaanne.skincheckr.diseaseProfile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.trishiaanne.skincheckr.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer skinDisease;
    private Integer [] atopic = {R.drawable.atopic_1, R.drawable.atopic_2, R.drawable.atopic_3, R.drawable.atopic_4, R.drawable.atopic_5};
    private Integer [] contact = {R.drawable.contact_1, R.drawable.contact_2, R.drawable.contact_3, R.drawable.contact_4, R.drawable.contact_5};
    private Integer [] dys = {R.drawable.dys_1, R.drawable.dys_2, R.drawable.dys_3, R.drawable.dys_4, R.drawable.dys_5};
    private Integer [] intertrigo = {R.drawable.intertrigo_1, R.drawable.intertrigo_2, R.drawable.intertrigo_3, R.drawable.intertrigo_4, R.drawable.intertrigo_5};
    private Integer [] melanoma = {R.drawable.melanoma_1, R.drawable.melanoma_2, R.drawable.melanoma_3, R.drawable.melanoma_4, R.drawable.melanoma_5};
    private Integer [] pity = {R.drawable.pity_1, R.drawable.pity_2, R.drawable.pity_3, R.drawable.pity_4, R.drawable.pity_5};
    private Integer [] psor = {R.drawable.psor_1, R.drawable.psor_2, R.drawable.psor_3, R.drawable.psor_4, R.drawable.psor_5};
    private Integer [] corp = {R.drawable.corp_1, R.drawable.corp_2, R.drawable.corp_3, R.drawable.corp_4, R.drawable.corp_5};
    private Integer [] pedis = {R.drawable.pedis_1, R.drawable.pedis_2, R.drawable.pedis_3, R.drawable.pedis_4, R.drawable.pedis_5};
    private Integer [] benign = {R.drawable.benign_1, R.drawable.benign_2, R.drawable.benign_3, R.drawable.benign_4, R.drawable.benign_5};
    private Integer [] skin = {R.drawable.skin_1, R.drawable.skin_2, R.drawable.skin_3, R.drawable.skin_4, R.drawable.skin_5};
    public ViewPagerAdapter(Context context, Integer skinDisease) {
        this.context = context;
        this.skinDisease = skinDisease;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.img_layout, null);
        ImageView imageView = view.findViewById(R.id.image_slider_img);
        switch (skinDisease) {
            case 0:
                imageView.setImageResource(atopic[position]);
                break;
            case 1:
                imageView.setImageResource(contact[position]);
                break;
            case 2:
                imageView.setImageResource(dys[position]);
                break;
            case 3:
                imageView.setImageResource(intertrigo[position]);
                break;
            case 4:
                imageView.setImageResource(melanoma[position]);
                break;
            case 5:
                imageView.setImageResource(pity[position]);
                break;
            case 6:
                imageView.setImageResource(psor[position]);
                break;
            case 7:
                imageView.setImageResource(corp[position]);
                break;
            case 8:
                imageView.setImageResource(pedis[position]);
                break;
            case 9:
                imageView.setImageResource(benign[position]);
                break;
            case 10:
                imageView.setImageResource(skin[position]);
                break;
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
