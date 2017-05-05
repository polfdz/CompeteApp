package com.compete.ppj.compete_test.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.compete.ppj.compete_test.mainMenu.GlobalStatistics;
import com.compete.ppj.compete_test.mainMenu.LastRaceStatistics;
import com.compete.ppj.compete_test.raceMenu.TeamStatistics;
import com.compete.ppj.compete_test.raceMenu.RivalsStatistics;

public class TabsPagerAdapterRaceMenu extends FragmentPagerAdapter {

	public TabsPagerAdapterRaceMenu(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new TeamStatistics();
		case 1:
			// Games fragment activity
			return new RivalsStatistics();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
