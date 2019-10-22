package com.wedoops.pingjueclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.ViewTreeObserver;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.NewsItemAdapter;
import com.wedoops.pingjueclub.adapters.ServiceItemAdapter;
import com.wedoops.pingjueclub.adapters.ServicesTopBannerRecyclerAdapter;
import com.wedoops.pingjueclub.database.MemberDashboardEventData;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.ServicesListData;
import com.wedoops.pingjueclub.database.ServicesOtherNewsData;
import com.wedoops.pingjueclub.database.ServicesTopBannerData;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.helper.LinePagerIndicatorDecoration;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ServicesFragment extends Fragment {

    private static final Handler handler = new Handler();
    private int counter = 0;
    public static int position = 0;
    private static View view;
    private static RecyclerView recyclerView, recyclerViewServices, recyclerViewNews;
    private static Runnable runnable;
    private static ServicesTopBannerRecyclerAdapter topBanner_adapter;
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

    private static void setupAutoScroll() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (position == 3) {
                    position = 0;
                }
                recyclerView.smoothScrollToPosition(position);
                position++;
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.services_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callWebService();
        setupDeclaration(view);

    }

    private void callWebService() {
        CustomProgressDialog.showProgressDialog(view.getContext());

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


    private void setupDeclaration(View view) {
        recyclerView = view.findViewById(R.id.recyclerview_service_top_banner);
        recyclerViewServices = view.findViewById(R.id.recyclerview_service_list);
        recyclerViewNews = view.findViewById(R.id.recyclerview_service_new);
    }

    private static void setupRecyclerView() {

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        List<ServicesTopBannerData> stbd_all = ServicesTopBannerData.listAll(ServicesTopBannerData.class);

        topBanner_adapter = new ServicesTopBannerRecyclerAdapter(stbd_all);
        RecyclerView.LayoutManager top_banner_mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(view.getContext()) {
                    private static final float SPEED = 100f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        setupAutoScroll();
        recyclerView.setLayoutManager(top_banner_mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(topBanner_adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    try {
                        position = ((LinearLayoutManager) recyclerView.getLayoutManager())
                                .findFirstVisibleItemPosition();
                    } catch (Exception e) {
                        position = 0;
                    }
                }
            }
        });

        //Paging Feature
        if (recyclerView.getOnFlingListener() == null) {
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(recyclerView);
        }

        //Indicator
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        topBanner_adapter.setOnTopBannerItemClickListener(onTopBannerItemClickListener);
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

        setupRecyclerView();


        ViewCompat.setNestedScrollingEnabled(recyclerViewServices, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewNews, false);

        List<ServicesListData> sld_all = ServicesListData.listAll(ServicesListData.class);
        List<ServicesOtherNewsData> son_all = ServicesOtherNewsData.listAll(ServicesOtherNewsData.class);

        ServiceItemAdapter serviceItemAdapter = new ServiceItemAdapter(view.getContext(), sld_all);
        final NewsItemAdapter newsItemAdapter = new NewsItemAdapter(view.getContext(), son_all);


//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3) {
//            @Override
//            public boolean canScrollHorizontally() {
//                return false;
//            }
//
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//
//        recyclerViewServices.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                recyclerViewServices.getViewTreeObserver().removeOnGlobalLayoutListener(this);
////                int viewWidth = recyclerViewServices.getMeasuredWidth();
////                float cardViewWidth = view.getResources().getDimension(R.dimen.card_width);
////                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
////                gridLayoutManager.setSpanCount(newSpanCount);
//                gridLayoutManager.requestLayout();
//            }
//        });
        recyclerViewServices.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        recyclerViewServices.setNestedScrollingEnabled(false);
        recyclerViewServices.setAdapter(serviceItemAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }

                    @Override
                    public boolean canScrollHorizontally() {
                        return true;
                    }
                };
                recyclerViewNews.setLayoutManager(layoutManager);
                recyclerViewNews.setNestedScrollingEnabled(false);
                recyclerViewNews.setAdapter(newsItemAdapter);
            }
        }, 1000);


    }


    public static void processWSData(JSONObject returnedObject, int command) {
        CustomProgressDialog.closeProgressDialog();

        if (command == Api_Constants.API_SERVICE_PAGE_DETAILS) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONArray service_list_array = response_object.getJSONArray("ServiceList");
                        JSONArray banner_data_array = response_object.getJSONArray("BannerData");
                        JSONArray other_news_array_data = response_object.getJSONArray("OtherNewsData");

                        List<ServicesTopBannerData> stbd_all = ServicesTopBannerData.listAll(ServicesTopBannerData.class);
                        if (stbd_all.size() > 0) {
                            ServicesTopBannerData.deleteAll(ServicesTopBannerData.class);
                        }

                        List<ServicesListData> mded_all = ServicesListData.listAll(ServicesListData.class);
                        if (mded_all.size() > 0) {
                            ServicesListData.deleteAll(ServicesListData.class);
                        }

                        List<ServicesOtherNewsData> sond_all = ServicesOtherNewsData.listAll(ServicesOtherNewsData.class);
                        if (sond_all.size() > 0) {
                            ServicesOtherNewsData.deleteAll(ServicesOtherNewsData.class);
                        }

                        for (int i = 0; i < banner_data_array.length(); i++) {

                            JSONObject bd = banner_data_array.getJSONObject(i);

                            ServicesTopBannerData stbd = new ServicesTopBannerData(String.valueOf(bd.getInt("Srno")), bd.getString("ImagePath"), bd.getString("RedirectURL"), bd.getString("AnnouncementType"), bd.getString("AdminRemarks"), bd.getString("PublishDate"), bd.getBoolean("Active"), bd.getString("CreatedBy"), bd.getString("CreatedDate"));
                            stbd.save();
                        }

                        for (int i = 0; i < service_list_array.length(); i++) {

                            JSONObject sl = service_list_array.getJSONObject(i);

                            ServicesListData sld = new ServicesListData(String.valueOf(sl.getInt("Srno")), String.valueOf(sl.getString("ServiceName")), String.valueOf(sl.getString("ServiceDescription")), String.valueOf(sl.getString("ServiceImagePath")), String.valueOf(sl.getString("Status")), String.valueOf(sl.getString("CreatedDate")));
                            sld.save();
                        }

                        for (int i = 0; i < other_news_array_data.length(); i++) {

                            JSONObject ona = other_news_array_data.getJSONObject(i);

                            ServicesOtherNewsData sond = new ServicesOtherNewsData(String.valueOf(ona.getString("EventGUID")), String.valueOf(ona.getString("EventName")), String.valueOf(ona.getString("EventCategoryCode")), String.valueOf(ona.getString("EventBannerImagePath")), String.valueOf(ona.getDouble("EventPrice")), String.valueOf(ona.getString("EventDescription")), String.valueOf(ona.getInt("TOTALCOUNT")));
                            sond.save();
                        }


                        displayResult();


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), view.getContext());

                    }

                } else {

                    if (returnedObject.getInt("StatusCode") == 401) {
//                        CustomProgressDialog.showProgressDialog(get_context);
//
//                        callRefreshTokenWebService();

                    } else {
//                        JSONObject errorCode_object = returnedObject.getJSONObject("ErrorCode");
//                        new DisplayAlertDialog().displayAlertDialogError(errorCode_object.getInt("Code"), view.getContext());

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

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
    }
}
