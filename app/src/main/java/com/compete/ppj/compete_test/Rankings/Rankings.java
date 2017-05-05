package com.compete.ppj.compete_test.Rankings;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Rankings.TabsPagerAdapter.RankingsTabsPagerAdapter;
import com.compete.ppj.compete_test.Server.ServerGetRankings;
import com.compete.ppj.compete_test.mainMenu.MainMenu;

import java.util.Vector;

/**
 * Created by Pol on 28/12/2015.
 */
public class Rankings extends FragmentActivity implements View.OnClickListener, TabHost.OnTabChangeListener{
    private ViewPager viewPager;
    private RankingsTabsPagerAdapter mAdapter;
    private TabHost tabs;
    private TabHost.TabSpec spec1, spec2;
    private TextView tRankingOverall, tRankingCountry;
    SharedPreferences preferences;

    Button bRunRace;
    Context context;
    ServerGetRankings serverGetRankings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_rankings);
        context = getApplicationContext();
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        tRankingOverall = (TextView) findViewById(R.id.tTabRankingOverall);
        tRankingCountry = (TextView) findViewById(R.id.tTabRankingCountry);
        setFragmentsAndTabsLayout();
    }

    public  void setFragmentsAndTabsLayout()
    {
        viewPager = (ViewPager) findViewById(R.id.pagerRankings);
        mAdapter = new RankingsTabsPagerAdapter(getSupportFragmentManager());

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
