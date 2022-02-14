package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.NotificationDetail;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.NotificationAdapter;
import com.wedoops.platinumnobleclub.adapters.RecordsCreditAdapter;
import com.wedoops.platinumnobleclub.database.InboxList;
import com.wedoops.platinumnobleclub.database.TransactionsCreditData;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class NotificationFragment extends Fragment implements NotificationAdapter.OnNotiListener {

    private static Context get_context;
    private static View view;
    private static RecyclerView recyclerView_notification;
    private static NotificationAdapter notificationAdapter;
    private static NotificationAdapter.OnNotiListener onNotiListener;
    public static Activity get_activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notification, container, false);
        get_context = getContext();
        get_activity = getActivity();
        onNotiListener = this;
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


    private static void setupAdapter(){

        List<InboxList> inboxLists = InboxList.listAll(InboxList.class);

        notificationAdapter = new NotificationAdapter(get_context, inboxLists, onNotiListener);
        RecyclerView.LayoutManager talent_mLayoutManager = new LinearLayoutManager(get_context, LinearLayoutManager.VERTICAL, false);
        recyclerView_notification.setLayoutManager(talent_mLayoutManager);
        recyclerView_notification.setAdapter(notificationAdapter);
        recyclerView_notification.setNestedScrollingEnabled(false);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public int onNotiClick(int position) {

        List<InboxList> inboxLists = InboxList.listAll(InboxList.class);
        int inboxid = inboxLists.get(position).getinboxid();

        Intent intent = new Intent(get_context, NotificationDetail.class);
        intent.putExtra("inboxID", inboxid);
        get_context.startActivity(intent);

        return position;
    }
}