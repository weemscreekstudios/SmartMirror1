package com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.city;

/**
 * Created by Parents on 4/7/2016.
 */
public class city {

    public String id;
    public String name;
    public coord coord;
    public String country;
    private String population;

    public String getid() {
        return id;
    }
    public void setid(String idIn) {
        id = idIn;
    }

    public String getname() {return name; }
    public void setname(String name) {
        this.name = name;
    }

    public String getPopulation() {return population; }
    public void setPopulation(String population) {
        this.population = population;
    }

    public String getcountry() {return country; }
    public void setcountry(String country) {
            this.country = country;
    }

    public coord getcoord() {
        return coord;
    }
    public void setcoord(coord coordIn) {
        this.coord = coordIn;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id="+String.valueOf(getid())+", name="+String.valueOf(getname())+", coord="+getcoord()+", country="+country+", population="+population);
        return sb.toString();
    }

}
