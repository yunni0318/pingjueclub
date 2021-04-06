package com.wedoops.platinumnobleclub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.MyBookingList;

import java.util.List;

public class RoomTypeSelectionAdapter extends RecyclerView.Adapter<RoomTypeSelectionAdapter.MyViewHolder> {
    private List<MyBookingList> mbl;
    private OnClickListener onClickListener;


    // Provide a suitable constructor (depends on the kind of dataset)
    public RoomTypeSelectionAdapter(List<MyBookingList> mbl, OnClickListener onClickListener) {
        this.mbl = mbl;
        this.onClickListener = onClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageview;
        public TextView text;
        OnClickListener onClickListener1;

        public MyViewHolder(View v, OnClickListener onClickListener) {
            super(v);
            imageview = v.findViewById(R.id.imageview);
            text = v.findViewById(R.id.text);
            this.onClickListener1 = onClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickListener(text.getText().toString());
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

        myViewHolder.text.setText( "Room Title " + i);
    }

    public interface OnClickListener {

        void onClickListener(String roomtitle);
    }


    @Override
    public int getItemCount() {
        return mbl.size();
    }

}
