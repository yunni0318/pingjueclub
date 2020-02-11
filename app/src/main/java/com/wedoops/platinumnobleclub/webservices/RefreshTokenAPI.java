package com.wedoops.platinumnobleclub.webservices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wedoops.platinumnobleclub.BookingFragment;
import com.wedoops.platinumnobleclub.EditProfileActivity;
import com.wedoops.platinumnobleclub.EventDetailActivity;
import com.wedoops.platinumnobleclub.MemberDashboardActivity;
import com.wedoops.platinumnobleclub.MyBookingActivity;
import com.wedoops.platinumnobleclub.MyBookingDetail;
import com.wedoops.platinumnobleclub.PayFragment;
import com.wedoops.platinumnobleclub.RecordsList;
import com.wedoops.platinumnobleclub.ServicesFragment;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefreshTokenAPI {

    //    private static String url_authentication = "http://103.244.0.237:54126/api/Authentication/";
    private static String url_authentication = "http://api.platinumnobleclub.com/api/Authentication/";

    private static String access_token_prefix = "bearer ";

    public static final int API_REFRESH_TOKEN = 44;

    public static final String COMMAND = "command";

    public static final int ORIGIN_MEMBER_DASHBOARD = 33;
    public static final int ORIGIN_EVENT_DETAILS = 55;
    public static final int ORIGIN_EVENT_DETAILS_MAKE_BOOKING = 66;
    public static final int ORIGIN_MEMBER_ACCOUNT_SETTING = 77;
    public static final int ORIGIN_MEMBER_ACCOUNT_COUNTRY_STATE_LIST = 88;
    public static final int ORIGIN_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION = 99;
    public static final int ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME = 99;
    public static final int ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY = 1010;
    public static final int ORIGIN_EVENT_BOOKING_LIST = 1111;
    public static final int ORIGIN_EVENT_BOOKING_DETAIL = 1212;
    public static final int ORIGIN_CASH_WALLET_TRANSACTION = 1313;
    public static final int ORIGIN_CASH_WALLET_TRANSACTION_V2 = 13131313;
    public static final int ORIGIN_MEMBER_CHANGE_PROFILE_PICTURE = 1414;
    public static final int ORIGIN_SERVICE_PAGE_DETAIL = 1616;
    public static final int ORIGIN_MAKE_QR_CODE_PAYMENT = 1717;

    private static Context context;
    private static int origin;

    public static Bundle processRequest(final Bundle b, final Context currentContext, int currentOrigin, final Activity activity) {

        context = currentContext;
        origin = currentOrigin;

        RequestQueue queue = Volley.newRequestQueue(context);

        int command = b.getInt(COMMAND);

        try {

            StringRequest postRequest = new StringRequest(Request.Method.POST, url_authentication + "RefreshToken",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (origin == ORIGIN_MEMBER_DASHBOARD) {
                                MemberDashboardActivity.processWSData(convertResponseToJsonObject(response), API_REFRESH_TOKEN);
                            }

                            if (origin == ORIGIN_MEMBER_ACCOUNT_SETTING) {
                                EditProfileActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_MEMBER_ACCOUNT_SETTING);
                            }

                            if (origin == ORIGIN_MEMBER_ACCOUNT_COUNTRY_STATE_LIST) {
                                EditProfileActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);

                            }

//                                    if (origin == ORIGIN_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION) {
//                                        EditProfileActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);
//
//                                    }
                            if (origin == ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME) {
                                EditProfileActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME);

                            }

                            if (origin == ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY) {
                                EditProfileActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY);

                            }

                            if (origin == ORIGIN_EVENT_BOOKING_LIST) {
                                MyBookingActivity.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_EVENT_BOOKING_LIST);
                            }

                            if (origin == ORIGIN_EVENT_BOOKING_DETAIL) {
                                ((MyBookingDetail) context).processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_EVENT_BOOKING_DETAIL);
                            }
                            if (origin == ORIGIN_CASH_WALLET_TRANSACTION) {
                                RecordsList.processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_CASH_WALLET_TRANSACTION);
                            }
                            if (origin == ORIGIN_EVENT_DETAILS) {
                                ((EventDetailActivity) context).processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_EVENT_DETAILS);
                            }
                            if (origin == ORIGIN_EVENT_DETAILS_MAKE_BOOKING) {
                                ((EventDetailActivity) context).processRefreshToken(convertResponseToJsonObject(response), API_REFRESH_TOKEN, ORIGIN_EVENT_DETAILS_MAKE_BOOKING);
                            }
                            if (origin == ORIGIN_SERVICE_PAGE_DETAIL) {
                                ServicesFragment.processWSData(convertResponseToJsonObject(response), ORIGIN_SERVICE_PAGE_DETAIL);
                            }
                            if (origin == ORIGIN_MAKE_QR_CODE_PAYMENT) {
                                PayFragment.processWSData(convertResponseToJsonObject(response), ORIGIN_MAKE_QR_CODE_PAYMENT);
                            }

                            if (origin == ORIGIN_CASH_WALLET_TRANSACTION_V2) {
                                RecordsList.processWSData(convertResponseToJsonObject(response), ORIGIN_CASH_WALLET_TRANSACTION_V2);
                            }


//                                    if (currentContext == MemberDashboardActivity.get_activity) {
//                                        MemberDashboardActivity.processWSData(convertResponseToJsonObject(response), API_REFRESH_TOKEN);
//                                    } else if (currentContext == EditProfileActivity.get_activity) {
//                                        EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_REFRESH_TOKEN);
//                                    }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error.Response", error.networkResponse.toString());
                            new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context, activity);

                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("RefreshToken", b.getString("refresh_token"));
                    params.put("IPAddress", "");

                    return params;
                }

            };
            queue.add(postRequest);

        } catch (Exception e) {
            Log.i("API_Constants", e.toString());
        }

        return b;
    }

    private static JSONObject convertResponseToJsonObject(String responses) {
        JSONObject jObject;

        try {
            jObject = new JSONObject(responses);

        } catch (Exception e) {
            jObject = new JSONObject();
        }
        return jObject;
    }
}
