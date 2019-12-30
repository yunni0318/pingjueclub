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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.StringUtil;
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

    private static Context get_context;

    private static MyBookingAdapter bookinglist_adapter;



    private static View view;
    private static RecyclerView recyclerview_bookingdata;
    private static Activity get_activity;
    private static CustomProgressDialog customDialog;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_booking_activity, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        get_activity = getActivity();
        customDialog = new CustomProgressDialog();

        setupViewByID();
        callBookingListWebService();

    }

    private void setupViewByID() {
        recyclerview_bookingdata = view.findViewById(R.id.recyclerview_bookingdata);
    }

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

    private static void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, get_activity, origin).execute(b);
    }

    private static void setupRecyclerView() {

        List<MyBookingList> mbl = MyBookingList.listAll(MyBookingList.class);
        bookinglist_adapter = new MyBookingAdapter(mbl);

        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerview_bookingdata.setLayoutManager(booking_list_mLayoutManager);

        recyclerview_bookingdata.setAdapter(bookinglist_adapter);

        bookinglist_adapter.setOnBookingListItemClickListener(onMyBookingItemClickListener);

    }

    private static void displayResult() {

        setupRecyclerView();
        bookinglist_adapter.notifyDataSetChanged();

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

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

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

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());

                        }
                    }


                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext());

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
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

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

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

}
