package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.database.EventDetailBookingData;
import com.wedoops.platinumnobleclub.database.EventDetailEventData;
import com.wedoops.platinumnobleclub.database.MemberDashboardTopBanner;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallRefreshToken;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;
import com.wedoops.platinumnobleclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class EventDetailActivity extends Activity {

    private ImageView imageview_eventdetail, imageview_user_rank_bronze, imageview_user_rank_silver, imageview_user_rank_gold, imageview_user_rank_platinum;
    private Button event_detail_button_jointrip;
    private WebView event_detail_webview;
    private SwipeRefreshLayout swipeRefreshLayout;
    public TextView textview_upfront_payment, textview_event_date, textview_join_trip_amount, textview_event_name, textview_event_price, textview_join_trip_payment;
    private static final String KEY_LANG = "key_lang"; // preference key
    private AlertDialog alert;

    private static int counter = 0;


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

        if (counter < 4) {
            counter++;

            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            Bundle b = new Bundle();
            b.putString("refresh_token", ud_list.get(0).getRefreshToken());
            b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

            new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, EventDetailActivity.this, EventDetailActivity.this, origin).execute(b);
        } else {
            displayResult();
        }
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
        imageview_user_rank_silver = findViewById(R.id.imageview_user_rank_silver);
        imageview_user_rank_gold = findViewById(R.id.imageview_user_rank_gold);
        imageview_user_rank_platinum = findViewById(R.id.imageview_user_rank_platinum);

        textview_upfront_payment = findViewById(R.id.textview_upfront_payment);
        textview_event_date = findViewById(R.id.textview_event_date);
        textview_join_trip_amount = findViewById(R.id.textview_join_trip_amount);
        textview_event_name = findViewById(R.id.textview_event_name);
        textview_event_price = findViewById(R.id.textview_event_price);
        textview_join_trip_payment = findViewById(R.id.textview_join_trip_payment);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-regular.ttf");
        textview_upfront_payment.setTypeface(typeface);
        textview_event_date.setTypeface(typeface);
        textview_join_trip_amount.setTypeface(typeface);

        Typeface typeface_bold_500 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-500.ttf");
        textview_event_price.setTypeface(typeface_bold_500);

        Typeface typeface_bold_700 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-700.ttf");
        textview_event_name.setTypeface(typeface_bold_700);

        event_detail_button_jointrip = findViewById(R.id.event_detail_button_jointrip);
        event_detail_button_jointrip.setText(this.getResources().getString(R.string.event_detail_join_trip));
        event_detail_webview = findViewById(R.id.event_detail_webview);

        event_detail_button_jointrip.setEnabled(true);

    }

    private void displayResult() {

        List<EventDetailEventData> eded_all = EventDetailEventData.listAll(EventDetailEventData.class);

        Double d_upfrontrate = eded_all.get(0).getEventUpfrontRate();
//        Double d_eventprice = eded_all.get(0).getEventPrice();

        BigDecimal bd_event_price = new BigDecimal(eded_all.get(0).getEventPrice());
        bd_event_price = bd_event_price.setScale(4, BigDecimal.ROUND_HALF_UP);
        bd_event_price = bd_event_price.setScale(3, BigDecimal.ROUND_DOWN);
        bd_event_price = bd_event_price.setScale(2, BigDecimal.ROUND_HALF_UP);

        String upfront_value = String.valueOf(d_upfrontrate.intValue());
        String eventprice_value = String.valueOf(bd_event_price.doubleValue());

        String startdate = eded_all.get(0).getEventStartDate();
        String enddate = eded_all.get(0).getEventEndDate();
        String new_startdate = "";
        String new_enddate = "";

        String lang = this.getResources().getConfiguration().locale.toString().toLowerCase();

        if (this.getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us") || this.getResources().getConfiguration().locale.toString().toLowerCase().equals("en_gb") || this.getResources().getConfiguration().locale.toString().toLowerCase().equals("")) {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy hha", Locale.US);
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

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy hha", Locale.US);
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

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy hha", Locale.US);
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

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy hha", Locale.US);
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

        if (final_startDate.equals(final_endDate)) {
            textview_event_date.setText(String.format("%s", final_endDate + " " + splited_enddate[3]));

        } else {
            textview_event_date.setText(String.format("%s - %s", final_startDate + " " + splited_enddate[3], final_endDate + " " + splited_enddate[3]));

        }


        textview_join_trip_amount.setText(String.format("%s/%s", eded_all.get(0).getReservedSeat(), eded_all.get(0).getMaxParticipant()));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        textview_event_name.setWidth(displayMetrics.widthPixels / 2);

        textview_event_name.setText(eded_all.get(0).getEventName());
        textview_event_price.setText(String.format("%s PTS", eventprice_value));

        imageview_user_rank_bronze.setVisibility(View.GONE);
        imageview_user_rank_silver.setVisibility(View.GONE);
        imageview_user_rank_gold.setVisibility(View.GONE);
        imageview_user_rank_platinum.setVisibility(View.GONE);

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            imageview_user_rank_bronze.setVisibility(View.VISIBLE);
        }

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_SILVER)) {
            imageview_user_rank_silver.setVisibility(View.VISIBLE);
        }

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank_gold.setVisibility(View.VISIBLE);
        }

        if (eded_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
            imageview_user_rank_platinum.setVisibility(View.VISIBLE);
        }

//        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_eventdate_upfront_payment);
//        textview_upfront_payment.setAnimation(rotate);


        List<EventDetailBookingData> edbd_all = EventDetailBookingData.listAll(EventDetailBookingData.class);


        if (edbd_all.get(0).getIsJoined().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_JOINED_STATUS)) {

            event_detail_button_jointrip.setText(this.getResources().getString(R.string.event_detail_is_joined));
            event_detail_button_jointrip.setEnabled(false);

            if (edbd_all.get(0).getBookingStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_BOOKING_STATUS_NEW)) {

                if (edbd_all.get(0).getPaymentStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_STATUS_PARTIAL)) {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(edbd_all.get(0).getPaidAmount()));
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String event_price_decimal = decimalFormat.format(eded_all.get(0).getEventPrice());
                    String upfront_decimal = String.format("%.2f", eded_all.get(0).getEventUpfrontRate());

                    textview_join_trip_payment.setText(String.format("Booking No. %s. You have paid %s/%s. Upfront payment rate is: %s. Please pay the remaining amount.", edbd_all.get(0).getBookingNumber(), paidAmount_decimal, event_price_decimal, upfront_decimal));


                } else {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(edbd_all.get(0).getPaidAmount()));
                    textview_join_trip_payment.setText(String.format("Booking No. %s You have paid %s", edbd_all.get(0).getBookingNumber(), paidAmount_decimal));

                }
            } else {
                textview_join_trip_payment.setText(this.getResources().getString(R.string.event_detail_joined_status_false));
                event_detail_button_jointrip.setEnabled(true);

            }

        } else {
            textview_join_trip_payment.setText(this.getResources().getString(R.string.event_detail_joined_status_false));
            event_detail_button_jointrip.setEnabled(true);

        }

        event_detail_webview.loadData("<body style=\"word-wrap:break-word;\" >" + eded_all.get(0).getEventDescription() + "</body>", "text/html", null);


        event_detail_webview.setBackgroundColor(Color.TRANSPARENT);
    }


    public void button_join_trip(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
        TextView textview_title = customLayout.findViewById(R.id.textview_title);
        TextView textview_message = customLayout.findViewById(R.id.textview_message);
        Button button_cancel = customLayout.findViewById(R.id.button_cancel);
        Button button_ok = customLayout.findViewById(R.id.button_ok);

        textview_title.setText(this.getString(R.string.warning_title));
        textview_message.setText(this.getResources().getString(R.string.event_detail_confirm_join_trip));
        button_ok.setText(this.getString(R.string.join_trip));

        builder.setView(customLayout);

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
                callEventDetailMakeBooking();
            }
        });

        alert = builder.create();

        alert.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                this);
//        builder.setTitle(this.getString(R.string.warning_title));
//        builder.setMessage(this.getResources().getString(R.string.event_detail_confirm_join_trip));
//        builder.setPositiveButton(this.getString(R.string.join_trip),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//
//                        dialog.dismiss();
//                        callEventDetailMakeBooking();
//
//
//                    }
//                });
//        builder.setNegativeButton(this.getString(R.string.cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        dialog.dismiss();
//
//                    }
//                });
//        builder.show();

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
                        event_detail_button_jointrip.setEnabled(false);

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), EventDetailActivity.this, EventDetailActivity.this);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_DETAILS);

                    } else {
                        event_detail_button_jointrip.setEnabled(false);

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this, this);

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
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, this, this);
            }
        } else if (command == Api_Constants.API_EVENT_DETAILS_MAKE_BOOKING) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        View customLayout = getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
                        TextView textview_title = customLayout.findViewById(R.id.textview_title);
                        TextView textview_message = customLayout.findViewById(R.id.textview_message);
                        Button button_ok = customLayout.findViewById(R.id.button_ok);


                        textview_title.setText(this.getString(R.string.success_title));
                        textview_message.setText(this.getResources().getString(R.string.event_detail_trip_confirmed));

                        builder.setView(customLayout);

                        builder.setCancelable(false);

                        button_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                onBackPressed();
                            }
                        });

                        alert = builder.create();

                        alert.show();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), EventDetailActivity.this, EventDetailActivity.this);

                    }

                } else {


                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_DETAILS_MAKE_BOOKING);

                    } else {

                        event_detail_button_jointrip.setEnabled(false);

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

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this, this);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this, this);

                        }
                    }

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, this, this);

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
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this, this);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this, this);

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
