package com.compete.ppj.compete_test.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.Profile;
import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Rankings.Rankings;
import com.compete.ppj.compete_test.Server.ServerCheckForRace;
import com.compete.ppj.compete_test.Server.ServerGetLastRaceStatistics;
import com.compete.ppj.compete_test.Server.ServerGetUserStatistics;
import com.compete.ppj.compete_test.mainMenu.GlobalStatisticsFragments.TabsPagerAdapterGlobalStats;
import com.compete.ppj.compete_test.mainMenu.LastRaceStatisticsFragments.TabsPagerAdapterLastRaceStats;
import com.compete.ppj.compete_test.raceMenu.RaceMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 03/11/2015.
 */
public class MainMenu extends FragmentActivity implements View.OnClickListener{
    Context context;
    Button bRun, bRanking, bProfile;
    TextView tTypeStatistics;
    private ViewPager overallPager, lastRacePager;
    private TabsPagerAdapterGlobalStats fragmentAdapterGlobalStats;
    private TabsPagerAdapterLastRaceStats fragmentAdapterLastRace;
    private TabHost tabs;
    private TabHost.TabSpec spec1, spec2;
    static String BASE_URL, AUTH_KEY;
    SharedPreferences preferences;
    LinearLayout lGlobalIndicator, lLastRaceIndicator;
    ServerGetUserStatistics serverUserStats;
    ServerGetLastRaceStatistics serverGetLastRaceStatistics;
    ServerCheckForRace serverCheckForRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainmenu2);
        context = getApplicationContext();

        BASE_URL = getResources().getString(R.string.BASE_URL);
        AUTH_KEY = getResources().getString(R.string.AUTHENTICATION_KEY);
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);


        bRun = (Button) findViewById(R.id.bRun);
        bRanking = (Button) findViewById(R.id.bRanking);
        bProfile = (Button) findViewById(R.id.bProfile);
        String gender = preferences.getString("gender","Male");
        switch(gender){
            case "Male":
                bRun.setBackgroundResource(R.drawable.anim_button_man_run);
                break;
            case "Female":
                bRun.setBackgroundResource(R.drawable.anim_button_woman_run);
                break;
        }


        //onClickListeners
        bRun.setOnClickListener(this);
        bRanking.setOnClickListener(this);
        bProfile.setOnClickListener(this);
        lGlobalIndicator = (LinearLayout) findViewById(R.id.lGlobalIndicator);
        lLastRaceIndicator  = (LinearLayout) findViewById(R.id.lLastRaceIndicator);

        getUserStatistics();
        getLastRaceStatistics();

        setFragmentsAndTabsLayout();

    }

    public void getUserStatistics(){
        serverUserStats = new ServerGetUserStatistics();

        String user_id = preferences.getString("id", null);
//        Log.d("USER_ID",user_id);
        try {
            JSONObject result = serverUserStats.execute(user_id).get();

            int conn = result.getInt("result");
            Log.d("RESULT", result.toString());

            switch (conn){
                case 200:
                    //update shared preferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("RegisterStatus","registered");
                    editor.commit();
                    //get user values
                    String total_km = result.getString("total_km");
                    String avg_speed = result.getString("avg_speed");
                    String won = result.getString("won");
                    String entered = result.getString("entered");
                    String points = result.getString("points");
                    String time_run = result.getString("time_run");
                    //update SharedPreferences
                    updateGlobalStatsSharedPreferences(total_km, avg_speed, won, entered, points, time_run);
                    break;
                case 504:
                    Toast.makeText(context, "", 5000).show();
                    break;
                case 600:
                    Toast.makeText(context, "", 5000).show();
                    break;
            }

        } catch (JSONException e) {
            Log.d("JSONERROR", "error loadingStats");
            updateGlobalStatsSharedPreferences("0", "0", "0", "0", "0", "0");
            //popUpToast("Connection failed");
            e.printStackTrace();
        }catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void getLastRaceStatistics(){
        serverGetLastRaceStatistics = new ServerGetLastRaceStatistics();

        String user_id = preferences.getString("id", null);
        try {
            JSONObject result = serverGetLastRaceStatistics.execute(user_id).get();
            int conn = result.getInt("result");
            Log.d("RESULT", result.toString());
            switch (conn){
                case 200:
                    //get user values
                    String race_result = result.getString("race_result");
                    String total_km = result.getString("total_km");
                    String avg_speed = result.getString("avg_speed");
                    String points = result.getString("points");
                    String time_run = result.getString("time_run");
                    String status = result.getString("status");
                    //update SharedPreferences
                    updateLastRaceSharedPreferences(race_result, total_km, avg_speed, points, time_run, status);
                    break;
                case 504:
                    Toast.makeText(context, "", 5000).show();
                    break;
                case 600:
                    Toast.makeText(context, "", 5000).show();
                    break;
            }

        } catch (JSONException e) {
            Log.d("JSONERROR", "error loadingLastRaceStats");
            updateLastRaceSharedPreferences("--", "0", "0", "0", "0", "0");
            e.printStackTrace();
        }catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void updateGlobalStatsSharedPreferences(String _total_km, String _avg_speed, String _won, String _entered, String _points, String time_run){
        //SHARED PREFERENCES
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("total_km",_total_km);
        editor.putString("avg_speed", _avg_speed);
        editor.putString("won", _won);
        editor.putString("entered", _entered);
        editor.putString("points", _points);
        editor.putString("time_run", time_run);
        editor.commit();
    }
    private void updateLastRaceSharedPreferences(String race_result, String total_km, String avg_speed, String points, String time_run, String status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("last_result",race_result);
        editor.putString("last_total_km",total_km);
        editor.putString("last_avg_speed", avg_speed);
        editor.putString("last_points", points);
        editor.putString("last_time_run", time_run);
        editor.putString("last_status", status);
        editor.commit();

    }

    public  void setFragmentsAndTabsLayout()
    {
        overallPager = (ViewPager) findViewById(R.id.pagerGlobalStats);
        lastRacePager = (ViewPager) findViewById(R.id.pagerLastStats);

        fragmentAdapterGlobalStats = new TabsPagerAdapterGlobalStats(getSupportFragmentManager());
        fragmentAdapterLastRace = new TabsPagerAdapterLastRaceStats(getSupportFragmentManager());

        overallPager.setAdapter(fragmentAdapterGlobalStats);
        lastRacePager.setAdapter(fragmentAdapterLastRace);



        overallPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab
                switch (position) {
                    case 0:
                        lGlobalIndicator.setBackgroundResource(R.drawable.indicator_1);
                        break;
                    case 1:
                        lGlobalIndicator.setBackgroundResource(R.drawable.indicator_2);
                        break;
                    case 2:
                        lGlobalIndicator.setBackgroundResource(R.drawable.indicator_3);
                        break;
                    case 3:
                        lGlobalIndicator.setBackgroundResource(R.drawable.indicator_4);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        lastRacePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab
                switch (position) {
                    case 0:
                        lLastRaceIndicator.setBackgroundResource(R.drawable.indicator_1);
                        break;
                    case 1:
                        lLastRaceIndicator.setBackgroundResource(R.drawable.indicator_2);
                        break;
                    case 2:
                        lLastRaceIndicator.setBackgroundResource(R.drawable.indicator_3);
                        break;
                    case 3:
                        lLastRaceIndicator.setBackgroundResource(R.drawable.indicator_4);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    public void checkForRace(){
        serverCheckForRace = new ServerCheckForRace();

        String _user_id = preferences.getString("id", null);
        String _race_id = preferences.getString("race_id", "-1");
        //_race_id = "21";
        try {
            JSONObject result = serverCheckForRace.execute(_user_id,_race_id).get();

            int conn = result.getInt("result");
           // Log.d("RESULT", result.toString());

            switch (conn) {
                case 200:
                    //get race values
                    String team_id = result.getString("team_id");
                    String race_id = result.getString("race_id");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("team_id",team_id);
                    editor.putString("race_id",race_id);
                    editor.commit();

                    Intent run = new Intent(this,RaceMenu.class);
                    startActivity(run);
                    finish();
                    break;
                case 506:
                    //already in a race, so raceId and team_id are in preferences
                    Intent run2 = new Intent(this,RaceMenu.class);
                    startActivity(run2);
                    finish();
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bRun:
                checkForRace();

                break;
            case R.id.bRanking:
                Intent ranking = new Intent(this, Rankings.class);
                startActivity(ranking);
                finish();
                break;
            case R.id.bProfile:
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
                finish();
        }
    }

    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
