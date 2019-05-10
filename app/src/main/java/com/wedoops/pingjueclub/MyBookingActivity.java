package com.wedoops.pingjueclub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.MyBookingAdapter;
import com.wedoops.pingjueclub.database.MyBookingList;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MyBookingActivity extends Fragment {

    private static ProgressDialog progress;

    private static MyBookingAdapter bookinglist_adapter;

    private static TextView textview_my_booking;


    private static View view;
    private static RecyclerView recyclerview_bookingdata;

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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progress = new ProgressDialog(view.getContext());

        setupViewByID();
        callBookingListWebService();

    }

    private void setupViewByID() {
        textview_my_booking = view.findViewById(R.id.textview_my_booking);
        recyclerview_bookingdata = view.findViewById(R.id.recyclerview_bookingdata);
    }

    private static void callBookingListWebService() {

        new ApplicationClass().showProgressDialog(progress);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_EVENT_BOOKING_LIST);

        new CallWebServices(Api_Constants.API_EVENT_BOOKING_LIST, view.getContext(), true).execute(b);


    }

    private static void setupRecyclerView() {

        List<MyBookingList> mbl = MyBookingList.listAll(MyBookingList.class);
        bookinglist_adapter = new MyBookingAdapter(mbl);

        RecyclerView.LayoutManager booking_list_mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerview_bookingdata.setLayoutManager(booking_list_mLayoutManager);

        recyclerview_bookingdata.setAdapter(bookinglist_adapter);

        bookinglist_adapter.setOnBookingListItemClickListener(onMyBookingItemClickListener);

    }

    private static void displayResult(){

        setupRecyclerView();
        bookinglist_adapter.notifyDataSetChanged();

    }
    public static void processWSData(JSONObject returnedObject, int command) {

        new ApplicationClass().closeProgressDialog(progress);

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
                    JSONArray errorCode_array = returnedObject.getJSONArray("ErrorCode");

                    int errorCode = 0;
                    String errorMessageEN = "";
                    String errorMessageCN = "";

                    for (int i = 0; i < errorCode_array.length(); i++) {
                        JSONObject error_object = errorCode_array.getJSONObject(i);
                        errorCode = error_object.getInt("Code");
                        errorMessageEN = error_object.getString("MessageEN");
                        errorMessageCN = error_object.getString("MessageCN");

                    }

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (errorCode == 1506) {
                        new DisplayAlertDialog().displayAlertDialogError(1506, view.getContext());
                    } else {
                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode,errorMessageEN, false, view.getContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode,errorMessageCN, false, view.getContext());

                        }
                    }


                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

}
