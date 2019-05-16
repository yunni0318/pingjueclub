package com.wedoops.pingjueclub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoginActivity extends AppCompatActivity {

    private Button button_forgot_password;
    private EditText edittext_username;
    private EditText edittext_password;
    private ImageButton imagebutton_language;
    private ACProgressFlower progress;
    private static final String KEY_LANG = "key_lang"; // preference key


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        progress = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(this.getResources().getString(R.string.loading_please_wait))
                .petalThickness(5)
                .textColor(Color.WHITE)
                .textSize(30)
                .fadeColor(Color.DKGRAY).build();
        loadLanguage();
        setupViewByID();
        setupLanguage();

    }

    private void setupViewByID() {
        button_forgot_password = findViewById(R.id.login_acitivity_button_forgotpassword);
        button_forgot_password.setText(Html.fromHtml("<font color='#AAAAAA'>" + getString(R.string.login_activity_button_forgot_password) + "</font>" + " <font color='#F5CB5A'>" + getString(R.string.login_activity_button_forgot_password_clickhere) + "</font>"));

        edittext_username = findViewById(R.id.edittext_username);
        edittext_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
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
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    button_login_onclick(v);
                    handled = true;
                }
                return handled;
            }
        });

        imagebutton_language = findViewById(R.id.imagebutton_language);

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

        if (username.length() < 1) {
            edittext_username.setError(getString(R.string.login_username_error));
            isValid = false;
        }

        if (password.length() < 1) {
            edittext_password.setError(getString(R.string.login_password_error));
            isValid = false;
        }

        if (isValid) {
            new ApplicationClass().hideSoftKeyboard(this);
            callMemberLoginWebService(username, password);

        }

    }

    private void callMemberLoginWebService(String username, String password) {


        new ApplicationClass().showProgressDialog(progress);

        Bundle b = new Bundle();
        b.putString("USERNAME", username);
        b.putString("PASSWORD", password);
        b.putString("IP_ADDRESS", "");
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_LOGIN);

        new CallWebServices(Api_Constants.API_MEMBER_LOGIN, LoginActivity.this, true).execute(b);
    }

    public void processWSData(JSONObject returnedObject) {

        new ApplicationClass().closeProgressDialog(progress);
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


                    UserDetails ud = new UserDetails(accessToken, refreshToken, Srno, LoginID, Name, DOB, Email, Phone, CountryCode, StateCode, Address, Gender, ProfilePictureImagePath, UserLevelCode, JoinedDate, CashWallet);
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
        }

    }
}
