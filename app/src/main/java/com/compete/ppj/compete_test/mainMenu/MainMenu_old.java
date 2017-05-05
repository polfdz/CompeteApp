package com.compete.ppj.compete_test.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.Profile;
import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.adapter.TabsPagerAdapterMainMenu;
import com.compete.ppj.compete_test.raceMenu.RaceMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Pol on 03/11/2015.
 */
public class MainMenu_old extends FragmentActivity implements View.OnClickListener, TabHost.OnTabChangeListener {
    Context context;
    Button bRun, bRanking, bProfile;
    TextView tTypeStatistics;
    private ViewPager viewPager;
    private TabsPagerAdapterMainMenu mAdapter;
    private TabHost tabs;
    private TabHost.TabSpec spec1, spec2;
    static String BASE_URL, AUTH_KEY;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainmenu);
        context = getApplicationContext();

        BASE_URL = getResources().getString(R.string.BASE_URL);
        AUTH_KEY = getResources().getString(R.string.AUTHENTICATION_KEY);
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        tTypeStatistics= (TextView) findViewById(R.id.tTypeStatistics);
        bRun = (Button) findViewById(R.id.bRun);
        bRanking = (Button) findViewById(R.id.bRanking);
        bProfile = (Button) findViewById(R.id.bProfile);

        //onClickListeners
        bRun.setOnClickListener(this);
        bRanking.setOnClickListener(this);
        bProfile.setOnClickListener(this);


        getUserStatistics();
        setFragmentsAndTabsLayout();

    }

    public void getUserStatistics(){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        RequestParams rp = new RequestParams();
        param.put("Authentication", AUTH_KEY);
        //param2.put("Email", preferences.getString("user_email",null));
        param.put("Function", "getUserStats");
        param.put("Id", preferences.getString("id", null));
        RequestParams params = new RequestParams(param);
        client.get(BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {

                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of login
                        JSONArray jData = jObject.getJSONArray("Data");

                        JSONObject jsonObject = jData.getJSONObject(0);
                        String total_km = jsonObject.getString("total_km");
                        String avg_speed = jsonObject.getString("avg_speed");
                        String won = jsonObject.getString("won");
                        String entered = jsonObject.getString("entered");
                        String points = jsonObject.getString("points");
                        String status = "tes22";//jsonObject.getString("status");

                        updateRankingSharedPreferences(total_km, avg_speed, won, entered, points, status);
                        // Log.i("PJ ERROR", "usr stats " + jsonObject.toString());

                    } else if (jObject.getInt("MessageCode") == 504) //Wrong Query Result
                    {
                    } else {
                        Toast.makeText(context, "", 5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void updateRankingSharedPreferences(String _total_km, String _avg_speed, String _won, String _entered, String _points, String _status){
        //SHARED PREFERENCES
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("total_km",_total_km);
        editor.putString("avg_speed", _avg_speed);
        editor.putString("won", _won);
        editor.putString("entered", _entered);
        editor.putString("points", _points);
        editor.putString("status", _status);

        editor.commit();
    }

    public  void setFragmentsAndTabsLayout()
    {
        viewPager = (ViewPager) findViewById(R.id.pagerMainMenu);
        mAdapter = new TabsPagerAdapterMainMenu(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);


        tabs = (TabHost) findViewById(R.id.tabHostMainMenu);
        tabs.setOnTabChangedListener(this);

        tabs.setup();
        spec1 = tabs.newTabSpec("0");
        spec1.setContent(R.id.tabGlobal);
        spec1.setIndicator("");//text

        tabs.addTab(spec1);


        tabs.setup();
        spec2 = tabs.newTabSpec("1");
        spec2.setContent(R.id.tabLastRace);
        spec2.setIndicator("");//text

        tabs.addTab(spec2);
        tabs.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.anim_tab_global_statistics);
        tabs.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.anim_tab_last_race_statistics);

        //tabs.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.anim_tab_mainmenu);
        //tabs.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.anim_tab_mainmenu);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab
                switch (position) {
                    case 0:
                        tabs.setCurrentTab(0);
                        tTypeStatistics.setText(R.string.textOverall);
                        break;
                    case 1:
                        tabs.setCurrentTab(1);
                        tTypeStatistics.setText(R.string.textLastRace);
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
        switch(v.getId())
        {
            case R.id.bRun:
                Intent run = new Intent(this,RaceMenu.class);
                startActivity(run);
                finish();
                break;
            case R.id.bRanking:
                break;
            case R.id.bProfile:
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
                finish();
        }
    }
    @Override
    public void onTabChanged(String tabId) {

        switch (tabId){
            case "0":
                viewPager.setCurrentItem(0);
                break;
            case "1":
                viewPager.setCurrentItem(1);
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
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
