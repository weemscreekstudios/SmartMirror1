package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.city;

/**
 * Created by Parents on 4/7/2016.
 *
 * 		"coord":{
            "lon":138.933334,
             "lat":34.966671
            },
 */
public class coord {

    private float lon;
    private float lat;

    public float getLon() {return lon; }
    public void setLon(float lon) {
        this.lon = lon;
    }
    public float getLat() {
        return lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("lat="+String.valueOf(getLat())+", lon="+String.valueOf(getLon()));
        return sb.toString();
    }
}
