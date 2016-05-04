package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parents on 4/8/2016.
 */
public  class snow {
    @SerializedName("3h") private float threeHours;

    public float getThreeHours() {
        return threeHours;
    }
    public void setThreeHours(float threeHoursIn) {
        this.threeHours = threeHours;
    }

    private void snowNullChecking(float threeHoursIn){
        //member function to make sure there are no null values
        System.out.println("snow.snowNullChecking():");

        Float object = new Float(threeHoursIn);
        if (object != null){
            System.out.println("snow.snowNullChecking(): null value");
            threeHours = object;
        }

        else{
            System.out.println("snow.snowNullChecking(): not null value");
            threeHours = 0.0f;
        }

    }

    @Override
    public String toString(){
        snowNullChecking(threeHours);
        StringBuilder sb = new StringBuilder();
        sb.append("snow.3h="+String.valueOf(threeHours));
        return sb.toString();
    }
}