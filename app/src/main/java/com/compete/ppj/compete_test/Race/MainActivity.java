package com.compete.ppj.compete_test.Race;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private Context context;
    public TextView lat, speed, longi, distance;
    public float[] res;
    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;
    Position pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.r);
        // Acquire a reference to the system Location Manager
        context = getApplicationContext();

        pos = new Position();

        /*lat = (TextView) findViewById(R.id.latitude);
        longi = (TextView) findViewById(R.id.longi);
        speed = (TextView) findViewById(R.id.speed);
        distance = (TextView) findViewById(R.id.tdistance);*/

       // btnShowLocation = (Button) findViewById(R.id.bLocation);
        btnShowLocation.setOnClickListener(this);

        // create class object
        gps = new GPSTracker(MainActivity.this);

        gps.onLocationChanged(gps.getLocation());

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        lat.setText("" + latitude);
        longi.setText("" + longitude);
        speed.setText("" + gps.getSpeed() + " km/h");
        distance.setText("" + gps.getDistance());
        pos.setLatitude(latitude);
        pos.setLongitude(longitude);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case 0:// R.id.bLocation:
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    double lastLatitude = pos.getLastLatitude();
                    double lastLongitude = pos.getLastLongitude();

                    //gps.getDistanceBetweenPoints(lastLatitude, lastLongitude, latitude, longitude, res);

                    lat.setText("" + latitude);
                    longi.setText("" + longitude);
                    String runner_speed = String.format("%.2f", gps.getSpeed());
                    speed.setText("" + runner_speed + " km/h");
                    String runner_distance = String.format("%.2f", gps.getDistance());
                    distance.setText(""+ runner_distance);


                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                break;
        }
    }












        /*LocationManager locationManager = (LocationManager) this .getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                Log.i("latitude", "" + location.getLatitude());
                lat.setText("" + location.getLatitude());
                Toast.makeText(context, "Current speed:" + location.getSpeed(),
                        Toast.LENGTH_SHORT).show();

                speed.setText(""+location.getSpeed());
                Toast.makeText(context, "Current latitude:" + location.getLatitude(),
                        Toast.LENGTH_SHORT).show();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
*/



}
