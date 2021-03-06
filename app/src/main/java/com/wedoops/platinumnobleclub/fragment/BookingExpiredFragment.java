package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.CustomProgressDialog;
import com.wedoops.platinumnobleclub.MyBookingDetail;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.MyBookingAdapter;
import com.wedoops.platinumnobleclub.database.MyBookingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BookingExpiredFragment extends Fragment {


    private static View view;
    private static Activity get_activity;
    private static Context get_context;
    private static CustomProgressDialog customDialog;
    private static RecyclerView recyclerview_expired_bookingdata;
    private static MyBookingAdapter bookinglist_adapter;

    private static View.OnClickListener onMyBookingItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            List<MyBookingList> bl = MyBookingList.listAll(MyBookingList.class);

            Intent intent = new Intent(view.getContext(), MyBookingDetail.class);
            intent.putExtra("eventGUID", bl.get(position).getEventGUID());
            intent.putExtra("bookingNumber", bl.get(position).getBookingNumber());
            view.getContext().startActivity(intent);

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.booking_expired_fragment, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        get_activity = getActivity();
        customDialog = new CustomProgressDialog();

        setupViewByID();
        setupRecyclerView();
        bookinglist_adapter.notifyDataSetChanged();
    }

    private void setupViewByID() {
        recyclerview_expired_bookingdata = view.findViewById(R.id.recyclerview_expired_bookingdata);
    }

    private static void setupRecyclerView() {

        List<MyBookingList> mbl = MyBookingList.listAll(MyBookingList.class);

        List<MyBookingList> filtered_mbl = checkExpiredEvent(mbl);

        bookinglist_adapter = new MyBookingAdapter(filtered_mbl);

        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerview_expired_bookingdata.setLayoutManager(booking_list_mLayoutManager);

        recyclerview_expired_bookingdata.setAdapter(bookinglist_adapter);

        bookinglist_adapter.setOnBookingListItemClickListener(onMyBookingItemClickListener);

    }


    private static List<MyBookingList> checkExpiredEvent(List<MyBookingList> mbl) {

        List<MyBookingList> new_mbl =  new ArrayList<>();

        for (int i = 0; i < mbl.size(); i++) {
            String enddate = mbl.get(i).getEventEndDate();
            String new_startdate = "";
            String new_enddate = "";

            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_endDate = format.parse(enddate);

                Calendar c = Calendar.getInstance();
                Date currentDate = c.getTime();

                if (currentDate.after(new_date_endDate)) {
                    new_mbl.add(mbl.get(i));
                }


            } catch (Exception e) {
                try {
                    TimeZone tz = TimeZone.getTimeZone("SGT");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(tz);
                    Date new_date_endDate = format.parse(enddate);

                    Calendar c = Calendar.getInstance();
                    Date currentDate = c.getTime();

                    if (currentDate.after(new_date_endDate)) {
                        new_mbl.add(mbl.get(i));
                    }

                } catch (Exception ex) {
                    Log.e("Date", e.toString());

                }

            }
        }


        return new_mbl;
    }


}
