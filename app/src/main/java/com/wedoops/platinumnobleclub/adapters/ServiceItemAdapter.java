package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.ServiceDetails;
import com.wedoops.platinumnobleclub.database.ServicesListData;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;

import java.util.List;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.MyViewHolder> {
    private Context mContext;
    private List<ServicesListData> mData;
    private View.OnClickListener mOnItemClickListener;
    private static final String KEY_LANG = "key_lang";

    public ServiceItemAdapter(Context context, List<ServicesListData> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.services_item, viewGroup, false);

        int height = viewGroup.getMeasuredWidth() / 3;
        view.setMinimumHeight(height);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

//        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getMainServiceImagePath()).placeholder(R.drawable.loading).timeout(10000).into(myViewHolder.imageView);
        String lang = new ApplicationClass().readFromSharedPreferences(mContext, KEY_LANG);

        Glide.with(myViewHolder.imageView.getContext())
                .asBitmap()
                .load(mData.get(i).getMainServiceImagePath())
                .timeout(10000)
                .placeholder(R.drawable.loading)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        myViewHolder.imageView.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        String[] service_split = mData.get(i).getMainServiceName().split("\\|");

        if (service_split.length > 1) {
            if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                myViewHolder.serviceName.setText(service_split[0]);
            } else {
                myViewHolder.serviceName.setText(service_split[1]);
            }
        }
    }
    public void setOnServiceItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView serviceName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

            imageView = itemView.findViewById(R.id.service_image);
            serviceName = itemView.findViewById(R.id.service_name);
        }
    }


}
