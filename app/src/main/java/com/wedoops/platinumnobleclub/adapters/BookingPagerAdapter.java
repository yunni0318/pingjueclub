package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wedoops.platinumnobleclub.fragment.BookingExpiredFragment;
import com.wedoops.platinumnobleclub.fragment.BookingFragment;
import com.wedoops.platinumnobleclub.R;

public class BookingPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public BookingPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment booking_fragment = new BookingFragment();
        Fragment booking_expired_fragment = new BookingExpiredFragment();

        if(position == 0){
            return booking_fragment;

        }else{
            return booking_expired_fragment;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String mybooking = mContext.getResources().getString(R.string.my_booking_title);
        String expiredBooking = mContext.getResources().getString(R.string.my_expired_booking_title);

        if(position == 0){
            return mybooking;
        }else{
            return expiredBooking;
        }

    }
}
