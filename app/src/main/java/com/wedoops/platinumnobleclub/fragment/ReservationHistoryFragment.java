package com.wedoops.platinumnobleclub.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.ReservationHistoryAdapter;
import com.wedoops.platinumnobleclub.database.EventDetailBookingData;
import com.wedoops.platinumnobleclub.database.MyBookingList;

import java.util.List;

public class ReservationHistoryFragment extends Fragment {

    private static Context get_context;
    private RecyclerView recyclerview_bookingdata;
    private View view;
    private ReservationHistoryAdapter reservationHistoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservation_history_fragment, container, false);
        get_context = getContext();

        recyclerview_bookingdata = view.findViewById(R.id.recyclerview_bookingdata);

        List<MyBookingList> mbl = MyBookingList.listAll(MyBookingList.class);

        reservationHistoryAdapter = new ReservationHistoryAdapter(mbl);

        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerview_bookingdata.setLayoutManager(booking_list_mLayoutManager);

        recyclerview_bookingdata.setAdapter(reservationHistoryAdapter);

//        reservationHistoryAdapter.setOnBookingListItemClickListener(onMyBookingItemClickListener);
        return view;
    }
}
