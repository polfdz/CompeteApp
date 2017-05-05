package com.compete.ppj.compete_test.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.compete.ppj.compete_test.mainMenu.GlobalStatistics;
import com.compete.ppj.compete_test.mainMenu.LastRaceStatistics;
import com.compete.ppj.compete_test.raceMenu.RivalsStatistics;
import com.compete.ppj.compete_test.raceMenu.TeamStatistics;

public class TabsPagerAdapterMainMenu extends FragmentPagerAdapter {

	public TabsPagerAdapterMainMenu(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Global Statistics fragment activity
			return new GlobalStatistics();
		case 1:
			// Last race Statistics fragment activity
			return new LastRaceStatistics();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
