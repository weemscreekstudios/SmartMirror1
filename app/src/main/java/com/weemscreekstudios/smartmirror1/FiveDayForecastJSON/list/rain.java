package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parents on 4/8/2016.
 */
public  class rain {
    @SerializedName("3h") private float threeHours;


    public float getThreeHours() {
        return threeHours;
    }
    public void setThreeHours(float threeHoursIn) {
        this.threeHours = threeHours;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("rain.3h="+String.valueOf(threeHours));
        return sb.toString();
    }

}