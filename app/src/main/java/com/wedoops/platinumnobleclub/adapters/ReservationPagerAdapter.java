package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.fragment.ReservationFragment;
import com.wedoops.platinumnobleclub.fragment.ReservationHistoryFragment;

public class ReservationPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public ReservationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment reservationFragment = new ReservationFragment();
        Fragment reservationHistoryFragment = new ReservationHistoryFragment();

        if (position == 0) {
            return reservationFragment;

        } else {
            return reservationHistoryFragment;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String mybooking = mContext.getResources().getString(R.string.reservation_title);
        String expiredBooking = mContext.getResources().getString(R.string.reservation_history_title);

        if (position == 0) {
            return mybooking;
        } else {
            return expiredBooking;
        }

    }
}
