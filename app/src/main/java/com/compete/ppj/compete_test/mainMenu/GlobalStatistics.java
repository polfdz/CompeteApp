package com.compete.ppj.compete_test.mainMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class GlobalStatistics extends Fragment {
	TextView tTotalDistance, tAverageSpeed, tRacesRunned, tRacesWon, tTotalPoints, tStatus;
	SharedPreferences preferences;
	ListView listGlobalStatistics;
	String total_km, avg_speed, won, entered, points, status;
	ArrayList<String> arrayStatistics, arrayTypeStatistics;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//View rootView = inflater.inflate(R.layout.layout_global_statistics_list, container, false);
		View rootView = inflater.inflate(R.layout.layout_global_statistics, container, false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
		//arrayStatistics = new ArrayList<>();
		//arrayTypeStatistics = new ArrayList<>();

		tTotalDistance = (TextView) getActivity().findViewById(R.id.tTotalDistance);
		tAverageSpeed = (TextView) getActivity().findViewById(R.id.tAverageSpeed);
		tRacesRunned = (TextView) getActivity().findViewById(R.id.tRacesRunned);
		tRacesWon = (TextView) getActivity().findViewById(R.id.tRacesWon);
		tTotalPoints = (TextView) getActivity().findViewById(R.id.tTotalPoints);
		tStatus = (TextView) getActivity().findViewById(R.id.tStatus);

		setStatistics();

		/*listGlobalStatistics = (ListView) getActivity().findViewById(R.id.listGlobalStatistics);
		StatisticsAdapter adapterStatistics = new StatisticsAdapter(getActivity(), arrayStatistics, arrayTypeStatistics);
		listGlobalStatistics.setAdapter(adapterStatistics);*/

	}

	public void setStatistics(){
		total_km = preferences.getString("total_km",null);
		avg_speed = preferences.getString("avg_speed",null);
		won = preferences.getString("won",null);
		entered = preferences.getString("entered",null);
		points = preferences.getString("points",null);
		status = preferences.getString("status",null);

		tTotalDistance.setText(total_km + " km");
		tAverageSpeed.setText(avg_speed + " km/h");
		tRacesWon.setText(won);
		tRacesRunned.setText(entered);
		tTotalPoints.setText(points);
		tStatus.setText(status);

		/*arrayStatistics.add(total_km);
		arrayTypeStatistics.add("Total km");
		arrayStatistics.add(avg_speed);
		arrayTypeStatistics.add("Avg Speed");*/


	}

}
