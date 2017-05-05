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
public class GlobalAverageSpeed extends Fragment {

    SharedPreferences preferences;
    TextView tAvgSpeed;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_global_average_speed, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tAvgSpeed = (TextView) getActivity().findViewById(R.id.tGlobalAverageSpeed);
        String avgSpeed = preferences.getString("avg_speed","0");
        avgSpeed = String.format("%.1f", Double.parseDouble(avgSpeed));
        tAvgSpeed.setText(avgSpeed);
    }
}
