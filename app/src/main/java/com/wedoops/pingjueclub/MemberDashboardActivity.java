package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.MemberDashboardEventDataRecyclerAdapter;
import com.wedoops.pingjueclub.adapters.MemberDashboardTopBannerRecyclerAdapter;
import com.wedoops.pingjueclub.database.MemberDashboardEventData;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.helper.LinePagerIndicatorDecoration;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallRefreshToken;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.wedoops.pingjueclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MemberDashboardActivity extends Fragment {

    private static View view;
    private static RecyclerView recyclerview_top_banner, recyclerview_eventdata;
    private static TextView textview_popular_trip;
    private static ImageButton imagebutton_class, imagebutton_business, imagebutton_style;

    private static final Handler handler = new Handler();
    private static Runnable runnable;
    public static Activity get_activity;
    public static Context get_context;

    private static MemberDashboardTopBannerRecyclerAdapter topBanner_adapter;
    private static MemberDashboardEventDataRecyclerAdapter eventData_adapter;

    private static int counter;
    public static int position = 0;

    private static String currentSelectedCategory;

    private static View.OnClickListener onEventDataItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            String tablename_ed = StringUtil.toSQLName("MemberDashboardEventData");
            String fieldname_category_code = StringUtil.toSQLName("EventCategoryCode");

            List<MemberDashboardEventData> ed = MemberDashboardEventData.findWithQuery(MemberDashboardEventData.class, "Select * from " + tablename_ed + " where " + fieldname_category_code + " = ?", currentSelectedCategory);

            Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
            intent.putExtra("eventGUID", ed.get(position).getEventGUID());
            view.getContext().startActivity(intent);

        }
    };

    private static View.OnClickListener onTopBannerItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            List<MemberDashboardTopBanner> ed = MemberDashboardTopBanner.listAll(MemberDashboardTopBanner.class);

            if (ed.get(position).getAnnouncementType().equals(CONSTANTS_VALUE.EVENT_TOP_BANNER_NEWTRIP)) {

                Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
                intent.putExtra("eventGUID", ed.get(position).getRedirectURL());
                view.getContext().startActivity(intent);

            } else {
                String url = ed.get(position).getRedirectURL();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.member_dashboard_activity, container, false);
        get_context=getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();

        checkLoginStatus();
        setupViewByID();
        setupListener();
        setupCustomFont();

        button_category_setTint(CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS);

    }

    private void checkLoginStatus() {

        List<UserDetails> ud = UserDetails.findWithQuery(UserDetails.class, "Select access_token from USER_DETAILS");

        if (ud.size() < 1) {
            get_activity.finish();
            Intent intent = new Intent(get_activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            callMemberDashboardWebService();
        }

    }

    private void setupViewByID() {
        recyclerview_top_banner = view.findViewById(R.id.recyclerview_top_banner);
        recyclerview_eventdata = view.findViewById(R.id.recyclerview_eventdata);
        textview_popular_trip = view.findViewById(R.id.textview_popular_trip);
        imagebutton_class = view.findViewById(R.id.imagebutton_class);
        imagebutton_business = view.findViewById(R.id.imagebutton_business);
        imagebutton_style = view.findViewById(R.id.imagebutton_style);
    }

    private void setupListener() {

        imagebutton_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_category_setTint(CONSTANTS_VALUE.EVENT_CATEGORY_CLASS);

                String tablename_ed = StringUtil.toSQLName("MemberDashboardEventData");
                String fieldname_category_code = StringUtil.toSQLName("EventCategoryCode");

                currentSelectedCategory = CONSTANTS_VALUE.EVENT_CATEGORY_CLASS;

                List<MemberDashboardEventData> ed = MemberDashboardEventData.findWithQuery(MemberDashboardEventData.class, "Select * from " + tablename_ed + " where " + fieldname_category_code + " = ?", CONSTANTS_VALUE.EVENT_CATEGORY_CLASS);

                eventData_adapter = new MemberDashboardEventDataRecyclerAdapter(ed);
                recyclerview_eventdata.setAdapter(eventData_adapter);
                eventData_adapter.setOnEventDataItemClickListener(onEventDataItemClickListener);
                eventData_adapter.notifyDataSetChanged();
            }
        });

        imagebutton_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_category_setTint(CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS);

                String tablename_ed = StringUtil.toSQLName("MemberDashboardEventData");
                String fieldname_category_code = StringUtil.toSQLName("EventCategoryCode");

                currentSelectedCategory = CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS;

                List<MemberDashboardEventData> ed = MemberDashboardEventData.findWithQuery(MemberDashboardEventData.class, "Select * from " + tablename_ed + " where " + fieldname_category_code + " = ?", CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS);

                eventData_adapter = new MemberDashboardEventDataRecyclerAdapter(ed);
                recyclerview_eventdata.setAdapter(eventData_adapter);
                eventData_adapter.setOnEventDataItemClickListener(onEventDataItemClickListener);
                eventData_adapter.notifyDataSetChanged();
            }
        });

        imagebutton_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_category_setTint(CONSTANTS_VALUE.EVENT_CATEGORY_STYLE);

                String tablename_ed = StringUtil.toSQLName("MemberDashboardEventData");
                String fieldname_category_code = StringUtil.toSQLName("EventCategoryCode");

                currentSelectedCategory = CONSTANTS_VALUE.EVENT_CATEGORY_STYLE;

                List<MemberDashboardEventData> ed = MemberDashboardEventData.findWithQuery(MemberDashboardEventData.class, "Select * from " + tablename_ed + " where " + fieldname_category_code + " = ?", CONSTANTS_VALUE.EVENT_CATEGORY_STYLE);

                eventData_adapter = new MemberDashboardEventDataRecyclerAdapter(ed);
                recyclerview_eventdata.setAdapter(eventData_adapter);
                eventData_adapter.setOnEventDataItemClickListener(onEventDataItemClickListener);
                eventData_adapter.notifyDataSetChanged();
            }
        });
    }

    private static void setupRecyclerView() {

        String tablename_tb = StringUtil.toSQLName("MemberDashboardTopBanner");
        List<MemberDashboardTopBanner> ud = MemberDashboardTopBanner.findWithQuery(MemberDashboardTopBanner.class, "Select * from " + tablename_tb);

        String tablename_ed = StringUtil.toSQLName("MemberDashboardEventData");
        String fieldname_category_code = StringUtil.toSQLName("EventCategoryCode");

        List<MemberDashboardEventData> ed = MemberDashboardEventData.findWithQuery(MemberDashboardEventData.class, "Select * from " + tablename_ed + " where " + fieldname_category_code + " = ?", CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS);
        currentSelectedCategory = CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS;
        topBanner_adapter = new MemberDashboardTopBannerRecyclerAdapter(ud);
        eventData_adapter = new MemberDashboardEventDataRecyclerAdapter(ed);

        RecyclerView.LayoutManager eventdata_mLayoutManager = new LinearLayoutManager(view.getContext());
        RecyclerView.LayoutManager top_banner_mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(view.getContext()) {
                    private static final float SPEED = 100f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        setupAutoScroll();

        recyclerview_eventdata.setLayoutManager(eventdata_mLayoutManager);
        recyclerview_eventdata.setItemAnimator(new DefaultItemAnimator());
        recyclerview_top_banner.setLayoutManager(top_banner_mLayoutManager);
        recyclerview_top_banner.setItemAnimator(new DefaultItemAnimator());
        recyclerview_top_banner.setAdapter(topBanner_adapter);
        recyclerview_eventdata.setAdapter(eventData_adapter);
        recyclerview_eventdata.setNestedScrollingEnabled(false);

        recyclerview_top_banner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    try {
                        position = ((LinearLayoutManager) recyclerview_top_banner.getLayoutManager())
                                .findFirstVisibleItemPosition();
                    } catch (Exception e) {
                        position = 0;
                    }

                }
            }
        });

        //Paging Feature
        if (recyclerview_top_banner.getOnFlingListener() == null) {
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(recyclerview_top_banner);
        }


        //Indicator
        recyclerview_top_banner.addItemDecoration(new LinePagerIndicatorDecoration());

        //Recycler OnClick Alternative
        eventData_adapter.setOnEventDataItemClickListener(onEventDataItemClickListener);
        topBanner_adapter.setOnTopBannerItemClickListener(onTopBannerItemClickListener);

    }


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private static void setupAutoScroll() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (position == 3) {
                    position = 0;
                }
                recyclerview_top_banner.smoothScrollToPosition(position);
                position++;
                handler.postDelayed(this, 2000);
            }

        };


        handler.postDelayed(runnable, 2000);
    }

    private static void setupCustomFont() {
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
        textview_popular_trip.setTypeface(typeface);
    }

    private static void callMemberDashboardWebService() {
        CustomProgressDialog.showProgressDialog(get_context);

        if (counter < 4) {
            counter++;

            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());



            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_DASHBOARD);

            new CallWebServices(Api_Constants.API_MEMBER_DASHBOARD, view.getContext(), true).execute(b);

        } else {
            displayResult();
        }


    }

    private static void callRefreshTokenWebService() {

//        String table_name = UserDetails.getTableName(UserDetails.class);
//        String username_field = StringUtil.toSQLName("username");
//        String username_value = new ApplicationClass().readFromSharedPreferences(this, "username");
//
//        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + username_field + " = ?", username_value);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

//        new CallWebServices(Api_Constants.API_REFRESH_TOKEN, view.getContext(), true).execute(b);
        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, get_activity, RefreshTokenAPI.ORIGIN_MEMBER_DASHBOARD).execute(b);

    }


    private static void displayResult() {

        setupRecyclerView();
        topBanner_adapter.notifyDataSetChanged();
        eventData_adapter.notifyDataSetChanged();

    }


    private void button_category_setTint(String currentCategory) {
        if (currentCategory.equals(CONSTANTS_VALUE.EVENT_CATEGORY_CLASS)) {

            imagebutton_class.setImageResource(R.drawable.category_class);
            imagebutton_business.setImageResource(R.drawable.category_business_inactive);
            imagebutton_style.setImageResource(R.drawable.category_style_inactive);

        } else if (currentCategory.equals(CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS)) {
            imagebutton_class.setImageResource(R.drawable.category_class_inactive);
            imagebutton_business.setImageResource(R.drawable.category_business);
            imagebutton_style.setImageResource(R.drawable.category_style_inactive);

        } else {
            imagebutton_class.setImageResource(R.drawable.category_class_inactive);
            imagebutton_business.setImageResource(R.drawable.category_business_inactive);
            imagebutton_style.setImageResource(R.drawable.category_style);

        }

    }


    public static void processWSData(JSONObject returnedObject, int command) {
        CustomProgressDialog.closeProgressDialog();

        if (command == Api_Constants.API_MEMBER_DASHBOARD) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        counter = 0;
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONArray banner_data_array = response_object.getJSONArray("BannerData");
                        JSONArray event_data_array = response_object.getJSONArray("EventData");

                        List<MemberDashboardTopBanner> mdtb_all = MemberDashboardTopBanner.listAll(MemberDashboardTopBanner.class);
                        if (mdtb_all.size() > 0) {
                            MemberDashboardTopBanner.deleteAll(MemberDashboardTopBanner.class);
                        }

                        List<MemberDashboardEventData> mded_all = MemberDashboardEventData.listAll(MemberDashboardEventData.class);
                        if (mded_all.size() > 0) {
                            MemberDashboardEventData.deleteAll(MemberDashboardEventData.class);
                        }

                        for (int i = 0; i < banner_data_array.length(); i++) {

                            JSONObject bd = banner_data_array.getJSONObject(i);

                            MemberDashboardTopBanner mdtp = new MemberDashboardTopBanner(String.valueOf(bd.getInt("Srno")), bd.getString("ImagePath"), bd.getString("RedirectURL"), bd.getString("AnnouncementType"), bd.getString("AdminRemarks"), bd.getString("PublishDate"), bd.getBoolean("Active"), bd.getString("CreatedBy"), bd.getString("CreatedDate"));
                            mdtp.save();
                        }

                        for (int i = 0; i < event_data_array.length(); i++) {

                            JSONObject ed = event_data_array.getJSONObject(i);

                            MemberDashboardEventData mded = new MemberDashboardEventData(String.valueOf(ed.getInt("Srno")), ed.getString("EventGUID"), ed.getString("EventName"), ed.getString("EventCategoryCode"), ed.getString("EventDescription").replace("\\\"", "\""), ed.getString("EventBannerImagePath"), ed.getDouble("EventPrice"), ed.getDouble("EventUpfrontRate"), ed.getString("UserLevelCode"), ed.getString("UserLevelCodeVisibility")
                                    , String.valueOf(ed.getInt("MaxParticipant")), ed.getString("EventStartDate"), ed.getString("EventEndDate"), ed.getString("RegistrationStartDate"), ed.getString("RegistrationEndDate"), ed.getBoolean("Active"), ed.getString("CreatedBy"), ed.getString("CreatedDate"), String.valueOf(ed.getInt("ReservedSeat")), ed.getString("BookingNumber"));
                            mded.save();
                        }

                        displayResult();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    if (returnedObject.getInt("StatusCode") == 401) {
                        CustomProgressDialog.showProgressDialog(get_context);

                        callRefreshTokenWebService();

                    } else {
//                        JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
//                        new DisplayAlertDialog().displayAlertDialogError(errorCode_object.getInt("Code"), view.getContext());

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
        } else if (command == RefreshTokenAPI.API_REFRESH_TOKEN) {

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

                        callMemberDashboardWebService();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {
//                    JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
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


