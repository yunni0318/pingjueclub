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
import com.wedoops.platinumnobleclub.adapters.RecordsCreditAdapter;
import com.wedoops.platinumnobleclub.adapters.RecordsListAdapter;
import com.wedoops.platinumnobleclub.database.TransactionsCreditData;
import com.wedoops.platinumnobleclub.database.TransactionsReportData;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class RecordsListCreditFragment extends Fragment {

    private static RecyclerView recyclerview_transaction;
    private static View view;
    private static Context get_context;
    private static Activity get_activity;
    private LinearLayout dateSelection, dateChoices;
    private static TextView dateType, weekly, monthly, yearly, dateAll;
    private static final String KEY_LANG = "key_lang";
    private static RecordsCreditAdapter recordsCreditAdapter;
    private static TextInputEditText textinputedittext_filter;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customDialog = new CustomProgressDialog();
        setupDeclaration();
        getCreditTransaction();
        recordFilter();
    }

    private void setupDeclaration() {
        textinputedittext_filter = view.findViewById(R.id.textinputedittext_filter);
        recyclerview_transaction = view.findViewById(R.id.recyclerview_credit);
        dateType = view.findViewById(R.id.dateType_credit);
        dateSelection = view.findViewById(R.id.dateSelection_credit);
        dateChoices = view.findViewById(R.id.dateChoices);
        dateAll = view.findViewById(R.id.dateAll_credit);
        weekly = view.findViewById(R.id.dateWeekly_credit);
        monthly = view.findViewById(R.id.dateMonthly_credit);
        yearly = view.findViewById(R.id.dateYearly_credit);

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
        inputFilter();
    }

    private static void inputFilter(){

        List<TransactionsCreditData> transactionsCreditData = TransactionsCreditData.listAll(TransactionsCreditData.class);
        recordsCreditAdapter = new RecordsCreditAdapter(get_context, transactionsCreditData);

        textinputedittext_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String table_name = TransactionsCreditData.getTableName(TransactionsCreditData.class);
                String remarks_field = StringUtil.toSQLName("Remarks");
                String tdate_field = StringUtil.toSQLName("TDate");

                List<TransactionsCreditData> trd_list_all;
                if (dateType.getText().equals("All") || dateType.getText().equals("全部")) {
                    trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + remarks_field + " LIKE '%" + textinputedittext_filter.getText().toString() + "%'");
                } else if (dateType.getText().equals("Weekly") || dateType.getText().equals("每星期")) {
                    Calendar cl = Calendar.getInstance();
                    cl.setFirstDayOfWeek(1);

                    //first day of week
                    cl.set(Calendar.DAY_OF_WEEK, 1);
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    //last day of week
                    cl.set(Calendar.DAY_OF_WEEK, 7);
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                    trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + remarks_field + " LIKE '%" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");


                } else if (dateType.getText().equals("Monthly") || dateType.getText().equals("每个月")) {

                    Calendar cl = Calendar.getInstance();

                    //first day of month
                    cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                    //last day of week
                    cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + remarks_field + " LIKE '%" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");


                } else if (dateType.getText().equals("Yearly") || dateType.getText().equals("每年")) {
                    Calendar cl = Calendar.getInstance();

                    //first day of year
                    cl.set(Calendar.DAY_OF_YEAR, cl.getActualMinimum(Calendar.DAY_OF_YEAR));
                    String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    //last day of year
                    cl.set(Calendar.DAY_OF_YEAR, cl.getActualMaximum(Calendar.DAY_OF_YEAR));
                    String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                    trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + remarks_field + " LIKE '%" + textinputedittext_filter.getText().toString() + "%' AND " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                } else {
                    trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + remarks_field + " LIKE '%" + textinputedittext_filter.getText().toString() + "%'");

                }

                recordsCreditAdapter.UpdateRecordListAdapter(trd_list_all);

            }
        });

    }

    private void getCreditTransaction() {
        //customDialog.showDialog(get_context);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_CASH_WALLET_TRANSACTION_CLUB);

        new CallWebServices(Api_Constants.API_CASH_WALLET_TRANSACTION_CLUB, view.getContext(), true).execute(b);

    }

    public static void processWSData(JSONObject returnedObject, int command) {

        // customDialog.hideDialog();

        if (command == Api_Constants.API_CASH_WALLET_TRANSACTION_CLUB) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONArray response_object = returnedObject.getJSONArray("ResponseData");

                        List<TransactionsCreditData> tcd = TransactionsCreditData.listAll(TransactionsCreditData.class);
                        if (tcd.size() > 0) {
                            TransactionsCreditData.deleteAll(TransactionsCreditData.class);
                        }

                        for (int i = 0; i < response_object.length(); i++) {

                            JSONObject bd = response_object.getJSONObject(i);
                            JSONObject bd_type = bd.getJSONObject("TType");
                            String TRederenceCode = bd.getString("TRederenceCode");
                            String TDate = bd.getString("TDate");


                            String type = bd_type.getString("Type");
                            String status = bd_type.getString("TStatus");
                            double amount = bd_type.getDouble("Amount");
                            boolean IsCashIn = bd_type.getBoolean("IsCashIn");
                            String remarks = bd_type.getString("Remarks");

                            TransactionsCreditData tcredit = new TransactionsCreditData(TRederenceCode, type, remarks, TDate, status, amount, IsCashIn);
                            tcredit.save();

                        }

                        List<TransactionsCreditData> transactionsCreditData = TransactionsCreditData.listAll(TransactionsCreditData.class);
                        recyclerview_transaction.setHasFixedSize(true);
                        recyclerview_transaction.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        recordsCreditAdapter = new RecordsCreditAdapter(get_context, transactionsCreditData);
                        recyclerview_transaction.setAdapter(recordsCreditAdapter);

                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        }
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

                customDialog.showDialog(get_context);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<TransactionsCreditData> trd_list_all = TransactionsCreditData.listAll(TransactionsCreditData.class);

                        recordsCreditAdapter.UpdateRecordListAdapter(trd_list_all);

                        recyclerview_transaction.post(new Runnable() {
                            @Override
                            public void run() {
                                customDialog.hideDialog();
                            }
                        });
                    }
                }, 500);

            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Weekly");
                } else {
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

                String table_name = TransactionsCreditData.getTableName(TransactionsCreditData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                final List<TransactionsCreditData> trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");
                dateChoices.setVisibility(View.GONE);

                customDialog.showDialog(get_context);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        recordsCreditAdapter.UpdateRecordListAdapter(trd_list_all);

                        recyclerview_transaction.post(new Runnable() {
                            @Override
                            public void run() {
                                customDialog.hideDialog();
                            }
                        });
                    }
                }, 500);

            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Monthly");
                } else {
                    dateType.setText("每个月");
                }
                Calendar cl = Calendar.getInstance();

                //first day of month
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                //last day of week
                cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();


                String table_name = TransactionsCreditData.getTableName(TransactionsCreditData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                final List<TransactionsCreditData> trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                customDialog.showDialog(get_context);
                dateChoices.setVisibility(View.GONE);
//
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        recordsCreditAdapter.UpdateRecordListAdapter(trd_list_all);
                        recyclerview_transaction.post(new Runnable() {
                            @Override
                            public void run() {
                                customDialog.hideDialog();

                            }
                        });
                    }
                }, 500);

            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    dateType.setText("Yearly");
                } else {
                    dateType.setText("每年");
                }

                Calendar cl = Calendar.getInstance();

                //first day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMinimum(Calendar.DAY_OF_YEAR));
                String date1 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                //last day of year
                cl.set(Calendar.DAY_OF_YEAR, cl.getActualMaximum(Calendar.DAY_OF_YEAR));
                String date2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.sss", cl).toString();

                String table_name = TransactionsCreditData.getTableName(TransactionsCreditData.class);
                String tdate_field = StringUtil.toSQLName("TDate");

                final List<TransactionsCreditData> trd_list_all = TransactionsCreditData.findWithQuery(TransactionsCreditData.class, "SELECT * from " + table_name + " where " + tdate_field + " between '" + date1 + "' " + " and " + "'" + date2 + "'");

                customDialog.showDialog(get_context);

                dateChoices.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        recordsCreditAdapter.UpdateRecordListAdapter(trd_list_all);
                        recyclerview_transaction.post(new Runnable() {
                            @Override
                            public void run() {
                                customDialog.hideDialog();

                            }
                        });
                    }
                }, 500);

            }
        });
    }
}
