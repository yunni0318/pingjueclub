package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.ServiceDetails;
import com.wedoops.platinumnobleclub.database.SubServicesListData;

import java.util.List;

public class ServiceSubItemAdapter extends RecyclerView.Adapter<ServiceSubItemAdapter.MyViewHolder> {
    private Context mContext;
    private List<SubServicesListData> mData;

    public ServiceSubItemAdapter(Context context, List<SubServicesListData> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.sub_services_item, viewGroup, false);

        int height = viewGroup.getMeasuredWidth() / 3;
        view.setMinimumHeight(height);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getSubServiceImagePath()).timeout(10000).into(myViewHolder.imageView);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ServiceDetails.class);
                intent.putExtra("subservice_id", mData.get(i).getSubServicesID());
                intent.putExtra("subservice_name", mData.get(i).getSubServiceName());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.service_image);
        }
    }
}
