package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.MyBookingList;
import com.wedoops.platinumnobleclub.database.Reservation_roomType;

import java.util.List;

public class RoomTypeSelectionAdapter extends RecyclerView.Adapter<RoomTypeSelectionAdapter.MyViewHolder> {
    private List<Reservation_roomType> mbl;
    private OnClickListener onClickListener;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RoomTypeSelectionAdapter(List<Reservation_roomType> mbl, OnClickListener onClickListener,Context context) {
        this.mbl = mbl;
        this.onClickListener = onClickListener;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageview;
        public TextView text,text2;
        OnClickListener onClickListener1;

        public MyViewHolder(View v, OnClickListener onClickListener) {
            super(v);
            imageview = v.findViewById(R.id.imageview);
            text = v.findViewById(R.id.text);
            text2 = v.findViewById(R.id.text2);
            this.onClickListener1 = onClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickListener(text.getText().toString(),mbl.get(getAdapterPosition()).getProductGUID());
        }
    }

    @NonNull
    @Override
    public RoomTypeSelectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_room_type_selection, viewGroup, false);

        return new RoomTypeSelectionAdapter.MyViewHolder(itemView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomTypeSelectionAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.text.setText(mbl.get(i).getProductName());
        myViewHolder.text2.setText(mbl.get(i).getProductDescription());
    }

    public interface OnClickListener {

        void onClickListener(String roomtitle,String productGUID);
    }


    @Override
    public int getItemCount() {
        return mbl.size();
    }

}
