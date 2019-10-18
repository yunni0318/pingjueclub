package com.wedoops.pingjueclub.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.view.inputmethod.InputMethodManager;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationClass extends com.orm.SugarApp {

    private static Context mContext;
    private static String prefs_name = "PJC";

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

    @Override
    public void onCreate() {

        super.onCreate();

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

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();

        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkSelfPermissions(Activity activity) {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CONSTANTS_VALUE.CAMERA_STORAGE_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CONSTANTS_VALUE.ONLY_CAMERA_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CONSTANTS_VALUE.ONLY_STORAGE_REQUEST_CODE);
            return false;
        }
        return true;
    }

}
