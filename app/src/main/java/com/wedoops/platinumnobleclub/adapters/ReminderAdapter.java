package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    private Context context;

    public ReminderAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_reminder, viewGroup, false);

        return new ReminderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout remind_background;
        TextView remind_title;
        ImageView remind_icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            remind_icon = itemView.findViewById(R.id.remind_icon);
            remind_title = itemView.findViewById(R.id.remind_title);
            remind_background = itemView.findViewById(R.id.remind_background);
        }
    }
}
