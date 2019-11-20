package com.wedoops.pingjueclub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallRefreshToken;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.wedoops.pingjueclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PayFragment extends Fragment {

    private static LinearLayout payConfirmation, linearlayout_paySuccess;
    private static CardView cancel, confirm;
    private static String transaction_id, remarks, currency, actual_amount_points, actual_amount_money, discounted_amount_points, discounted_amount_money, selected_currency;
    private static TextView textview_remarks, textview_points, textview_converted_amount, paySuccessMessage;
    private static int counter;
    private static CustomProgressDialog customDialog;
    private static Context get_context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            transaction_id = bundle.getString("TRANSACTION_ID", "");
            remarks = bundle.getString("REMARKS", "");
            actual_amount_points = bundle.getString("ACTUAL_AMOUNT_POINTS", "");
            actual_amount_money = bundle.getString("ACTUAL_AMOUNT_MONEY", "");
            discounted_amount_points = bundle.getString("DISCOUNTED_AMOUNT_POINTS", "");
            discounted_amount_money = bundle.getString("DISCOUNTED_AMOUNT_MONEY", "");
            selected_currency = bundle.getString("SELECTED_CURRENCY", "");

        } else {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).loadHomeFragment();
            }

            new DisplayAlertDialog().displayAlertDialogString(0, "Cannot get data from QR Code, please try again!", false, getContext());

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pay_fragment, container, false);
        get_context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payConfirmation = view.findViewById(R.id.payConfirmation);
        cancel = view.findViewById(R.id.payCancelBtn);
        confirm = view.findViewById(R.id.payConfirmBtn);
        linearlayout_paySuccess = view.findViewById(R.id.linearlayout_paySuccess);
        textview_remarks = view.findViewById(R.id.textview_remarks);
        textview_points = view.findViewById(R.id.textview_points);
        textview_converted_amount = view.findViewById(R.id.textview_converted_amount);
        paySuccessMessage = view.findViewById(R.id.paySuccessMessage);
        textview_remarks.setText(remarks);

        BigDecimal actual_amount_points_decimal = new BigDecimal(actual_amount_points);
        actual_amount_points_decimal = actual_amount_points_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal actual_amount_money_decimal = new BigDecimal(actual_amount_money);
        actual_amount_money_decimal = actual_amount_money_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        NumberFormat formatter = new DecimalFormat("#,###,###.00");
        String actual_amount_points_string = formatter.format(actual_amount_points_decimal.doubleValue());
        String actual_amount_money_string = formatter.format(actual_amount_money_decimal.doubleValue());

        textview_points.setText(String.format("%s PTS", actual_amount_points_string));
        textview_converted_amount.setText(String.format("= %s %s", selected_currency, actual_amount_money_string));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callWebServices();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadHomeFragment();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customDialog = new CustomProgressDialog();
    }

    private static void displayResult(String message) {
        payConfirmation.setVisibility(View.GONE);
        linearlayout_paySuccess.setVisibility(View.VISIBLE);
        paySuccessMessage.setText(message);
    }


    private static void callWebServices() {
//        CustomProgressDialog.showProgressDialog(get_activity.getApplicationContext());
        customDialog.showDialog(get_context);

        if (counter < 4) {
            counter++;
            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putString("transactionid", transaction_id);
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MAKE_QR_CODE_PAYMENT);

            new CallWebServices(Api_Constants.API_MAKE_QR_CODE_PAYMENT, get_context, true).execute(b);

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
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.ORIGIN_MAKE_QR_CODE_PAYMENT, get_context, RefreshTokenAPI.ORIGIN_MAKE_QR_CODE_PAYMENT).execute(b);

    }


    public static void processWSData(JSONObject returnedObject, int command) {
        customDialog.hideDialog();

        if (command == Api_Constants.API_MAKE_QR_CODE_PAYMENT) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        counter = 0;
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");

                        displayResult(response_object.getString("MessageEN"));

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), get_context);

                    }

                } else {

                    if (returnedObject.getInt("StatusCode") == 401) {
//                        CustomProgressDialog.showProgressDialog(get_activity.getApplicationContext());
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService();

                    } else {
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

                        cancel.performClick();

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(get_context, "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, get_context);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, get_context);

                        }

                    }

                }

            } catch (Exception e) {
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, get_context);
            }
        } else if (command == RefreshTokenAPI.ORIGIN_MAKE_QR_CODE_PAYMENT) {

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
                        PayFragment.callWebServices();
                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), get_context);

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(get_context, "key_lang");

                    if (errorCode == 1506) {
                        new DisplayAlertDialog().displayAlertDialogError(1506, get_context);
                    } else {
                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, get_context);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, get_context);

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, get_context);

            }
        }
    }
}
