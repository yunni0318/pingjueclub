package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.fragment.ReminderFragment;
import com.wedoops.platinumnobleclub.fragment.NotificationFragment;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public NewsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment notificationFragment = new NotificationFragment();
        Fragment reminderFragment = new ReminderFragment();

        if(position == 0){
            return notificationFragment;

        }else{
            return reminderFragment;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String noti = mContext.getResources().getString(R.string.news_notification);
        String remind = mContext.getResources().getString(R.string.news_reminder);

        if(position == 0){
            return noti;
        }else{
            return remind;
        }

    }
}
