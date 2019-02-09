package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    //Global variables to be used to store the data
    private ArrayList<String> dImgName = new ArrayList<>();
    private ArrayList<Bitmap> dImg = new ArrayList<>();
    private ArrayList<String> dImgSummary = new ArrayList<>();
    private Context dContext;

    public RecyclerViewAdapter( Context dContext, ArrayList<String> dImgName, ArrayList<Bitmap> dImg, ArrayList<String> dImgSummary) {
        this.dContext = dContext;
        this.dImgName = dImgName;
        this.dImg = dImg;
        this.dImgSummary = dImgSummary;
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

        viewHolder.diagnosed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked an image" + dImgName.get(i));
                Toast.makeText(dContext, dImgName.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dImgName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView diagnosed_image;
        TextView disease_name, disease_summary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diagnosed_image = itemView.findViewById(R.id.diagnosed_image);
            disease_name = itemView.findViewById(R.id.disease_name);
            disease_summary = itemView.findViewById(R.id.disease_summary);
        }
    }
}
