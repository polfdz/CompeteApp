package com.compete.ppj.compete_test.Race;

/**
 * Created by Pol on 16/10/2015.
 */
public class Position {
    double distance, latitude, longitude;
    public Position(){}

    public double getDistance(){
        return distance;
    }
    public void sumDistance(int _distance){

    }
    public void setLatitude(double _lat){
        latitude = _lat;
    }
    public void setLongitude(double _long){
        longitude = _long;
    }
    public double getLastLongitude(){
        return longitude;
    }
    public double getLastLatitude(){
        return latitude;
    }
}
