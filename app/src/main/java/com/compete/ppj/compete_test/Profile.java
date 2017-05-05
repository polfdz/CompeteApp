package com.compete.ppj.compete_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.compete.ppj.compete_test.Server.ServerUpdateUser;
import com.compete.ppj.compete_test.mainMenu.MainMenu;
import com.compete.ppj.compete_test.mainMenu.MainMenu_old;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 03/11/2015.
 */
public class Profile extends Activity implements View.OnClickListener {
    Spinner listview;
    RadioGroup rGender;
    RadioButton rMale, rFemale;
    Button bSave, bSkip, bLogout, bAge;
    EditText tUsername, tHeight;
    Context context;
    HashMap<String, String> param;
    SharedPreferences preferences;

    ServerUpdateUser serverUpdateUser;

    //DateAgePicker
    DatePicker dpAge;
    LinearLayout lDatePicker;
    Button bSaveAge, bSkipAge;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    //


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile);
        context = getApplicationContext();
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        param = new HashMap<String, String>();

        tUsername = (EditText) findViewById(R.id.tUsername);
        tHeight = (EditText) findViewById(R.id.tHeight);

        listview = (Spinner) findViewById(R.id.lCountry);
        setListViewAdapter();

        dpAge = (DatePicker) findViewById(R.id.dpAge);
        lDatePicker = (LinearLayout) findViewById(R.id.lDatePicker);

        bSave = (Button) findViewById(R.id.bSaveProfile);
        bSkip = (Button) findViewById(R.id.bSkipProfile);
        bLogout = (Button) findViewById(R.id.bLogout);
        rMale = (RadioButton) findViewById(R.id.rMale);
        rFemale = (RadioButton) findViewById(R.id.rFamele);
        bAge = (Button) findViewById(R.id.bAge);
        bSaveAge = (Button) findViewById(R.id.bSetAge);
        bSkipAge = (Button) findViewById(R.id.bSkipAge);
        // onClickListeners
        bSave.setOnClickListener(this);
        bSkip.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bAge.setOnClickListener(this);
        bSaveAge.setOnClickListener(this);
        bSkipAge.setOnClickListener(this);

        rGender = (RadioGroup) findViewById(R.id.rGenderGroup);
        //String username = tUsername.getText().toString();

        setProfileValues();

    }
    public void setListViewAdapter()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listview.setAdapter(adapter);
    }

    public void setProfileValues()
    {
        SharedPreferences.Editor editor = preferences.edit();
        String userName = preferences.getString("username", null);
        String sex = preferences.getString("gender", null);
        String country = preferences.getString("country", null);
        String age = preferences.getString("age",null); //OJOOO
        String height = preferences.getString("height",null); //OJOOO

        if( userName != null)
        {
            tUsername.setText(userName);
        }

        if (sex != null){
            if(sex.equalsIgnoreCase("Male"))
            {
                rMale.setChecked(true);
            }else if(sex.equalsIgnoreCase("Female"))
            {
                rFemale.setChecked(true);
            }
        }
        if(country != null)
        {
            if(country.equalsIgnoreCase("United_States")){
                country = "United States";
            }
            int pos = getCountryPosition(country);
            if(pos != -1)
                listview.setSelection(pos);
            else listview.setSelection(0);
        }
        if( height != null || height != "0")
        {
            tHeight.setText(height);
        }
        if(age != null){
            String[] ageDates = age.split("-");
            // display current date
            if(ageDates.length >= 3){
                year = Integer.parseInt(ageDates[0]);
                month = Integer.parseInt(ageDates[1]);
                day = Integer.parseInt(ageDates[2]);
                // set current date into datepicker
                dpAge.init(year, month, day, null);
                //dpAge.getMonth();
            }

        }
    }

    public int getCountryPosition(String _country){
        String[] countries = getResources().getStringArray(R.array.country);
        for(int i=0; i<countries.length; i++)
        {
            if(countries[i].equals(_country)){
                return i;
            }
        }
        return -1;
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.bSaveProfile:
               setProfileSharedProferences();
                break;
            case R.id.bSkipProfile:
                skip();
                break;
            case R.id.bLogout:
                deleteEmailSharedPreferences();
                Intent logout = new Intent(this, Register.class);
                startActivity(logout);
                finish();
                break;
            case R.id.bAge:
                lDatePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.bSkipAge:
                lDatePicker.setVisibility(View.GONE);
                break;
            case R.id.bSetAge:
                lDatePicker.setVisibility(View.GONE);

                break;
        }
    }
    public void skip(){
        Intent gMenu = new Intent(this, MainMenu.class);
        startActivity(gMenu);
        finish();
    }
    public void setProfileSharedProferences()
    {
        String userName = preferences.getString("username", null);
        String sex = preferences.getString("gender", null);
        String country = preferences.getString("country", null);
        String age = preferences.getString("age", null);
        String height = preferences.getString("height", null);

        checkUpdatedProfileParams(userName, sex, country, age, height);
    }

    private void checkUpdatedProfileParams(String _userName, String _sex, String _country, String _age, String _height)
    {
        SharedPreferences.Editor editor = preferences.edit();
        int check = 0;
        if(_userName != tUsername.getText().toString())
        {
            String newUserName = tUsername.getText().toString();
            editor.putString("username", tUsername.getText().toString());
            editor.commit();
            addParamsToSend("newUserName", newUserName);
            check = 1;
        }
        if(_sex != getSelectedGender())
        {
            String newGender = getSelectedGender();
            editor.putString("gender", getSelectedGender());
            editor.commit();
            addParamsToSend("newGender", newGender);
            check = 1;
        }
        if(_height != ""+tHeight.getText()){
            String newHeight = ""+tHeight.getText();
            editor.putString("height", newHeight);
            editor.commit();
            addParamsToSend("newHeight", newHeight);
            check = 1;
        }
        if( _country != listview.getSelectedItem().toString())
        {
            String newCountry = listview.getSelectedItem().toString();
            if(newCountry.equals("United States")){
                newCountry = "United_States";
            }
            editor.putString("country", newCountry);
            editor.commit();
            addParamsToSend("newCountry", newCountry);
            check = 1;
        }
        if( _age != ""+dpAge.getYear()+"-"+dpAge.getMonth()+"-"+dpAge.getDayOfMonth())
        {
            String newAge = ""+dpAge.getYear()+"-"+dpAge.getMonth()+"-"+dpAge.getDayOfMonth();
            editor.putString("age", newAge);
            editor.commit();
            addParamsToSend("newAge", newAge);
            check = 1;
        }
        if(check == 1){
            param.put("Function", "updateUser");
            String email = preferences.getString("email", null);
            param.put("Email", email);
            Log.d("Email user", email);
            notifyServerProfileUpdate();
        }else{
            skip();
        }

    }
    public void addParamsToSend(String _param, String _param2)
    {
        switch (_param){
            case "newUserName":
                param.put("username", _param2);
                break;
            case "newGender":
                param.put("gender", _param2);
                break;
            case "newCountry":
                param.put("country", _param2);
                break;
            case "newAge":
                param.put("age", _param2);
                break;
            case "newHeight":
                param.put("height", _param2);
                break;
        }
    }

    private void notifyServerProfileUpdate(){
        serverUpdateUser = new ServerUpdateUser();

        try {
            JSONObject result = serverUpdateUser.execute(param).get();
            int conn = result.getInt("result");

            switch (conn){
                case 200:
                    //success of updateProfile
                    serverUpdateUser.cancel(true);
                    saveProfile();
                    break;
                case 504:
                    popUpToast("Error " + result.getInt("result"));
                    serverUpdateUser.cancel(true);
                    break;
            }
        } catch (JSONException e) {
            Log.d("JSONERROR","error result");
            serverUpdateUser.cancel(true);
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
            serverUpdateUser.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
   /* private void notifyServerProfileUpdate() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        RequestParams params = new RequestParams(param);
        Log.i("PJ ERROR", params.toString());
        client.get(BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of updateProfile
                        saveProfile();
                    } else {
                        popUpToast("Error " + jObject.getInt("MessageCode"));
                        //Toast.makeText(context, "no login", 5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
    public void saveProfile(){
        Intent goMenu = new Intent(this, MainMenu.class);
        startActivity(goMenu);
    }
    public void deleteEmailSharedPreferences()
    {
        //LOGOUT
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("RegisterStatus");
        editor.remove("email");
        editor.remove("username");
        editor.remove("country");
        editor.remove("gender");
        editor.remove("id");
        editor.remove("total_km");
        editor.remove("avg_speed");
        editor.remove("won");
        editor.remove("entered");
        editor.remove("points");
        editor.remove("status");
        editor.remove("age");
        editor.remove("height");

        editor.remove("race_id");
        editor.remove("team_id");
        editor.remove("rivals_id");

        editor.remove("rivals_total_km");
        editor.remove("rivals_avg_speed");
        editor.remove("team_total_km");
        editor.remove("team_avg_speed");

        editor.commit();
    }

    private String getSelectedGender()
    {
        int selectedId = rGender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton rSexButton = (RadioButton) findViewById(selectedId);
        return rSexButton.getText().toString();
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
