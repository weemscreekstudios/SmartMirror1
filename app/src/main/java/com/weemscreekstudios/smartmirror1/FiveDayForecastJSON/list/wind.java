package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

/**
 * Created by Parents on 4/8/2016.
 */

public class wind {
    private float speed;
    private float deg;

    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDeg() {
        return deg;
    }
    public void setDeg(float deg) {
        this.deg = deg;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("wind.speed="+String.valueOf(getSpeed())+", wind.deg="+String.valueOf(getDeg()));
        return sb.toString();
    }

}

