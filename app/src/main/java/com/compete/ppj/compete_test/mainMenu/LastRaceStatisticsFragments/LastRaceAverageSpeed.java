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
public class LastRaceAverageSpeed extends Fragment {

    SharedPreferences preferences;
    TextView tAvgSpeed;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_last_race_average_speed, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tAvgSpeed = (TextView) getActivity().findViewById(R.id.tLastAverageSpeed);
        String last_avg_speed = preferences.getString("last_avg_speed","0");
        last_avg_speed = String.format("%.1f", Double.parseDouble(last_avg_speed));

        tAvgSpeed.setText(last_avg_speed);
    }
}
