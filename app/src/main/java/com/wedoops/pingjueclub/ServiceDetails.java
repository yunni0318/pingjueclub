package com.wedoops.pingjueclub;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.database.ServicesMerchantDetails;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ServiceDetails extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private static CustomProgressDialog customDialog;
    private String subservice_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);


        Intent intent = getIntent();
        subservice_id = intent.getExtras().getString("subservice_id");

        setupDeclaration();
        callWebService();

    }

    private void setupDeclaration() {
        customDialog = new CustomProgressDialog();

        textView = findViewById(R.id.service_detail_text);
        imageView = findViewById(R.id.service_detail_iamge);
    }

    private void callWebService() {
        customDialog.showDialog(this);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);
        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");
        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("ServicesID", subservice_id);
        b.putString("access_token", ud_list.get(0).getAccessToken());


        b.putInt(Api_Constants.COMMAND, Api_Constants.API_SERVICE_MERCHANT_LIST);

        new CallWebServices(Api_Constants.API_SERVICE_MERCHANT_LIST, ServiceDetails.this, true).execute(b);
    }

    public void processWSData(JSONObject returnedObject) {

        customDialog.hideDialog();
        boolean isSuccess = false;
        try {
            isSuccess = returnedObject.getBoolean("Success");

            if (isSuccess) {

                if (returnedObject.getInt("StatusCode") == 200) {

//                    JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                    JSONArray response_array = returnedObject.getJSONArray("ResponseData");

                    for (int i = 0; i < response_array.length(); i++) {
                        ServicesMerchantDetails smd = new ServicesMerchantDetails(response_array.getJSONObject(i).getString("Srno"), response_array.getJSONObject(i).getString("MerchantID"), response_array.getJSONObject(i).getString("CompanyRegisterNo"), response_array.getJSONObject(i).getString("CompanyCategory"), response_array.getJSONObject(i).getString("CompanyName"), response_array.getJSONObject(i).getString("CompanyName"), response_array.getJSONObject(i).getString("CompanyLatX"), response_array.getJSONObject(i).getString("CompanyLatY"), response_array.getJSONObject(i).getString("CompanyDescription"), response_array.getJSONObject(i).getString("CompanyLogo"), response_array.getJSONObject(i).getString("CompanyImages"), response_array.getJSONObject(i).getString("Status"));
                        smd.save();
                    }

                    Log.d("SUCCESS", "")
                    ;
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
