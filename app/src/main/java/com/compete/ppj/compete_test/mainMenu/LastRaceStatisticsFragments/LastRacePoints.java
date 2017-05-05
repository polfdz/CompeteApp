package com.compete.ppj.compete_test.mainMenu.LastRaceStatisticsFragments;

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
public class LastRacePoints extends Fragment {

    SharedPreferences preferences;
    TextView tLastPoints;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_last_race_points, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tLastPoints = (TextView) getActivity().findViewById(R.id.tLastPoints);
        String last_points = preferences.getString("last_points","0");

        tLastPoints.setText(last_points);
    }
}
