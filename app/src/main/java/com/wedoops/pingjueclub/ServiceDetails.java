package com.wedoops.pingjueclub;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.ServiceDetailsItemAdapter;
import com.wedoops.pingjueclub.adapters.ServiceSubItemAdapter;
import com.wedoops.pingjueclub.database.ServicesMerchantDetails;
import com.wedoops.pingjueclub.database.SubServicesListData;
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

    private static CustomProgressDialog customDialog;
    //    private String subservice_id, subservice_name;
    private String mainservicesid, mainservicesname;
    private RecyclerView recyclerview;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);


        Intent intent = getIntent();
//        subservice_id = intent.getExtras().getString("subservice_id");
//        subservice_name = intent.getExtras().getString("subservice_name");
        mainservicesid = intent.getStringExtra("main_services_id");
        mainservicesname = intent.getStringExtra("main_services_name");
        setupDeclaration();
        callWebService();

    }

    private void setupDeclaration() {
        customDialog = new CustomProgressDialog();
        recyclerview = findViewById(R.id.recyclerview);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);
//        toolbar_title.setText(subservice_name);
        toolbar_title.setText(mainservicesname);


        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
//        String tablename_ssld = StringUtil.toSQLName("ServicesMerchantDetails");

        List<ServicesMerchantDetails> smd_list_all = ServicesMerchantDetails.listAll(ServicesMerchantDetails.class);

        RecyclerView.LayoutManager sub_mLayoutManager = new LinearLayoutManager(this);
        ServiceDetailsItemAdapter adapter = new ServiceDetailsItemAdapter(this, smd_list_all);

        recyclerview.setLayoutManager(sub_mLayoutManager);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(adapter);
    }

    private void callWebService() {
        customDialog.showDialog(this);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);
        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");
        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
//        b.putString("ServicesID", subservice_id);
        b.putString("ServicesID", mainservicesid);

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

                    List<ServicesMerchantDetails> smd_listall = ServicesMerchantDetails.listAll(ServicesMerchantDetails.class);
                    if (smd_listall.size() > 0) {
                        ServicesMerchantDetails.deleteAll(ServicesMerchantDetails.class);
                    }

                    for (int i = 0; i < response_array.length(); i++) {
                        ServicesMerchantDetails smd = new ServicesMerchantDetails(response_array.getJSONObject(i).getString("Srno"), response_array.getJSONObject(i).getString("MerchantID"), response_array.getJSONObject(i).getString("CompanyRegisterNo"), response_array.getJSONObject(i).getString("CompanyCategory"), response_array.getJSONObject(i).getString("CompanyName"), response_array.getJSONObject(i).getString("CompanyName"), response_array.getJSONObject(i).getString("CompanyLatX"), response_array.getJSONObject(i).getString("CompanyLatY"), response_array.getJSONObject(i).getString("CompanyDescription"), response_array.getJSONObject(i).getString("CompanyLogo"), response_array.getJSONObject(i).getString("CompanyImages"), response_array.getJSONObject(i).getString("Status"));
                        smd.save();
                    }

                    setupRecyclerView();

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
