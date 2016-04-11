package com.weemscreekstudios.smartmirror1.apiFixerIOJSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Parents on 4/6/2016.
 * <p/>
 * * class to parse http://api.fixer.io/latest?base=USD
 * {"base":"USD","date":"2016-04-06","rates":{"AUD":1.3258,"BGN":1.7253,"BRL":3.705,"CAD":1.3155,"CHF":0.96039,"CNY":6.4882,
 * "CZK":23.844,"DKK":6.5642,"GBP":0.71284,"HKD":7.7564,"HRK":6.6249,"HUF":275.52,"IDR":13254.0,"ILS":3.8193,"INR":66.671,
 * "JPY":110.38,"KRW":1163.0,"MXN":17.774,"MYR":3.9397,"NOK":8.3592,"NZD":1.4736,"PHP":46.274,"PLN":3.7611,"RON":3.9419,
 * "RUB":68.636,"SEK":8.1711,"SGD":1.3561,"THB":35.295,"TRY":2.8531,"ZAR":15.244,"EUR":0.88215}}
 */
public class apiFixerIOBase implements Serializable {

    @SerializedName("base")
    private String base;
    private String date;
    public apiFixerIORates rates;

    public String getbase(){return base;}
    public void setbase(String baseIn){this.base = baseIn;}

    public String getdate(){return date;}
    public void setdate(String datein){this.date = datein;}

    public apiFixerIORates getrates(){return rates;}
    public void setrates(apiFixerIORates ratesIn){this.rates = ratesIn;}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("***** Currency Exchange Details *****\n");
        sb.append("base="+getbase()+"\n");
        sb.append("date="+getdate()+"\n");
        sb.append("rates="+getrates()+"\n");
        sb.append("*****************************");
        return sb.toString();
    }

    public boolean apiFixerDeserializer(String JSONIn){
        System.out.println("apiFixerIOBase.apiFixerDeserializer() starting");

        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        apiFixerIOBase apidata = new apiFixerIOBase();  //temp variable
        // parse json string to object
        apidata = gson.fromJson(JSONIn, apiFixerIOBase.class);
        this.setbase(apidata.getbase());
        this.setdate(apidata.getdate());
        this.setrates(apidata.getrates());
        System.out.println("apiFixerIOBase.apiFixerDeserializer() returning");
        return true;
    }

    //method to make sure the created object has values in it
    public void loadDefaultData(){
        this.base = "ZZZ";
        this.date = "99 Jan 9999 11:11 AM";
        this.rates.setGBP(9.9999f);
        this.rates.setUSD(8.8888f);
    }

}



