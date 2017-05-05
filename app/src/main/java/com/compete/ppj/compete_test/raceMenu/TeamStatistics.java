package com.compete.ppj.compete_test.raceMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Server.ServerGetTeamStats;
import com.compete.ppj.compete_test.Server.ServerGetTeamsInfo;
import com.compete.ppj.compete_test.raceMenu.gridAdapter.GridAdapter;
import com.compete.ppj.compete_test.raceMenu.gridAdapter.RunnerBlock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class TeamStatistics extends Fragment {
	SharedPreferences preferences;
	GridView gridView;

	ArrayList<RunnerBlock> gridArray;
	GridAdapter customGridAdapter;
	TextView tTeamKm;
	TextView tTeamAvSpeed;
	ServerGetTeamsInfo serverGetTeamsInfo;
	ServerGetTeamStats serverGetTeamStats;
	Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_team, container, false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

		//textViews
		tTeamKm = (TextView) getActivity().findViewById(R.id.tTeamTotalKm);
		tTeamAvSpeed = (TextView) getActivity().findViewById(R.id.tTeamAvSpeed);

		gridView = (GridView) getActivity().findViewById(R.id.gridTeam1);

		getTeamMembers();
		getTeamStats();
		timer = new Timer();
		timerTask();

		//	fillGrid();


	}
	public void timerTask(){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Your logic here...

				// When you need to modify a UI element, do so on the UI thread.
				// 'getActivity()' is required as this is being ran from a Fragment.
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// This code will always run on the UI thread, therefore is safe to modify UI elements.
						getTeamMembers();
						getTeamStats();
					}
				});
			}
		}, 0, 20000);
	}

	public void getTeamMembers() {
		gridArray =  new ArrayList<RunnerBlock>();
		serverGetTeamsInfo = new ServerGetTeamsInfo();
		try {
			String race_id = preferences.getString("race_id",null);

			JSONObject result = serverGetTeamsInfo.execute(race_id).get(); //if team --> return team members info!!

			int conn = result.getInt("MessageCode");

			switch (conn) {
				case 200:
					boolean team1 = false;
					//get team members
					JSONArray jTeamMembers = result.getJSONArray("team1");
					JSONArray jRaceMembers = result.getJSONArray("userData");
					//check if user is in team 1 or team 2
					for(int i = 0 ; i < jTeamMembers.length(); i++)
					{

						String member_id = jTeamMembers.getJSONObject(i).getString("user_id");
						Log.d("TeamUserId ","team 1 mem id: "+member_id+ "pref id: "+preferences.getString("id",null));

						if(preferences.getString("id",null).equals(member_id)){
							team1 = true;
							Log.d("user in team: ","team 1 mem id: "+member_id+ "pref id: "+preferences.getString("id",null));
						}
					}

					if(team1 == false){ //means that user is in team2
						jTeamMembers = result.getJSONArray("team2");
					}else{
						//nothing user in team1
					}

					//adding members to team list
					for(int i = 0 ; i < jTeamMembers.length(); i++)
					{
						String member_total_km = jTeamMembers.getJSONObject(i).getString("total_km");
						String member_id = jTeamMembers.getJSONObject(i).getString("user_id");
						String member_av_speed = jTeamMembers.getJSONObject(i).getString("avg_speed");
						String member_status = jTeamMembers.getJSONObject(i).getString("status");
						String member_name = null;
						String member_country = null;

						//team_id
						String team_id =  jTeamMembers.getJSONObject(i).getString("team_id");
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("team_id", team_id);
						editor.commit();

						//team members info
						JSONObject user_data = getUserData(jRaceMembers, member_id);
						if(user_data != null){
							member_name = user_data.getString("username");
							if(member_name == "null" || member_name == ""){
								member_name = user_data.getString("email").split("@")[0];
							}
							member_country = user_data.getString("country");
							if(member_country.equals("noncountry")||member_country.equals("null")||member_country.equals(null)||member_country.equals("")){
								member_country = "noncountry";
							}
							if(member_total_km == "null"){
								member_total_km = "0";
							}
						}
						//Setting drawables (country + status)
						Drawable drawable = getResources().getDrawable(getResources()
								.getIdentifier(member_country.toLowerCase(), "drawable", getActivity().getPackageName()));
						Bitmap flag  =  ((BitmapDrawable)drawable).getBitmap();

						Bitmap status = null;
						if(member_status == "1"){
							status = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.online_indicator);
						}else{
							status = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.offline_indicator);
						}
						gridArray.add(new RunnerBlock(flag,member_name,status,member_total_km));

					}
					fillGrid(gridArray); //send array of members to gridAdapter
					Log.d("Team Members", result.toString());
					Log.d("Team Members team1", result.getJSONArray("team1").toString());
					break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public void getTeamStats() {
		serverGetTeamStats = new ServerGetTeamStats();
		try {
			String team_id = preferences.getString("team_id",null);
			Log.d("ServerGetTeamStats","team_id: "+team_id);
			JSONObject result = serverGetTeamStats.execute(team_id).get(); //if team --> return team members info!!

			int conn = result.getInt("MessageCode");

			switch (conn) {
				case 200:
					boolean team1 = false;
					//get team members
					JSONArray jData = result.getJSONArray("Data");
					Log.d("JSON Array", jData.toString());
					JSONObject jObject = jData.getJSONObject(0);
					//JSONObject jObject = result.getJSONObject("Data");
					String team_total_km = jObject.getString("total_km");
					String team_avg_speed = jObject.getString("avg_speed");
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("team_total_km", team_total_km);
					editor.putString("team_avg_speed", team_avg_speed);
					editor.commit();

					team_total_km = String.format("%.1f", Double.parseDouble(team_total_km));
					team_avg_speed = String.format("%.1f", Double.parseDouble(team_avg_speed));
					tTeamKm.setText(team_total_km +" km");
					tTeamAvSpeed.setText(team_avg_speed+" km/h");
					break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}




	private JSONObject getUserData(JSONArray race_members, String member_id) throws JSONException {
		//Log.d("Race Members",race_members.toString());
		for(int i=0; i < race_members.length(); i++)
		{
			if( member_id.equalsIgnoreCase(race_members.getJSONObject(i).getString("id"))){
				//Log.d("FoundMemberCountry",member_id);
				return race_members.getJSONObject(i);
			}
		}
		return null; //means user not found.... baaad!
	}

	public void fillGrid(ArrayList<RunnerBlock> _gridArray){
		customGridAdapter = new GridAdapter(getActivity(), R.layout.row_teams, _gridArray);
		gridView.setAdapter(customGridAdapter);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		timer.cancel();
	}
	@Override
	public void onResume() {
		Log.e("DEBUG", "onResume of HomeFragment");
		super.onResume();
	}
	@Override
	public void onPause() {
		Log.e("DEBUG", "OnPause of HomeFragment");
		super.onPause();
	}


}
