package com.compete.ppj.compete_test.Server;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerGetTimeLeft extends AsyncTask<String, String, JSONObject> {
    Constants constants;
    JSONObject result;
    String race_id;

    public ServerGetTimeLeft(){
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
            race_id = userInfo[0];
            getTimeLeft(race_id);

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


    public JSONObject getTimeLeft(String _race_id)throws SocketTimeoutException {
        AsyncHttpClient client = new SyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        RequestParams rp = new RequestParams();
        param.put("Authentication", constants.AUTH_KEY);
        param.put("Function", "getTime");
        param.put("RaceId", _race_id);
        RequestParams params = new RequestParams(param);
        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                Log.d("serverGetTime", jObject.toString());
                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        result = jObject;
                    } else if (jObject.getInt("MessageCode") == 506) //Wrong Query Result
                    {
                        result = jObject;
                    } else {
                        result = jObject;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e){
                Log.d("OnFailure getTime",""+a+ "head "+as+" string" +aas+" throw"+ e.toString());
                e.printStackTrace();
            }
        });
        Log.d("getTime", "Result:  "+result.toString());
        return result;
    }

}
