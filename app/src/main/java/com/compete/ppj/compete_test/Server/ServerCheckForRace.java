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
public class ServerCheckForRace extends AsyncTask<String, String, JSONObject> {
    Constants constants;
    JSONObject result;
    String id, race_id;

    public ServerCheckForRace(){
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
            race_id = userInfo[1];
            getCheckForRace(id,race_id);

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


    public JSONObject getCheckForRace(String _id, String _race_id)throws SocketTimeoutException {
        Log.d("CheckForRace params", "id "+_id+" race_id "+_race_id);
        AsyncHttpClient client = new SyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        RequestParams rp = new RequestParams();
        param.put("Authentication", constants.AUTH_KEY);
        param.put("Function", "checkForRace");
        param.put("UserId", _id);
        param.put("RaceId",_race_id);
        RequestParams params = new RequestParams(param);
        Log.d("serverCheckForRace send", "userId "+_id+" raceId "+_race_id);
        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                Log.d("serverCheckForRace", jObject.toString());
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of login
                        //JSONArray jData = jObject.getJSONArray("Data");
                        //Log.d("JARR", jData.toString());

                        //JSONObject jsonObject = jData.getJSONObject(0);
                        JSONObject jsonObject = jObject.getJSONObject("Data");

                        String race_id = jsonObject.getString("race_id");
                        String team_id = jsonObject.getString("team_id");

                        result.put("result",200);

                        result.put("race_id", race_id);
                        result.put("team_id",team_id);

                    } else if (jObject.getInt("MessageCode") == 506) //Wrong Query Result
                    {
                        result.put("result",506);
                    } else {
                        result.put("result", 600);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e){
                Log.d("OnFailure CheckForRace",""+a+ "head "+as+" string" +aas+" throw"+ e.toString());
                e.printStackTrace();
            }
        });
        Log.d("ServerCheckForRace", "Result:  "+result.toString());

        return result;


    }

}
