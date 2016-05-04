package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

/**
 * Created by Parents on 4/8/2016.
 */
public  class clouds {
    private String all;

    public String getPerc() {
        return all;
    }

    public void setPerc(String allIn) {
        this.all = allIn;
    }

    @Override
    public String toString(){
        return all;
    }
}