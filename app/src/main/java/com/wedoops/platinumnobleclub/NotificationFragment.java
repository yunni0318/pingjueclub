package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedoops.platinumnobleclub.adapters.NotificationAdapter;

public class NotificationFragment extends Fragment {

    private static Context get_context;
    private static View view;
    private static RecyclerView recyclerView_notification;
    private static NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notification, container, false);
        get_context = getContext();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewByID();
        setupAdapter();
    }


    private void setupViewByID() {
        recyclerView_notification = view.findViewById(R.id.RecyclerView_notification);
    }


    private void setupAdapter() {
        notificationAdapter = new NotificationAdapter(get_context);
        RecyclerView.LayoutManager talent_mLayoutManager = new LinearLayoutManager(get_context, LinearLayoutManager.VERTICAL, false);
        recyclerView_notification.setLayoutManager(talent_mLayoutManager);
        recyclerView_notification.setAdapter(notificationAdapter);
        recyclerView_notification.setNestedScrollingEnabled(false);
        notificationAdapter.notifyDataSetChanged();
    }
}