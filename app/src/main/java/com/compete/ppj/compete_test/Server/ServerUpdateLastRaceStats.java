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

import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerUpdateLastRaceStats extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    Constants constants;
    JSONObject result;

    public ServerUpdateLastRaceStats(){
        constants = new Constants();
        result = new JSONObject();
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        while (!isCancelled()) {
            try {
                HashMap<String, String> paramsrec = params[0];
                updateLastRaceStats(paramsrec);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
            return result;
        }
        this.onCancelled();
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);
    }

    public JSONObject updateLastRaceStats( HashMap<String, String> param) {
        AsyncHttpClient client = new SyncHttpClient();
        param.put("Authentication", constants.AUTH_KEY);
        param.put("Function", "updateLastRaceStats");

        RequestParams params = new RequestParams(param);
        Log.d("ServerUpdateLastRace", params.toString());
        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                Log.d("ServerUpdateLastRace", jObject.toString());

                try {
                    if (jObject.getInt("MessageCode") == 200) //OK
                    {
                        result.put("result", 200);

                    } else {
                        result.put("result", 504);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e) {
                Log.d("OnFailure", "" + a + "head " + as + " string" + aas + " throw" + e.toString());
                e.printStackTrace();
            }
        });
        return result;
    }
    @Override
    protected void onCancelled(){
        super.onCancelled();
    }
}
