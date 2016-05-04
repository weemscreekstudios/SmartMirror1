package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

/**
 * Created by Parents on 4/8/2016.
 */
public class list {

    @SerializedName("dt") private long dt;
    @SerializedName("weather") private weather[] weather;
    @SerializedName("main") private main main;
    @SerializedName("snow") snow snow;
    @SerializedName("wind")private wind wind;
    @SerializedName("rain")private rain rain;
    @SerializedName("clouds")private clouds clouds;
    @SerializedName("dt_text")private String dt_text;
    @SerializedName("sys")private sys sys;


    public void setdt(long dtIn) {dt = dtIn;  }
    public long getdt() {return dt;}

    public void setWeather(weather[] weatherIn){this.weather = weatherIn;}
    public weather[] getWeather(){return weather;}

    public void setMain(main mainIn){this.main = mainIn;}
    public main getMain() {return main;}

    public void setRain(rain rainIn){this.rain = rainIn;}
    public rain getRain(){return rain;}

    public void setSnow(snow snowIn){this.snow = snowIn;}
    public snow getSnow(){return snow;}

    public void setWind(wind windIn){this.wind = windIn;}
    public wind getWind(){return wind;}

    public void setClouds(clouds cloudsIn){this.clouds = cloudsIn;}
    public clouds getClouds(){return clouds;}

    public void setSys(sys sysIn){this.sys = sysIn;}
    public sys getSys(){return sys;}

    public void setDt_text(String dt_textIn){this.dt_text = dt_textIn;}
    public String getDt_text(){return dt_text;}

  /*  @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("list.dt=" + String.valueOf(dt) + ", main=" + main + ", weather=" + "WWWWWWW");
        sb.append("clouds=" + clouds + ", wind=" + wind + ", rain=" + rain + ", snow="+snow + ", dt_text="+dt);
        return sb.toString();
    } */

    @Override
    public String toString(){
        listNullChecking();
        StringBuilder sb = new StringBuilder();
        sb.append("list.dt=" + String.valueOf(dt));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa"); //set format to AM/PM
        String datatime = String.valueOf(sdf.format(dt*1000));  //need to convert from millise
        sb.append(", list.dt="+datatime);
        sb.append(", dt_text="+dt_text+", clouds="+clouds.toString()+wind.toString());
        sb.append(", "+rain.toString());
        //sb.append(", snow="+snow.toString());
        sb.append(", "+sys.toString());
        sb.append(", "+main.toString());
        sb.append(", weather="+weather[0].toString());
        return sb.toString();
    }

    private void listNullChecking(){
        //member function to make sure there are no null values
        if (dt_text == null)
            dt_text = "YYYY-MM-DD HH:MM:SS";
    }
}
