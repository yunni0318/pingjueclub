package com.wedoops.platinumnobleclub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.ServicesTopBannerData;

import java.util.List;

public class ServicesTopBannerRecyclerAdapter extends RecyclerView.Adapter<ServicesTopBannerRecyclerAdapter.MyViewHolder> {
    private List<ServicesTopBannerData> ud;
    private View.OnClickListener mOnItemClickListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;

        public MyViewHolder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
            imageview = v.findViewById(R.id.imageview_memberdashboard_top_banner);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ServicesTopBannerRecyclerAdapter(List<ServicesTopBannerData> ud) {
        this.ud = ud;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_member_dashboard_top_banner_recycler, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Glide.with(myViewHolder.imageview.getContext()).load(ud.get(i).getImagePath()).timeout(10000).into(myViewHolder.imageview);

    }


    public void setOnTopBannerItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return ud.size();
    }
}
