package com.wedoops.platinumnobleclub.adapters;

import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.MemberDashboardEventData;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MemberDashboardEventDataRecyclerAdapter extends RecyclerView.Adapter<MemberDashboardEventDataRecyclerAdapter.MyViewHolder> {
    private List<MemberDashboardEventData> ed;
    private View.OnClickListener mOnItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageview_evenetdata_detail;
        public TextView textview_upfront_payment;
        public TextView textview_event_start_date;
        public TextView textview_event_end_date;
        public TextView textview_join_trip_amount;
        public ImageView imageview_user_rank_bronze, imageview_user_rank_silver, imageview_user_rank_gold, imageview_user_rank_platinum;
        public TextView textview_event_name;
        public TextView textview_event_price;

        public MyViewHolder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
            imageview_evenetdata_detail = v.findViewById(R.id.imageview_evenetdata_detail);
            textview_upfront_payment = v.findViewById(R.id.textview_upfront_payment);
            textview_event_start_date = v.findViewById(R.id.textview_event_start_date);
            textview_event_end_date = v.findViewById(R.id.textview_event_end_date);
            textview_join_trip_amount = v.findViewById(R.id.textview_join_trip_amount);
            imageview_user_rank_bronze = v.findViewById(R.id.imageview_user_rank_bronze);
            imageview_user_rank_silver = v.findViewById(R.id.imageview_user_rank_silver);
            imageview_user_rank_gold = v.findViewById(R.id.imageview_user_rank_gold);
            imageview_user_rank_platinum = v.findViewById(R.id.imageview_user_rank_platinum);
            textview_event_name = v.findViewById(R.id.textview_event_name);
            textview_event_price = v.findViewById(R.id.textview_event_price);


            Typeface typeface = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
            textview_upfront_payment.setTypeface(typeface);
            textview_event_start_date.setTypeface(typeface);
            textview_event_end_date.setTypeface(typeface);
            textview_join_trip_amount.setTypeface(typeface);

            Typeface typeface_bold_500 = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-500.ttf");
            textview_event_price.setTypeface(typeface_bold_500);

            Typeface typeface_bold_700 = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-700.ttf");
            textview_event_name.setTypeface(typeface_bold_700);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MemberDashboardEventDataRecyclerAdapter(List<MemberDashboardEventData> ed) {
        this.ed = ed;
    }

    @NonNull
    @Override
    public MemberDashboardEventDataRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_member_dashboard_eventdata, viewGroup, false);

        return new MemberDashboardEventDataRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final ViewTreeObserver observer = myViewHolder.imageview_evenetdata_detail.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    myViewHolder.imageview_evenetdata_detail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    myViewHolder.imageview_evenetdata_detail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int containerWidth = myViewHolder.imageview_evenetdata_detail.getWidth();
                int imageViewHeight = containerWidth / 2;

                // set fixed height to the imageView
                ViewGroup.LayoutParams imgParams = myViewHolder.imageview_evenetdata_detail.getLayoutParams();
                imgParams.height = imageViewHeight;
                myViewHolder.imageview_evenetdata_detail.setLayoutParams(imgParams);

                int cWidth = myViewHolder.itemView.getWidth();
                int textview_width = cWidth / 2;

                ViewGroup.LayoutParams textviewParams = myViewHolder.textview_event_name.getLayoutParams();
                textviewParams.width = textview_width;
                myViewHolder.textview_event_name.setLayoutParams(textviewParams);


            }
        });

        Double d_upfrontrate = ed.get(i).getEventUpfrontRate();
//        Double d_eventprice = ed.get(i).getEventPrice();

        BigDecimal bd_event_price = new BigDecimal(ed.get(i).getEventPrice());
        bd_event_price = bd_event_price.setScale(4, BigDecimal.ROUND_HALF_UP);
        bd_event_price = bd_event_price.setScale(3, BigDecimal.ROUND_DOWN);
        bd_event_price = bd_event_price.setScale(2, BigDecimal.ROUND_HALF_UP);

        String upfront_value = String.valueOf(d_upfrontrate.intValue());
        String eventprice_value = String.valueOf(bd_event_price.doubleValue());

        String startdate = ed.get(i).getEventStartDate();
        String enddate = ed.get(i).getEventEndDate();
        String new_startdate = "";
        String new_enddate = "";

        if (myViewHolder.textview_event_start_date.getContext().getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us") || myViewHolder.textview_event_start_date.getContext().getResources().getConfiguration().locale.toString().toLowerCase().equals("en_gb") || myViewHolder.textview_event_start_date.getContext().getResources().getConfiguration().locale.toString().toLowerCase().equals("")) {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy hha", Locale.US);
                outFormat.setTimeZone(tz);
                new_startdate = outFormat.format(new_date_startDate);
                new_enddate = outFormat.format(new_date_endDate);

            } catch (Exception e) {
                try {

                    TimeZone tz = TimeZone.getTimeZone("SGT");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(tz);
                    Date new_date_startDate = format.parse(startdate);
                    Date new_date_endDate = format.parse(enddate);

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy hha", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);
                } catch (Exception ee) {

                }
            }
        } else {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy hha", Locale.US);
                outFormat.setTimeZone(tz);
                new_startdate = outFormat.format(new_date_startDate);
                new_enddate = outFormat.format(new_date_endDate);

            } catch (Exception e) {

                try {

                    TimeZone tz = TimeZone.getTimeZone("SGT");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(tz);
                    Date new_date_startDate = format.parse(startdate);
                    Date new_date_endDate = format.parse(enddate);

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy hha", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);

                } catch (Exception ee) {

                }
            }
        }

        String[] splited_startdate = new_startdate.split(" ");
        String[] splited_enddate = new_enddate.split(" ");

        String final_startDate = String.format(myViewHolder.textview_event_start_date.getContext().getString(R.string.member_dashboard_eventstartdate), splited_startdate[0], splited_startdate[1], splited_startdate[2]);
        String final_endDate = String.format(myViewHolder.textview_event_end_date.getContext().getString(R.string.member_dashboard_eventenddate), splited_enddate[0], splited_enddate[1], splited_enddate[2]);


//        int imageViewHeight =
//
//        // set fixed height to the imageView
//        ViewGroup.LayoutParams imgParams = myViewHolder.imageview_evenetdata_detail.getLayoutParams();
//        imgParams.height = imageViewHeight;
//        myViewHolder.imageview_evenetdata_detail.setLayoutParams(imgParams);


        Glide.with(myViewHolder.imageview_evenetdata_detail.getContext())
                .load(ed.get(i).getEventBannerImagePath()).placeholder(R.drawable.loading)
                .timeout(10000)
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.TOP)))
                .into(myViewHolder.imageview_evenetdata_detail);
        myViewHolder.textview_upfront_payment.setText(String.format("%s%% UPFRONT", upfront_value));

//        if (final_startDate.equals(final_endDate)) {
//            myViewHolder.textview_event_date.setText(String.format("%s", final_endDate + " (" + splited_enddate[3] + ")"));
//
//        } else {
//            myViewHolder.textview_event_date.setText(String.format("%s - %s", final_startDate + " (" + splited_startdate[3] + ")", final_endDate + " (" + splited_enddate[3] + ")"));
//
//        }

        if (final_startDate.equals(final_endDate)) {
            myViewHolder.textview_event_start_date.setText("-");
            myViewHolder.textview_event_end_date.setText(String.format("%s", final_endDate + " (" + splited_enddate[3] + ")"));
        } else {
            myViewHolder.textview_event_start_date.setText(String.format("%s", final_startDate + " (" + splited_startdate[3] + ")"));
            myViewHolder.textview_event_end_date.setText(String.format("%s", final_endDate + " (" + splited_enddate[3] + ")"));
        }

        myViewHolder.textview_join_trip_amount.setText(String.format("%s/%s", ed.get(i).getReservedSeat(), ed.get(i).getMaxParticipant()));
        myViewHolder.textview_event_name.setText(ed.get(i).getEventName());
        myViewHolder.textview_event_price.setText(String.format("%s PTS", eventprice_value));

        myViewHolder.imageview_user_rank_bronze.setVisibility(View.GONE);
        myViewHolder.imageview_user_rank_silver.setVisibility(View.GONE);
        myViewHolder.imageview_user_rank_gold.setVisibility(View.GONE);
        myViewHolder.imageview_user_rank_platinum.setVisibility(View.GONE);

        if (ed.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            myViewHolder.imageview_user_rank_bronze.setVisibility(View.VISIBLE);
        }

        if (ed.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_SILVER)) {
            myViewHolder.imageview_user_rank_silver.setVisibility(View.VISIBLE);
        }

        if (ed.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            myViewHolder.imageview_user_rank_gold.setVisibility(View.VISIBLE);
        }

        if (ed.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
            myViewHolder.imageview_user_rank_platinum.setVisibility(View.VISIBLE);
        }

    }

    public void setOnEventDataItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return ed.size();
    }
}
