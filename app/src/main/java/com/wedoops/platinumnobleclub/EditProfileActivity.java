package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.database.CountryList;
import com.wedoops.platinumnobleclub.database.StateList;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallRefreshToken;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;
import com.wedoops.platinumnobleclub.webservices.RefreshTokenAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends Fragment {

    private static View view;
    public static Activity get_activity;

    private static ImageView imageview_user_profile, imageview_user_rank;
    private static TextView textview_user_rank, textview_user_nickname, textview_profile_title, textview_nickname_title, textview_change_password_title, textview_current_password_title, textview_new_password_title, textview_confirm_password;
    private static EditText edittext_nickname, edittext_current_password, edittext_new_password, edittext_confirm_password;

    private static Button button_save_profile, button_change_password;
    private static Context get_context;
    private static final String KEY_LANG = "key_lang"; // preference key

    private static CustomProgressDialog customDialog;
    private static AlertDialog alert;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_profile_activity, container, false);
        get_context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        get_activity = getActivity();
        customDialog = new CustomProgressDialog();

        loadLanguage();
        setupViewById();
        setupCustomFont();
        setupEditText();
        setupListener();
//        callCountryStateListWebService();
        callMemberAccountSettingWebService();
    }

    private static void setupViewById() {
        imageview_user_profile = view.findViewById(R.id.imageview_user_profile);
        imageview_user_rank = view.findViewById(R.id.imageview_user_rank);

        textview_user_rank = view.findViewById(R.id.textview_user_rank);
        textview_user_nickname = view.findViewById(R.id.textview_user_nickname);
        textview_profile_title = view.findViewById(R.id.textview_profile_title);
        textview_nickname_title = view.findViewById(R.id.textview_nickname_title);

        edittext_nickname = view.findViewById(R.id.edittext_nickname);

        button_save_profile = view.findViewById(R.id.button_save_profile);

        textview_change_password_title = view.findViewById(R.id.textview_change_password_title);
        textview_current_password_title = view.findViewById(R.id.textview_current_password_title);
        textview_new_password_title = view.findViewById(R.id.textview_new_password_title);
        textview_confirm_password = view.findViewById(R.id.textview_confirm_password);

        edittext_current_password = view.findViewById(R.id.edittext_current_password);
        edittext_current_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    edittext_new_password.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        edittext_new_password = view.findViewById(R.id.edittext_new_password);
        edittext_new_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    edittext_confirm_password.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        edittext_confirm_password = view.findViewById(R.id.edittext_confirm_password);
        edittext_confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    button_change_password.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

        button_change_password = view.findViewById(R.id.button_change_password);
    }

    private static void setupEditText() {

        edittext_nickname.setEnabled(true);

        textview_profile_title.setText(view.getContext().getResources().getString(R.string.edit_profile_profile_title));
        textview_nickname_title.setText(view.getContext().getResources().getString(R.string.edit_profile_nickname_title));

        edittext_nickname.setTextColor(Color.parseColor("#aaaaaa"));

    }

    private static void callMemberAccountSettingWebService() {

//        CustomProgressDialog.showProgressDialog(get_context);

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_ACCOUNT_SETTING);

        new CallWebServices(Api_Constants.API_MEMBER_ACCOUNT_SETTING, view.getContext(), true).execute(b);

    }

//    private static void callCountryStateListWebService() {
//
//        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);
//
//        Bundle b = new Bundle();
//        b.putString("access_token", ud_list.get(0).getAccessToken());
//        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);
//
//        new CallWebServices(Api_Constants.API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST, view.getContext(), true).execute(b);
//
//    }

    private static void setupListener() {

        button_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ApplicationClass().hideSoftKeyboard(get_activity);
                callUpdateAccountProfileWebService();

            }
        });

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ApplicationClass().hideSoftKeyboard(get_activity);
                callUpdateAccountSecurityWebService();
            }
        });


    }

    private static void callUpdateAccountSecurityWebService() {

        String current_password = edittext_current_password.getText().toString();
        String new_password = edittext_new_password.getText().toString();
        String confirm_password = edittext_confirm_password.getText().toString();

        boolean isValid = true;

        if (current_password.length() < 1) {
            edittext_current_password.setError(view.getContext().getString(R.string.edit_profile_change_password_empty_error));
            isValid = false;
        }

        if (new_password.length() < 1) {
            edittext_new_password.setError(view.getContext().getString(R.string.edit_profile_change_password_empty_error));
            isValid = false;
        }

        if (confirm_password.length() < 1) {
            edittext_confirm_password.setError(view.getContext().getString(R.string.edit_profile_change_password_empty_error));
            isValid = false;
        }

        if (!isValidPassword(current_password)) {
            edittext_current_password.setError(view.getContext().getString(R.string.edit_profile_change_password_invalid_error));
            isValid = false;
        }

        if (!isValidPassword(new_password)) {
            edittext_new_password.setError(view.getContext().getString(R.string.edit_profile_change_password_invalid_error));
            isValid = false;
        }

        if (!isValidPassword(confirm_password)) {
            edittext_confirm_password.setError(view.getContext().getString(R.string.edit_profile_change_password_invalid_error));
            isValid = false;
        }

        if (!new ApplicationClass().readFromSharedPreferences(view.getContext(), CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_PASSWORD).equals(current_password)) {
            edittext_current_password.setError(view.getContext().getString(R.string.edit_profile_change_password_current_password_error));
            isValid = false;
        }

        if (!new_password.equals(confirm_password)) {
            edittext_confirm_password.setError(view.getContext().getString(R.string.edit_profile_change_password_confirm_password_error));
            isValid = false;
        }


        if (isValid) {

//            CustomProgressDialog.showProgressDialog(get_context);
            customDialog.showDialog(get_context);

            List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

            Bundle b = new Bundle();
            b.putString("CurrentPassword", current_password);
            b.putString("NewPassword", new_password);
            b.putString("ConfirmPassword", confirm_password);
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_UPDATE_ACCOUNT_SECURITY);

            new CallWebServices(Api_Constants.API_MEMBER_UPDATE_ACCOUNT_SECURITY, view.getContext(), true).execute(b);
        }


    }


    private static void callUpdateAccountProfileWebService() {

        String nickname = edittext_nickname.getText().toString();

        boolean isValid = true;

        if (nickname.length() < 1) {
            edittext_nickname.setError(view.getContext().getString(R.string.edittext_nickname_empty_error));
            isValid = false;
        }

        if (isValid) {

//            CustomProgressDialog.showProgressDialog(get_context);
            customDialog.showDialog(get_context);

            List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

            Bundle b = new Bundle();
            b.putString("NickName", nickname);
            b.putString("access_token", ud_list.get(0).getAccessToken());

            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_UPDATE_ACCOUNT_NICKNAME);

            new CallWebServices(Api_Constants.API_MEMBER_UPDATE_ACCOUNT_NICKNAME, view.getContext(), true).execute(b);


//            Bundle b = new Bundle();
//            b.putString("Name", ud_list.get(0).getName());
//            b.putString("Email", email);
//            b.putString("Country", ud_list.get(0).getCountryCode());
//            b.putString("State", ud_list.get(0).getStateCode());
//            b.putString("Address", ud_list.get(0).getAddress());
//            b.putString("Phone", ud_list.get(0).getPhone());
//            b.putString("access_token", ud_list.get(0).getAccessToken());
//            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);
//
//            new CallWebServices(Api_Constants.API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION, view.getContext(), true).execute(b);
        }


    }

    private static void setupCustomFont() {

        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
        textview_user_nickname.setTypeface(typeface);
        textview_profile_title.setTypeface(typeface);
        textview_nickname_title.setTypeface(typeface);

        textview_change_password_title.setTypeface(typeface);
        textview_current_password_title.setTypeface(typeface);
        textview_new_password_title.setTypeface(typeface);
        textview_confirm_password.setTypeface(typeface);

        Typeface typeface_crimson_700 = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/crimson-text-v9-latin-700.ttf");
        textview_user_rank.setTypeface(typeface_crimson_700);

    }

    private static void displayResult() {

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

//        Glide.with(view.getContext()).load(ud_list.get(0).getProfilePictureImagePath()).into(imageview_user_profile);
        Glide.with(view.getContext()).load(ud_list.get(0).getProfilePictureImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(imageview_user_profile);

        if (ud_list.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            imageview_user_rank.setImageResource(R.drawable.bronze_medal);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_bronze));

        } else if (ud_list.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_SILVER)) {
            imageview_user_rank.setImageResource(R.drawable.silver_medal);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_silver));

        } else if (ud_list.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank.setImageResource(R.drawable.gold_medal);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_gold));

        } else {
            imageview_user_rank.setImageResource(R.drawable.user_level_platinum);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_platinum));
        }

        textview_user_nickname.setText(ud_list.get(0).getNickName());

        edittext_nickname.setText(ud_list.get(0).getNickName());

        String tablename_cl = StringUtil.toSQLName("CountryList");
        String fieldname_country_code = StringUtil.toSQLName("countryCode");
        List<CountryList> cl_filter = CountryList.findWithQuery(CountryList.class, "Select * from " + tablename_cl + " where " + fieldname_country_code + " = ?", ud_list.get(0).getCountryCode());

//        String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");
//
//        String country_name = "";
//        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
//            String[] split_one = cl_filter.get(0).getCountryName().split(";");
//            String[] split_two = split_one[0].split("=");
//            country_name = split_two[1];
//
//        } else {
//            String[] split_one = cl_filter.get(0).getCountryName().split(";");
//            String[] split_two = split_one[1].split("=");
//            country_name = split_two[1];
//        }
//
//        edittext_country.setText(country_name);
//
//        String tablename_sl = StringUtil.toSQLName("StateList");
//        String fieldname_state_code = StringUtil.toSQLName("stateCode");
//        List<StateList> sl_filter = StateList.findWithQuery(StateList.class, "Select * from " + tablename_sl + " where " + fieldname_state_code + " = ?", ud_list.get(0).getStateCode());
//
//        String state_name = "";
//        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
//            String[] split_one = sl_filter.get(0).getStateName().split(";");
//            String[] split_two = split_one[0].split("=");
//            state_name = split_two[1];
//
//        } else {
//            String[] split_one = sl_filter.get(0).getStateName().split(";");
//            String[] split_two = split_one[1].split("=");
//            state_name = split_two[1];
//        }
//
//        edittext_state.setText(state_name);
//
//        edittext_address.setText(ud_list.get(0).getAddress());
//        edittext_phone.setText(ud_list.get(0).getPhone());

    }

    private static void callRefreshTokenWebService(int origin) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.API_REFRESH_TOKEN);

        new CallRefreshToken(RefreshTokenAPI.API_REFRESH_TOKEN, get_context, get_activity, origin).execute(b);
    }


    public static void processWSData(JSONObject returnedObject, int command) {

//        CustomProgressDialog.closeProgressDialog();
        customDialog.hideDialog();

        if (command == Api_Constants.API_MEMBER_ACCOUNT_SETTING) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");

                        List<UserDetails> ud_listall = UserDetails.listAll(UserDetails.class);
                        String loginid_field = StringUtil.toSQLName("LoginID");

                        UserDetails ud = (UserDetails.find(UserDetails.class, loginid_field + " = ?", ud_listall.get(0).getLoginID())).get(0);

                        ud.setSrno(response_object.getString("Srno"));
                        ud.setLoginID(response_object.getString("LoginID"));
                        ud.setName(response_object.getString("Name"));
                        ud.setNickName(response_object.getString("NickName"));
                        ud.setDOB(response_object.getString("DOB"));
                        ud.setEmail(response_object.getString("Email"));
                        ud.setPhone(response_object.getString("Phone"));
                        ud.setCountryCode(response_object.getString("CountryCode"));
                        ud.setStateCode(response_object.getString("StateCode"));
                        ud.setAddress(response_object.getString("Address"));
                        ud.setGender(response_object.getString("Gender"));
                        ud.setProfilePictureImagePath(response_object.getString("ProfilePictureImagePath"));
                        ud.setUserLevelCode(response_object.getString("UserLevelCode"));
                        ud.setJoinedDate(response_object.getString("JoinedDate"));
                        ud.setCashWallet(response_object.getString("CashWallet"));

                        ud.save();

                        displayResult();

                    } else {
                        if (returnedObject.getInt("StatusCode") == 401) {
//                            CustomProgressDialog.showProgressDialog(get_context);
                            customDialog.showDialog(get_context);
                            callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_SETTING);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                        }
                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_SETTING);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, view.getContext(), get_activity);

                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);
            }
        } else if (command == Api_Constants.API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONArray response_country_array = response_object.getJSONArray("CountryData");
                        JSONArray response_state_array = response_object.getJSONArray("StateData");

                        CountryList.deleteAll(CountryList.class);
                        StateList.deleteAll(StateList.class);

                        for (int i = 0; i < response_country_array.length(); i++) {
                            JSONObject array_object = response_country_array.getJSONObject(i);
                            CountryList cl = new CountryList(array_object.getString("CountryCode"), array_object.getString("PhoneCode"), array_object.getString("CountryName"), array_object.getString("CountryDefaultName"), array_object.getString("Active"), array_object.getString("CreatedDate"));
                            cl.save();
                        }

                        for (int i = 0; i < response_state_array.length(); i++) {
                            JSONObject array_object = response_state_array.getJSONObject(i);
                            StateList sl = new StateList(array_object.getString("StateCode"), array_object.getString("StateName"), array_object.getString("StateDefaultName"), array_object.getString("CountryCode"), array_object.getString("Active"), array_object.getString("CreatedDate"));
                            sl.save();
                        }

                    } else {
                        if (returnedObject.getInt("StatusCode") == 401) {

                            callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                        }
                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_SETTING);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, view.getContext(), get_activity);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        } else if (command == Api_Constants.API_MEMBER_UPDATE_ACCOUNT_NICKNAME) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(get_context);

                        View customLayout = get_activity.getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
                        TextView textview_title = customLayout.findViewById(R.id.textview_title);
                        TextView textview_message = customLayout.findViewById(R.id.textview_message);
                        Button button_ok = customLayout.findViewById(R.id.button_ok);

                        textview_title.setText(view.getContext().getString(R.string.success_title));
                        textview_message.setText(view.getContext().getResources().getString(R.string.edit_profile_save_profile_success));
                        button_ok.setText(view.getContext().getString(R.string.Ok));

                        builder.setView(customLayout);

                        builder.setCancelable(false);


                        button_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                callMemberAccountSettingWebService();
                            }
                        });

                        alert = builder.create();

                        alert.show();

                    } else {

                        if (returnedObject.getInt("StatusCode") == 401) {

//                            callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);
                            callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME);


                        } else {
                            new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                        }
                    }

                } else {

                    int statuscode = returnedObject.getInt("StatusCode");

                    if (statuscode == 401) {
//                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(statuscode, view.getContext(), get_activity);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        } else if (command == Api_Constants.API_MEMBER_UPDATE_ACCOUNT_SECURITY) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        new ApplicationClass().writeIntoSharedPreferences(view.getContext(), CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_PASSWORD, edittext_new_password.getText().toString());

                        edittext_current_password.setText("");
                        edittext_new_password.setText("");
                        edittext_confirm_password.setText("");

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(get_context);

                        View customLayout = get_activity.getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
                        TextView textview_title = customLayout.findViewById(R.id.textview_title);
                        TextView textview_message = customLayout.findViewById(R.id.textview_message);
                        Button button_ok = customLayout.findViewById(R.id.button_ok);

                        textview_title.setText(view.getContext().getString(R.string.success_title));
                        textview_message.setText(view.getContext().getResources().getString(R.string.edit_profile_change_password_success));
                        button_ok.setText(view.getContext().getString(R.string.Ok));

                        builder.setView(customLayout);

                        builder.setCancelable(false);


                        button_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                callMemberAccountSettingWebService();
                            }
                        });

                        alert = builder.create();

                        alert.show();


//                        AlertDialog.Builder builder = new AlertDialog.Builder(
//                                view.getContext());
//                        builder.setTitle(view.getContext().getString(R.string.success_title));
//                        builder.setMessage(view.getContext().getResources().getString(R.string.edit_profile_change_password_success));
//                        builder.setPositiveButton(view.getContext().getString(R.string.Ok),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        dialog.dismiss();
////                                        callCountryStateListWebService();
//                                        callMemberAccountSettingWebService();
//                                    }
//                                });
//
//                        builder.show();

                    } else {

                        if (returnedObject.getInt("StatusCode") == 401) {

                            callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                        }

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, view.getContext(), get_activity);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        }
    }

    public static void processRefreshToken(JSONObject returnedObject, int command, int origin) {
        if (command == RefreshTokenAPI.API_REFRESH_TOKEN) {

//            CustomProgressDialog.closeProgressDialog();
            customDialog.hideDialog();

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

                        if (origin == RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_SETTING) {
                            callMemberAccountSettingWebService();

                        } else if (origin == RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_COUNTRY_STATE_LIST) {
//                            callCountryStateListWebService();
                            callMemberAccountSettingWebService();

                        } else if (origin == RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_NICKNAME) {
                            callUpdateAccountProfileWebService();

                        } else if (origin == RefreshTokenAPI.ORIGIN_MEMBER_UPDATE_ACCOUNT_SECURITY) {
                            callUpdateAccountSecurityWebService();

                        }


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                    }

                } else {

                    int errorCode = returnedObject.getInt("StatusCode");

                    if (errorCode == 401) {
                        callRefreshTokenWebService(RefreshTokenAPI.ORIGIN_MEMBER_ACCOUNT_SETTING);

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(errorCode, view.getContext(), get_activity);

                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }

    private static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void loadLanguage() {
        String lang = new ApplicationClass().readFromSharedPreferences(get_activity, KEY_LANG);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
