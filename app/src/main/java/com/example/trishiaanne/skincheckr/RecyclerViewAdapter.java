package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.example.trishiaanne.skincheckr.diseaseProfile.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private String viewProfile;

    //Global variables to be used to store the data
    private ArrayList<String> dImgName = new ArrayList<>();
    private ArrayList<Bitmap> dImg = new ArrayList<>();
    private ArrayList<String> dImgSummary = new ArrayList<>();
    private ArrayList<String> label = new ArrayList<>();
//    private ArrayList<String> percentage = new ArrayList<>();
    private Context dContext;

    public RecyclerViewAdapter( Context dContext, ArrayList<String> dImgName, ArrayList<String> percentage, ArrayList<Bitmap> dImg, ArrayList<String> dImgSummary, ArrayList<String> label) {
        this.dContext = dContext;
        this.dImgName = dImgName;
        this.dImg = dImg;
        this.dImgSummary = dImgSummary;
        this.label = label;
//        this.percentage = percentage;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called: ");

        //set the disease sample image
        viewHolder.diagnosed_image.setImageBitmap(dImg.get(i));

        //set the disease name
        viewHolder.disease_name.setText(dImgName.get(i));

        //set the disease summary
        viewHolder.disease_summary.setText(dImgSummary.get(i));

        //set the more information
        viewHolder.label.setText(label.get(i));

        //set the percentage
//        viewHolder.perc.setText(String.format("%.2f%%", Double.valueOf(percentage.get(i))*100));
//        Log.d(TAG, "Percentage: " + percentage.get(i));

        viewHolder.diagnosed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Log.d(TAG, "onClick: clicked an image" + dImgName.get(i));
                //Toast.makeText(dContext, dImgName.get(i), Toast.LENGTH_SHORT).show();
                viewProfile = dImgName.get(i).toLowerCase();
                switch (viewProfile) {
                    case "atopic dermatitis":
                        context.startActivity(new Intent(context, atopic_profile.class));
                        break;
                    case "contact dermatitis":
                        context.startActivity(new Intent(context, contact_profile.class));
                        break;
                    case "dyshidrotic eczema":
                        context.startActivity(new Intent(context, dys_profile.class));
                        break;
                    case "intertrigo":
                        context.startActivity(new Intent(context, intertrigo_profile.class));
                        break;
                    case "melanoma":
                        context.startActivity(new Intent(context, melanoma_profile.class));
                        break;
                    case "pityriasis versicolor":
                        context.startActivity(new Intent(context, pity_profile.class));
                        break;
                    case "psoriasis":
                        context.startActivity(new Intent(context, psor_profile.class));
                        break;
                    case "tinea corporis":
                        context.startActivity(new Intent(context, corporis_profile.class));
                        break;
                    case "tinea pedis":
                        context.startActivity(new Intent(context, pedis_profile.class));
                        break;
                    case "benign mole":
                        context.startActivity(new Intent(context, benign_profile.class));
                        break;
                    case "healthy skin":
                        context.startActivity(new Intent(context, skin_profile.class));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dImgName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView diagnosed_image;
        TextView disease_name, disease_summary, label, perc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diagnosed_image = itemView.findViewById(R.id.diagnosed_image);
            disease_name = itemView.findViewById(R.id.disease_name);
            disease_summary = itemView.findViewById(R.id.disease_summary);
            label = itemView.findViewById(R.id.more_info);
//            perc = itemView.findViewById(R.id.percentage);
        }
    }
}
