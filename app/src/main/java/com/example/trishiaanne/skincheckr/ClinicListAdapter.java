package com.example.trishiaanne.skincheckr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.dermaSearch.Clinic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClinicListAdapter extends RecyclerView.Adapter<ClinicListAdapter.ViewHolder> {
    public ClinicListAdapter(List<Clinic> clinicList, Context context) {
        this.clinicList = clinicList;
        this.context = context;
    }

    private List<Clinic> clinicList;
    private Context context;

    ArrayList<HashMap<String, String>> data;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_clinic, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d("ClinicListAdapter", "onBindViewHolder: called");

        final Clinic c = clinicList.get(i);

        viewHolder.clinicLoc.setText(c.getLocation());
        viewHolder.clinicName.setText(c.getName());
        if (c.getFee().equals("null"))
            viewHolder.clinicFee.setVisibility(View.GONE);
        else
            viewHolder.clinicFee.setText(c.getFee());
        viewHolder.clinicAddress.setText(c.getAddress());

        String[] schedDays = new String[c.getSchedDays().size()];
        String[] schedTime = new String[c.getSchedTimes().size()];

        schedDays = c.getSchedDays().toArray(schedDays);
        schedTime = c.getSchedTimes().toArray(schedTime);

        data = new ArrayList<>();

        for (int j = 0; j < schedDays.length; j++) {
            HashMap<String, String> sched = new HashMap<>();
            sched.put("SchedDay", schedDays[j]);
            sched.put("SchedTime", schedTime[j]);
            data.add(sched);
        }

        SimpleAdapter adapter = new SimpleAdapter(context, data, android.R.layout.simple_list_item_2,
                new String[]{"SchedDay", "SchedTime"}, new int[]{android.R.id.text1, android.R.id.text2});

        viewHolder.schedList.setAdapter(adapter);

        viewHolder.schedList.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                for (int i = 0; i < viewHolder.schedList.getChildCount(); i++)
                    height += viewHolder.schedList.getChildAt(i).getHeight();
                ViewGroup.LayoutParams layoutParams = viewHolder.schedList.getLayoutParams();
                layoutParams.height = height;
                viewHolder.schedList.setLayoutParams(layoutParams);
            }
        });

        //TODO: Get Directions
        viewHolder.getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), c.getDirections(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("dir", c.getDirections());
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView clinicLoc;
        TextView clinicName;
        TextView clinicFee;
        TextView clinicAddress;
        Button getDirections;
        NonScrollListView schedList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clinicLoc = itemView.findViewById(R.id.clinic_loc);
            clinicName = itemView.findViewById(R.id.clinic_name);
            clinicFee = itemView.findViewById(R.id.clinic_fee);
            clinicAddress = itemView.findViewById(R.id.clinic_add);
            getDirections = itemView.findViewById(R.id.btn_dir);
            schedList = itemView.findViewById(R.id.sched_list);
        }
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }
}
