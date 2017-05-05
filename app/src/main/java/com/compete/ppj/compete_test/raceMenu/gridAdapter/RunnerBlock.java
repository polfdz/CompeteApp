package com.compete.ppj.compete_test.raceMenu.gridAdapter;

/**
 * Created by Pol on 16/11/2015.
 */
import android.graphics.Bitmap;

/**
 *
 * @author manish.s
 *
 */

public class RunnerBlock {
    Bitmap image;
    String runnerName, runnerDistance;
    Bitmap connection;
    public RunnerBlock(Bitmap image, String name, Bitmap connection, String distance) {
        super();
        this.image = image;
        runnerName = name;
        this.connection = connection;
        runnerDistance = distance;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getRunnerName() {
        return runnerName;
    }
    public void setRunnerName(String _runnerName) {
        runnerName = _runnerName;
    }
    public String getRunnerDistance(){ return runnerDistance; }
    public void setRunnerDistance(String _distance) {
        runnerDistance = _distance;
    }

    public Bitmap getImageConnection(){ return this.connection;}
    public void setConnection(Bitmap connection){ this.connection = connection;}

}