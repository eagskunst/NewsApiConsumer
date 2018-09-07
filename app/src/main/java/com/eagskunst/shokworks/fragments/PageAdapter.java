package com.eagskunst.shokworks.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eagskunst.shokworks.R;

public class PageAdapter extends FragmentPagerAdapter {

    private Context activityContext;

    public PageAdapter(Context activityContext,FragmentManager fm) {
        super(fm);
        this.activityContext = activityContext;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return NewsFragment.newInstance(false);
        else
            return NewsFragment.newInstance(true);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return activityContext.getString(R.string.lastest);
        }
        else if(position == 1){
            return activityContext.getString(R.string.favorites);
        }
        else
            return null;
    }
}
