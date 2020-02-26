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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.ServiceDetails;
import com.wedoops.platinumnobleclub.database.ServicesListData;

import java.util.List;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.MyViewHolder> {
    private Context mContext;
    private List<ServicesListData> mData;


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

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ServicesSubActivity.class);
                Intent intent = new Intent(mContext, ServiceDetails.class);
                intent.putExtra("main_services_id", mData.get(i).getMainServicesID());
                intent.putExtra("main_services_name", mData.get(i).getMainServiceName());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //        TextView title;
        ImageView imageView;
//        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            cardView=(CardView)itemView.findViewById(R.id.cardview_id);
//            title=(TextView)itemView.findViewById(R.id.service_title);
            imageView = itemView.findViewById(R.id.service_image);
        }
    }


}
