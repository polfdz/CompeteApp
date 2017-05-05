package com.compete.ppj.compete_test.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerGetUserStatistics extends AsyncTask<String, String, JSONObject> {
    Constants constants;
    JSONObject result;
    String id;

    public ServerGetUserStatistics(){
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
            getUserStatistics(id);
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


    public JSONObject getUserStatistics(String _id)throws SocketTimeoutException {
        AsyncHttpClient client = new SyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        RequestParams rp = new RequestParams();
        param.put("Authentication", constants.AUTH_KEY);
        param.put("Function", "getUserStats");
        param.put("Id", _id);
        RequestParams params = new RequestParams(param);
        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                Log.d("ServerGetUserStatistics", jObject.toString());
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        //success of login
                        JSONArray jData = jObject.getJSONArray("Data");
                        //Log.d("JARR", jData.toString());

                        JSONObject jsonObject = jData.getJSONObject(0);
                        //Log.d("JOBJ", jsonObject.toString());

                        String total_km = jsonObject.getString("total_km");
                        String avg_speed = jsonObject.getString("avg_speed");
                        String won = jsonObject.getString("won");
                        String entered = jsonObject.getString("entered");
                        String points = jsonObject.getString("points");
                        String time_run = jsonObject.getString("time_run");

                        result.put("result",200);

                        result.put("total_km", total_km);
                        result.put("avg_speed",avg_speed);
                        result.put("won",won);
                        result.put("entered",entered);
                        result.put("points",points);
                        result.put("time_run", time_run);
                        //Log.d("RESULT1", "a " + result.getString("total_km"));

                        Log.d("ServerGetUserStatistics", result.toString());

                    } else if (jObject.getInt("MessageCode") == 504) //Wrong Query Result
                    {
                        result.put("result",504);
                    } else {
                        result.put("result", 600);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
       // Log.d("RESULT", "b "+result.toString());

        return result;


    }

}
