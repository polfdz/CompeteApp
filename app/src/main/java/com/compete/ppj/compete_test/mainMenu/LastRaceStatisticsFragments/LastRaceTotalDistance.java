package com.compete.ppj.compete_test.mainMenu.LastRaceStatisticsFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

/**
 * Created by Pol on 15/11/2015.
 */
public class LastRaceTotalDistance extends Fragment {

    SharedPreferences preferences;
    TextView tTotalDistace;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_last_race_total_distance, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tTotalDistace = (TextView) getActivity().findViewById(R.id.tLastTotalDistance);
        String last_total_km = preferences.getString("last_total_km","0");
        Log.d("TOTAL KM", last_total_km);
        last_total_km = String.format("%.1f", Double.parseDouble(last_total_km));

        tTotalDistace.setText(last_total_km);
    }
}
