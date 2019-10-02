package com.wedoops.pingjueclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.adapters.MemberDashboardTopBannerRecyclerAdapter;
import com.wedoops.pingjueclub.adapters.ServiceItemAdapter;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.Services;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.LinePagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    private static View view;
    private static RecyclerView recyclerView,recyclerViewServices,recyclerViewNews;
    private static Runnable runnable;
    public static int position = 0;
    private static final Handler handler = new Handler();
    private static MemberDashboardTopBannerRecyclerAdapter topBanner_adapter;
    private List<Services> services;
    private ServiceItemAdapter serviceItemAdapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.services_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_service_top_banner);

        services=new ArrayList<>();
        services.add(new Services("Entertainment Bar","fsfs"));
        services.add(new Services("Luxury Transport","fsfs"));
        services.add(new Services("Luxury Travel","fsfs"));
        services.add(new Services("Luxury Brand","fsfs"));
        services.add(new Services("Property Services","fsfs"));
        services.add(new Services("Award Services","fsfs"));
        services.add(new Services("Personal Services","fsfs"));
        services.add(new Services("Sport Services","fsfs"));
        services.add(new Services("Premium Lounge","fsfs"));

        setupRecyclerView();
        recyclerViewServices=(RecyclerView)view.findViewById(R.id.recyclerview_service_list);
        recyclerViewNews=(RecyclerView)view.findViewById(R.id.recyclerview_service_new);
        serviceItemAdapter=new ServiceItemAdapter(getContext(),services);
        recyclerViewServices.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerViewServices.setAdapter(serviceItemAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewNews.setLayoutManager(layoutManager);
        recyclerViewNews.setAdapter(serviceItemAdapter);



    }

    public static void setupRecyclerView() {
        String tablename_tb = StringUtil.toSQLName("MemberDashboardTopBanner");
        List<MemberDashboardTopBanner> ud = MemberDashboardTopBanner.findWithQuery(MemberDashboardTopBanner.class, "Select * from " + tablename_tb);
        topBanner_adapter = new MemberDashboardTopBannerRecyclerAdapter(ud);
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

        };
        setupAutoScroll();
        recyclerView.setLayoutManager(top_banner_mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
}
