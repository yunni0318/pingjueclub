package com.wedoops.pingjueclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.ServicesOtherNewsData;

import java.util.List;
import java.util.Random;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServicesOtherNewsData> mData;

    public NewsItemAdapter(Context mContext, List<ServicesOtherNewsData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.news_item, viewGroup, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.width = (viewGroup.getMeasuredWidth() * 8) / 10;
        view.setLayoutParams(params);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.title.setText(mData.get(i).getEventName());

        int endIndex = 0;

        if (mData.get(i).getEventDescription().length() > 40) {
            endIndex = 40;
        } else {
            if (mData.get(i).getEventDescription().length() > 20) {
                endIndex = 20;
            } else {
                endIndex = 10;
            }
        }

        String description_sub = mData.get(i).getEventDescription().substring(0, endIndex) + "...";

        myViewHolder.desc.setText(description_sub);
        int minRandom = 1;
        int maxRandom = 100;
        myViewHolder.like.setText(String.valueOf(new Random().nextInt((maxRandom - minRandom) + 1) + minRandom));
        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getEventBannerImagePath()).into(myViewHolder.imageView);
//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView imageView;
        private TextView title, desc, like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewnews_id);
            imageView = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            desc = itemView.findViewById(R.id.news_desc);
            like = itemView.findViewById(R.id.news_like);
        }
    }
}
