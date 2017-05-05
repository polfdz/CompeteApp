package com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

/**
 * Created by Pol on 15/11/2015.
 */
public class GlobalRaces extends Fragment {

    SharedPreferences preferences;
    TextView tRacesWon, tRacesRun;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_global_races_stats, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tRacesWon = (TextView) getActivity().findViewById(R.id.tGlobalRaces);
        tRacesRun = (TextView) getActivity().findViewById(R.id.tUnitGlobalRaces);
        String racesWon = preferences.getString("won","0");
        String racesRun = preferences.getString("entered","0");

        tRacesWon.setText(racesWon);
        tRacesRun.setText("/"+racesRun);
    }
}
