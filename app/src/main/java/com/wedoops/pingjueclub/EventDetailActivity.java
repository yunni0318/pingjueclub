package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.database.EventDetailBookingData;
import com.wedoops.pingjueclub.database.EventDetailEventData;
import com.wedoops.pingjueclub.database.MemberDashboardEventData;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallRefreshToken;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.wedoops.pingjueclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class EventDetailActivity extends Activity {

    private ImageView imageview_eventdetail, imageview_user_rank_bronze, imageview_user_rank_gold, imageview_user_rank_platinum;
    private ListView listview_event_detail_timeline;
    private Button event_detail_button_jointrip;
    private WebView event_detail_webview;
    private SwipeRefreshLayout swipeRefreshLayout;
    public TextView textview_upfront_payment, textview_event_date, textview_join_trip_amount, textview_event_name, textview_event_price;
    private static final String KEY_LANG = "key_lang"; // preference key


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);

        loadLanguage();
        setupFindById();
        callEventDetailWebServices();

    }

    private void callEventDetailWebServices() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        Bundle extras = getIntent().getExtras();
        String eventGUID = "";

        if (extras != null) {
            eventGUID = extras.getString("eventGUID");
        }

        Bundle b = new Bundle();
        b.putString("eventGUID", eventGUID);
        b.putString("access_token", ud.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_EVENT_DETAILS);

        new CallWebServices(Api_Constants.API_EVENT_DETAILS, EventDetailActivity.this, true).execute(b);
    }

    private void callEventDetailMakeBooking() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);
        List<EventDetailEventData> eded_all = EventDetailEventData.listAll(EventDetailEventData.class);

        String eventGUID = eded_all.get(0).getEventGUID();

        Bundle b = new Bundle();
        b.putString("eventGUID", eventGUID);
        b.putString("access_token", ud.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_EVENT_DETAILS_MAKE_BOOKING);

        new CallWebServices(Api_Constants.API_EVENT_DETAILS_MAKE_BOOKING, EventDetailActivity.this, true).execute(b);
    }

    private void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, EventDetailActivity.this, origin).execute(b);
    }

    private void setupFindById() {

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF0000"), Color.parseColor("#0000FF"), Color.parseColor("#FFFF00"));

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        swipeRefreshLayout.setRefreshing(true);
                        callEventDetailWebServices();

                    }
                }
        );

        imageview_eventdetail = findViewById(R.id.imageview_eventdetail);

        imageview_user_rank_bronze = findViewById(R.id.imageview_user_rank_bronze);
        imageview_user_rank_gold = findViewById(R.id.imageview_user_rank_gold);
        imageview_user_rank_platinum = findViewById(R.id.imageview_user_rank_platinum);

        textview_upfront_payment = findViewById(R.id.textview_upfront_payment);
        textview_event_date = findViewById(R.id.textview_event_date);
        textview_join_trip_amount = findViewById(R.id.textview_join_trip_amount);
        textview_event_name = findViewById(R.id.textview_event_name);
        textview_event_price = findViewById(R.id.textview_event_price);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-regular.ttf");
        textview_upfront_payment.setTypeface(typeface);
        textview_event_date.setTypeface(typeface);
        textview_join_trip_amount.setTypeface(typeface);

        Typeface typeface_bold_500 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-500.ttf");
        textview_event_price.setTypeface(typeface_bold_500);

        Typeface typeface_bold_700 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-700.ttf");
        textview_event_name.setTypeface(typeface_bold_700);

        listview_event_detail_timeline = findViewById(R.id.listview_event_detail_timeline);
        event_detail_button_jointrip = findViewById(R.id.event_detail_button_jointrip);
        event_detail_button_jointrip.setText(this.getResources().getString(R.string.event_detail_join_trip));
        event_detail_webview = findViewById(R.id.event_detail_webview);
    }

    private void displayResult() {

        List<EventDetailEventData> eded_all = EventDetailEventData.listAll(EventDetailEventData.class);

        Double d_upfrontrate = eded_all.get(0).getEventUpfrontRate();
        Double d_eventprice = eded_all.get(0).getEventPrice();

        String upfront_value = String.valueOf(d_upfrontrate.intValue());
        String eventprice_value = String.valueOf(d_eventprice.intValue());

        String startdate = eded_all.get(0).getEventStartDate();
        String enddate = eded_all.get(0).getEventEndDate();
        String new_startdate = "";
        String new_enddate = "";

        if (this.getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us")) {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                outFormat.setTimeZone(tz);
                new_startdate = outFormat.format(new_date_startDate);
                new_enddate = outFormat.format(new_date_endDate);

            } catch (Exception e) {

                try {
                    TimeZone tz = TimeZone.getTimeZone("SGT");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(tz);
                    Date new_date_startDate = format.parse(startdate);
                    Date new_date_endDate = format.parse(enddate);

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);

                } catch (Exception ex) {
                    Log.e("Date", e.toString());

                }


            }
        } else {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
                outFormat.setTimeZone(tz);
                new_startdate = outFormat.format(new_date_startDate);
                new_enddate = outFormat.format(new_date_endDate);

            } catch (Exception e) {

                try {
                    TimeZone tz = TimeZone.getTimeZone("SGT");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(tz);
                    Date new_date_startDate = format.parse(startdate);
                    Date new_date_endDate = format.parse(enddate);

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);
                } catch (Exception ex) {
                    Log.e("Date", ex.toString());

                }
            }

        }

        String[] splited_startdate = new_startdate.split(" ");
        String[] splited_enddate = new_enddate.split(" ");

        String final_startDate = String.format(this.getString(R.string.member_dashboard_eventstartdate), splited_startdate[0], splited_startdate[1], splited_startdate[2]);
        String final_endDate = String.format(this.getString(R.string.member_dashboard_eventenddate), splited_enddate[0], splited_enddate[1], splited_enddate[2]);

        Glide.with(this).load(eded_all.get(0).getEventBannerImagePath()).apply(new RequestOptions().transform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.TOP))).into(imageview_eventdetail);
        textview_upfront_payment.setText(String.format("%s%% UPFRONT", upfront_value));
        textview_event_date.setText(String.format("%s - %s", final_startDate, final_endDate));
        textview_join_trip_amount.setText(String.format("%s/%s", eded_all.get(0).getReservedSeat(), eded_all.get(0).getMaxParticipant()));
        textview_event_name.setText(eded_all.get(0).getEventName());
        textview_event_price.setText(String.format("RM %s", eventprice_value));

        imageview_user_rank_bronze.setVisibility(View.GONE);
        imageview_user_rank_gold.setVisibility(View.GONE);
        imageview_user_rank_platinum.setVisibility(View.GONE);

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            imageview_user_rank_bronze.setVisibility(View.VISIBLE);
        }

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank_gold.setVisibility(View.VISIBLE);
        }

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
            imageview_user_rank_platinum.setVisibility(View.VISIBLE);
        }

        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_eventdate_upfront_payment);
        textview_upfront_payment.setAnimation(rotate);


        List<EventDetailBookingData> edbd_all = EventDetailBookingData.listAll(EventDetailBookingData.class);

        ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

        if (edbd_all.get(0).getIsJoined().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_JOINED_STATUS)) {

            if (edbd_all.get(0).getBookingStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_BOOKING_STATUS_NEW)) {

                if (edbd_all.get(0).getPaymentStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_STATUS_PARTIAL)) {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(edbd_all.get(0).getPaidAmount()));
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String event_price_decimal = decimalFormat.format(eded_all.get(0).getEventPrice());
                    String upfront_decimal = String.format("%.2f", eded_all.get(0).getEventUpfrontRate());

                    TimelineRow myRow = new TimelineRow(0);
//                    myRow.setTitle(" ");
                    myRow.setDescription(String.format("Booking No. %s. You have paid %s/%s. Upfront payment rate is: %s. Please pay the remaining amount.", edbd_all.get(0).getBookingNumber(), paidAmount_decimal, event_price_decimal, upfront_decimal));
                    myRow.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.bell_small));
                    myRow.setBackgroundSize(50);
                    myRow.setBellowLineColor(Color.parseColor("#ffd35c"));
                    myRow.setBellowLineSize(2);
                    myRow.setImageSize(40);
                    myRow.setBackgroundColor(Color.TRANSPARENT);
                    myRow.setDescriptionColor(Color.WHITE);

                    TimelineRow myRow2 = new TimelineRow(1);
                    myRow2.setBackgroundSize(0);


                    timelineRowsList.add(myRow);
                    timelineRowsList.add(myRow2);

                    ArrayAdapter<TimelineRow> timeline_Adapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                            //if true, list will be sorted by date
                            false);
                    listview_event_detail_timeline.setAdapter(timeline_Adapter);

                } else {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(edbd_all.get(0).getPaidAmount()));

                    TimelineRow myRow = new TimelineRow(0);
//                    myRow.setTitle(" ");
                    myRow.setDescription(String.format("Booking No. %s You have paid %s", edbd_all.get(0).getBookingNumber(), paidAmount_decimal));
                    myRow.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.bell_small));
                    myRow.setImageSize(40);
                    myRow.setBackgroundSize(50);
                    myRow.setBackgroundColor(Color.TRANSPARENT);
                    myRow.setDescriptionColor(Color.WHITE);

                    TimelineRow myRow2 = new TimelineRow(1);
                    myRow2.setTitle(" ");
                    myRow2.setBackgroundSize(100);

                    timelineRowsList.add(myRow);
                    timelineRowsList.add(myRow2);


                    ArrayAdapter<TimelineRow> timeline_Adapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                            //if true, list will be sorted by date
                            false);
                    listview_event_detail_timeline.setAdapter(timeline_Adapter);
                }
            } else {
                TimelineRow myRow = new TimelineRow(0);
//                myRow.setTitle(" ");
                myRow.setDescription(this.getResources().getString(R.string.event_detail_joined_status_false));
                myRow.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.bell_small));
                myRow.setImageSize(40);
                myRow.setBackgroundSize(50);
                myRow.setBackgroundColor(Color.TRANSPARENT);
                myRow.setDescriptionColor(Color.WHITE);

                TimelineRow myRow2 = new TimelineRow(1);
                myRow2.setTitle(" ");
                myRow2.setBackgroundSize(100);

                timelineRowsList.add(myRow);
                timelineRowsList.add(myRow2);


                ArrayAdapter<TimelineRow> timeline_Adapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                        //if true, list will be sorted by date
                        false);
                listview_event_detail_timeline.setAdapter(timeline_Adapter);
            }

        } else {
            TimelineRow myRow = new TimelineRow(0);
//            myRow.setTitle(" ");
            myRow.setDescription(this.getResources().getString(R.string.event_detail_joined_status_false));
            myRow.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.bell_small));
            myRow.setImageSize(40);
            myRow.setBackgroundSize(50);
            myRow.setBackgroundColor(Color.TRANSPARENT);
            myRow.setDescriptionColor(Color.WHITE);

            TimelineRow myRow2 = new TimelineRow(1);
            myRow2.setTitle(" ");
            myRow2.setBackgroundSize(100);

            timelineRowsList.add(myRow);
            timelineRowsList.add(myRow2);


            ArrayAdapter<TimelineRow> timeline_Adapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                    //if true, list will be sorted by date
                    false);
            listview_event_detail_timeline.setAdapter(timeline_Adapter);
        }

        event_detail_webview.loadData("<body style=\"word-wrap:break-word;\" >" + eded_all.get(0).getEventDescription() + "</body>", "text/html", null);


        event_detail_webview.setBackgroundColor(Color.TRANSPARENT);
    }


    public void button_join_trip(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle(this.getString(R.string.warning_title));
        builder.setMessage(this.getResources().getString(R.string.event_detail_confirm_join_trip));
        builder.setPositiveButton(this.getString(R.string.join_trip),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        dialog.dismiss();
                        callEventDetailMakeBooking();


                    }
                });
        builder.setNegativeButton(this.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                    }
                });
        builder.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventDetailEventData.deleteAll(EventDetailEventData.class);
        EventDetailBookingData.deleteAll(EventDetailBookingData.class);
    }

    public void processWSData(JSONObject returnedObject, int command) {

        swipeRefreshLayout.setRefreshing(false);

        if (command == Api_Constants.API_EVENT_DETAILS) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONObject event_data_object = response_object.getJSONObject("EventData");
                        JSONObject booking_object = response_object.getJSONObject("EventBookingData");

                        EventDetailEventData.deleteAll(EventDetailEventData.class);
                        EventDetailBookingData.deleteAll(EventDetailBookingData.class);

                        JSONObject bd = booking_object;
                        EventDetailBookingData edbd = new EventDetailBookingData(bd.getString("IsJoined"), bd.getString("LoginID"), bd.getString("EventGUID"), bd.getString("BookingNumber"), bd.getString("BookingDate"), bd.getString("BookingStatus"), String.valueOf(bd.getInt("PaidAmount")), bd.getString("PaymentStatus"));
                        edbd.save();


                        JSONObject ed = event_data_object;
                        EventDetailEventData mded = new EventDetailEventData(String.valueOf(ed.getInt("Srno")), ed.getString("EventGUID"), ed.getString("EventName"), ed.getString("EventCategoryCode"), ed.getString("EventDescription"), ed.getString("EventBannerImagePath"), ed.getDouble("EventPrice"), ed.getDouble("EventUpfrontRate"), ed.getString("UserLevelCode"), ed.getString("UserLevelCodeVisibility")
                                , String.valueOf(ed.getInt("MaxParticipant")), ed.getString("EventStartDate"), ed.getString("EventEndDate"), ed.getString("RegistrationStartDate"), ed.getString("RegistrationEndDate"), ed.getString("Active"), ed.getString("CreatedBy"), ed.getString("CreatedDate"), String.valueOf(ed.getInt("ReservedSeat")), ed.getString("BookingNumber"));
                        mded.save();


                        displayResult();
                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), EventDetailActivity.this);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_DETAILS);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this);

                    }

//                    JSONArray errorCode_array = returnedObject.getJSONArray("ErrorCode");
//
//                    int errorCode = 0;
//                    String errorMessageEN = "";
//                    String errorMessageCN = "";
//
//                    for (int i = 0; i < errorCode_array.length(); i++) {
//                        JSONObject error_object = errorCode_array.getJSONObject(i);
//                        errorCode = error_object.getInt("Code");
//                        errorMessageEN = error_object.getString("MessageEN");
//                        errorMessageCN = error_object.getString("MessageCN");
//
//                    }
//                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");
//
//                    if (errorCode == 3001) {
//
//                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
//                            new DisplayAlertDialog().displayAlertDialogString(3001, errorMessageEN, false, this);
//
//                        } else {
//                            new DisplayAlertDialog().displayAlertDialogString(3001, errorMessageCN, false, this);
//
//                        }
//
//                    } else {
//                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
//                            new DisplayAlertDialog().displayAlertDialogString(0, errorMessageEN, false, this);
//
//                        } else {
//                            new DisplayAlertDialog().displayAlertDialogString(0, errorMessageCN, false, this);
//
//                        }
//                    }


                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } else if (command == Api_Constants.API_EVENT_DETAILS_MAKE_BOOKING) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                this);
                        builder.setTitle(this.getString(R.string.success_title));
                        builder.setMessage(this.getResources().getString(R.string.event_detail_trip_confirmed));
                        builder.setPositiveButton(this.getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        onBackPressed();

                                    }
                                });

                        builder.show();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), EventDetailActivity.this);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_DETAILS_MAKE_BOOKING);

                    } else {

//                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this);
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

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this);

                        }
                    }

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

    public void processRefreshToken(JSONObject returnedObject, int command, int origin) {
        if (command == RefreshTokenAPI.API_REFRESH_TOKEN) {

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

                        if (origin == RefreshTokenAPI.ORIGIN_EVENT_DETAILS) {
                            callEventDetailWebServices();
                        } else if (origin == RefreshTokenAPI.ORIGIN_EVENT_DETAILS_MAKE_BOOKING) {
                            callEventDetailMakeBooking();
                        }


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this);

                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

    private void loadLanguage() {
        String lang = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void button_back(View v) {
        this.onBackPressed();
    }

}
