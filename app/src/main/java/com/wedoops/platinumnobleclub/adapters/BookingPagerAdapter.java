package com.wedoops.platinumnobleclub.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wedoops.platinumnobleclub.BookingExpiredFragment;
import com.wedoops.platinumnobleclub.BookingFragment;

public class BookingPagerAdapter extends FragmentStatePagerAdapter {

    public BookingPagerAdapter(FragmentManager fm){
        super(fm);
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

        if(position == 0){
            return "MY BOOKING";
        }else{
            return "EXPIRED BOOKING";
        }

    }
}
