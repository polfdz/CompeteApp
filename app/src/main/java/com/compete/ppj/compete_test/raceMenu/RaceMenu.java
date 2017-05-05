package com.compete.ppj.compete_test.raceMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Race.Run;
import com.compete.ppj.compete_test.Server.ServerCheckForRace;
import com.compete.ppj.compete_test.Server.ServerGetTeamsInfo;
import com.compete.ppj.compete_test.Server.ServerGetTimeLeft;
import com.compete.ppj.compete_test.adapter.TabsPagerAdapterRaceMenu;
import com.compete.ppj.compete_test.mainMenu.MainMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 03/11/2015.
 */

public class RaceMenu extends FragmentActivity implements View.OnClickListener, TabHost.OnTabChangeListener{
    private ViewPager viewPager;
    private TabsPagerAdapterRaceMenu mAdapter;
    private TabHost tabs;
    private TabHost.TabSpec spec1, spec2;
    private TextView tTeam, tRivals, tCountDown;
    SharedPreferences preferences;
    CountDownTimer cT;

    Button bRunRace;
    Context context;
    ServerCheckForRace serverCheckForRace;
    ServerGetTeamsInfo serverGetTeamsInfo;
    ServerGetTimeLeft serverGetTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.race_menu);
        context = getApplicationContext();
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        tTeam = (TextView) findViewById(R.id.tTab1);
        tRivals = (TextView) findViewById(R.id.tTab2);
        tCountDown = (TextView) findViewById(R.id.tCountdown);

        bRunRace = (Button) findViewById(R.id.bRunRace);
        bRunRace.setOnClickListener(this);

        setTimeLeft();
        setFragmentsAndTabsLayout();

    }

    public void setTimeLeft(){
        int raceTimestamp=0;
        serverGetTimeLeft = new ServerGetTimeLeft();
        try {
            String race_id = preferences.getString("race_id",null);
            Log.d("getTime raceId", race_id);
            JSONObject result = serverGetTimeLeft.execute(race_id).get(); //if team --> return team members info!!
            int conn = result.getInt("MessageCode");
            switch (conn) {
                case 200:
                    //get team members
                    JSONArray array = result.getJSONArray("Data");
                    JSONObject data = array.getJSONObject(0);
                    raceTimestamp = data.getInt("end_time");
                    Log.d("getTime timestamp", "" + raceTimestamp);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //raceTimestamp = 10000;
        cT =  new CountDownTimer(raceTimestamp*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished /1000;
                long s = seconds % 60;
                long m = (seconds / 60) % 60;
                long h = (seconds / (60 * 60)) % 24;
                tCountDown.setText("Time remaining: "+String.format("%02d:%02d:%02d", h,m,s));
                // String v = String.format("%02d", millisUntilFinished/60000);
                // int va = (int)( (millisUntilFinished%60000)/1000);
                // tCountDown.setText("Time remaining: "+v+":"+String.format("%02d",va));

            }
            public void onFinish() {
                tCountDown.setText("Race finished!");
                bRunRace.setClickable(false);
            }
        };
        cT.start();

        /*
        * send race_id to Server to get user*/
        //serverCheckForRace = new ServerCheckForRace();
    }

    public  void setFragmentsAndTabsLayout()
    {
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapterRaceMenu(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);


        tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setOnTabChangedListener(this);
        tabs.setup();
        spec1 = tabs.newTabSpec("0");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("");//TEAM
        tabs.addTab(spec1);


        tabs.setup();
        spec2 = tabs.newTabSpec("1");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("");//RIVALS
        tabs.addTab(spec2);
        tabs.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.anim_tabindicator_race_menu);
        tabs.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.anim_tabindicator_race_menu);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab
                switch (position) {
                    case 0:
                        tabs.setCurrentTab(0);
                        break;
                    case 1:
                        tabs.setCurrentTab(1);
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
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bRunRace:
                Intent run = new Intent(this, Run.class);
                startActivity(run);
                finish();
                break;
        }
    }

    @Override
    public void onTabChanged(String tabId) {

        switch (tabId){
            case "0":
                viewPager.setCurrentItem(0);
                //tTeam.setTextScaleX(1);
                //tRivals.setTextScaleX((float) 0.7);
                break;
            case "1":
                viewPager.setCurrentItem(1);
                //tTeam.setTextScaleX((float) 0.7);
                //tRivals.setTextScaleX(1);

                break;
        }
       // Log.i("PJ ERROR", tabId);

    }
    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent backPress = new Intent(this, MainMenu.class);
        startActivity(backPress);
        finish();
    }

}
