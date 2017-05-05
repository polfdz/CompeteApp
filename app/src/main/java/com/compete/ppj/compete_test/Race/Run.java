package com.compete.ppj.compete_test.Race;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.compete.ppj.compete_test.R;
import com.compete.ppj.compete_test.Race.Extras.SlideButton;
import com.compete.ppj.compete_test.Server.ServerGetTeamsInfo;
import com.compete.ppj.compete_test.Server.ServerGetTimeLeft;
import com.compete.ppj.compete_test.Server.ServerUpdateLastRaceStats;
import com.compete.ppj.compete_test.raceMenu.RaceMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Run extends Activity implements View.OnClickListener{
    // GPSTracker class
    GPSTracker gps;

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    String locationProvider;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private double totalDistance;
    float[] lastDistance = new float[3];
    double distance;
    TextView tLatitude, tLongitude, tSpeed, tDistance, tTimeLeft;
    ArrayList<Location> locations;
    Context context;
    Button bStop,bPause,bRestart;
    HashMap<String, String> param;
    ServerUpdateLastRaceStats serverUpdateLastRaceStats;
    SharedPreferences preferences;
    Chronometer crono;
    CountDownTimer cT;
    ServerGetTimeLeft serverGetTimeLeft;

    ServerGetTeamsInfo serverGetTeamsInfo;
    private long lastPause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_running);
        context = getApplicationContext();
        preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        param = new HashMap<String, String>();


        //text views
        //tLatitude = (TextView) findViewById(R.id.tLatitude);
        //tLongitude = (TextView) findViewById(R.id.tLongitude);
        tSpeed = (TextView) findViewById(R.id.tSpeed);
        tDistance = (TextView) findViewById(R.id.tDistance);
        tTimeLeft = (TextView) findViewById(R.id.tTimeLeft);

        //crono
        crono = (Chronometer) findViewById(R.id.chronometer);
        setChrono();

        //buttons
        bPause = (Button) findViewById(R.id.bPause);
        //bStop = (Button) findViewById(R.id.bStop);
        bRestart = (Button) findViewById(R.id.bRestart);
        bPause.setOnClickListener(this);
        //bStop.setOnClickListener(this);
        bRestart.setOnClickListener(this);
        bRestart.setVisibility(View.INVISIBLE);
        ((SlideButton) findViewById(R.id.bSlideStop)).setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void handleSlide() {
                //popUpToast("STOPED");
                actionStopRunning();
            }
        });
        setTimeLeft();
        locations = new ArrayList<Location>();
        //getLocation();

        // create class object
        gps = new GPSTracker(Run.this, this);
        startGPS();

    }
    public void setChrono(){
        crono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(hh + ":" + mm + ":" + ss);
            }
        });
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
    }
    public void setTimeLeft(){
        int raceTimestamp=0;
        serverGetTimeLeft = new ServerGetTimeLeft();
        try {
            String race_id = preferences.getString("race_id",null);
            Log.d("getTime raceId", race_id);
            JSONObject result = serverGetTimeLeft.execute(race_id).get(); //if team --> return team members info!!
            int conn = result.getInt("MessageCode");
            switch (conn) {
                case 200:
                    //get team members
                    JSONArray array = result.getJSONArray("Data");
                    JSONObject data = array.getJSONObject(0);
                    raceTimestamp = data.getInt("end_time");
                    Log.d("getTime timestamp", "" + raceTimestamp);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cT =  new CountDownTimer(raceTimestamp*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished /1000;
                long s = seconds % 60;
                long m = (seconds / 60) % 60;
                long h = (seconds / (60 * 60)) % 24;
                tTimeLeft.setText("Time remaining: "+String.format("%02d:%02d:%02d", h,m,s));
            }
            public void onFinish() {

                tTimeLeft.setText("Race finished!");
                actionStopRunning();
            }
        };
        cT.start();

        /*
        * send race_id to Server to get user*/
        //serverCheckForRace = new ServerCheckForRace();
    }
    public void startGPS(){
        if(gps.canGetLocation()){
            gps.onLocationChanged(gps.getLocation());
        }else{
            gps.showSettingsAlert();
        }
    }
    /// OLD IMPLEMENTATION
    public void getLocation() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                if(lastKnownLocation == null){
                    lastKnownLocation = location;
                }
                makeUseOfNewLocation(location, lastKnownLocation);
                lastKnownLocation = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    public void makeUseOfNewLocation(Location _location, Location _lastKnownLocation){
        locationProvider = LocationManager.GPS_PROVIDER;//LocationManager.NETWORK_PROVIDER;
        // Or, use GPS location data:
        // String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        //Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);


        /*if(isBetterLocation(_location, lastKnownLocation)){
            tLatitude.setText("Latitude: "+_location.getLatitude());
            tLongitude.setText("Longitude: "+_location.getLongitude());
            tSpeed.setText("Speed: "+_location.getSpeed());
        }else{
            tLatitude.setText("Latitude: "+lastKnownLocation.getLatitude());
            tLongitude.setText("Longitude: "+lastKnownLocation.getLongitude());
            tSpeed.setText("Speed: "+_location.getSpeed());
            //tDistance.setText("Distance: --");
        }*/
        //tLatitude.setText("Latitude: "+_location.getLatitude());
        //tLongitude.setText("Longitude: "+_location.getLongitude());
        String runner_speed = String.format("%.1f", _location.getSpeed() * 3.6);
        tSpeed.setText("Speed: " + runner_speed + " km/h");

        Location.distanceBetween(_lastKnownLocation.getLatitude(),_lastKnownLocation.getLongitude(),_location.getLatitude(),_location.getLongitude(),lastDistance);
        // distance = calculateDistance(_location, _lastKnwonLocation);
        distance += lastDistance[0];
        //distance = calculateDistance(_location, _lastKnownLocation);
        String runner_distance = String.format("%.1f", distance);
        tDistance.setText("Distance: " + runner_distance + " meters");



    }

    /**Calculates distance between two Locatons*/
    public double calculateDistance(Location _location, Location _lastLoc){

        double ttf = (_location.getTime() - _lastLoc.getTime()) / 1000;
        int R = 6371;
        double lat1 = Math.PI / 180.0 *_lastLoc.getLatitude();
        double lon1 = Math.PI / 180.0 *_lastLoc.getLongitude();
        double lat2 = Math.PI / 180.0 *_location.getLatitude();
        double lon2 = Math.PI / 180.0 *_location.getLongitude();
        //  double dLon = Math.PI / 180.0 * (location.getLongitude() - lastLoc.getLongitude());
        double dLat = (lat2-lat1);
        double dLon = (lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        totalDistance += d;

        return totalDistance;
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent backPress = new Intent(this, RaceMenu.class);
        startActivity(backPress);
        sendTime(); //time from crono
        updateStats("0");
        crono.stop();
        //serverUpdateLastRaceStats = null;
        finish();
    }

    public void gpsNotSet() {
        Intent intent = new Intent(this,RaceMenu.class);
        startActivity(intent);
        finish();
    }

    public void setLatitude(double latitude) {
      // tLatitude.setText("" + latitude);
    }

    public void setLongitude(double longitude) {
       // tLongitude.setText(""+longitude);
    }

    public void setSpeed(double _speed) {
        double speed = _speed *3.6; //recived in m/s
        String runner_speed = String.format("%.2f", speed);
        tSpeed.setText(""+runner_speed+" km/h");
    }

    public void setTotalDistance(double totalDistance) {
        String runner_distance = String.format("%.2f", totalDistance);
        tDistance.setText("" + runner_distance + " km");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //gps.stopUsingGPS();
        //moveTaskToBack(true);
        //updateStats("0");
    }

    @Override
    public void onResume() {
        // Always call the superclass method first
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPause:
                gps.stopUsingGPS();
                bPause.setVisibility(View.INVISIBLE);
                bRestart.setVisibility(View.VISIBLE);
                updateStats("0");
                lastPause = SystemClock.elapsedRealtime();
                crono.stop();
                break;
            case R.id.bRestart:
                gps.startUsingGPS();
                bRestart.setVisibility(View.INVISIBLE);
                bPause.setVisibility(View.VISIBLE);
                crono.setBase(crono.getBase() + SystemClock.elapsedRealtime() - lastPause);
                crono.start();
                break;

        }
    }

    public void actionStopRunning(){
        sendTime(); //time from crono
        gps.stopUsingGPS();
        Intent back = new Intent(this,RaceMenu.class);
        startActivity(back);
        updateStats("0");
        crono.stop();
        finish();
    }
    public void updateStats(String status){
        String user_id = preferences.getString("id",null);
        String race_id = preferences.getString("race_id",null);
        String team_id = preferences.getString("team_id",null);
        param.put("status",status); //running
        param.put("RaceId", race_id);
        param.put("UserId", user_id);
        param.put("TeamId", team_id);
        serverUpdateLastRaceStats = new ServerUpdateLastRaceStats();
        try {
            JSONObject result = serverUpdateLastRaceStats.execute(param).get();
            int conn = result.getInt("result");

            switch (conn){
                case 200:
                    //success of updateProfile
                    break;
                case 504:
                    popUpToast("Error " + result.getInt("result"));
                    break;
            }
        } catch (JSONException e) {
            Log.d("JSONERROR", "error result");
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (InterruptedException e) {
            popUpToast("Connection failed");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**/
    public void sendTime(){
        Log.d("time crono","" + crono.getText().toString());
        param.put("time_run",""+crono.getText().toString());
    }
    public void sendDistance(double d) {
        param.put("total_km", "" + d);
    }

    public void sendAvSpeed(float _speed) {
        double speed = _speed *3.6;
        param.put("avg_speed", "" + speed);
    }
}
