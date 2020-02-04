package com.wedoops.platinumnobleclub;

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

import com.google.android.material.tabs.TabLayout;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.adapters.BookingPagerAdapter;
import com.wedoops.platinumnobleclub.adapters.MyBookingAdapter;
import com.wedoops.platinumnobleclub.database.MyBookingList;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallRefreshToken;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;
import com.wedoops.platinumnobleclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MyBookingActivity extends Fragment {

    private static BookingPagerAdapter booking_pager_adapter;
    private static TabLayout tablayout;

    private static Context get_context;
    private static ViewPager viewpager;
    private static View view;
    private static Activity get_activity;
    private static CustomProgressDialog customDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_booking_activity, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customDialog = new CustomProgressDialog();

        callBookingListWebService();
        setupViewByID();

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        get_activity = getActivity();
//        customDialog = new CustomProgressDialog();
//
//        setupViewByID();
//        setupAdapter();
//    }

    private static void callBookingListWebService() {

//        CustomProgressDialog.showProgressDialog(get_context);
        customDialog.showDialog(get_context);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_EVENT_BOOKING_LIST);

        new CallWebServices(Api_Constants.API_EVENT_BOOKING_LIST, view.getContext(), true).execute(b);

    }

    private void setupViewByID() {
        viewpager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tab_layout);
        tablayout.setupWithViewPager(viewpager);
        booking_pager_adapter = new BookingPagerAdapter(getChildFragmentManager());

    }

    private static void displayResult() {
        viewpager.setAdapter(booking_pager_adapter);
    }


    public static void processWSData(JSONObject returnedObject, int command) {

//        CustomProgressDialog.closeProgressDialog();
        customDialog.hideDialog();

        if (command == Api_Constants.API_EVENT_BOOKING_LIST) {

            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        JSONArray response_array = returnedObject.getJSONArray("ResponseData");

                        MyBookingList.deleteAll(MyBookingList.class);

                        for (int i = 0; i < response_array.length(); i++) {

                            JSONObject ed = response_array.getJSONObject(i);

                            MyBookingList mbl = new MyBookingList(String.valueOf(ed.getInt("Srno")), ed.getString("EventGUID"), ed.getString("EventName"), ed.getString("EventCategoryCode"), ed.getString("EventDescription"), ed.getString("EventBannerImagePath"), ed.getDouble("EventPrice"), ed.getDouble("EventUpfrontRate"), ed.getString("UserLevelCode"), ed.getString("UserLevelCodeVisibility")
                                    , String.valueOf(ed.getInt("MaxParticipant")), ed.getString("EventStartDate"), ed.getString("EventEndDate"), ed.getString("RegistrationStartDate"), ed.getString("RegistrationEndDate"), ed.getBoolean("Active"), ed.getString("CreatedBy"), ed.getString("CreatedDate"), String.valueOf(ed.getInt("ReservedSeat")), ed.getString("BookingNumber"));
                            mbl.save();
                        }

                     displayResult();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST);

                    } else {

                        JSONArray errorCode_array = returnedObject.getJSONArray("ErrorCode");

                        errorCode = 0;
                        String errorMessageEN = "";
                        String errorMessageCN = "";

                        for (int i = 0; i < errorCode_array.length(); i++) {
                            JSONObject error_object = errorCode_array.getJSONObject(i);
                            errorCode = error_object.getInt("Code");
                            errorMessageEN = error_object.getString("MessageEN");
                            errorMessageCN = error_object.getString("MessageCN");

                        }

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext(), get_activity);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext(), get_activity);

                        }
                    }


                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        }
    }

    public static void processRefreshToken(JSONObject returnedObject, int command, int origin) {
        if (command == RefreshTokenAPI.API_REFRESH_TOKEN) {

//            CustomProgressDialog.closeProgressDialog();

            customDialog.hideDialog();

            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");

                        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

                        String table_name = UserDetails.getTableName(UserDetails.class);
                        String loginid_field = StringUtil.toSQLName("LoginID");

                        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

                        ud_list.get(0).setAccessToken(response_object.getString("AccessToken"));
                        ud_list.get(0).setRefreshToken(response_object.getString("RefreshToken"));

                        ud_list.get(0).save();

                        if (origin == RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST) {
                            callBookingListWebService();
                        }


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST);

                    } else {

                        JSONArray errorCode_array = returnedObject.getJSONArray("ErrorCode");

                        errorCode = 0;
                        String errorMessageEN = "";
                        String errorMessageCN = "";

                        for (int i = 0; i < errorCode_array.length(); i++) {
                            JSONObject error_object = errorCode_array.getJSONObject(i);
                            errorCode = error_object.getInt("Code");
                            errorMessageEN = error_object.getString("MessageEN");
                            errorMessageCN = error_object.getString("MessageCN");

                        }

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext(), get_activity);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext(), get_activity);

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }



    private static void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, get_context, get_activity, origin).execute(b);
    }
}
