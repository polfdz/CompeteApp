package com.compete.ppj.compete_test.mainMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LastRaceStatistics extends Fragment {
	SharedPreferences preferences;
	static String BASE_URL, AUTH_KEY;
	TextView tResult,tTotalDistance, tAverageSpeed, tTotalPoints, tStatus;
	ListView listGlobalStatistics;
	String total_km, avg_speed, result, time_run, points, status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.layout_last_race_statistics, container, false);
		
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
		BASE_URL = getResources().getString(R.string.BASE_URL);
		AUTH_KEY = getResources().getString(R.string.AUTHENTICATION_KEY);
	}



}
