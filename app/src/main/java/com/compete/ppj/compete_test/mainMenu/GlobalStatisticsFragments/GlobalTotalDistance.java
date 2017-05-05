package com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

import java.util.ArrayList;

/**
 * Created by Pol on 15/11/2015.
 */
public class GlobalTotalDistance extends Fragment {

    SharedPreferences preferences;
    TextView tTotalDistance;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_global_total_distance, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tTotalDistance = (TextView) getActivity().findViewById(R.id.tGlobalTotalDistance);
        String totalDistance = preferences.getString("total_km","0");
        totalDistance = String.format("%.1f", Double.parseDouble(totalDistance));
        tTotalDistance.setText(totalDistance);
    }
}
