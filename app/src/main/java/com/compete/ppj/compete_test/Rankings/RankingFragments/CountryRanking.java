package com.compete.ppj.compete_test.Rankings.RankingFragments;

/**
 * Created by Pol on 28/12/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Rankings.listAdapter.RankingListAdapter;
import com.compete.ppj.compete_test.Rankings.listAdapter.RankingRunnerBlock;
import com.compete.ppj.compete_test.Register;
import com.compete.ppj.compete_test.Server.ServerGetRankings;
import com.compete.ppj.compete_test.raceMenu.gridAdapter.GridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class CountryRanking  extends Fragment{
    private View rootView;
    SharedPreferences preferences;
    ListView listView;
    ArrayList<RankingRunnerBlock> listArray;
    RankingListAdapter customListAdapter;
    ServerGetRankings serverGetRankings;
    Timer timer;
    TextView tWarning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_country_ranking, container, false);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);


        listView = (ListView) getActivity().findViewById(R.id.listViewCountryRanking);
        tWarning = (TextView) getActivity().findViewById(R.id.textCountryRankingWarning);
        getCountryRanking();
        //timer = new Timer();
        //timerTask();
    }

    public void getCountryRanking() {
        listArray =  new ArrayList<RankingRunnerBlock>();
        serverGetRankings = new ServerGetRankings();
        try {
            String country = preferences.getString("country", null);
            Log.d("RankingUserCountry",country);
             /*
            null to get all users, preferences.getString("country",null);
            to get country users
             */

            if (country.equals("noncountry")||country.equals(null)||country.equals("")) {
                tWarning.setVisibility(View.VISIBLE);
            } else {
                tWarning.setVisibility(View.GONE);


                JSONObject result = serverGetRankings.execute(country).get(); //if team --> return team members info!!

                int conn = result.getInt("MessageCode");

                switch (conn) {
                    case 200:
                        /*
                        "Data":[{"id":"7","total_km":"0","won":"0","entered":"0","points":"0","avg_speed":"0","time_run":null,"email":"testttttt@test.coom","username":null,"country":null,"gender":null,"age":"0000-00-00","height":"0","token":null,"timestamp":"1448035608"}
                         */
                        //get members
                        JSONArray jRanking = result.getJSONArray("Data");
                        //check if user is in team 1 or team 2
                        for (int i = 0; i < jRanking.length(); i++) {

                            String user_name = jRanking.getJSONObject(i).getString("username");
                            String user_country = jRanking.getJSONObject(i).getString("country");
                            String email = jRanking.getJSONObject(i).getString("email");
                            String points = jRanking.getJSONObject(i).getString("points");
                            if (user_name.equals("null") || user_name.equals("")) {
                                user_name = email.split("@")[0];
                            }
                            Log.d("CountryUser", user_country);
                            if (user_country.equals("null") || user_country.equals("buh") || user_country.equals(null)) {
                                user_country = "noncountry";
                            }
                            if (points == "null") {
                                points = "0";
                            }
                            //Setting drawables (country)
                            Drawable drawable = getResources().getDrawable(getResources()
                                    .getIdentifier(user_country.toLowerCase(), "drawable", getActivity().getPackageName()));
                            Bitmap flag = ((BitmapDrawable) drawable).getBitmap();
                            listArray.add(new RankingRunnerBlock(flag, user_name, points));
                        }
                        break;
                }

                fillList(listArray); //send array of members to gridAdapter
                Log.d("Ranking users", result.toString());
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(JSONException e) {
        e.printStackTrace();
        }
    }

    private void fillList(ArrayList<RankingRunnerBlock> _listArray) {
        customListAdapter = new RankingListAdapter(getActivity(), R.layout.row_rankings, _listArray);
        listView.setAdapter(customListAdapter);
    }
    private void popUpToast(String message){
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
