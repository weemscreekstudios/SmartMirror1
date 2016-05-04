package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parents on 4/8/2016.
 */
public class main {

    @SerializedName("temp")
    private float temp;  //list.main.temp
    private float temp_min; //list.main.temp_min
    private float temp_max; //list.main.temp_max

    private float pressure;
    private float sea_level;  //list.main.sea_level
    private float grnd_level;   //list.main.sea_level

    @SerializedName("humidity")
    private float humidity;  //list.main.humidity
    public String temp_kf;   //list.main.temp_kf

    public float getTemp() {
        return temp;
    }
    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float gettemp_min() {
        return temp_min;
    }
    public void settemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float gettemp_max() {
        return temp_max;
    }
    public void settemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getpressure() {
        return pressure;
    }

    public float getpressureImperial() {
        float pressureImp = pressure/38.63886666718317f;
        return pressureImp;
    }
    public void setpressure(float pressure) {
        this.pressure = pressure;
    }

    public float getsea_level() {
        return sea_level;
    }
    public void setsea_level(float sea_level) {
        this.sea_level = sea_level;
    }

    public float getgrnd_level() {
        return grnd_level;
    }
    public void setgrnd_level(float grnd_level) {
        this.grnd_level = grnd_level;
    }

    public float gethumidity() {
        return humidity;
    }
    public void sethumidity(float humidityIn) {
        this.humidity = humidityIn;
    }

    public String getTemp_kf(){return temp_kf;}
    public void setTemp_kf(String temp_kf){this.temp_kf=temp_kf;}



    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("main.temp="+String.valueOf(temp) +", main.temp_min="+String.valueOf(temp_min)+", main.temp_max="+String.valueOf(temp_max));
        sb.append(", main.pressure="+String.valueOf(pressure)+", humidity="+String.valueOf(humidity)+", temp_kf="+temp_kf);
        return sb.toString();
    }
}
