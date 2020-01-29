package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.wedoops.platinumnobleclub.database.MyBookingBookingData;
import com.wedoops.platinumnobleclub.database.MyBookingDetailData;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyBookingDetail extends Activity {
    private ImageView imageview_eventdetail;
    private ImageView imageview_user_rank_bronze, imageview_user_rank_silver, imageview_user_rank_gold, imageview_user_rank_platinum;
    private Button event_detail_button_jointrip;
    private WebView event_detail_webview;
    public TextView textview_upfront_payment, textview_event_date, textview_join_trip_amount, textview_event_name, textview_event_price, textview_join_trip_payment;
    ;
    private static final String KEY_LANG = "key_lang"; // preference key
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail_activity);

        loadLanguage();
        setupFindById();
        callEventDetailWebServices();

    }

    private void callEventDetailWebServices() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        Bundle extras = getIntent().getExtras();
        String eventGUID = "";
        String bookingNumber = "";

        if (extras != null) {
            eventGUID = extras.getString("eventGUID");
            bookingNumber = extras.getString("bookingNumber");
        }

        Bundle b = new Bundle();
        b.putString("eventGUID", eventGUID);
        b.putString("bookingNumber", bookingNumber);
        b.putString("access_token", ud.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_EVENT_BOOKING_DETAIL);

        new CallWebServices(Api_Constants.API_EVENT_BOOKING_DETAIL, MyBookingDetail.this, true).execute(b);
    }

    private void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.ORIGIN_EVENT_BOOKING_DETAIL);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, MyBookingDetail.this, MyBookingDetail.this, origin).execute(b);
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

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-regular.ttf");
        textview_upfront_payment.setTypeface(typeface);
        textview_event_date.setTypeface(typeface);
        textview_join_trip_amount.setTypeface(typeface);

        Typeface typeface_bold_500 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-500.ttf");
        textview_event_price.setTypeface(typeface_bold_500);

        Typeface typeface_bold_700 = Typeface.createFromAsset(this.getAssets(), "fonts/poppins-v6-latin-700.ttf");
        textview_event_name.setTypeface(typeface_bold_700);

        textview_join_trip_payment = findViewById(R.id.textview_join_trip_payment);
        event_detail_button_jointrip = findViewById(R.id.event_detail_button_jointrip);
        event_detail_webview = findViewById(R.id.event_detail_webview);
    }

    private void displayResult() {

        List<MyBookingDetailData> mbed_all = MyBookingDetailData.listAll(MyBookingDetailData.class);

        Double d_upfrontrate = mbed_all.get(0).getEventUpfrontRate();
        Double d_eventprice = mbed_all.get(0).getEventPrice();

        String upfront_value = String.valueOf(d_upfrontrate.intValue());
        String eventprice_value = String.valueOf(d_eventprice.intValue());

        String startdate = mbed_all.get(0).getEventStartDate();
        String enddate = mbed_all.get(0).getEventEndDate();
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

                } catch (Exception ee) {
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

                } catch (Exception ee) {
                    Log.e("Date", e.toString());
                }
            }
        }

        String[] splited_startdate = new_startdate.split(" ");
        String[] splited_enddate = new_enddate.split(" ");

        String final_startDate = String.format(this.getString(R.string.member_dashboard_eventstartdate), splited_startdate[0], splited_startdate[1], splited_startdate[2]);
        String final_endDate = String.format(this.getString(R.string.member_dashboard_eventenddate), splited_enddate[0], splited_enddate[1], splited_enddate[2]);

        Glide.with(this).load(mbed_all.get(0).getEventBannerImagePath()).apply(new RequestOptions().transform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.TOP))).into(imageview_eventdetail);
        textview_upfront_payment.setText(String.format("%s%% UPFRONT", upfront_value));
        textview_event_date.setText(String.format("%s - %s", final_startDate, final_endDate));
        textview_join_trip_amount.setText(String.format("%s/%s", mbed_all.get(0).getReservedSeat(), mbed_all.get(0).getMaxParticipant()));
        textview_event_name.setText(mbed_all.get(0).getEventName());
        textview_event_price.setText(String.format("RM %s", eventprice_value));

        imageview_user_rank_bronze.setVisibility(View.GONE);
        imageview_user_rank_silver.setVisibility(View.GONE);
        imageview_user_rank_gold.setVisibility(View.GONE);
        imageview_user_rank_platinum.setVisibility(View.GONE);

        if (mbed_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            imageview_user_rank_bronze.setVisibility(View.VISIBLE);
        }

        if (mbed_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_SILVER)) {
            imageview_user_rank_silver.setVisibility(View.VISIBLE);
        }

        if (mbed_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank_gold.setVisibility(View.VISIBLE);
        }

        if (mbed_all.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
            imageview_user_rank_platinum.setVisibility(View.VISIBLE);
        }

//        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_eventdate_upfront_payment);
//        textview_upfront_payment.setAnimation(rotate);

        List<MyBookingBookingData> mbbd_all = MyBookingBookingData.listAll(MyBookingBookingData.class);

        if (mbbd_all.get(0).getIsJoined().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_JOINED_STATUS)) {

            if (mbbd_all.get(0).getBookingStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_BOOKING_STATUS_NEW)) {

                if (mbbd_all.get(0).getPaymentStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_STATUS_PARTIAL)) {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(mbbd_all.get(0).getPaidAmount()));
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String event_price_decimal = decimalFormat.format(mbed_all.get(0).getEventPrice());
                    String upfront_decimal = String.format("%.2f", mbed_all.get(0).getEventUpfrontRate());
                    textview_join_trip_payment.setText(String.format("Booking No. %s. You have paid %s/%s. Upfront payment rate is: %s. Please pay the remaining amount.", mbbd_all.get(0).getBookingNumber(), paidAmount_decimal, event_price_decimal, upfront_decimal));


                } else {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(mbbd_all.get(0).getPaidAmount()));
                    textview_join_trip_payment.setText(String.format("Booking No. %s You have paid %s", mbbd_all.get(0).getBookingNumber(), paidAmount_decimal));

                }
            } else {

                textview_join_trip_payment.setText(this.getResources().getString(R.string.event_detail_joined_status_false));

            }

        } else {

            textview_join_trip_payment.setText(this.getResources().getString(R.string.event_detail_joined_status_false));

        }

        event_detail_webview.loadData("<body style=\"word-wrap:break-word;\">" + mbed_all.get(0).getEventDescription() + "</body>", "text/html", null);
        event_detail_webview.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventDetailEventData.deleteAll(EventDetailEventData.class);
        EventDetailBookingData.deleteAll(EventDetailBookingData.class);
    }

    public void processWSData(JSONObject returnedObject, int command) {

        swipeRefreshLayout.setRefreshing(false);

        if (command == Api_Constants.API_EVENT_BOOKING_DETAIL) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONObject event_data_object = response_object.getJSONObject("EventData");
                        JSONObject booking_object = response_object.getJSONObject("EventBookingData");

                        MyBookingDetailData.deleteAll(MyBookingDetailData.class);
                        MyBookingBookingData.deleteAll(MyBookingBookingData.class);

                        JSONObject bd = booking_object;
                        MyBookingBookingData mbbd = new MyBookingBookingData(bd.getString("IsJoined"), bd.getString("LoginID"), bd.getString("EventGUID"), bd.getString("BookingNumber"), bd.getString("BookingDate"), bd.getString("BookingStatus"), String.valueOf(bd.getInt("PaidAmount")), bd.getString("PaymentStatus"));
                        mbbd.save();


                        JSONObject ed = event_data_object;
                        MyBookingDetailData mbed = new MyBookingDetailData(String.valueOf(ed.getInt("Srno")), ed.getString("EventGUID"), ed.getString("EventName"), ed.getString("EventCategoryCode"), ed.getString("EventDescription"), ed.getString("EventBannerImagePath"), ed.getDouble("EventPrice"), ed.getDouble("EventUpfrontRate"), ed.getString("UserLevelCode"), ed.getString("UserLevelCodeVisibility")
                                , String.valueOf(ed.getInt("MaxParticipant")), ed.getString("EventStartDate"), ed.getString("EventEndDate"), ed.getString("RegistrationStartDate"), ed.getString("RegistrationEndDate"), ed.getString("Active"), ed.getString("CreatedBy"), ed.getString("CreatedDate"), String.valueOf(ed.getInt("ReservedSeat")), ed.getString("BookingNumber"));
                        mbed.save();


                        displayResult();
                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MyBookingDetail.this, MyBookingDetail.this);

                    }

                } else {
                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_DETAIL);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, this, MyBookingDetail.this);

                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, this, MyBookingDetail.this);
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

                        if (origin == RefreshTokenAPI.ORIGIN_EVENT_BOOKING_DETAIL) {
                            callEventDetailWebServices();
                        }


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this, MyBookingDetail.this);

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

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this, MyBookingDetail.this);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this, MyBookingDetail.this);

                        }
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
