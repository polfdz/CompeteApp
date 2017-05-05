package com.compete.ppj.compete_test.Server;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerRegister extends AsyncTask<String, String, JSONObject> {

    Constants constants;
    JSONObject result;
    String email, password, id, facebook;
    AsyncHttpClient client;

    public ServerRegister(){
        constants = new Constants();
        result = new JSONObject();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... userInfo) {
        while(!isCancelled()){
            try {
                email = userInfo[0];
                password = userInfo[1];
                id = userInfo[2];
                facebook = userInfo[3];
                registerToServer(email, password, id, facebook);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
            return result;
        }
        this.onCancelled();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);

    }

    public JSONObject registerToServer(String _email , String _password , String _id, String _mail_o_facebook){
        client = new SyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",constants.AUTH_KEY);
        param.put("Function", "registerUser");
        param.put("Email", _email);
        param.put("Password", _password);
        RequestParams params = new RequestParams(param);

        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                //Toast.makeText(context, ""+jObject.toString(), 5000).show();
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        result.put("result", 200);
                        //success of login


                    } else if (jObject.getInt("MessageCode") == 504) //Wrong Query Result
                    {
                        result.put("result", 504);

                    } else {
                        result.put("result", 600);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return  result;
    }
    @Override
    protected void onCancelled(){
        super.onCancelled();
    }
}
