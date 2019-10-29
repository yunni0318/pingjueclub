package com.wedoops.pingjueclub;

import android.app.Activity;
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
import com.wedoops.pingjueclub.database.MemberDashboardEventData;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallRefreshToken;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.wedoops.pingjueclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PayFragment extends Fragment {

    private static LinearLayout payConfirmation, paySuccess;
    private CardView cancel, confirm;
    private static String transaction_id, remarks, currency, pay_amount;
    private static TextView payTitle, payPointFront, payPointEnd, payCurrency, payAmount, paySuccessMessage, paySuccessID;
    public static Activity get_activity;
    private static int counter;
    private static CustomProgressDialog customDialog;
    private static Context get_context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            transaction_id = bundle.getString("TRANSACTION_ID", "");
            remarks = "GYMS Member";
            currency = "EUR";
            pay_amount = "1000";

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
        get_activity = getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payConfirmation = view.findViewById(R.id.payConfirmation);
        cancel = view.findViewById(R.id.payCancelBtn);
        confirm = view.findViewById(R.id.payConfirmBtn);
        paySuccess = view.findViewById(R.id.paySuccess);
        payTitle = view.findViewById(R.id.payTitle);
        payPointFront = view.findViewById(R.id.payPointFront);
        payPointEnd = view.findViewById(R.id.payPointEnd);
        payCurrency = view.findViewById(R.id.payCurrency);
        payAmount = view.findViewById(R.id.payAmount);
        paySuccessMessage = view.findViewById(R.id.paySuccessMessage);
        paySuccessID = view.findViewById(R.id.paySuccessID);

        payTitle.setText(String.format(getResources().getString(R.string.pay_confirmation_msg), remarks));
        payPointFront.setText(pay_amount);
        payAmount.setText(String.valueOf(pay_amount));
//        paySuccessMessage.setText(String.format(getResources().getString(R.string.pay_success_msg), pay_amount, remarks));

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
        paySuccess.setVisibility(View.VISIBLE);
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

            new CallWebServices(Api_Constants.API_MAKE_QR_CODE_PAYMENT, get_activity.getApplicationContext(), true).execute(b);

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

        new CallRefreshToken(RefreshTokenAPI.ORIGIN_MAKE_QR_CODE_PAYMENT, get_activity, RefreshTokenAPI.ORIGIN_MAKE_QR_CODE_PAYMENT).execute(b);

    }


    public static void processWSData(JSONObject returnedObject, int command) {
//        CustomProgressDialog.closeProgressDialog();
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
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), get_activity.getApplicationContext());

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

                        String currentLanguage = new ApplicationClass().readFromSharedPreferences(get_activity.getApplicationContext(), "key_lang");

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, get_activity.getApplicationContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, get_activity.getApplicationContext());

                        }

                    }

                }

            } catch (Exception e) {
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, get_activity.getApplicationContext());
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
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, get_activity.getApplicationContext());

            }
        }
    }
}
