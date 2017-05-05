package com.compete.ppj.compete_test.Rankings.listAdapter;

/**
 * Created by Pol on 28/12/2015.
 */

import android.graphics.Bitmap;

public class RankingRunnerBlock {
    Bitmap image;
    String runnerName, runnerPoints;
    public RankingRunnerBlock(Bitmap image, String name, String points) {
        super();
        this.image = image;
        runnerName = name;
        runnerPoints = points;
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
    public String getRunnerPoints(){ return runnerPoints; }
    public void setRunnerPoints(String _points) {
        runnerPoints = _points;
    }


}