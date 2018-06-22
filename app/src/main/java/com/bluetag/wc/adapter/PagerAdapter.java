package com.bluetag.wc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bluetag.wc.fragments.GoalsFragment;
import com.bluetag.wc.fragments.MatchesFragment;
import com.bluetag.wc.fragments.RecentMatchesFragment;


/**
 * Created by Dream Land on 1/12/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RecentMatchesFragment tab1 = new RecentMatchesFragment();
                return tab1;
           case 1:
               MatchesFragment tab2 = new MatchesFragment();
               return tab2;
               //TablesFragment tab2 = new TablesFragment();
               //return tab2;
            case 2:
                GoalsFragment tab3 = new GoalsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}