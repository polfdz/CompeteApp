package com.compete.ppj.compete_test.mainMenu.LastRaceStatisticsFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

/**
 * Created by Pol on 15/11/2015.
 */
public class LastRaceWin extends Fragment {

    SharedPreferences preferences;
    TextView tWon;
    LinearLayout lResult;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_last_race_win, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        tWon = (TextView) getActivity().findViewById(R.id.tResult);
        lResult = (LinearLayout) getActivity().findViewById(R.id.layout_last_race_result);
        String race_result = preferences.getString("last_result","0"); //CHECK!!!
        if(race_result.equals("1")){
            tWon.setText("WON");
            lResult.setBackgroundColor(Color.YELLOW);
            tWon.setTextColor(Color.parseColor("#08aaf3"));
        }else if (race_result.equals("2")){
            tWon.setText("LOST");
            lResult.setBackgroundColor(Color.RED);
            tWon.setTextColor(Color.parseColor("#08aaf3"));
        }else{
            tWon.setText("no result");
            lResult.setBackgroundColor(Color.WHITE);
        }

    }
}
