package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wedoops.platinumnobleclub.adapters.BookingPagerAdapter;
import com.wedoops.platinumnobleclub.adapters.NewsPagerAdapter;

public class NewsFragment extends Fragment {
    private static NewsPagerAdapter newsPagerAdapter;
    private static TabLayout tablayout;
    private static Context get_context;
    private static ViewPager viewpager;
    private static View view;
    private static Activity get_activity;
    private static CustomProgressDialog customDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customDialog = new CustomProgressDialog();
        setupViewByID();
        displayPager();
    }

    private void setupViewByID() {
        viewpager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tab_layout);
        tablayout.setupWithViewPager(viewpager);
    }

    private void displayPager() {
        newsPagerAdapter = new NewsPagerAdapter(getChildFragmentManager(), get_context);
        viewpager.setAdapter(newsPagerAdapter);
    }
}