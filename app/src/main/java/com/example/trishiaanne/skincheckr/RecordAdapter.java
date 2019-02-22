package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UploadResult> mUpload;
    public RecordAdapter(Context context, ArrayList<UploadResult> uploads) {
        this.mContext = context;
        this.mUpload = uploads;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.timeline_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewGroup, int i) {
        UploadResult uploadResult = mUpload.get(i);
        viewGroup.tDate.setText(uploadResult.getDate());
        viewGroup.tDisease.setText(uploadResult.getDisease());

        viewGroup.viewT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                context.startActivity(new Intent(context, Profile2.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tDate, tDisease;
        Button viewT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tDate = itemView.findViewById(R.id.timelineDate);
            tDisease = itemView.findViewById(R.id.timelineDisease);
            viewT = itemView.findViewById(R.id.viewTimeline);
        }
    }
}
