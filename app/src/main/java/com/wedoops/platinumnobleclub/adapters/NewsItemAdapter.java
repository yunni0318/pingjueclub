package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.ServicesOtherNewsData;

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
        String html_striped_text = stripHtml(mData.get(i).getEventDescription());
        if (html_striped_text.length() > 120) {
            endIndex = 120;
        } else {
            if (html_striped_text.length() > 60) {
                endIndex = 60;
            }
        }

        String description_sub = html_striped_text.substring(0, endIndex) + "...";

        myViewHolder.desc.setText(description_sub);

        int minRandom = 1;
        int maxRandom = 100;
        myViewHolder.like.setText(String.valueOf(new Random().nextInt((maxRandom - minRandom) + 1) + minRandom));

//        myViewHolder.like_button.setIcon(IconType.Thumb);
//        myViewHolder.like_button.setLikeDrawableRes(R.drawable.like_icon);

        Glide.with(myViewHolder.imageView.getContext()).load(mData.get(i).getEventBannerImagePath()).timeout(10000).into(myViewHolder.imageView);
//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView imageView;
        private TextView title, desc, like;
//        private LikeButton like_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewnews_id);
            imageView = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            desc = itemView.findViewById(R.id.news_desc);
            like = itemView.findViewById(R.id.news_like);
//            like_button = itemView.findViewById(R.id.like_button);
        }
    }
}
