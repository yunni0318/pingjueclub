package com.wedoops.pingjueclub.adapters;

import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.MyBookingList;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder> {
    private List<MyBookingList> mbl;
    private View.OnClickListener mOnItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageview_my_booking_detail;
        public TextView textview_upfront_payment;
        public TextView textview_event_date;
        public TextView textview_join_trip_amount;
        public ImageView imageview_user_rank_bronze, imageview_user_rank_gold, imageview_user_rank_platinum;
        public TextView textview_event_name;
        public TextView textview_event_price;

        public MyViewHolder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnItemClickListener);
            imageview_my_booking_detail = v.findViewById(R.id.imageview_my_booking_detail);
            textview_upfront_payment = v.findViewById(R.id.textview_upfront_payment);
            textview_event_date = v.findViewById(R.id.textview_event_date);
            textview_join_trip_amount = v.findViewById(R.id.textview_join_trip_amount);
            imageview_user_rank_bronze = v.findViewById(R.id.imageview_user_rank_bronze);
            imageview_user_rank_gold = v.findViewById(R.id.imageview_user_rank_gold);
            imageview_user_rank_platinum = v.findViewById(R.id.imageview_user_rank_platinum);
            textview_event_name = v.findViewById(R.id.textview_event_name);
            textview_event_price = v.findViewById(R.id.textview_event_price);

            Typeface typeface = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
            textview_upfront_payment.setTypeface(typeface);
            textview_event_date.setTypeface(typeface);
            textview_join_trip_amount.setTypeface(typeface);

            Typeface typeface_bold_500 = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-500.ttf");
            textview_event_price.setTypeface(typeface_bold_500);

            Typeface typeface_bold_700 = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/poppins-v6-latin-700.ttf");
            textview_event_name.setTypeface(typeface_bold_700);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyBookingAdapter(List<MyBookingList> mbl) {
        this.mbl = mbl;
    }

    @NonNull
    @Override
    public MyBookingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_my_booking, viewGroup, false);

        return new MyBookingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBookingAdapter.MyViewHolder myViewHolder, int i) {

        final ViewTreeObserver observer = myViewHolder.imageview_my_booking_detail.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    myViewHolder.imageview_my_booking_detail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    myViewHolder.imageview_my_booking_detail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int containerWidth = myViewHolder.imageview_my_booking_detail.getWidth();

                int imageViewHeight = containerWidth / 2;

                // set fixed height to the imageView
                ViewGroup.LayoutParams imgParams = myViewHolder.imageview_my_booking_detail.getLayoutParams();
                imgParams.height = imageViewHeight;
                myViewHolder.imageview_my_booking_detail.setLayoutParams(imgParams);

            }
        });

        Double d_upfrontrate = mbl.get(i).getEventUpfrontRate();
        Double d_eventprice = mbl.get(i).getEventPrice();

        String upfront_value = String.valueOf(d_upfrontrate.intValue());
        String eventprice_value = String.valueOf(d_eventprice.intValue());

        String startdate = mbl.get(i).getEventStartDate();
        String enddate = mbl.get(i).getEventEndDate();
        String new_startdate = "";
        String new_enddate = "";


        if (myViewHolder.textview_event_date.getContext().getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us")) {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
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

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);

                } catch (Exception ex) {
                    Log.e("Date", e.toString());

                }

            }
        } else {
            try {
                TimeZone tz = TimeZone.getTimeZone("SGT");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
                format.setTimeZone(tz);
                Date new_date_startDate = format.parse(startdate);
                Date new_date_endDate = format.parse(enddate);

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
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

                    SimpleDateFormat outFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
                    outFormat.setTimeZone(tz);
                    new_startdate = outFormat.format(new_date_startDate);
                    new_enddate = outFormat.format(new_date_endDate);

                } catch (Exception ex) {
                    Log.e("Date", e.toString());

                }
            }
        }

        String[] splited_startdate = new_startdate.split(" ");
        String[] splited_enddate = new_enddate.split(" ");

        String final_startDate = String.format(myViewHolder.textview_event_date.getContext().getString(R.string.member_dashboard_eventstartdate), splited_startdate[0], splited_startdate[1], splited_startdate[2]);
        String final_endDate = String.format(myViewHolder.textview_event_date.getContext().getString(R.string.member_dashboard_eventenddate), splited_enddate[0], splited_enddate[1], splited_enddate[2]);

        Glide.with(myViewHolder.imageview_my_booking_detail.getContext()).load(mbl.get(i).getEventBannerImagePath()).apply(new RequestOptions().transform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.TOP))).into(myViewHolder.imageview_my_booking_detail);
        myViewHolder.textview_upfront_payment.setText(String.format("%s%% UPFRONT", upfront_value));
        myViewHolder.textview_event_date.setText(String.format("%s - %s", final_startDate, final_endDate));
        myViewHolder.textview_join_trip_amount.setText(String.format("%s/%s", mbl.get(i).getReservedSeat(), mbl.get(i).getMaxParticipant()));
        myViewHolder.textview_event_name.setText(mbl.get(i).getEventName());
        myViewHolder.textview_event_price.setText(String.format("RM %s", eventprice_value));

        myViewHolder.imageview_user_rank_bronze.setVisibility(View.GONE);
        myViewHolder.imageview_user_rank_gold.setVisibility(View.GONE);
        myViewHolder.imageview_user_rank_platinum.setVisibility(View.GONE);

        if (mbl.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
            myViewHolder.imageview_user_rank_bronze.setVisibility(View.VISIBLE);
        }

        if (mbl.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
            myViewHolder.imageview_user_rank_gold.setVisibility(View.VISIBLE);
        }

        if (mbl.get(i).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
            myViewHolder.imageview_user_rank_platinum.setVisibility(View.VISIBLE);
        }


        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(myViewHolder.textview_upfront_payment.getContext(), R.anim.animation_eventdate_upfront_payment);
        myViewHolder.textview_upfront_payment.setAnimation(rotate);

    }

    public void setOnBookingListItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mbl.size();
    }

}
