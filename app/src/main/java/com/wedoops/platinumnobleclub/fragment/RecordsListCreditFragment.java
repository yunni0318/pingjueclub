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
    private static Activity get_activity;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
