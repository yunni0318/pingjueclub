package com.wedoops.pingjueclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.ServiceDetails;
import com.wedoops.pingjueclub.database.ServicesMerchantDetails;
import com.wedoops.pingjueclub.database.SubServicesListData;

import java.util.List;

public class ServiceDetailsItemAdapter extends RecyclerView.Adapter<ServiceDetailsItemAdapter.MyViewHolder> {
    private Context mContext;
    private List<ServicesMerchantDetails> mData;

    public ServiceDetailsItemAdapter(Context context, List<ServicesMerchantDetails> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.adapter_service_details, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getCompanyImages()).into(myViewHolder.imageView);
        myViewHolder.textview_company_name.setText(mData.get(i).getCompanyName());
        myViewHolder.textview_company_address.setText(mData.get(i).getCompanyAddress());
        myViewHolder.textview_company_description.setText(mData.get(i).getCompanyDescription());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textview_company_name, textview_company_address, textview_company_description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            textview_company_name = itemView.findViewById(R.id.textview_company_name);
            textview_company_address = itemView.findViewById(R.id.textview_company_address);
            textview_company_description = itemView.findViewById(R.id.textview_company_description);

        }
    }
}
