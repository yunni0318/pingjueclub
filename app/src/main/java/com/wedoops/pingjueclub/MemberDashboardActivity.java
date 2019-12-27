package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.internal.ads.zzcax;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.MemberDashboardEventDataRecyclerAdapter;
import com.wedoops.pingjueclub.adapters.MemberDashboardTopBannerRecyclerAdapter;
import com.wedoops.pingjueclub.database.CurrencyList;
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

    private static CustomProgressDialog customDialog;

    private static AdLoader adLoader;
    private static AlertDialog alert;

    private CountDownTimer cdt;

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
        get_context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
        customDialog = new CustomProgressDialog();
        checkLoginStatus();
        setupViewByID();
        setupListener();
        setupAdvertisement();

        button_category_setTint(CONSTANTS_VALUE.EVENT_CATEGORY_BUSINESS);


    }


    private void setupAdvertisement() {


        View askDialog = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(get_context);

        TextView textview_title = askDialog.findViewById(R.id.textview_title);
        TextView textview_message = askDialog.findViewById(R.id.textview_message);
        Button button_cancel = askDialog.findViewById(R.id.button_cancel);
        Button button_ok = askDialog.findViewById(R.id.button_ok);

        textview_title.setText("ATTENTION");
        textview_message.setText("You can receive PTS by viewing the following advertisement");
        builder.setView(askDialog);

        builder.setCancelable(false);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

                final AlertDialog alert_video;

                View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom_video_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(get_context);

                VideoView videoview = customLayout.findViewById(R.id.videoview);
                final Button button_skip = customLayout.findViewById(R.id.button_skip);

                String path = "android.resource://com.wedoops.pingjueclub/" + R.raw.sample_ads;
                videoview.setVideoURI(Uri.parse(path));
                videoview.start();

                builder.setView(customLayout);
                builder.setCancelable(false);

                alert_video = builder.create();

                alert_video.show();


                cdt = new CountDownTimer(6000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        button_skip.setText(millisUntilFinished / 1000 + " seconds to skip");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        button_skip.setText("Skip");
                    }

                }.start();

                button_skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(button_skip.getText().toString().equals("Skip")){
                            alert_video.dismiss();

                            cdt.cancel();

                            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

                            String table_name = UserDetails.getTableName(UserDetails.class);
                            String loginid_field = StringUtil.toSQLName("LoginID");

                            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

                            Bundle b = new Bundle();
                            b.putString("access_token", ud_list.get(0).getAccessToken());
                            b.putInt(Api_Constants.COMMAND, Api_Constants.API_DISTRIBUTE_PENDING_AMOUNT);

                            new CallWebServices(Api_Constants.API_DISTRIBUTE_PENDING_AMOUNT, view.getContext(), true).execute(b);


                        }
                    }
                });

//                adLoader.loadAd(new AdRequest.Builder().build());
            }
        });

        alert = builder.create();
//
//
////        adLoader = new AdLoader.Builder(get_context, "ca-app-pub-2772663182449117/8882399941")
//        adLoader = new AdLoader.Builder(get_context, "ca-app-pub-4167585096532227/2369549218")
////        adLoader = new AdLoader.Builder(get_context, "ca-app-pub-3940256099942544/2247696110")
//                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                        // Show the ad.
//
//                        if (adLoader.isLoading()) {
//
//                        } else {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(get_context);
//
//                            View customLayout = getLayoutInflater().inflate(R.layout.advertisement_dialog_custom_view, null);
//                            FrameLayout framelayout = customLayout.findViewById(R.id.framelayout);
//
//                            builder.setView(customLayout);
////                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                }
////                            });
//                            builder.setCancelable(true);
//
//                            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.advertisement_unified_native_adview, null);
//                            MediaView mv = adView.findViewById(R.id.mediaview);
//
//                            mv.setMediaContent(unifiedNativeAd.getMediaContent());
//                            adView.setMediaView(mv);
//                            adView.setNativeAd(unifiedNativeAd);
//                            framelayout.removeAllViews();
//                            framelayout.addView(adView);
//
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//
//
//                        }
//
//
//                    }
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        // Handle the failure by logging, altering the UI, and so on.
//                        Log.e("AdFailed", String.format("%d", errorCode));
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();

    }


    private void checkLoginStatus() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.size() < 1) {
            get_activity.finish();
            Intent intent = new Intent(get_activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            customDialog.showDialog(get_context);
            callMemberDashboardWebService();
        }

    }

    private void setupViewByID() {
        recyclerview_top_banner = view.findViewById(R.id.recyclerview_top_banner);
        recyclerview_eventdata = view.findViewById(R.id.recyclerview_eventdata);
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
                try{
                    startSmoothScroll(smoothScroller);
                }catch (Exception ex){
                    setupRecyclerView();
                }
            }

        };

        setupAutoScroll();

        recyclerview_eventdata.setLayoutManager(eventdata_mLayoutManager);
        recyclerview_eventdata.setItemAnimator(new DefaultItemAnimator());
        recyclerview_top_banner.setLayoutManager(top_banner_mLayoutManager);
        recyclerview_top_banner.setItemAnimator(new DefaultItemAnimator());
        recyclerview_top_banner.setAdapter(topBanner_adapter);
        recyclerview_eventdata.setAdapter(eventData_adapter);


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

//        ViewCompat.setNestedScrollingEnabled(recyclerview_eventdata, false);
//        ViewCompat.setNestedScrollingEnabled(recyclerview_top_banner, false);
//        recyclerview_top_banner.setNestedScrollingEnabled(false);
//        recyclerview_eventdata.setNestedScrollingEnabled(false);

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


    private static void callMemberDashboardWebService() {
//        CustomProgressDialog.showProgressDialog(get_context);

        if (counter < 4) {
            counter++;

            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_DASHBOARDV2);

            new CallWebServices(Api_Constants.API_MEMBER_DASHBOARDV2, view.getContext(), true).execute(b);

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

    private static void checkTopUpStatus() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.get(0).isGotNewTopUp()) {
            alert.show();
        }
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
//        CustomProgressDialog.closeProgressDialog();
        customDialog.hideDialog();

        if (command == Api_Constants.API_MEMBER_DASHBOARDV2) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        counter = 0;
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONObject user_quick_profile_object = response_object.getJSONObject("UserQuickProfile");

                        JSONArray currency_list_array = response_object.getJSONArray("CurrencyList");
                        JSONObject dashboard_data_object = response_object.getJSONObject("DashboardData");
                        JSONArray banner_data_array = dashboard_data_object.getJSONArray("BannerData");
                        JSONArray event_data_array = dashboard_data_object.getJSONArray("EventData");

                        List<MemberDashboardTopBanner> mdtb_all = MemberDashboardTopBanner.listAll(MemberDashboardTopBanner.class);
                        if (mdtb_all.size() > 0) {
                            MemberDashboardTopBanner.deleteAll(MemberDashboardTopBanner.class);
                        }

                        List<MemberDashboardEventData> mded_all = MemberDashboardEventData.listAll(MemberDashboardEventData.class);
                        if (mded_all.size() > 0) {
                            MemberDashboardEventData.deleteAll(MemberDashboardEventData.class);
                        }

                        List<CurrencyList> cl_all = CurrencyList.listAll(CurrencyList.class);
                        if (cl_all.size() > 0) {
                            CurrencyList.deleteAll(CurrencyList.class);
                        }

                        List<UserDetails> ud_listall = UserDetails.listAll(UserDetails.class);
                        String loginid_field = StringUtil.toSQLName("LoginID");

                        UserDetails ud = (UserDetails.find(UserDetails.class, loginid_field + " = ?", ud_listall.get(0).getLoginID())).get(0);

                        ud.setSrno(user_quick_profile_object.getString("Srno"));
                        ud.setLoginID(user_quick_profile_object.getString("LoginID"));
                        ud.setName(user_quick_profile_object.getString("Name"));
                        ud.setNickName(user_quick_profile_object.getString("NickName"));
                        ud.setDOB(user_quick_profile_object.getString("DOB"));
                        ud.setEmail(user_quick_profile_object.getString("Email"));
                        ud.setPhone(user_quick_profile_object.getString("Phone"));
                        ud.setCountryCode(user_quick_profile_object.getString("CountryCode"));
                        ud.setStateCode(user_quick_profile_object.getString("StateCode"));
                        ud.setAddress(user_quick_profile_object.getString("Address"));
                        ud.setGender(user_quick_profile_object.getString("Gender"));
                        ud.setProfilePictureImagePath(user_quick_profile_object.getString("ProfilePictureImagePath"));
                        ud.setUserLevelCode(user_quick_profile_object.getString("UserLevelCode"));
                        ud.setJoinedDate(user_quick_profile_object.getString("JoinedDate"));
                        ud.setCashWallet(user_quick_profile_object.getString("CashWallet"));
                        ud.setGotNewTopUp(user_quick_profile_object.getBoolean("GotNewTopUp"));

                        ud.save();


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

                        for (int i = 0; i < currency_list_array.length(); i++) {
                            JSONObject cl = currency_list_array.getJSONObject(i);
                            CurrencyList cl_db = new CurrencyList(String.valueOf(cl.getInt("Srno")), cl.getString("CurrencyName"), cl.getString("CurrencyCode"), cl.getInt("CurrencyRate"), cl.getString("Status"), cl.getString("ImagePath"), cl.getString("CreatedDate"));
                            cl_db.save();
                        }

                        checkTopUpStatus();
                        displayResult();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    if (returnedObject.getInt("StatusCode") == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
//                        customDialog.showDialog(get_context);

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
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(null);
    }
}


