package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trishiaanne.skincheckr.dermaSearch.Dermatologist;

import java.util.List;

public class DermaAdapter extends RecyclerView.Adapter<DermaAdapter.ViewHolder> {
    private static final String TAG = "DermaAdapter";

    public DermaAdapter(List<Dermatologist> dermaList, Context context) {
        this.dermaList = dermaList;
        this.context = context;
    }

    private List<Dermatologist> dermaList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_derma, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.dermaName.setText(dermaList.get(i).getName());
        viewHolder.dermaLocation.setText(dermaList.get(i).getLocation());
        if (dermaList.get(i).getFee().equals("null"))
            viewHolder.dermaFee.setVisibility(View.GONE);
        else
            viewHolder.dermaFee.setText(dermaList.get(i).getFee());
        if (dermaList.get(i).getYears() == 0)
            viewHolder.dermaYear.setVisibility(View.GONE);
        else
            viewHolder.dermaYear.setText(dermaList.get(i).getYears() + " years of experience");

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DermaInfo.class);
                intent.putExtra("derma", dermaList.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dermaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dermaName;
        TextView dermaYear;
        TextView dermaLocation;
        TextView dermaFee;
        Button btn;
        RelativeLayout dermaLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dermaName = itemView.findViewById(R.id.derma_name);
            dermaYear = itemView.findViewById(R.id.derma_year);
            dermaLocation = itemView.findViewById(R.id.derma_loc);
            dermaFee = itemView.findViewById(R.id.derma_fee);
            btn = itemView.findViewById(R.id.derma_btn);
            dermaLayout = itemView.findViewById(R.id.derma_layout);
        }
    }
}
