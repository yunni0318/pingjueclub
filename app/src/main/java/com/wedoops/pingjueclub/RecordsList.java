package com.wedoops.pingjueclub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.RecordsListAdapter;
import com.wedoops.pingjueclub.adapters.TransactionsReportAdapter;
import com.wedoops.pingjueclub.adapters.TransactionsReportTopExpandableGroup;
import com.wedoops.pingjueclub.database.TransactionReportExpandableParcable;
import com.wedoops.pingjueclub.database.TransactionsReportData;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallRefreshToken;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.wedoops.pingjueclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordsList extends Fragment {

    private static RecyclerView recyclerview_transaction;
    private static View view;
    private static Activity get_activity;
    private static Context get_context;
    private LinearLayout dateSelection, dateChoices;
    private static TextView dateType, weekly, monthly, yearly, dateAll;
    private static String DATA_TYPE_MERCHANT_PAYMENT = "MERCHANT-PAYMENT";
    private static String DATA_TYPE_ADMIN_TOPUP = "ADMIN-TOPUP";
    private static String DATA_TYPE_PAYMENT = "PAYMENT-";

    private static RecordsListAdapter adapter;

    private static int counter;
    private static CustomProgressDialog customDialog;

    private static void callCashWalletTransactionWebService() {

//        CustomProgressDialog.showProgressDialog(get_context);
        customDialog.showDialog(get_context);

        if (counter < 4) {
            counter++;
            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_CASH_WALLET_TRANSACTION_V2);

            new CallWebServices(Api_Constants.API_CASH_WALLET_TRANSACTION_V2, view.getContext(), true).execute(b);

        } else {
            callRefreshTokenWebService();

        }
    }

    private static void callRefreshTokenWebService() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION_V2);

        new CallRefreshToken(RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION_V2, get_activity, RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION_V2).execute(b);
    }

    private static void displayResult() {
        setupRecyclerView();
    }

    private static void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

//        List<TransactionsReportData> trd_list_all = TransactionsReportData.listAll(TransactionsReportData.class);

//        adapter = new RecordsListAdapter(trd_list_all);

        recyclerview_transaction.setLayoutManager(layoutManager);
        dateAll.performClick();
//        recyclerview_transaction.setAdapter(adapter);

    }

    public static void processWSData(JSONObject returnedObject, int command) {

//        CustomProgressDialog.closeProgressDialog();
        customDialog.hideDialog();

        if (command == Api_Constants.API_CASH_WALLET_TRANSACTION_V2) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        JSONArray response_object = returnedObject.getJSONArray("ResponseData");

                        List<TransactionsReportData> trd_all = TransactionsReportData.listAll(TransactionsReportData.class);
                        if (trd_all.size() > 0) {
                            TransactionsReportData.deleteAll(TransactionsReportData.class);
                        }

                        for (int i = 0; i < response_object.length(); i++) {

                            JSONObject bd = response_object.getJSONObject(i);
                            JSONObject bd_type = bd.getJSONObject("TType");

                            if (bd.getJSONObject("TType") != null) {
                                String TStatus;

                                //Merchant Payment
                                int actualamount = 0;
                                int discountrate = 0;
                                int discountamount = 0;

                                //Payment-
                                int pointamount;
                                boolean iscashin;

                                String remarks;

                                if (bd_type.getString("Type").equals(DATA_TYPE_MERCHANT_PAYMENT)) {

                                    actualamount = bd_type.getInt("ActualAmount");
                                    discountrate = bd_type.getInt("DiscountRate");
                                    discountamount = bd_type.getInt("DiscountAmount");
                                    remarks = bd_type.getString("Remarks");
                                    TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), actualamount, discountrate, discountamount, remarks, bd.getString("TDate"), null, 0, false, null);
                                    trd.save();


                                } else if (bd_type.getString("Type").equals(DATA_TYPE_ADMIN_TOPUP)) {
                                    TStatus = bd_type.getString("TStatus");
                                    pointamount = bd_type.getInt("PointAmount");
                                    iscashin = bd_type.getBoolean("IsCashIn");
                                    remarks = bd_type.getString("Remarks");
                                    TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), 0, 0, 0, remarks, bd.getString("TDate"), TStatus, pointamount, iscashin, null);
                                    trd.save();

                                } else if (bd_type.getString("Type").contains(DATA_TYPE_PAYMENT)) {
                                    TStatus = bd_type.getString("TStatus");
                                    pointamount = bd_type.getInt("PointAmount");
                                    iscashin = bd_type.getBoolean("IsCashIn");
                                    remarks = bd_type.getString("Remarks");
                                    TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), 0, 0, 0, remarks, bd.getString("TDate"), TStatus, pointamount, iscashin, bd_type.getString("EventTitle"));
                                    trd.save();
                                }
                            } else {
                                Log.e("Error", "NULL from Type");
                            }
                        }

                        displayResult();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService();

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
        } else if (command == RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION_V2) {

        }
    }

    public static void processRefreshToken(JSONObject returnedObject, int command, int origin) {
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
                        RecordsList.callCashWalletTransactionWebService();
                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), get_activity.getApplicationContext());

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(get_activity.getApplicationContext(), "key_lang");

                    if (errorCode == 1506) {
                        new DisplayAlertDialog().displayAlertDialogError(1506, get_activity.getApplicationContext());
                    } else {
                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, get_activity.getApplicationContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, get_activity.getApplicationContext());

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_transactions_report, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextInputEditText textinputedittext_filter = view.findViewById(R.id.textinputedittext_filter);

        recyclerview_transaction = view.findViewById(R.id.recyclerview_transaction);
        dateType = view.findViewById(R.id.dateType);
        dateSelection = view.findViewById(R.id.dateSelection);
        dateChoices = view.findViewById(R.id.dateChoices);
        dateAll = view.findViewById(R.id.dateAll);
        weekly = view.findViewById(R.id.dateWeekly);
        monthly = view.findViewById(R.id.dateMonthly);
        yearly = view.findViewById(R.id.dateYearly);
        dateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateChoices.getVisibility() == View.VISIBLE) {
                    dateChoices.setVisibility(View.GONE);
                } else {
                    dateChoices.setVisibility(View.VISIBLE);
                }
            }
        });


        textinputedittext_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
                String referencecode_field = StringUtil.toSQLName("TRederenceCode");
                String tdate_field = StringUtil.toSQLName("TDate");


                List<TransactionsReportData> trd_list_all;
                if (dateType.getText().equals("All")) {
                    trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + referencecode_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%'");
                } else if (dateType.getText().equals("Weekly")) {
                    Calendar cl = Calendar.getInstance();
                    cl.setFirstDayOfWeek(1);

                    //first day of week
                    cl.set(Calendar.DAY_OF_WEEK, 1);
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    //last day of week
                    cl.set(Calendar.DAY_OF_WEEK, 7);
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                    trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where "+ referencecode_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");


                } else if (dateType.getText().equals("Monthly")) {

                    Calendar cl = Calendar.getInstance();

                    //first day of month
                    cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                    //last day of week
                    cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where "+ referencecode_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");


                } else if (dateType.getText().equals("Yearly")) {
                    Calendar cl = Calendar.getInstance();

                    //first day of year
                    cl.set(Calendar.DAY_OF_YEAR, cl.getActualMinimum(Calendar.DAY_OF_YEAR));
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    //last day of year
                    cl.set(Calendar.DAY_OF_YEAR, cl.getActualMaximum(Calendar.DAY_OF_YEAR));
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where "+ referencecode_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                } else {
                    trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + referencecode_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%'");

                }


                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);
            }
        });

        dateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateType.setText("All");

                List<TransactionsReportData> trd_list_all = TransactionsReportData.listAll(TransactionsReportData.class);

                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);

                dateChoices.setVisibility(View.GONE);

            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Weekly");

                Calendar cl = Calendar.getInstance();
                cl.setFirstDayOfWeek(1);

                //first day of week
                cl.set(Calendar.DAY_OF_WEEK, 1);
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                //last day of week
                cl.set(Calendar.DAY_OF_WEEK, 7);
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);

                dateChoices.setVisibility(View.GONE);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Monthly");

                Calendar cl = Calendar.getInstance();

                //first day of month
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                //last day of week
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);

                dateChoices.setVisibility(View.GONE);
            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Yearly");

                Calendar cl = Calendar.getInstance();

                //first day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMinimum(Calendar.DAY_OF_YEAR));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                //last day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMaximum(Calendar.DAY_OF_YEAR));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);

                dateChoices.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
        customDialog = new CustomProgressDialog();
        callCashWalletTransactionWebService();
    }

}
