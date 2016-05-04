package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parents on 4/8/2016.
 *
 *        "weather":[{
            "id":804,
            "main":"Clouds",
             "description":"overcast clouds",
             "icon":"04d"
            }],
 *
 *
 */
public  class weather {
    @SerializedName("id") private String weatherId;
    //@SerializedName("main")
    private String main;
    //@SerializedName("description")
    private String description;
    //@SerializedName("icon")
    private String icon;



    //private long dt;

    public String getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("weather.id=" + weatherId + ", weather.main=" + main + ", weather.description=" + description + ", weather.icon=" + icon);
        return sb.toString();
    }


}