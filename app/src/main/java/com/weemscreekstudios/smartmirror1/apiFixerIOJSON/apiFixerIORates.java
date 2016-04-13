package com.weemscreekstudios.smartmirror1.apiFixerIOJSON;

/**
 * Created by Parents on 4/6/2016.
 * <p/>
 * class to parse http://api.fixer.io/latest?base=USD
 * {"base":"USD","date":"2016-04-06","rates":{"AUD":1.3258,"BGN":1.7253,"BRL":3.705,"CAD":1.3155,"CHF":0.96039,"CNY":6.4882,
 * "CZK":23.844,"DKK":6.5642,"GBP":0.71284,"HKD":7.7564,"HRK":6.6249,"HUF":275.52,"IDR":13254.0,"ILS":3.8193,"INR":66.671,
 * "JPY":110.38,"KRW":1163.0,"MXN":17.774,"MYR":3.9397,"NOK":8.3592,"NZD":1.4736,"PHP":46.274,"PLN":3.7611,"RON":3.9419,
 * "RUB":68.636,"SEK":8.1711,"SGD":1.3561,"THB":35.295,"TRY":2.8531,"ZAR":15.244,"EUR":0.88215}}
 *
 */


public class apiFixerIORates {

    private float GBP;
    private float USD;
    private float EUR;


    public float getGBP() {
        return GBP;
    }
    public void setGBP(float GBPIn){this.GBP = GBPIn;}

    public float getUSD(){return USD;}
    public void setUSD(float USDIn ) {
        this.USD= USDIn;
    }

    public float getEUR(){return EUR;}
    public void setEUR(float EURIn ) {
        this.EUR= EURIn;
    }

    @Override
    public String toString(){
        return "GBP="+ String.valueOf(getGBP()) + ", EUR="+String.valueOf(getEUR());
    }
}
