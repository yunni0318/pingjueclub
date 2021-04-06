package com.wedoops.platinumnobleclub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edittext_email;
    private CustomProgressDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passowrd_activity);

        customDialog = new CustomProgressDialog();

//        getSupportActionBar().hide();

        setupViewById();
    }

    private void setupViewById() {
        edittext_email = findViewById(R.id.edittext_email);
    }

    public void button_forgot_password_onclick(View v) {

        String email = edittext_email.getText().toString();

        boolean isValid = true;
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textview_sidebar));

        if (email.length() < 1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.forgotpassword_email_error));
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, getResources().getString(R.string.forgotpassword_email_error).length(), 0);
            edittext_email.setError(spannableStringBuilder);
            isValid = false;
        }

        if (!ApplicationClass.isEmailValid(email)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.forgotpassword_email_invalid_error));
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, getResources().getString(R.string.forgotpassword_email_invalid_error).length(), 0);
            edittext_email.setError(spannableStringBuilder);
            isValid = false;
        }

        if (isValid) {
            customDialog.showDialog(this);
            callForgotPassword(email);

        }

    }

    private void callForgotPassword(String email) {

        Bundle b = new Bundle();
        b.putString("email", email);

        b.putInt(Api_Constants.COMMAND, Api_Constants.API_FORGOT_PASSWORD);

        new CallWebServices(Api_Constants.API_FORGOT_PASSWORD, ForgotPasswordActivity.this, true).execute(b);
    }

    public void processWSData(JSONObject returnedObject) {
        customDialog.hideDialog();
        boolean isSuccess = false;
        try {
            isSuccess = returnedObject.getBoolean("Success");

            if (isSuccess) {

                if (returnedObject.getInt("StatusCode") == 200) {

                    JSONArray response_array = returnedObject.getJSONArray("ResponseData");

//                    new DisplayAlertDialog().displayAlertDialogSuccess(response_object.getInt("Code"),this);
                    new DisplayAlertDialog().displayAlertDialogString(0, response_array.getJSONObject(0).getString("MessageEN"), true, this, this);

                } else {
                    new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this, this);

                }

            } else {
//                JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
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

                if (errorCode == 1506) {
                    new DisplayAlertDialog().displayAlertDialogError(1506, this, this);
                } else {
                    if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, this, this);

                    } else {
                        new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, this, this);

                    }
                }

            }

        } catch (Exception e) {
            Log.e("Error", e.toString());
            new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again",
                    false, this, this);

        }

    }

    public void button_back(View view){
        this.onBackPressed();
    }

}

