package com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.compete.ppj.compete_test.mainMenu.GlobalStatistics;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalAverageSpeed;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalPoints;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalRaces;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalTotalDistance;
import com.compete.ppj.compete_test.mainMenu.LastRaceStatistics;

public class TabsPagerAdapterGlobalStats extends FragmentPagerAdapter {

	public TabsPagerAdapterGlobalStats(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// TotalDistance fragment activity
			return new GlobalTotalDistance();
		case 1:
			// AvgSpeed  fragment activity
			return new GlobalAverageSpeed();
		case 2:
			// Race fragment activity
			return new GlobalRaces();
		case 3:
			// Points fragment activity
			return new GlobalPoints();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
