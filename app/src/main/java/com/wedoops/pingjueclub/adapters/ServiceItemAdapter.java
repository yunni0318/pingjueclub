package com.wedoops.pingjueclub.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.ServiceDetails;
import com.wedoops.pingjueclub.database.Services;
import com.wedoops.pingjueclub.database.ServicesListData;

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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getServiceImagePath()).into(myViewHolder.imageView);
//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ServiceDetails.class);
//                intent.putExtra("Title", mData.get(i).getTitle());
//                intent.putExtra("Image", mData.get(i).getImage());
//                intent.putExtra("Thumbnail", mData.get(i).getThumbnail());
//                mContext.startActivity(intent);
//            }
//        });
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
