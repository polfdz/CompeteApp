package com.compete.ppj.compete_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.compete.ppj.compete_test.Server.ServerLogin;
import com.compete.ppj.compete_test.Server.ServerRegister;
import com.compete.ppj.compete_test.mainMenu.MainMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 03/11/2015.
 */
public class Register extends Activity implements View.OnClickListener{
    Button bRegister,bLogin;
    EditText tEmail, tPassword;
    Context context;
    CountriesListAdapter cAdapter;
    static  String BASE_URL;
    static  String AUTH_KEY;
    SharedPreferences preferences;
    ServerLogin serverLogin, serverLoginRegister;
    ServerRegister serverRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        context = getApplicationContext();

        BASE_URL = getResources().getString(R.string.BASE_URL);
        AUTH_KEY = getResources().getString(R.string.AUTHENTICATION_KEY);

        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);


        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        tEmail = (EditText) findViewById(R.id.tEmail);
        tPassword = (EditText) findViewById(R.id.tPassword);


        checkUserRegisterStatus(); //consulting sharedPreferences

    }

    public void checkUserRegisterStatus()
    {
        //Check if usr is already logged in registered
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String registerStatus = preferences.getString("RegisterStatus", null);
        if(registerStatus != null){ //
            Intent usRegistrat = new Intent(Register.this, MainMenu.class);
            startActivity(usRegistrat);
            finish();
        }
    }

    public void checkParameters(int _i)
    {
        String email = tEmail.getText().toString();
        String password = tPassword.getText().toString();
        if(checkEmail(email)== 1){
            if(checkPassword(password)==1){
                switch (_i){
                    case 0: //Register
                        registerToServer(email, password, "", "1");
                        break;
                    case 1://Login
                        loginToServer(email, password, "", "1");
                        break;

                }
            }else{
                Toast.makeText(context, "password too short (6 characters)", 5000).show();
            }
        }else{
            Toast.makeText(context, "email not valid", 5000).show();
        }
    }

    public void registerToServer( String _email , String _password, String _id, String _mail_o_facebook){
        serverRegister = new ServerRegister();

        try {
            JSONObject result = serverRegister.execute(_email,_password,_id,_mail_o_facebook).get();

            int conn = result.getInt("result");

            switch (conn){
                case 200:
                    //update shared preferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("RegisterStatus","registered");
                    editor.putString("email", _email);
                    editor.commit();
                    //gotoProfile
                    //getIDafterRegister
                    serverLoginRegister = new ServerLogin();

                    try {
                        JSONObject result2 = serverLoginRegister.execute(_email, _password, "", "1").get();
                        int conn2 = result2.getInt("result");
                        switch (conn){
                            case 200:
                                //update shared preferences
                                preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = preferences.edit();
                                editor2.putString("RegisterStatus","registered");
                                editor2.commit();
                                //get user values
                                String id = result2.getString("id");
                               /* String email = result2.getString("email");
                                String country = result2.getString("country");
                                String gender = result2.getString("gender");
                                String username = result2.getString("username");
                                String age = result2.getString("age");
                                String height = result2.getString("height");
                                String token = result2.getString("token");
                                String timestamp = result2.getString("timestamp");
                                String race_id = result2.getString("race_id");// if == null race_id = -1*/

                                //update profile with user last values
                                //updateProfileSharedPreferences(id, email, country, gender, username,age,height,token,timestamp, race_id);
                                updateIdSharedPreferences(id);
                                serverLoginRegister.cancel(true);
                                //GoToProfile
                                Intent usuariRegistrat = new Intent(Register.this, Profile.class);
                                startActivity(usuariRegistrat);
                                finish();
                                serverRegister.cancel(true);
                                break;
                        }
                    }catch (JSONException e) {
                        //popUpToast("Connection failed");
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        popUpToast("Connection failed");
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    break;
                case 504:
                    serverRegister.cancel(true);
                    popUpToast("Email already exists");
                    break;
                case 600:
                    serverRegister.cancel(true);
                    Toast.makeText(context, "Connection failed", 5000).show();
                    break;
            }

        } catch (JSONException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
   /* public void registerToServer(final String _email , final String _password , final String _id, final int _mail_o_facebook){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",AUTH_KEY);
        param.put("Function", "registerUser");
        param.put("Email", _email);
        param.put("Password", _password);
        RequestParams params = new RequestParams(param);

        client.get(BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                //Toast.makeText(context, ""+jObject.toString(), 5000).show();
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of login
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("RegisterStatus","registered");
                        editor.putString("email", _email);
                        editor.commit();

                        Intent usuariRegistrat = new Intent(Register.this, Profile.class);
                        startActivity(usuariRegistrat);
                        finish();
                    }else if(jObject.getInt("MessageCode") == 504) //Wrong Query Result
                    {
                        popUpToast("Email already exists");
                    }else{
                        Toast.makeText(context, "no login", 5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private void loginToServer( String _email, String _password, String s, String facebook) {
        serverLogin = new ServerLogin();

        try {
            JSONObject result = serverLogin.execute(_email, _password, s, facebook).get();

            int conn = result.getInt("result");

            switch (conn){
                case 200:
                    //update shared preferences
                    preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("RegisterStatus","registered");
                    editor.commit();
                    //get user values
                    String id = result.getString("id");
                    String email = result.getString("email");
                    String country = result.getString("country");
                    String gender = result.getString("gender");
                    String username = result.getString("username");
                    String age = result.getString("age");
                    String height = result.getString("height");
                    String token = result.getString("token");
                    String timestamp = result.getString("timestamp");
                    String race_id = result.getString("race_id");// if == null race_id = -1

                    //update profile with user last values
                    updateProfileSharedPreferences(id, email, country, gender, username,age,height,token,timestamp, race_id);
                    //go to menu
                    Intent usuariLogin = new Intent(Register.this, MainMenu.class);
                    startActivity(usuariLogin);
                    finish();
                    serverLogin.cancel(true);
                    break;
                case 504:
                    serverLogin.cancel(true);
                    popUpToast("Invalid mail or password");
                    break;
                case 600:
                    serverLogin.cancel(true);
                    Toast.makeText(context, "no login", 5000).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            popUpToast("Connection failed");
        } catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    /*
    private void loginToServer(final String _email, final String _password, final String s, final int facebook) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",AUTH_KEY);
        param.put("Function", "login");
        param.put("Email", _email);
        param.put("Password", _password);
        RequestParams params = new RequestParams(param);

        client.get(BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                //Toast.makeText(context, ""+jObject.toString(), 5000).show();
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of login
                        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("RegisterStatus","registered");
                        editor.putString("email", _email);
                        JSONArray jData = jObject.getJSONArray("Data");

                        JSONObject jsonObject = jData.getJSONObject(0);
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String country = jsonObject.getString("country");
                        String gender = jsonObject.getString("gender");
                        //Log.i("PJ ERROR", id + email +country +gender);
                        updateProfileSharedPreferences(id, email, country, gender);
                        editor.commit();
                        //Log.i("PJ ERROR", jsonObject.toString());

                        Intent usuariLogin = new Intent(Register.this, MainMenu.class);
                        startActivity(usuariLogin);
                        finish();
                    }else if(jObject.getInt("MessageCode") == 504) //Wrong Query Result
                    {
                        popUpToast("Invalid mail or password");
                    }else{
                        Toast.makeText(context, "no login", 5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }*/

    private void updateIdSharedPreferences(String _id){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", _id);
        editor.commit();
    }
    private void updateProfileSharedPreferences(String _id, String _email, String _country, String _gender, String _username, String _age, String _height, String _token, String _timestamp, String _race_id)
    {
        //SHARED PREFERENCES
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", _id);
        editor.putString("email",_email);
        editor.putString("gender",_gender);
        editor.putString("country", _country);
        editor.putString("username", _username);
        editor.putString("age", _age);
        editor.putString("height", _height);
        editor.putString("token", _token);
        editor.putString("timestamp", _timestamp);
        editor.putString("race_id", _race_id);

        editor.commit();
    }


    private int checkPassword(String _password) {
        if(_password.length() < 6){
            return 0; //incorrect
        }
        return 1; //correct
    }

    private int checkEmail(String _email) {
        String[] checkAt = _email.split("\\@");
        if(checkAt.length != 2){
            return 0; //incorrect
        }
        return 1;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:
                checkParameters(0);
                break;
            case R.id.bLogin:
                checkParameters(1);
                break;
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
