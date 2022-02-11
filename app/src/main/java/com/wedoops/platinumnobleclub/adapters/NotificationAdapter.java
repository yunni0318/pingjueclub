package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.InboxList;
import com.wedoops.platinumnobleclub.database.MyBookingList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<InboxList> inbox;
    private OnNotiListener mOnNotiListener;

    public NotificationAdapter(Context context, List<InboxList> inbox, OnNotiListener onNotiListener){
        this.context = context;
        this.inbox = inbox;
        this.mOnNotiListener = onNotiListener;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_notification, viewGroup, false);

        return new NotificationAdapter.MyViewHolder(itemView, mOnNotiListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        holder.noti_title.setText(inbox.get(position).getTitle());

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss");
            Date date = formatter.parse(inbox.get(position).getDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String finalDate = newFormat.format(date);

            holder.noti_date.setText(finalDate);


        } catch (ParseException ex) {
            ex.toString();
        }
    }

    @Override
    public int getItemCount() {
        return inbox.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView noti_title, noti_date;
        ImageView noti_icon;
        OnNotiListener onNotiListener;

        public MyViewHolder(@NonNull View itemView, OnNotiListener onNotiListener) {
            super(itemView);
            noti_icon = itemView.findViewById(R.id.noti_icon);
            noti_title = itemView.findViewById(R.id.noti_title);
            noti_date = itemView.findViewById(R.id.noti_date);
            this.onNotiListener = onNotiListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNotiListener.onNotiClick(getAdapterPosition());
        }
    }

    public interface OnNotiListener {
        int onNotiClick(int position);
    }
}
