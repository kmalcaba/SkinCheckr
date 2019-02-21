package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<String> timelineDate;
    private ArrayList<String> timelineDisease;
    private ArrayList<Bitmap> timelineImage;

    public RecordAdapter(Context context, ArrayList<String> date, ArrayList<String> disease, ArrayList<Bitmap> image) {
        this.mContext = context;
        this.timelineDate = date;
        this.timelineDisease = disease;
        this.timelineImage = image;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.timeline_item, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return timelineDate.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tDate, tDisease;
        ImageView tImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            tDate = itemView.findViewById(R.id.timelineDate);
            tDisease = itemView.findViewById(R.id.timelineDisease);
            tImage = itemView.findViewById(R.id.timelineImage);
        }
    }
}
