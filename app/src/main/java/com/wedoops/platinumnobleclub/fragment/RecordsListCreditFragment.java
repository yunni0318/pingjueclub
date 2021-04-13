package com.wedoops.platinumnobleclub.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.CustomProgressDialog;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.adapters.RecordsListAdapter;
import com.wedoops.platinumnobleclub.database.TransactionsReportData;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallRefreshToken;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;
import com.wedoops.platinumnobleclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class RecordsListCreditFragment extends Fragment {

    private static RecyclerView recyclerview_transaction;
    private static View view;
    private static Context get_context;
    private LinearLayout dateSelection, dateChoices;
    private static TextView dateType, weekly, monthly, yearly, dateAll;
    private static String DATA_TYPE_MERCHANT_PAYMENT = "MERCHANT-PAYMENT";
    private static String DATA_TYPE_ADMIN_TOPUP = "ADMIN-TOPUP";
    private static String DATA_TYPE_ADMIN_DEDUCT = "ADMIN-DEDUCT";
    private static String DATA_TYPE_PAYMENT_PARTIAL = "PAYMENT-PARTIAL";
    private static String DATA_TYPE_PAYMENT_FULL = "PAYMENT-FULL";
    private static final String KEY_LANG = "key_lang";
    private static RecordsListAdapter records_list_adapter;

    private static Handler handler;

    private static int counter = 0;
    private static CustomProgressDialog customDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.credit_transactions_report, container, false);
        get_context = view.getContext();
        handler = new Handler();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customDialog = new CustomProgressDialog();
        setupDeclaration();
        recordFilter();
    }

    private void setupDeclaration() {
        recyclerview_transaction = view.findViewById(R.id.recyclerview_credit);
        dateType = view.findViewById(R.id.dateType_credit);
        dateSelection = view.findViewById(R.id.dateSelection_credit);
        dateChoices = view.findViewById(R.id.dateChoices);
        dateAll = view.findViewById(R.id.dateAll_credit);
        weekly = view.findViewById(R.id.dateWeekly_credit);
        monthly = view.findViewById(R.id.dateMonthly_credit);
        yearly = view.findViewById(R.id.dateYearly_credit);
    }

    private void recordFilter() {

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


        final String lang = new ApplicationClass().readFromSharedPreferences(get_context, KEY_LANG);

        dateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("All");
                } else {
                    dateType.setText("全部");
                }

                dateChoices.setVisibility(View.GONE);

//                customDialog.showDialog(get_context);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<TransactionsReportData> trd_list_all = TransactionsReportData.listAll(TransactionsReportData.class);
//
//                        records_list_adapter.UpdateRecordListAdapter(trd_list_all);
//
//                        recyclerview_transaction.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                customDialog.hideDialog();
//                            }
//                        });
//                    }
//                }, 500);

            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Weekly");}
                else{
                    dateType.setText("每星期");
                }

                Calendar cl = Calendar.getInstance();
                cl.setFirstDayOfWeek(1);

                //first day of week
                cl.set(Calendar.DAY_OF_WEEK, 1);
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                //last day of week
                cl.set(Calendar.DAY_OF_WEEK, 7);
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

//                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
//                String tdate_field = StringUtil.toSQLName("TDate");
//
//                final List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");
                dateChoices.setVisibility(View.GONE);
//
//                customDialog.showDialog(get_context);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        records_list_adapter.UpdateRecordListAdapter(trd_list_all);
//
//                        recyclerview_transaction.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                customDialog.hideDialog();
//                            }
//                        });
//                    }
//                }, 500);

            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Monthly");}
                else{
                    dateType.setText("每个月");
                }
                Calendar cl = Calendar.getInstance();

                //first day of month
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                //last day of week
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


//                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
//                String tdate_field = StringUtil.toSQLName("TDate");
//
//                final List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");
//
//                customDialog.showDialog(get_context);
                dateChoices.setVisibility(View.GONE);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        records_list_adapter.UpdateRecordListAdapter(trd_list_all);
//                        recyclerview_transaction.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                customDialog.hideDialog();
//
//                            }
//                        });
//                    }
//                }, 500);

            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Yearly");}
                else{
                    dateType.setText("每年");
                }

                Calendar cl = Calendar.getInstance();

                //first day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMinimum(Calendar.DAY_OF_YEAR));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                //last day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMaximum(Calendar.DAY_OF_YEAR));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

//                String table_name = TransactionsReportData.getTableName(TransactionsReportData.class);
//                String tdate_field = StringUtil.toSQLName("TDate");
//
//                final List<TransactionsReportData> trd_list_all = TransactionsReportData.findWithQuery(TransactionsReportData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");
//
//                customDialog.showDialog(get_context);
//
                dateChoices.setVisibility(View.GONE);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        records_list_adapter.UpdateRecordListAdapter(trd_list_all);
//                        recyclerview_transaction.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                customDialog.hideDialog();
//
//                            }
//                        });
//                    }
//                }, 500);

            }
        });
    }
}
