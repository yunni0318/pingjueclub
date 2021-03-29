package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wedoops.platinumnobleclub.CustomProgressDialog;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.BookingPagerAdapter;
import com.wedoops.platinumnobleclub.adapters.ReservationPagerAdapter;

public class MyReservationFragment extends Fragment {

    private static ReservationPagerAdapter reservationPagerAdapter;
    private static TabLayout tablayout;

    private static Context get_context;
    private static ViewPager viewpager;
    private static Activity get_activity;
    private static CustomProgressDialog customDialog;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        get_context = getContext();
        customDialog = new CustomProgressDialog();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_reservation, container, false);
        viewpager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tab_layout);
        tablayout.setupWithViewPager(viewpager);
        reservationPagerAdapter = new ReservationPagerAdapter(getChildFragmentManager(), get_context);
        viewpager.setAdapter(reservationPagerAdapter);

        if (getArguments() != null) {
           String data = getArguments().getString("YourKey");
            if(data.equals("YourValue")){
                customDialog.showDialog(getContext());
                viewpager.setCurrentItem(2);
                customDialog.hideDialog();
            }
        }

        return view;
    }
}