package com.compete.ppj.compete_test.Rankings.TabsPagerAdapter;

/**
 * Created by Pol on 28/12/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.compete.ppj.compete_test.Rankings.RankingFragments.CountryRanking;
import com.compete.ppj.compete_test.Rankings.RankingFragments.OverallRanking;

public class RankingsTabsPagerAdapter extends FragmentPagerAdapter {

    public RankingsTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new OverallRanking();
            case 1:
                return  new CountryRanking();
        }

        return null;
    }
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}
