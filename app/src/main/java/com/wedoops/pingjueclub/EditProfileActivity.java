package com.wedoops.pingjueclub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.database.CountryList;
import com.wedoops.pingjueclub.database.StateList;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends Fragment {

    private static View view;
    private static Activity activity;

    private static ImageView imageview_user_profile, imageview_user_rank;
    private static TextView textview_user_rank, textview_user_full_name, textview_user_email, textview_profile_title, textview_fullname_title, textview_username_title, textview_dob_title, textview_email_title, textview_country_title, textview_state_title, textview_address_title, textview_phone_title, textview_change_password_title, textview_current_password_title, textview_new_password_title, textview_confirm_password;
    private static EditText edittext_fullname, edittext_username, edittext_dob, edittext_email, edittext_country, edittext_state, edittext_address, edittext_phone, edittext_current_password, edittext_new_password, edittext_confirm_password;

    private static Button button_save_profile, button_change_password;

    private static ProgressDialog progress;
    private static final String KEY_LANG = "key_lang"; // preference key

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_profile_activity, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        progress = new ProgressDialog(view.getContext());

        loadLanguage();
        setupViewById();
        setupCustomFont();
        setupEditText();
        setupListener();
        callCountryStateListWebService();
        callMemberAccountSettingWebService();
    }

    private static void setupViewById() {
        imageview_user_profile = view.findViewById(R.id.imageview_user_profile);
        imageview_user_rank = view.findViewById(R.id.imageview_user_rank);

        textview_user_rank = view.findViewById(R.id.textview_user_rank);
        textview_user_full_name = view.findViewById(R.id.textview_user_full_name);
        textview_user_email = view.findViewById(R.id.textview_user_email);
        textview_profile_title = view.findViewById(R.id.textview_profile_title);
        textview_fullname_title = view.findViewById(R.id.textview_fullname_title);
        textview_username_title = view.findViewById(R.id.textview_username_title);
        textview_dob_title = view.findViewById(R.id.textview_dob_title);
        textview_email_title = view.findViewById(R.id.textview_email_title);
        textview_country_title = view.findViewById(R.id.textview_country_title);
        textview_state_title = view.findViewById(R.id.textview_state_title);
        textview_address_title = view.findViewById(R.id.textview_address_title);
        textview_phone_title = view.findViewById(R.id.textview_phone_title);

        edittext_fullname = view.findViewById(R.id.edittext_fullname);
        edittext_username = view.findViewById(R.id.edittext_username);
        edittext_dob = view.findViewById(R.id.edittext_dob);
        edittext_email = view.findViewById(R.id.edittext_email);
        edittext_country = view.findViewById(R.id.edittext_country);
        edittext_state = view.findViewById(R.id.edittext_state);
        edittext_address = view.findViewById(R.id.edittext_address);
        edittext_phone = view.findViewById(R.id.edittext_phone);

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

        edittext_fullname.setEnabled(false);
        edittext_username.setEnabled(false);
        edittext_dob.setEnabled(false);
        edittext_email.setEnabled(true);
        edittext_country.setEnabled(false);
        edittext_state.setEnabled(false);
        edittext_address.setEnabled(false);
        edittext_phone.setEnabled(false);

        textview_profile_title.setText(view.getContext().getResources().getString(R.string.edit_profile_profile_title));
        textview_fullname_title.setText(view.getContext().getResources().getString(R.string.edit_profile_fullname_title));
        textview_username_title.setText(view.getContext().getResources().getString(R.string.edit_profile_username_title));
        textview_dob_title.setText(view.getContext().getResources().getString(R.string.edit_profile_dob_title));
        textview_email_title.setText(view.getContext().getResources().getString(R.string.edit_profile_email_title));
        textview_country_title.setText(view.getContext().getResources().getString(R.string.edit_profile_country_title));
        textview_state_title.setText(view.getContext().getResources().getString(R.string.edit_profile_state_title));
        textview_address_title.setText(view.getContext().getResources().getString(R.string.edit_profile_address_title));
        textview_phone_title.setText(view.getContext().getResources().getString(R.string.edit_profile_phone_title));

        edittext_fullname.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_username.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_dob.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_email.setTextColor(Color.parseColor("#ffffff"));
        edittext_country.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_state.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_address.setTextColor(Color.parseColor("#aaaaaa"));
        edittext_phone.setTextColor(Color.parseColor("#aaaaaa"));
    }

    private static void callMemberAccountSettingWebService() {

        new ApplicationClass().showProgressDialog(progress);

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_ACCOUNT_SETTING);

        new CallWebServices(Api_Constants.API_MEMBER_ACCOUNT_SETTING, view.getContext(), true).execute(b);

    }

    private static void callCountryStateListWebService() {

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST);

        new CallWebServices(Api_Constants.API_MEMBER_ACCOUNT_COUNTRY_STATE_LIST, view.getContext(), true).execute(b);

    }

    private static void setupListener() {

        button_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ApplicationClass().hideSoftKeyboard(activity);
                callUpdateAccountProfileWebService();

            }
        });

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ApplicationClass().hideSoftKeyboard(activity);
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

            new ApplicationClass().showProgressDialog(progress);

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

        String email = edittext_email.getText().toString();

        boolean isValid = true;

        if (email.length() < 1) {
            edittext_email.setError(view.getContext().getString(R.string.edit_profile_email_empty_error));
            isValid = false;
        }

        if (!ApplicationClass.isEmailValid(email)) {
            edittext_email.setError(view.getContext().getString(R.string.edit_profile_email_invalid_format_error));
            isValid = false;
        }

        if (isValid) {

            new ApplicationClass().showProgressDialog(progress);

            List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

            Bundle b = new Bundle();
            b.putString("Name", ud_list.get(0).getName());
            b.putString("Email", email);
            b.putString("Country", ud_list.get(0).getCountryCode());
            b.putString("State", ud_list.get(0).getStateCode());
            b.putString("Address", ud_list.get(0).getAddress());
            b.putString("Phone", ud_list.get(0).getPhone());
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION);

            new CallWebServices(Api_Constants.API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION, view.getContext(), true).execute(b);
        }


    }

    private static void setupCustomFont() {

        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
        textview_user_full_name.setTypeface(typeface);
        textview_profile_title.setTypeface(typeface);
        textview_fullname_title.setTypeface(typeface);
        textview_username_title.setTypeface(typeface);
        textview_dob_title.setTypeface(typeface);
        textview_email_title.setTypeface(typeface);
        textview_country_title.setTypeface(typeface);
        textview_state_title.setTypeface(typeface);
        textview_address_title.setTypeface(typeface);
        textview_phone_title.setTypeface(typeface);
        textview_user_email.setTypeface(typeface);

        textview_change_password_title.setTypeface(typeface);
        textview_current_password_title.setTypeface(typeface);
        textview_new_password_title.setTypeface(typeface);
        textview_confirm_password.setTypeface(typeface);

        Typeface typeface_crimson_700 = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/crimson-text-v9-latin-700.ttf");
        textview_user_rank.setTypeface(typeface_crimson_700);

    }

    private static void displayResult() {

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

        Glide.with(view.getContext()).load(ud_list.get(0).getProfilePictureImagePath()).into(imageview_user_profile);

        if (ud_list.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            imageview_user_rank.setImageResource(R.drawable.user_level_bronze);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_bronze));

        } else if (ud_list.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            imageview_user_rank.setImageResource(R.drawable.user_level_gold);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_gold));

        } else {
            imageview_user_rank.setImageResource(R.drawable.user_level_platinum);
            textview_user_rank.setText(view.getContext().getResources().getString(R.string.userrank_platinum));
        }

        textview_user_full_name.setText(ud_list.get(0).getName().toUpperCase());
        textview_user_email.setText(ud_list.get(0).getEmail());

        edittext_fullname.setText(ud_list.get(0).getName());
        edittext_username.setText(ud_list.get(0).getLoginID());
        edittext_dob.setText(ud_list.get(0).getDOB());
        edittext_email.setText(ud_list.get(0).getEmail());

        String tablename_cl = StringUtil.toSQLName("CountryList");
        String fieldname_country_code = StringUtil.toSQLName("countryCode");
        List<CountryList> cl_filter = CountryList.findWithQuery(CountryList.class, "Select * from " + tablename_cl + " where " + fieldname_country_code + " = ?", ud_list.get(0).getCountryCode());

        String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

        String country_name = "";
        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
            String[] split_one = cl_filter.get(0).getCountryName().split(";");
            String[] split_two = split_one[0].split("=");
            country_name = split_two[1];

        } else {
            String[] split_one = cl_filter.get(0).getCountryName().split(";");
            String[] split_two = split_one[1].split("=");
            country_name = split_two[1];
        }

        edittext_country.setText(country_name);

        String tablename_sl = StringUtil.toSQLName("StateList");
        String fieldname_state_code = StringUtil.toSQLName("stateCode");
        List<StateList> sl_filter = StateList.findWithQuery(StateList.class, "Select * from " + tablename_sl + " where " + fieldname_state_code + " = ?", ud_list.get(0).getStateCode());

        String state_name = "";
        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
            String[] split_one = sl_filter.get(0).getStateName().split(";");
            String[] split_two = split_one[0].split("=");
            state_name = split_two[1];

        } else {
            String[] split_one = sl_filter.get(0).getStateName().split(";");
            String[] split_two = split_one[1].split("=");
            state_name = split_two[1];
        }

        edittext_state.setText(state_name);

        edittext_address.setText(ud_list.get(0).getAddress());
        edittext_phone.setText(ud_list.get(0).getPhone());

    }

    private static void callRefreshTokenWebService() {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_REFRESH_TOKEN);

        new CallWebServices(Api_Constants.API_REFRESH_TOKEN, view.getContext(), true).execute(b);
    }


    public static void processWSData(JSONObject returnedObject, int command) {

        new ApplicationClass().closeProgressDialog(progress);

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
                            new ApplicationClass().showProgressDialog(progress);

                            callRefreshTokenWebService();

                        }else{

                        }
                            new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
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
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());
                    }

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } else if (command == Api_Constants.API_MEMBER_UPDATE_ACCOUNT_PROFILE_VALIDATION) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                view.getContext());
                        builder.setTitle(view.getContext().getString(R.string.success_title));
                        builder.setMessage(view.getContext().getResources().getString(R.string.edit_profile_save_profile_success));
                        builder.setPositiveButton(view.getContext().getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        callCountryStateListWebService();
                                        callMemberAccountSettingWebService();
                                    }
                                });

                        builder.show();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());
                    }

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                view.getContext());
                        builder.setTitle(view.getContext().getString(R.string.success_title));
                        builder.setMessage(view.getContext().getResources().getString(R.string.edit_profile_change_password_success));
                        builder.setPositiveButton(view.getContext().getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        callCountryStateListWebService();
                                        callMemberAccountSettingWebService();
                                    }
                                });

                        builder.show();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());
                    }

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext());

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext());
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }else if (command == Api_Constants.API_REFRESH_TOKEN_EDIT_PROFILE) {

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

                        callCountryStateListWebService();
                        callMemberAccountSettingWebService();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(view.getContext(), "key_lang");

                    if (errorCode == 1506) {
                        new DisplayAlertDialog().displayAlertDialogError(1506, view.getContext());
                    } else {
                        if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode,errorMessageEN, false, view.getContext());

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode,errorMessageCN, false, view.getContext());

                        }
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
        String lang = new ApplicationClass().readFromSharedPreferences(activity, KEY_LANG);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
