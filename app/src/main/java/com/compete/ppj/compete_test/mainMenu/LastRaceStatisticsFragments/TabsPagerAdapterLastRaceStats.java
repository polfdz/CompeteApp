package com.compete.ppj.compete_test.mainMenu.LastRaceStatisticsFragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.compete.ppj.compete_test.mainMenu.GlobalStatistics;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalAverageSpeed;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.GlobalTotalDistance;
import com.compete.ppj.compete_test.mainMenu.LastRaceStatistics;

public class TabsPagerAdapterLastRaceStats extends FragmentPagerAdapter {

	public TabsPagerAdapterLastRaceStats(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
			case 0:
				// Last race result fragment activity
				return new LastRaceWin();
			case 1:
				// Last race total distance fragment activity
				return new LastRaceTotalDistance();
			case 2:
				// Last race average speed fragment activity
				return new LastRaceAverageSpeed();
			case 3:
				// Last race points fragment activity
				return new LastRacePoints();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
