package com.wedoops.pingjueclub.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.orm.SugarApp;
import com.orm.SugarDb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationClass extends com.orm.SugarApp {

    private static Context mContext;
    private static String prefs_name = "PJC";
    private ProgressDialog progress;

    @Override
    public void onCreate() {

        super.onCreate();

    }

    public ApplicationClass(Context context) {
        this.mContext = context;
    }

    public ApplicationClass() {
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void writeIntoSharedPreferences(Context context, String value_name, String value) {

//        SharedPreferences.Editor editor = getSharedPreferences(prefs_name, MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(value_name, value);
        editor.apply();
    }

    public String readFromSharedPreferences(Context context, String value_name) {

        String value;

//        SharedPreferences prefs = getSharedPreferences(prefs_name, MODE_PRIVATE);
//        value = prefs.getString(value_name, "");

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        value = sharedPreferences.getString(value_name, "");

        return value;
    }

    public void showProgressDialog(ProgressDialog mProgress) {
        progress = mProgress;
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    public void closeProgressDialog(ProgressDialog mProgress) {
        progress = mProgress;

        if (progress != null && progress.isShowing()) {
            try {
                progress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();

        if(focusedView != null){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }
}
