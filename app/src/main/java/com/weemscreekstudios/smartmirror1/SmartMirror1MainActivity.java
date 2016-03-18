package com.weemscreekstudios.smartmirror1;

import android.app.Activity;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;



/* Preferences
    AnnapolisOpenWeatherMapAPIHTML  = HTML returned for Annapolis, MD
    LondonOpenWeatherMapAPIHTML = HTML returned for London, UK
    VancouverOpenWeatherMapAPIHTML = HTML returned for Vancouver, WA
    CheltenhamOpenWeatherMapAPIHTML = HTML returned for Cheltenham, UK
 */

public class SmartMirror1MainActivity extends Activity {

    public String PREFS_NAME = "test";

    public String AnnapolisOpenWeatherMapAPIHTML; //HTML returned for Annapolis, MD
    public String LondonOpenWeatherMapAPIHTML; //HTML returned for London, UK
    public String VancouverOpenWeatherMapAPIHTML; //HTML returned for Vancouver, WA
    public String CheltenhamOpenWeatherMapAPIHTML; //HTML returned for Cheltenham, UK

    TextView textViewLastUpdateTime, textViewNextUpdateTime;
    ProgressBar timeToNextUpdateProgressBar;

    Handler handler = new Handler();    //handler for timer updates
    int Counter = 0; //counter for loop to show timer is work
    boolean timerFlag = false;  //flag to know if timer was activitated the first time
    long updateInterval = 600000;   //600,000 millsec is 10 minutes

    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //run in fulls screen mode
        setContentView(R.layout.activity_smart_mirror1_main);  //main activity

        // Hide the status bar and the navigation bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        PREFS_NAME = this.getString(R.string.preferenceName);  //set the PREFS_NAME at create time

        textViewLastUpdateTime = (TextView) findViewById(R.id.textViewLastUpdateTime);
        textViewNextUpdateTime = (TextView) findViewById(R.id.textViewNextUpdateTime);
        timeToNextUpdateProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //String data = this.getString(R.string.weatherlondonHTML);   //data == html data which you want to load
        String data1 = this.getString(R.string.annapolis);   //data == html data which you want to load
        //String webViewUrl = this.getString(R.string.LondonOpenWeatherMapAPI);  //loads the openweathermap API URL
        String webViewIUrl = this.getString(R.string.CheltenhamOpenWeatherMapAPI);  //loads the openweathermap API URL

        webview = (WebView) this.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setInitialScale(400);
       // webview.loadData(data1, "text/html", "UTF-8"); this loads from a string
        //webview.loadUrl("file:///android_asset/openweatherapi-annapolis-black.htm"); //load html file from asset library - works
        webview.loadUrl(webViewIUrl); //works

        String AnnapolisAPIUrl = this.getString(R.string.AnnapolisOpenWeatherMapAPI);

        WebView webview2 = (WebView) this.findViewById(R.id.webView2);
        webview2.getSettings().setJavaScriptEnabled(true);
        //webview2.getSettings().setUseWideViewPort(true);
        //webview2.getSettings().setLoadWithOverviewMode(true);
        //webview2.getSettings().setBuiltInZoomControls(true);
        //webview2.zoomBy(100.0f);
        webview2.setInitialScale(400);
        //webview2.setScaleX(1.0f);
        //webview2.setScaleY(1.0f);
        //webview2.zoomBy(90.0f);

        //webview2.loadUrl(AnnapolisAPIUrl); //works
        //webview2.loadUrl("http://api.openweathermap.org/data/2.5/weather?q=Annapolis&mode=html&appid=40ccc628e578669ca8c47e31599b0d04"); //works
        webview2.loadUrl("file:///android_asset/openweatherapi-london-black.htm"); //load html file from asset library

        Runnable runnable = new Runnable() {
             @Override
             public void run() {
                 Date dateTimeStampNow = new Date(); //initializes to current time
                 long dateTimeNowMillisec = dateTimeStampNow.getTime(); //get current time in millisecs
                 SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); //set format to AM/PM
                 textViewLastUpdateTime.setText(sdf.format(dateTimeStampNow)); //display last update time from a date
                 textViewNextUpdateTime.setText(sdf.format(dateTimeNowMillisec+updateInterval)); //next update time
                 //timeToNextUpdateProgressBar.setProgress(Counter++); //not using at the moment
                 webview.reload();  //refresh the page - API only allows once per 10 minutes
                 handler.postDelayed(this, updateInterval); //20 minutes - 1200 sec = 1,200,000 millisec
             }
         };
        handler.post(runnable);


    }

    protected void onResme(){
       
        /*Note the following:

        With this approach, touching anywhere on the screen causes the navigation bar (and status bar) to reappear and remain visible.
         The user interaction causes the flags to be be cleared. Once the flags have been cleared, your app needs to reset them if you
         want to hide the bars again. See Responding to UI Visibility Changes for a discussion of how to listen for UI visibility changes
          so that your app can respond accordingly. Where you set the UI flags makes a difference. If you hide the system bars in your
          activity's onCreate() method and the user presses Home, the system bars will reappear. When the user reopens the activity, onCreate()
          won't get called, so the system bars will remain visible. If you want system UI changes to persist as the user navigates in and out
          of your activity, set UI flags in onResume() or onWindowFocusChanged(). /*/

        // Hide the status bar and the navigation bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void RestorePrefrences() {
        // Restore preferences
        String defaultText = getString(R.string.loadErrorURLHTML);   //simple HTML to show load error

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        AnnapolisOpenWeatherMapAPIHTML = settings.getString(AnnapolisOpenWeatherMapAPIHTML,defaultText);
        LondonOpenWeatherMapAPIHTML = settings.getString(LondonOpenWeatherMapAPIHTML,defaultText);
        VancouverOpenWeatherMapAPIHTML = settings.getString(VancouverOpenWeatherMapAPIHTML,defaultText);
        CheltenhamOpenWeatherMapAPIHTML = settings.getString(CheltenhamOpenWeatherMapAPIHTML,defaultText);
        //test

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
