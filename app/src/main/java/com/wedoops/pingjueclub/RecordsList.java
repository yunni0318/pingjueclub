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
import java.util.List;

public class RecordsList extends Fragment {

    private static RecyclerView recyclerview_transaction;
    private static View view;
    private static Activity get_activity;
    private static Context get_context;
    private LinearLayout dateSelection, dateChoices;
    private TextView dateType, weekly, monthly, yearly;
    private static String DATA_TYPE_MERCHANT_PAYMENT = "MERCHANT-PAYMENT";
    private static String DATA_TYPE_ADMIN_TOPUP = "ADMIN-TOPUP";
    private static String DATA_TYPE_PAYMENT = "PAYMENT-";

    private static RecordsListAdapter adapter;

    private static void callCashWalletTransactionWebService() {

        CustomProgressDialog.showProgressDialog(get_context);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_CASH_WALLET_TRANSACTION_V2);

        new CallWebServices(Api_Constants.API_CASH_WALLET_TRANSACTION_V2, view.getContext(), true).execute(b);

    }

    private static void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, get_activity, origin).execute(b);
    }

    private static void displayResult() {
        setupRecyclerView();
    }

    private static void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        List<TransactionsReportData> trd_list_all = TransactionsReportData.listAll(TransactionsReportData.class);

        adapter = new RecordsListAdapter(trd_list_all);

        recyclerview_transaction.setLayoutManager(layoutManager);
        recyclerview_transaction.setAdapter(adapter);

    }

    public static void processWSData(JSONObject returnedObject, int command) {

        CustomProgressDialog.closeProgressDialog();

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
                                TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), actualamount, discountrate, discountamount, remarks, bd.getString("TDate"), null, 0, false,null);
                                trd.save();


                            } else if (bd_type.getString("Type").equals(DATA_TYPE_ADMIN_TOPUP)) {
                                TStatus = bd_type.getString("TStatus");
                                pointamount = bd_type.getInt("PointAmount");
                                iscashin = bd_type.getBoolean("IsCashIn");
                                remarks = bd_type.getString("Remarks");
                                TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), 0, 0, 0, remarks, bd.getString("TDate"), TStatus, pointamount, iscashin,null);
                                trd.save();

                            } else if (bd_type.getString("Type").contains(DATA_TYPE_PAYMENT)) {
                                TStatus = bd_type.getString("TStatus");
                                pointamount = bd_type.getInt("PointAmount");
                                iscashin = bd_type.getBoolean("IsCashIn");
                                remarks = bd_type.getString("Remarks");
                                TransactionsReportData trd = new TransactionsReportData(bd.getString("TRederenceCode"), bd_type.getString("Type"), 0, 0, 0, remarks, bd.getString("TDate"), TStatus, pointamount, iscashin,bd_type.getString("EventTitle"));
                                trd.save();
                            }

                        }

                        displayResult();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION);

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
            }
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

                        if (origin == RefreshTokenAPI.ORIGIN_CASH_WALLET_TRANSACTION) {
                            callCashWalletTransactionWebService();
                        }


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_EVENT_BOOKING_LIST);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, view.getContext());

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

        dateType = view.findViewById(R.id.dateType);
        dateSelection = view.findViewById(R.id.dateSelection);
        dateChoices = view.findViewById(R.id.dateChoices);
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
                String adminremarks_field = StringUtil.toSQLName("AdminRemarks");
                String cashout_field = StringUtil.toSQLName("TCashOutAmount");

                List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + cashout_field + " LIKE '" + textinputedittext_filter.getText().toString() + "%'");

                adapter = new RecordsListAdapter(trd_list_all);
                recyclerview_transaction.setAdapter(adapter);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Weekly");
                Toast.makeText(getContext(), "Weekly Selected", Toast.LENGTH_SHORT).show();
                dateChoices.setVisibility(View.GONE);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Monthly");
                Toast.makeText(getContext(), "Monthly Selected", Toast.LENGTH_SHORT).show();
                dateChoices.setVisibility(View.GONE);
            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType.setText("Yearly");
                Toast.makeText(getContext(), "Yearly Selected", Toast.LENGTH_SHORT).show();
                dateChoices.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
        setupFindViewById();
        callCashWalletTransactionWebService();
    }

    private void setupFindViewById() {
        recyclerview_transaction = view.findViewById(R.id.recyclerview_transaction);
    }

}
