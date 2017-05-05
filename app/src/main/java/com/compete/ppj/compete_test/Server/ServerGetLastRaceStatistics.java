package com.compete.ppj.compete_test.Server;

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

import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerGetLastRaceStatistics extends AsyncTask<String, String, JSONObject> {
    Constants constants;
    JSONObject result;
    String id;

    public ServerGetLastRaceStatistics(){
        constants = new Constants();
        result = new JSONObject();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... userInfo) {
        try {
            id = userInfo[0];
            getLastRaceStatistics(id);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);
    }


    public JSONObject getLastRaceStatistics(String _id)throws SocketTimeoutException {

        /*
        * {"MessageCode":"200","Data":[{"id":"87","user_id":"10","team_id":"65","race_id":"40","result":"0","total_km":"5","avg_speed":null,"points":"2","time_run":null,"status":"0"}]}
        * */
        AsyncHttpClient client = new SyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        RequestParams rp = new RequestParams();
        param.put("Authentication", constants.AUTH_KEY);
        param.put("Function", "getLastRaceStats");
        param.put("UserId", _id);
        RequestParams params = new RequestParams(param);
        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                Log.d("ServerGetLastRaceStats", jObject.toString());
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {   //success of login
                        JSONArray jData = jObject.getJSONArray("Data");
                        //Log.d("JARR", jData.toString());
                        JSONObject jsonObject = jData.getJSONObject(0);
                        //Log.d("JOBJ", jsonObject.toString());
                        String team_id = jsonObject.getString("team_id");
                        String race_id = jsonObject.getString("race_id");
                        String race_result = jsonObject.getString("result");
                        String total_km = jsonObject.getString("total_km");
                        String avg_speed = jsonObject.getString("avg_speed");
                        String points = jsonObject.getString("points");
                        String time_run = jsonObject.getString("time_run");
                        String status = jsonObject.getString("status");


                        result.put("result", 200);
                        result.put("team_id", team_id);
                        result.put("race_id", race_id);
                        result.put("race_result", race_result);
                        result.put("total_km", total_km);
                        result.put("avg_speed", avg_speed);
                        result.put("points", points);
                        result.put("time_run", time_run);
                        result.put("status", status);
                        //Log.d("RESULT1", "a " + result.getString("total_km"));

                        Log.d("ServerGetUserStatistics", result.toString());

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

            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e) {
                Log.d("OnFailure Login", "" + a + "head " + as + " string" + aas + " throw" + e.toString());
                e.printStackTrace();
            }
        });
        //Log.d("RESULT", "b "+result.toString());

        return result;


    }

}
