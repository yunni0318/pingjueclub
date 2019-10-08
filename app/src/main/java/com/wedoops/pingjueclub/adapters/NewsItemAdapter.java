package com.wedoops.pingjueclub.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.News;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.MyViewHolder>{

    private Context mContext;
    private List<News> mData;

    public NewsItemAdapter(Context mContext, List<News> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.news_item,viewGroup,false);
        RecyclerView.LayoutParams params= (RecyclerView.LayoutParams)view.getLayoutParams();
        params.width=(viewGroup.getMeasuredWidth()*8)/10;
        view.setLayoutParams(params);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.title.setText(mData.get(i).getTitle());
        myViewHolder.desc.setText(mData.get(i).getDesc());
        myViewHolder.like.setText(String.valueOf(mData.get(i).getLike()));
        myViewHolder.imageView.setImageResource(mData.get(i).getThumbnail());
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, String.valueOf(mData.get(i).getLike()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imageView;
        private TextView title,desc,like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardviewnews_id);
            imageView=(ImageView)itemView.findViewById(R.id.news_image);
            title=(TextView)itemView.findViewById(R.id.news_title);
            desc=(TextView)itemView.findViewById(R.id.news_desc);
            like=(TextView)itemView.findViewById(R.id.news_like);
        }
    }
}
