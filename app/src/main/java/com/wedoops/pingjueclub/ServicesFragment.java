package com.wedoops.pingjueclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
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
import com.wedoops.pingjueclub.adapters.NewsItemAdapter;
import com.wedoops.pingjueclub.adapters.ServiceItemAdapter;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.News;
import com.wedoops.pingjueclub.database.Services;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.LinePagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    private static final Handler handler = new Handler();
    public static int position = 0;
    private static View view;
    private static RecyclerView recyclerView, recyclerViewServices, recyclerViewNews;
    private static Runnable runnable;
    private static MemberDashboardTopBannerRecyclerAdapter topBanner_adapter;
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
    private List<Services> services;
    private List<News> news;
    private ServiceItemAdapter serviceItemAdapter;
    private NewsItemAdapter newsItemAdapter;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_service_top_banner);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        services = new ArrayList<>();
        services.add(new Services("Entertainment Bar", "fsfs", R.drawable.entertainment));
        services.add(new Services("Luxury Transport", "fsfs", R.drawable.transport));
        services.add(new Services("Luxury Travel", "fsfs", R.drawable.travel));
        services.add(new Services("Luxury Brand", "fsfs", R.drawable.brand));
        services.add(new Services("Property Services", "fsfs", R.drawable.properties));
        services.add(new Services("Award Services", "fsfs", R.drawable.award));
        services.add(new Services("Personal Services", "fsfs", R.drawable.personal));
        services.add(new Services("Sport Services", "fsfs", R.drawable.sporrt));
        services.add(new Services("Premium Lounge", "fsfs", R.drawable.premium));

        news = new ArrayList<>();
        news.add(new News("Self Image Design Course", "You will learn how to improve your self image. Make yourself more confidence!", 11, "image1", R.drawable.entertainment));
        news.add(new News("Self Image Design Course", "You will learn how to improve your self image. Make yourself more confidence!", 12, "image2", R.drawable.entertainment));
        news.add(new News("Self Image Design Course", "You will learn how to improve your self image. Make yourself more confidence!", 13, "image3", R.drawable.entertainment));
        news.add(new News("Self Image Design Course", "You will learn how to improve your self image. Make yourself more confidence!", 14, "image4", R.drawable.entertainment));
        news.add(new News("Self Image Design Course", "You will learn how to improve your self image. Make yourself more confidence!", 15, "image5", R.drawable.entertainment));

        setupRecyclerView();
        recyclerViewServices = (RecyclerView) view.findViewById(R.id.recyclerview_service_list);
        recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerview_service_new);
        ViewCompat.setNestedScrollingEnabled(recyclerViewServices, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewNews, false);
        serviceItemAdapter = new ServiceItemAdapter(getContext(), services);
        newsItemAdapter = new NewsItemAdapter(getContext(), news);
        RecyclerView.LayoutManager lll = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


        recyclerViewServices.setLayoutManager(lll);
        recyclerViewServices.setNestedScrollingEnabled(false);
        recyclerViewServices.setAdapter(serviceItemAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {
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
}
