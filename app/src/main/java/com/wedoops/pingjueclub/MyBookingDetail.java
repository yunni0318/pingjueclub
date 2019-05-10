package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.wedoops.pingjueclub.database.EventDetailBookingData;
import com.wedoops.pingjueclub.database.EventDetailEventData;
import com.wedoops.pingjueclub.database.MyBookingBookingData;
import com.wedoops.pingjueclub.database.MyBookingDetailData;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

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

public class MyBookingDetail extends Activity {
    private ImageView imageview_eventdetail, imageview_user_rank;
    private ListView listview_event_detail_timeline;
    private Button event_detail_button_jointrip;
    private WebView event_detail_webview;
    public TextView textview_upfront_payment, textview_event_date, textview_join_trip_amount, textview_event_name, textview_event_price;
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
        imageview_user_rank = findViewById(R.id.imageview_user_rank);

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
                Log.e("Date", e.toString());
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
                Log.e("Date", e.toString());
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

        if (mbed_all.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {

            imageview_user_rank.setImageResource(R.drawable.user_level_bronze);

        } else if (mbed_all.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank.setImageResource(R.drawable.user_level_gold);

        } else {
            imageview_user_rank.setImageResource(R.drawable.user_level_platinum);
        }


        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_eventdate_upfront_payment);
        textview_upfront_payment.setAnimation(rotate);


        List<MyBookingBookingData> mbbd_all = MyBookingBookingData.listAll(MyBookingBookingData.class);

        ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

        if (mbbd_all.get(0).getIsJoined().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_JOINED_STATUS)) {

            if (mbbd_all.get(0).getBookingStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_BOOKING_STATUS_NEW)) {

                if (mbbd_all.get(0).getPaymentStatus().equals(CONSTANTS_VALUE.EVENT_DETAIL_PAYMENT_STATUS_PARTIAL)) {
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(mbbd_all.get(0).getPaidAmount()));
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String event_price_decimal = decimalFormat.format(mbed_all.get(0).getEventPrice());
                    String upfront_decimal = String.format("%.2f", mbed_all.get(0).getEventUpfrontRate());

                    TimelineRow myRow = new TimelineRow(0);
                    myRow.setDescription(String.format("Booking No. %s. You have paid %s/%s. Upfront payment rate is: %s. Please pay the remaining amount.", mbbd_all.get(0).getBookingNumber(), paidAmount_decimal, event_price_decimal, upfront_decimal));
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
                    String paidAmount_decimal = String.format("%.2f", Double.parseDouble(mbbd_all.get(0).getPaidAmount()));

                    TimelineRow myRow = new TimelineRow(0);
//                    myRow.setTitle(" ");
                    myRow.setDescription(String.format("Booking No. %s You have paid %s", mbbd_all.get(0).getBookingNumber(), paidAmount_decimal));
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

        event_detail_webview.loadData(mbed_all.get(0).getEventDescription(), "text/html", null);
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

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MyBookingDetail.this);

                    }

                } else {

//                    JSONObject errorCode_object = returnedObject.getJSONObject("StatusCode");
//                    new DisplayAlertDialog().displayAlertDialogError(errorCode_object.getInt("Code"), EventDetailActivity.this);

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this);

                    }
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

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MyBookingDetail.this);

                    }

                } else {

//                    JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
//                    new DisplayAlertDialog().displayAlertDialogError(errorCode_object.getInt("Code"), EventDetailActivity.this);

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this);

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

}
