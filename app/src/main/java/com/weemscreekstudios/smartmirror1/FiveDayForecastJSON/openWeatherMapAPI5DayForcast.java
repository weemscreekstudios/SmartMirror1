package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.city.city;
import com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list.list;

import java.io.Serializable;

/**
 * Created by Parents on 4/7/2016.
 */
public class openWeatherMapAPI5DayForcast implements Serializable {

    @SerializedName("city") public city city;
    @SerializedName("cod")   public int cod;
    public float message;
    public int cnt;
    public list[] list;

    public int getCod() {
        return cod;
    }
    public void setCod(int codIn) {
        this.cod = codIn;
    }

    public int getcnt() {
        return cnt;
    }
    public void setcnt(int cntIn) {
        this.cnt = cntIn;
    }

    public float getmessage() {
        return message;
    }
    public void setmessage(float messageIn) {
        this.message = messageIn;
    }

    public city getCity() {
        return city;
    }
    public void setCity(city cityIn) {
        this.city = cityIn;
    }

  /*  public list[] getList(){return list;}
    public void setList(list[] listIn){this.list = listIn;}; */
    public list[] getList(){return list;}
    public void setList(list[] listIn){this.list = listIn;};

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("cnt="+String.valueOf(cnt) +", cod="+String.valueOf(cod)+", message="+String.valueOf(message)+", city="+city.toString());
        //sb.append(list[0].toString());
        sb.append("\nlist.length="+String.valueOf(list.length)+"\n");
        for (int i = 0; i < list.length; i++){
            sb.append("\n"+ list[i].toString());
        }
        return sb.toString();
    }

    public boolean openWeatherMapAPI5DayForcastDeserializer(String JSONIn){
        System.out.println("openWeatherMapAPI5DayForcast.openWeatherMapAPI5DayForcastDeserializer():starting");
        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        openWeatherMapAPI5DayForcast weatherdata = new openWeatherMapAPI5DayForcast();
        weatherdata = gson.fromJson(JSONIn, openWeatherMapAPI5DayForcast.class);// parse json string to object
        this.setCity(weatherdata.getCity());
        this.setcnt(weatherdata.getcnt());
        this.setCod(weatherdata.getCod());
        this.setList(weatherdata.getList());
        this.setmessage(weatherdata.getmessage());
        System.out.println("openWeatherMapAPI5DayForcast.openWeatherMapAPI5DayForcastDeserializer():returning");
        return true;
    }

}
