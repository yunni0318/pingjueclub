package com.wedoops.pingjueclub.webservices;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wedoops.pingjueclub.EditProfileActivity;
import com.wedoops.pingjueclub.EventDetailActivity;
import com.wedoops.pingjueclub.ForgotPasswordActivity;
import com.wedoops.pingjueclub.LoginActivity;
import com.wedoops.pingjueclub.MainActivity;
import com.wedoops.pingjueclub.MemberDashboardActivity;
import com.wedoops.pingjueclub.MyBookingActivity;
import com.wedoops.pingjueclub.MyBookingDetail;
import com.wedoops.pingjueclub.PayFragment;
import com.wedoops.pingjueclub.RecordsList;
import com.wedoops.pingjueclub.ServiceDetails;
import com.wedoops.pingjueclub.ServicesFragment;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Api_Constants {

    private static String url_authentication = "http://103.198.194.228:54126/api/Authentication/";
    private static String url_dashboard = "http://103.198.194.228:54126/api/Dashboard/";
    private static String url_event = "http://103.198.194.228:54126/api/Event/";
    private static String url_eventbooking = "http://103.198.194.228:54126/api/EventBooking/";
    private static String url_member = "http://103.198.194.228:54126/api/Member/";
    private static String url_userwallet = "http://103.198.194.228:54126/api/UserWallet/";
    private static String url_services = "http://103.198.194.228:54126/api/Services/";

    private static String access_token_prefix = "bearer ";
    private static String device_type_static = "android";

    public static final int API_MEMBER_LOGIN_V2 = 1;
    public static final int API_FORGOT_PASSWORD = 2;
    public static final int API_MEMBER_DASHBOARDV2 = 3;
    public static final int API_EVENT_DETAILS = 5;
    public static final int API_EVENT_DETAILS_MAKE_BOOKING = 6;
    public static final int API_MEMBER_ACCOUNT_SETTING = 7;
    public static final int API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST = 8;
    public static final int API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION = 9;
    public static final int API_MEMBER_UPDATE_ACCOUNT_NICKNAME = 99;
    public static final int API_MEMBER_UPDATE_ACCOUNT_SECURITY = 10;
    public static final int API_EVENT_BOOKING_LIST = 11;
    public static final int API_EVENT_BOOKING_DETAIL = 12;
    public static final int API_CASH_WALLET_TRANSACTION_V2 = 13;
    public static final int API_MEMBER_CHANGE_PROFILE_PICTURE = 14;
    public static final int API_MEMBER_QUICK_PROFILE = 15;
    public static final int API_SERVICE_PAGE_DETAILS = 16;
    public static final int API_MAKE_QR_CODE_PAYMENT = 17;
    public static final int API_SERVICE_MERCHANT_LIST = 18;
    public static final int API_UPDATE_DEVICE_ID = 19;
    public static final int API_DISTRIBUTE_PENDING_AMOUNT = 20;


    public static final String COMMAND = "command";

    private static Context context;

    public static Bundle processRequest(final Bundle b, final Context currentContext) {

        context = currentContext;
        RequestQueue queue = Volley.newRequestQueue(context);

        int command = b.getInt(COMMAND);

        try {
            switch (command) {

                case API_MEMBER_LOGIN_V2: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_authentication + "MemberLoginV2",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((LoginActivity) context).processWSData(convertResponseToJsonObject(response),API_MEMBER_LOGIN_V2);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.toString());
                                    ((LoginActivity) context).processWSData(convertResponseToJsonObject(""),API_MEMBER_LOGIN_V2);

//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);

                                }
                            }
                    ) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("LoginID", b.getString("USERNAME"));
                            params.put("Password", b.getString("PASSWORD"));
                            params.put("DeviceID", b.getString("DEVICEID"));
                            params.put("DeviceType", device_type_static);
                            params.put("IPAddress", "");

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_FORGOT_PASSWORD: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_authentication + "ForgotPassword",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((ForgotPasswordActivity) context).processWSData(convertResponseToJsonObject(response));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
                                    ((ForgotPasswordActivity) context).processWSData(null);

//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);

                                }
                            }
                    ) {

                        @Override
                        protected Map<String, String> getParams() {

                            String locale_language = "en-GB";
                            if (!Locale.getDefault().getLanguage().toString().equals("en")) {
                                locale_language = "zh-CN";
                            }
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", b.getString("email"));
                            params.put("language", locale_language);
                            params.put("Authorization", "");

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_DASHBOARDV2: {
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_dashboard + "MemberDashboardV2",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    MemberDashboardActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_DASHBOARDV2);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());

                                    if (error.networkResponse.statusCode == 401) {
                                        MemberDashboardActivity.processWSData(convertResponseToJsonObject("{\"Success\":true,\"StatusCode\":401}"), API_MEMBER_DASHBOARDV2);
                                    } else {
                                        MemberDashboardActivity.processWSData(null, API_MEMBER_DASHBOARDV2);

//                                        new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    }

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_EVENT_DETAILS: {

                    String get_url = url_event + "EventDetails?" + "eventGUID=" + b.getString("eventGUID");

                    StringRequest postRequest = new StringRequest(Request.Method.GET, get_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((EventDetailActivity) context).processWSData(convertResponseToJsonObject(response), API_EVENT_DETAILS);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
                                    ((EventDetailActivity) context).processWSData(null, API_EVENT_DETAILS);

//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }
                case API_EVENT_DETAILS_MAKE_BOOKING: {

                    String get_url = url_eventbooking + "MakeBooking?" + "eventGUID=" + b.getString("eventGUID");


                    StringRequest postRequest = new StringRequest(Request.Method.POST, get_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((EventDetailActivity) context).processWSData(convertResponseToJsonObject(response), API_EVENT_DETAILS_MAKE_BOOKING);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
                                    ((EventDetailActivity) context).processWSData(null, API_EVENT_DETAILS_MAKE_BOOKING);

//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);

                                }
                            }
                    ) {

//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("eventGUID", b.getString("eventGUID"));
//
//                            return params;
//                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            String auth_token = access_token_prefix + b.getString("access_token");

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", auth_token);

                            return params;
                        }
                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_ACCOUNT_SETTING: {

                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_member + "AccountSetting",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_ACCOUNT_SETTING);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
                                    EditProfileActivity.processWSData(null, API_MEMBER_ACCOUNT_SETTING);

//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }


                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST: {

                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_member + "CountryStateList",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    EditProfileActivity.processWSData(null, API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_member + "UpdateAccountProfileValidation",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    EditProfileActivity.processWSData(null, API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Name", b.getString("Name"));
                            params.put("Email", b.getString("Email"));
                            params.put("Country", b.getString("Country"));
                            params.put("State", b.getString("State"));
                            params.put("Address", b.getString("Address"));
                            params.put("Phone", b.getString("Phone"));
                            return params;
                        }
                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_UPDATE_ACCOUNT_NICKNAME: {


                    String get_url = url_member + "UpdateAccountNickname?" + "nickname=" + b.getString("NickName");


                    StringRequest postRequest = new StringRequest(Request.Method.POST, get_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_UPDATE_ACCOUNT_NICKNAME);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    EditProfileActivity.processWSData(null, API_MEMBER_UPDATE_ACCOUNT_NICKNAME);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("nickname", b.getString("NickName"));
//
//                            return params;
//                        }
                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_UPDATE_ACCOUNT_SECURITY: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_member + "UpdateAccountSecurity",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EditProfileActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_UPDATE_ACCOUNT_SECURITY);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    EditProfileActivity.processWSData(null, API_MEMBER_UPDATE_ACCOUNT_SECURITY);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("CurrentPassword", b.getString("CurrentPassword"));
                            params.put("NewPassword", b.getString("NewPassword"));
                            params.put("ConfirmPassword", b.getString("ConfirmPassword"));

                            return params;
                        }
                    };
                    queue.add(postRequest);

                    break;
                }
                case API_EVENT_BOOKING_LIST: {

                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_eventbooking + "MyBookingList",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    MyBookingActivity.processWSData(convertResponseToJsonObject(response), API_EVENT_BOOKING_LIST);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    MyBookingActivity.processWSData(null, API_EVENT_BOOKING_LIST);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("CurrentPassword", b.getString("CurrentPassword"));
//                            params.put("NewPassword", b.getString("NewPassword"));
//                            params.put("ConfirmPassword", b.getString("ConfirmPassword"));
//
//                            return params;
//                        }
                    };
                    queue.add(postRequest);

                    break;
                }

                case API_EVENT_BOOKING_DETAIL: {

                    String get_url = url_eventbooking + "MyBooking?" + "eventGUID=" + b.getString("eventGUID") + "&" + "bookingNumber=" + b.getString("bookingNumber");

                    StringRequest postRequest = new StringRequest(Request.Method.GET, get_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((MyBookingDetail) context).processWSData(convertResponseToJsonObject(response), API_EVENT_BOOKING_DETAIL);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    ((MyBookingDetail) context).processWSData(null, API_EVENT_BOOKING_DETAIL);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_CASH_WALLET_TRANSACTION_V2: {
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_userwallet + "CashWalletTransactionV2",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    RecordsList.processWSData(convertResponseToJsonObject(response), API_CASH_WALLET_TRANSACTION_V2);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    RecordsList.processWSData(null, API_CASH_WALLET_TRANSACTION_V2);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MEMBER_CHANGE_PROFILE_PICTURE: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_member + "ChangeProfilePicture",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((MainActivity) context).processWSData(convertResponseToJsonObject(response), API_MEMBER_CHANGE_PROFILE_PICTURE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    ((MainActivity) context).processWSData(null, API_MEMBER_CHANGE_PROFILE_PICTURE);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("ProfilePictureBase64", b.getString("ProfilePictureBase64"));
                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_MEMBER_QUICK_PROFILE: {
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_member + "QuickProfile",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                    MemberDashboardActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_QUICK_PROFILE);
                                    ((MainActivity) context).processWSData(convertResponseToJsonObject(response), API_MEMBER_QUICK_PROFILE);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());

                                    if (error.networkResponse.statusCode == 401) {
                                        MemberDashboardActivity.processWSData(convertResponseToJsonObject("{\"Success\":true,\"StatusCode\":401}"), API_MEMBER_QUICK_PROFILE);
                                    } else {
//                                        new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                        MemberDashboardActivity.processWSData(null, API_MEMBER_QUICK_PROFILE);

                                    }
                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            String auth_token = access_token_prefix + b.getString("access_token");
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Authorization", auth_token);
//
//                            return params;
//                        }

                    };
                    queue.add(postRequest);

                    break;
                }
                case API_SERVICE_PAGE_DETAILS: {
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_services + "ServicePageDetails",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                    MemberDashboardActivity.processWSData(convertResponseToJsonObject(response), API_MEMBER_QUICK_PROFILE);
                                    ServicesFragment.processWSData(convertResponseToJsonObject(response), API_SERVICE_PAGE_DETAILS);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());

                                    if (error.networkResponse.statusCode == 401) {
                                        ServicesFragment.processWSData(convertResponseToJsonObject("{\"Success\":true,\"StatusCode\":401}"), API_SERVICE_PAGE_DETAILS);
                                    } else {
//                                        new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                        ServicesFragment.processWSData(null, API_SERVICE_PAGE_DETAILS);

                                    }
                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }


                    };
                    queue.add(postRequest);

                    break;
                }
                case API_MAKE_QR_CODE_PAYMENT: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_userwallet + "MakeQRCodePayment",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PayFragment.processWSData(convertResponseToJsonObject(response), API_MAKE_QR_CODE_PAYMENT);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    PayFragment.processWSData(null, API_MAKE_QR_CODE_PAYMENT);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("TransactionID", b.getString("transactionid"));
                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_SERVICE_MERCHANT_LIST: {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_services + "ServiceMerchantList",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((ServiceDetails) currentContext).processWSData(convertResponseToJsonObject(response));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());
//                                    new DisplayAlertDialog().displayAlertDialogError(error.networkResponse.statusCode, context);
                                    ((ServiceDetails) currentContext).processWSData(null);

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("ServicesID", b.getString("ServicesID"));
                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_UPDATE_DEVICE_ID:{

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url_authentication + "UpdateDeviceID",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("DeviceID", b.getString("device_id"));
                            return params;
                        }

                    };
                    queue.add(postRequest);

                    break;
                }

                case API_DISTRIBUTE_PENDING_AMOUNT: {
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url_userwallet + "DistributePendingAmount",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error.Response", error.networkResponse.toString());

                                }
                            }
                    ) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String auth_token = access_token_prefix + b.getString("access_token");
                            params.put("Authorization", auth_token);

                            return params;
                        }
                    };
                    queue.add(postRequest);

                    break;
                }
                default: {
                    break;
                }
            }


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
