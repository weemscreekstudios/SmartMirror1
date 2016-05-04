package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parents on 4/8/2016.
 */
public  class sys {
    @SerializedName("pod")
    private String pod;


    public String getPod() {
        return pod;
    }
    public void setPod(String podIn) {
        this.pod = podIn;
    }

    @Override
    public String toString(){
        return "sys.pod="+pod;
    }

}