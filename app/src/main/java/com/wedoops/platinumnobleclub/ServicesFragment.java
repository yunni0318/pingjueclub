package com.wedoops.platinumnobleclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.adapters.ServiceItemAdapter;
import com.wedoops.platinumnobleclub.database.MemberDashboardEventData;
import com.wedoops.platinumnobleclub.database.MemberDashboardTopBanner;
import com.wedoops.platinumnobleclub.database.ServicesListData;
import com.wedoops.platinumnobleclub.database.SubServicesListData;
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

public class ServicesFragment extends Fragment {

    private static final Handler handler = new Handler();
    private static int counter = 0;
    public static int position = 0;
    private static View view;
    private static RecyclerView recyclerViewServices;
    private static Runnable runnable;
    private static CustomProgressDialog customDialog;
    private static Context get_context;
    private static Activity get_activity;
    private static CardView cardview_customer_service;
    private static ImageView contact_butler_image;
    private static AlertDialog alert;
    private static String lang;
    private static final String KEY_LANG = "key_lang";

    private static View.OnClickListener onTopBannerItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            List<MemberDashboardTopBanner> ed = MemberDashboardTopBanner.listAll(MemberDashboardTopBanner.class);

            if (ed.get(position).getAnnouncementType().equals(CONSTANTS_VALUE.EVENT_TOP_BANNER_NEWTRIP)) {
                Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
                intent.putExtra("eventGUID", ed.get(position).getRedirectURL());
                view.getContext().startActivity(intent);
            } else {
                String url = ed.get(position).getRedirectURL();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }
        }
    };

    private static View.OnClickListener onServiceItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            List<ServicesListData> sld_all = ServicesListData.listAll(ServicesListData.class);

            Intent intent = new Intent(get_activity, ServiceDetails.class);
            intent.putExtra("main_services_id", sld_all.get(position).getMainServicesID());
            String[] service_split = sld_all.get(position).getMainServiceName().split("\\|");

            if (service_split.length > 1) {
                if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
                    intent.putExtra("main_services_name", service_split[0]);
                } else {
                    intent.putExtra("main_services_name", service_split[1]);
                }
            }
            get_activity.startActivity(intent);

        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.services_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customDialog = new CustomProgressDialog();
        get_context = view.getContext();
        lang = new ApplicationClass().readFromSharedPreferences(get_context, KEY_LANG);
        callWebService();
        setupDeclaration(view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        get_activity = getActivity();
    }

    private static void callWebService() {
//        CustomProgressDialog.showProgressDialog(view.getContext());
        customDialog.showDialog(get_context);

        if (counter < 4) {
            counter++;

            List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());


            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_SERVICE_PAGE_DETAILS);

            new CallWebServices(Api_Constants.API_SERVICE_PAGE_DETAILS, view.getContext(), true).execute(b);

        } else {
            displayResult();
        }
    }

    private static void callRefreshTokenWebService() {
        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("refresh_token", ud_list.get(0).getRefreshToken());
        b.putInt(Api_Constants.COMMAND, RefreshTokenAPI.ORIGIN_SERVICE_PAGE_DETAIL);

//        new CallWebServices(Api_Constants.API_REFRESH_TOKEN, view.getContext(), true).execute(b);
        new CallRefreshToken(RefreshTokenAPI.ORIGIN_SERVICE_PAGE_DETAIL, view.getContext(), get_activity, RefreshTokenAPI.ORIGIN_SERVICE_PAGE_DETAIL).execute(b);

    }


    private void setupDeclaration(View view) {
        recyclerViewServices = view.findViewById(R.id.recyclerview_service_list);
        cardview_customer_service = view.findViewById(R.id.cardview_customer_service);
        contact_butler_image = view.findViewById(R.id.imageview_customer_service);
        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            contact_butler_image.setImageResource(R.drawable.administrator_services_icon);
        } else {
            contact_butler_image.setImageResource(R.drawable.administrator_services_chinese_icon);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }


    private static void displayResult() {

        ViewCompat.setNestedScrollingEnabled(recyclerViewServices, false);

        List<ServicesListData> sld_all = ServicesListData.listAll(ServicesListData.class);

        ServiceItemAdapter serviceItemAdapter = new ServiceItemAdapter(view.getContext(), sld_all);

        serviceItemAdapter.setOnServiceItemClickListener(onServiceItemClickListener);
        recyclerViewServices.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        recyclerViewServices.setNestedScrollingEnabled(false);
        recyclerViewServices.setAdapter(serviceItemAdapter);

        cardview_customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View askDialog = get_activity.getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(get_context);

                TextView textview_title = askDialog.findViewById(R.id.textview_title);
                TextView textview_message = askDialog.findViewById(R.id.textview_message);
                Button button_cancel = askDialog.findViewById(R.id.button_cancel);
                Button button_ok = askDialog.findViewById(R.id.button_ok);

                textview_title.setText("ATTENTION");
                textview_message.setText("This will open an url to message Administrator via Whatsapp.");
                builder.setView(askDialog);

                builder.setCancelable(false);

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });


                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert.dismiss();
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/601117938935"));
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/601117938935?text=Welcome%20to%20Platinum%20Noble%20Club%2C%20thanks%20for%20contacting%20our%20butler%20service.%20How%20may%20I%20assist%20you%3F%20(%20Services%20)"));

                        view.getContext().startActivity(browserIntent);
                    }
                });

                alert = builder.create();
                alert.show();
            }
        });


    }

    public static void processWSData(JSONObject returnedObject, int command) {
//        CustomProgressDialog.closeProgressDialog();
        customDialog.hideDialog();

        if (command == Api_Constants.API_SERVICE_PAGE_DETAILS) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {
                        counter = 0;
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONArray service_list_array = response_object.getJSONArray("ServiceList");
//                        JSONArray banner_data_array = response_object.getJSONArray("BannerData");
//                        JSONArray other_news_array_data = response_object.getJSONArray("OtherNewsData");

                        List<ServicesListData> mded_all = ServicesListData.listAll(ServicesListData.class);
                        if (mded_all.size() > 0) {
                            ServicesListData.deleteAll(ServicesListData.class);
                        }

                        List<SubServicesListData> ssld_all = SubServicesListData.listAll(SubServicesListData.class);
                        if (ssld_all.size() > 0) {
                            SubServicesListData.deleteAll(SubServicesListData.class);
                        }


                        for (int i = 0; i < service_list_array.length(); i++) {

                            JSONObject sl = service_list_array.getJSONObject(i);

                            ServicesListData sld = new ServicesListData(sl.getString("MainServicesID"), sl.getString("MainServiceName"), sl.getString("MainServiceImagePath"));
                            sld.save();


                            JSONArray sub_service_list_array = sl.getJSONArray("SubServiceList");

                            for (int j = 0; j < sub_service_list_array.length(); j++) {
                                String n = sub_service_list_array.getJSONObject(j).getString("SubServicesID");
                                if (!n.equals("null")) {

                                    SubServicesListData ssld = new SubServicesListData(sl.getString("MainServicesID"), sub_service_list_array.getJSONObject(j).getString("SubServicesID"), sub_service_list_array.getJSONObject(j).getString("SubServiceName"), sub_service_list_array.getJSONObject(j).getString("SubServiceImagePath"));
                                    ssld.save();
                                }
                            }

                        }

                        displayResult();


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

                    }

                } else {

                    if (returnedObject.getInt("StatusCode") == 401) {
//                        CustomProgressDialog.showProgressDialog(view.getContext());
                        customDialog.showDialog(get_context);

                        callRefreshTokenWebService();

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

                        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext(), get_activity);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext(), get_activity);

                        }

                    }

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        } else if (command == RefreshTokenAPI.ORIGIN_SERVICE_PAGE_DETAIL) {

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

                        callWebService();

                    } else {

                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext(), get_activity);

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
                        new DisplayAlertDialog().displayAlertDialogError(1506, view.getContext(), get_activity);
                    } else {
                        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageEN, false, view.getContext(), get_activity);

                        } else {
                            new DisplayAlertDialog().displayAlertDialogString(errorCode, errorMessageCN, false, view.getContext(), get_activity);

                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, view.getContext(), get_activity);

            }
        }
    }
}
