package com.wedoops.platinumnobleclub;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    private TextView button_forgot_password;
    private EditText edittext_username;
    private EditText edittext_password;
    private ImageButton imagebutton_language;
    //    private ACProgressCustom progress;
    private static final String KEY_LANG = "key_lang"; // preference key
    private String token = "";
    private static CustomProgressDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        customDialog = new CustomProgressDialog();
//        progress = new ACProgressCustom.Builder(this)
//                .useImages(R.drawable.pj_loading_1, R.drawable.pj_loading_2)
//                .speed(4)
//                .build();
        loadLanguage();
        setupViewByID();
        setupLanguage();

    }

    private void setupViewByID() {
        button_forgot_password = findViewById(R.id.login_acitivity_button_forgotpassword);
        //button_forgot_password.setText(Html.fromHtml("<font color='#AAAAAA'>" + getString(R.string.login_activity_button_forgot_password) + "</font>" + " <font color='#F5CB5A'>" + getString(R.string.login_activity_button_forgot_password_clickhere) + "</font>"));

        edittext_username = findViewById(R.id.edittext_username);
        edittext_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edittext_password.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        edittext_password = findViewById(R.id.edittext_password);
        edittext_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    button_login_onclick(v);
                    handled = true;
                }
                return handled;
            }
        });

        imagebutton_language = findViewById(R.id.imagebutton_language);

        button_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupLanguage() {

        if (getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us")) {
            imagebutton_language.setImageResource(R.drawable.language_usa);
        } else {
            imagebutton_language.setImageResource(R.drawable.language_china);
        }
    }

    public void imagebutton_change_language_onclick(View v) {

        String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        if (currentLanguage.equals("en_us")) {
            saveLanguage("zh");
        } else {
            saveLanguage("en_us");

        }
    }

    private void saveLanguage(String lang) {

        new ApplicationClass().writeIntoSharedPreferences(this, KEY_LANG, lang);

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        finish();

        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);

    }

    private void loadLanguage() {
        String lang = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void button_forgot_password_onclick(View v) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void button_login_onclick(View v) {

        String username = edittext_username.getText().toString();
        String password = edittext_password.getText().toString();

        boolean isValid = true;
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textview_sidebar));

        if (username.length() < 1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.login_username_error));
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, getResources().getString(R.string.login_username_error).length(), 0);
            edittext_username.setError(spannableStringBuilder);
            isValid = false;
        }

//        if (!ApplicationClass.isEmailValid(username)) {
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.forgotpassword_email_invalid_error));
//            spannableStringBuilder.setSpan(foregroundColorSpan, 0, getResources().getString(R.string.forgotpassword_email_invalid_error).length(), 0);
//            username.setError(spannableStringBuilder);
//            isValid = false;
//        }


        if (password.length() < 1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.login_password_error));
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, getResources().getString(R.string.login_password_error).length(), 0);
            edittext_password.setError(spannableStringBuilder);
            isValid = false;
        }

        if (isValid) {
            new ApplicationClass().hideSoftKeyboard(this);
            callMemberLoginWebService(username, password);

        }

    }

    private void callMemberLoginWebService(final String username, final String password) {
//        progress.show();
        customDialog.showDialog(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        Bundle b = new Bundle();
                        b.putString("USERNAME", username);
                        b.putString("PASSWORD", password);
                        b.putString("DEVICEID", token);
                        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_LOGIN_V2);

                        new CallWebServices(Api_Constants.API_MEMBER_LOGIN_V2, LoginActivity.this, true).execute(b);

                    }
                });


    }

    public void processWSData(JSONObject returnedObject, int command) {

//        progress.dismiss();
        customDialog.hideDialog();

        if (command == Api_Constants.API_MEMBER_LOGIN_V2) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONObject login_access_object = response_object.getJSONObject("UserLoginAccessToken");
                        JSONObject user_profile_object = response_object.getJSONObject("UserQuickProfile");

                        String accessToken = login_access_object.getString("AccessToken");
                        String refreshToken = login_access_object.getString("RefreshToken");

                        String Srno = String.valueOf(user_profile_object.getInt("Srno"));
                        String LoginID = user_profile_object.getString("LoginID");
                        String Name = user_profile_object.getString("Name");
                        String NickName = user_profile_object.getString("NickName");
                        String DOB = user_profile_object.getString("DOB");
                        String Email = user_profile_object.getString("Email");
                        String Phone = user_profile_object.getString("Phone");
                        String CountryCode = user_profile_object.getString("CountryCode");
                        String StateCode = user_profile_object.getString("StateCode");
                        String Address = user_profile_object.getString("Address");
                        String Gender = user_profile_object.getString("Gender");
                        String ProfilePictureImagePath = user_profile_object.getString("ProfilePictureImagePath");
                        String UserLevelCode = user_profile_object.getString("UserLevelCode");
                        String JoinedDate = user_profile_object.getString("JoinedDate");
                        String CashWallet = String.valueOf(user_profile_object.getString("CashWallet"));
                        boolean gotnewtopup = user_profile_object.getBoolean("GotNewTopUp");


                        UserDetails ud = new UserDetails(accessToken, refreshToken, Srno, LoginID, Name, NickName, DOB, Email, Phone, CountryCode, StateCode, Address, Gender, ProfilePictureImagePath, UserLevelCode, JoinedDate, CashWallet,gotnewtopup);
                        ud.save();

                        new ApplicationClass(this).writeIntoSharedPreferences(this, CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_PASSWORD, edittext_password.getText().toString());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this);

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

                    String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, "key_lang");

                    if (currentLanguage.equals("en_us") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this);

                    }

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, this);
            }
        }


    }
}
