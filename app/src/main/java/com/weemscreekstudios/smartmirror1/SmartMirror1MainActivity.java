package com.weemscreekstudios.smartmirror1;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.content.SharedPreferences;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/* Preferences
    AnnapolisOpenWeatherMapAPIHTML  = HTML returned for Annapolis, MD
    LondonOpenWeatherMapAPIHTML = HTML returned for London, UK
    VancouverOpenWeatherMapAPIHTML = HTML returned for Vancouver, WA
    CheltenhamOpenWeatherMapAPIHTML = HTML returned for Cheltenham, UK
 */

public class SmartMirror1MainActivity extends AppCompatActivity {

    public String PREFS_NAME = "test";

    public String AnnapolisOpenWeatherMapAPIHTML; //HTML returned for Annapolis, MD
    public String LondonOpenWeatherMapAPIHTML; //HTML returned for London, UK
    public String VancouverOpenWeatherMapAPIHTML; //HTML returned for Vancouver, WA
    public String CheltenhamOpenWeatherMapAPIHTML; //HTML returned for Cheltenham, UK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_mirror1_main);

        PREFS_NAME = this.getString(R.string.preferenceName);  //set the PREFS_NAME at create time

        //String data = this.getString(R.string.weatherlondonHTML);   //data == html data which you want to load
        String data1 = this.getString(R.string.annapolis);   //data == html data which you want to load

        WebView webview = (WebView) this.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadData(data1, "text/html", "UTF-8");

        String AnnapolisAPIUrl = this.getString(R.string.AnnapolisOpenWeatherMapAPI);

        WebView webview2 = (WebView) this.findViewById(R.id.webView2);
        webview2.getSettings().setJavaScriptEnabled(true);
        webview2.loadUrl(AnnapolisAPIUrl); //works
        //webview2.loadUrl("http://api.openweathermap.org/data/2.5/weather?q=Annapolis&mode=html&appid=40ccc628e578669ca8c47e31599b0d04"); //works

    }

    public void RestorePrefrences() {
        // Restore preferences
        String defaultText = getString(R.string.loadErrorURLHTML);   //simple HTML to show load error

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        AnnapolisOpenWeatherMapAPIHTML = settings.getString(AnnapolisOpenWeatherMapAPIHTML,defaultText);
        LondonOpenWeatherMapAPIHTML = settings.getString(LondonOpenWeatherMapAPIHTML,defaultText);
        VancouverOpenWeatherMapAPIHTML = settings.getString(VancouverOpenWeatherMapAPIHTML,defaultText);
        CheltenhamOpenWeatherMapAPIHTML = settings.getString(CheltenhamOpenWeatherMapAPIHTML,defaultText);

    }

    public void StorePrefrences() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();



        editor.putString("AnnapolisOpenWeatherMapAPIHTML",AnnapolisOpenWeatherMapAPIHTML ); //store the HTML values for reuse
        editor.putString("LondonOpenWeatherMapAPIHTML",LondonOpenWeatherMapAPIHTML); //store the HTML values for reuse
     /*   editor.putString(VancouverOpenWeatherMapAPIHTML,); //store the HTML values for reuse
        editor.putString(CheltenhamOpenWeatherMapAPIHTML,); //store the HTML values for reuse
        */
        // Commit the edits!
        editor.commit();

    }



}
