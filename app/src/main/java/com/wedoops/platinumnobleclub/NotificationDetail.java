package com.wedoops.platinumnobleclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;

import org.json.JSONObject;

import java.util.List;

public class NotificationDetail extends AppCompatActivity {

    private static TextView textview_inbox_title, textview_inbox_detail;
    private static ImageView imageview_inbox;
    private static int inboxId;
    private static NotificationDetail app;
    private static CustomProgressDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        app = NotificationDetail.this;
        customDialog = new CustomProgressDialog();
        setupDeclaration();
        callWebService();
    }

    private void setupDeclaration() {

        Intent intent = getIntent();
        inboxId = intent.getIntExtra("inboxID", 0);

        imageview_inbox = findViewById(R.id.imageview_inbox);
        textview_inbox_title = findViewById(R.id.textview_inbox_title);
        textview_inbox_detail = findViewById(R.id.textview_inbox_detail);
    }

    private void callWebService() {

        customDialog.showDialog(app);
        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putInt("inboxId", inboxId);
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_InboxDetail);

        new CallWebServices(Api_Constants.API_InboxDetail, app, true).execute(b);
    }

    public static void processWSData(JSONObject returnedObject, int command) {

        if (command == Api_Constants.API_InboxDetail) {
            boolean isSuccess = false;
            try {

                isSuccess = returnedObject.getBoolean("Success");
                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");

                        int boxid = response_object.getInt("id");
                        String LoginID = response_object.getString("LoginID");
                        String Title = response_object.getString("Title");
                        String Body = response_object.getString("Body");
                        String ImagePath = response_object.getString("ImagePath");
                        String Status = response_object.getString("Status");
                        boolean IsView = response_object.getBoolean("IsView");
                        String Date = response_object.getString("Date");


                        textview_inbox_title.setText(Title);
                        textview_inbox_detail.setText(Body);
                        Glide.with(app)
                                .load(ImagePath)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .timeout(10000)
                                .into(imageview_inbox);

                    }
                    customDialog.hideDialog();
                }

            } catch (Exception e) {
                customDialog.hideDialog();
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, app, app);

            }
        }
    }

    public void button_back(View v) {
        this.onBackPressed();
    }
}
