package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.fragment.NotificationFragment;
import com.wedoops.platinumnobleclub.fragment.RecordsListCreditFragment;
import com.wedoops.platinumnobleclub.fragment.RecordsListPtsFragment;
import com.wedoops.platinumnobleclub.fragment.ReminderFragment;

public class RecordsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public RecordsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment recordsListPtsFragment = new RecordsListPtsFragment();
        Fragment recordsListCreditFragment = new RecordsListCreditFragment();

        if(position == 0){
            return recordsListPtsFragment;

        }else{
            return recordsListCreditFragment;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String pts = mContext.getResources().getString(R.string.transaction_pts);
        String credit = mContext.getResources().getString(R.string.transaction_credit);

        if(position == 0){
            return pts;
        }else{
            return credit;
        }

    }
}
