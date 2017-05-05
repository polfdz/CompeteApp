package com.compete.ppj.compete_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by Pol on 03/11/2015.
 */

public class Logo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);

        SharedPreferences preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("MainMenu_old", 1);
        editor.commit();

        controlNextScreen();
    }

    public void controlNextScreen()
    {
        //check Tutorial
        final SharedPreferences tutorial = getSharedPreferences("Tutorial", 0);
        final SharedPreferences.Editor editor_tutorial = tutorial.edit();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if(tutorial.getInt("Tutorial", 0) == 1){
                Intent menu = new Intent(Logo.this, Register.class);
                startActivity(menu);
                finish();
                // }else{
               /*     Intent tut = new Intent(Logo.this, Tutorial.class);
                    //primer cop
                    editor_tutorial.putInt("Tutorial",1);
                    editor_tutorial.commit();
                    //indicar vaig de inici
                    SharedPreferences control = getSharedPreferences("Control_Tutorial", 0);
                    SharedPreferences.Editor editor_control = control.edit();
                    editor_control.putInt("Control_Tutorial", 1); //1=menu principal 2=info
                    editor_control.commit();
                    //start
                    startActivity(tut);
                    finish();
                }*/
            }
        }, 3000);
    }

    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());

    }


}
