package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.ServiceDetails;
import com.wedoops.platinumnobleclub.database.ServicesMerchantDetails;

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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getCompanyImages()).placeholder(R.drawable.not_found).into(myViewHolder.imageView);
        myViewHolder.textview_company_name.setText(mData.get(i).getCompanyName());
        myViewHolder.textview_company_address.setText(mData.get(i).getCompanyAddress());
        myViewHolder.textview_company_description.setText(mData.get(i).getCompanyDescription());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String params = String.format("geo:%s,%s?q=%s, %s", mData.get(i).getCompanyLatX(), mData.get(i).getCompanyLatY(), mData.get(i).getCompanyLatX(), mData.get(i).getCompanyLatY());

                Uri location = Uri.parse(params); // z param is zoom level
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(myViewHolder.itemView.getContext().getPackageManager()) != null) {
                mContext.startActivity(mapIntent);
//                }

            }
        });


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
