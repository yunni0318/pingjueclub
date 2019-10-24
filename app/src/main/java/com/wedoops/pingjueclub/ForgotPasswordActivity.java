package com.wedoops.pingjueclub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edittext_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passowrd_activity);

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

        boolean isSuccess = false;
        try {
            isSuccess = returnedObject.getBoolean("Success");

            if (isSuccess) {

                if (returnedObject.getInt("StatusCode") == 200) {

                    JSONObject response_object = returnedObject.getJSONObject("ResponseData");

//                    new DisplayAlertDialog().displayAlertDialogSuccess(response_object.getInt("Code"),this);
                    new DisplayAlertDialog().displayAlertDialogString(0, response_object.getString("MessageEN"), true, this);

                } else {
                    new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), this);

                }

            } else {
                JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
                new DisplayAlertDialog().displayAlertDialogError(errorCode_object.getInt("Code"), this);

            }

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

    }

}

