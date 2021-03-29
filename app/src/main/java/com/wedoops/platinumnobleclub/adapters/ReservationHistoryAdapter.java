package com.wedoops.platinumnobleclub.adapters;

import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.MyBookingList;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.MyViewHolder> {
    private List<MyBookingList> mbl;
    private View.OnClickListener mOnItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView remind_title, remind_desc, date_month, date_day, date_time;

        public MyViewHolder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
            date_month = v.findViewById(R.id.date_month);
            date_time = v.findViewById(R.id.date_time);
            date_day = v.findViewById(R.id.date_day);
            remind_title = v.findViewById(R.id.remind_title);
            remind_desc = v.findViewById(R.id.remind_desc);

            Typeface typeface = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
//            remind_icon.setTypeface(typeface);
////            textview_event_start_date.setTypeface(typeface);
//            remind_title.setTypeface(typeface);
//            remind_desc.setTypeface(typeface);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReservationHistoryAdapter(List<MyBookingList> mbl) {
        this.mbl = mbl;
    }

    @NonNull
    @Override
    public ReservationHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_my_reservation, viewGroup, false);

        return new ReservationHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationHistoryAdapter.MyViewHolder myViewHolder, int i) {

        String ts = mbl.get(i).getEventStartDate();
        String date = ts.split("T")[0];
        String[] splitDate = date.split("-");
        String day = splitDate[2];
        String month = splitDate[1];
        String year = splitDate[0];
        //Getting months as 1,2 instead of 01,02
        month = Integer.valueOf(month).toString();

        //Change Month name from Month Numbers
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int monthnum = Integer.parseInt(month);
        cal.set(Calendar.MONTH, monthnum);
        String month_name = month_date.format(cal.getTime());


        myViewHolder.date_month.setText(month_name);
        myViewHolder.date_day.setText(day);
        myViewHolder.date_time.setText("5.00 PM");
        myViewHolder.remind_desc.setText(mbl.get(i).getEventDescription());
        myViewHolder.remind_title.setText(mbl.get(i).getEventName());
    }

    public void setOnBookingListItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {

        return mbl.size();
    }
}
//https://image.winudf.com/v2/image1/Y29tLnh5emNhcl9zY3JlZW5fM18xNTY3MDM3ODcwXzAzOQ/screen-3.jpg?fakeurl=1&type=.jpg