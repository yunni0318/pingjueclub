package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.wedoops.platinumnobleclub.database.Reservation_History;
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
    private List<Reservation_History> mbl;
    private View.OnClickListener mOnItemClickListener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView reservation_roomtype, remind_desc, date_month, date_day, date_time, ReservationNumber, reservation_pax, reservation_status, reservation_remark;
        public ImageView image_new;

        public MyViewHolder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
            date_month = v.findViewById(R.id.date_month);
            date_time = v.findViewById(R.id.date_time);
            date_day = v.findViewById(R.id.date_day);
            reservation_roomtype = v.findViewById(R.id.reservation_roomtype);
            reservation_pax = v.findViewById(R.id.reservation_pax);
            reservation_status = v.findViewById(R.id.reservation_status);
            remind_desc = v.findViewById(R.id.reservation_desc);
            ReservationNumber = v.findViewById(R.id.reservation_number);
            reservation_remark = v.findViewById(R.id.reservation_remark);
            image_new = v.findViewById(R.id.image_new);


            Typeface typeface = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
//            remind_icon.setTypeface(typeface);
////            textview_event_start_date.setTypeface(typeface);
//            remind_title.setTypeface(typeface);
//            remind_desc.setTypeface(typeface);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReservationHistoryAdapter(List<Reservation_History> mbl, Context context) {
        this.mbl = mbl;
        this.context = context;
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

        String ts = mbl.get(i).getReservationtDate();
        String date = ts.split("T")[0];
        String[] splitDate = date.split("-");
        String day = splitDate[2];
        String month = splitDate[1];
        String year = splitDate[0];
        //Getting months as 1,2 instead of 01,02
        month = Integer.valueOf(month).toString();


        String time = ts.split("T")[1];
        String[] splittime = time.split(":");
        String hours = splittime[0];
        String min = splittime[1];
        String timeSet, strMin;
        int h = Integer.parseInt(hours);
        int m = Integer.parseInt(min);
        if (h > 12) {
            h -= 12;
            timeSet = "PM";
        } else if (h == 0) {
            h += 12;
            timeSet = "AM";
        } else if (h == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        //=====Adding zero in one dightleft  and right======
        if (m < 10)
            strMin = "0" + m;
        else
            strMin = String.valueOf(m);
        String aTime = new StringBuilder().append(pad(h)).append(':')
                .append(pad(Integer.parseInt(strMin))).append(" ").append(timeSet).toString();
        //Change Month name from Month Numbers

//        Calendar cal = Calendar.getInstance();
//        int monthnum = Integer.parseInt(month);
//        cal.set(Calendar.MONTH, monthnum);
//        String month_name = month_date.format(cal.getTime());



        SimpleDateFormat sdf = new SimpleDateFormat("M");
        try {
            Date d = sdf.parse(month);
            sdf.applyPattern("MMM");
            myViewHolder.date_month.setText(sdf.format(d));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }


        myViewHolder.date_day.setText(day);
        myViewHolder.date_time.setText(aTime);
        myViewHolder.remind_desc.setText(mbl.get(i).getProductDescription());
        myViewHolder.reservation_roomtype.setText(context.getString(R.string.reservations_room_type) + " " + mbl.get(i).getProductName());
        myViewHolder.reservation_pax.setText(context.getString(R.string.reservations_pax) + " " + mbl.get(i).getEstimateParticipant());
        myViewHolder.ReservationNumber.setText(context.getString(R.string.reservations_ReservationNumber) + mbl.get(i).getReservationNumber());
        if (mbl.get(i).getReservationStatus().equals("NEW")) {
            myViewHolder.reservation_status.setTextColor(Color.YELLOW);
//            myViewHolder.reservation_status.setText(context.getResources().getString(R.string.reservations_NEW));
            myViewHolder.reservation_status.setVisibility(View.GONE);
            myViewHolder.image_new.setVisibility(View.VISIBLE);


        } else if (mbl.get(i).getReservationStatus().equals("APPROVED")) {
            myViewHolder.reservation_status.setTextColor(Color.GREEN);
            myViewHolder.reservation_status.setText(context.getResources().getString(R.string.reservations_APPROVED));
            myViewHolder.image_new.setVisibility(View.INVISIBLE);
            myViewHolder.reservation_status.setVisibility(View.VISIBLE);


        } else if (mbl.get(i).getReservationStatus().equals("REJECT")) {
            myViewHolder.reservation_status.setTextColor(Color.RED);
            myViewHolder.reservation_status.setText(context.getResources().getString(R.string.reservations_REJECTED));
            myViewHolder.image_new.setVisibility(View.INVISIBLE);
            myViewHolder.reservation_status.setVisibility(View.VISIBLE);
        } else if (mbl.get(i).getReservationStatus().equals("CHECKIN")) {
            myViewHolder.reservation_status.setTextColor(Color.parseColor("#7ad7f0"));
            myViewHolder.reservation_status.setText(context.getResources().getString(R.string.reservations_CHECKIN));
            myViewHolder.image_new.setVisibility(View.INVISIBLE);
            myViewHolder.reservation_status.setVisibility(View.VISIBLE);
        }

        if (mbl.get(i).getRemark() == null) {
            myViewHolder.reservation_remark.setVisibility(View.GONE);
        } else {
            myViewHolder.reservation_remark.setVisibility(View.VISIBLE);
            myViewHolder.reservation_remark.setText(context.getString(R.string.reservations_ReservationRemark) + " " + mbl.get(i).getRemark());

        }


    }

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
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