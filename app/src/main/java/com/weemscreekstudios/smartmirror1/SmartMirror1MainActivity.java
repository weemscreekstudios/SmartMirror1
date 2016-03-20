package com.weemscreekstudios.smartmirror1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import java.text.SimpleDateFormat;
import java.util.Date;



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

    Handler handlerURLRefresh = new Handler();    //handlerURLRefresh for URL refresh timer updates (15 minutes +)
    Handler handlerProgressBar = new Handler();    //handler progress bar refresh timer updates (15 minutes +)

    int Counter = 1; //counter for loop to show timer is work
    boolean timerFlag = false;  //flag to know if timer was activitated the first time
    long updateIntervalURLRefresh = 900000;   //600,000 millsec is 10 minutes, 900,000 is 15 mins.
    long updateIntervalProgressBarRefresh = updateIntervalURLRefresh/100; //progress bar is 0 to 100

    WebView webview;

    private static final String TAG = SmartMirror1MainActivity.class.getSimpleName(); //set tag for debug logs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() - resetting full screen and hiding navigation bar");
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
        Log.d(TAG, "webview.loadUrl(webViewIUrl) just happened");

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
        Log.d(TAG, "webview2.loadUrl(file:///android_asset/openweatherapi-london-black.htm) - just happended");

        Runnable runnableURLRefresh = new Runnable() {
             @Override
             public void run() {
                 Log.d(TAG, "RunnableURLRefresh.run() - in the time function");
                 Date dateTimeStampNow = new Date(); //initializes to current time
                 long dateTimeNowMillisec = dateTimeStampNow.getTime(); //get current time in millisecs
                 SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); //set format to AM/PM
                 textViewLastUpdateTime.setText(sdf.format(dateTimeStampNow)); //display last update time from a date
                 textViewNextUpdateTime.setText(sdf.format(dateTimeNowMillisec + updateIntervalURLRefresh)); //next update time
                 //timeToNextUpdateProgressBar.setProgress(Counter++); //not using at the moment
                 webview.reload();  //refresh the page - API only allows once per 10 minutes
                 Log.d(TAG, "webview.reload() just happened");
                 Counter = 1; //reset the progress bar counter
                 timeToNextUpdateProgressBar.setProgress(Counter);  //reset progress bar
                 Log.d(TAG, "RunnableURLRefresh timeToNextUpdateProgressBar.setProgress(1) just happened");
                 handlerURLRefresh.postDelayed(this, updateIntervalURLRefresh); //20 minutes - 1200 sec = 1,200,000 millisec
             }
         };
        handlerURLRefresh.post(runnableURLRefresh);

        Runnable runnableProgressBarRefresh = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "runnableProgressBarRefresh.run() - in the time function");
                Log.d(TAG, "updateIntervalURLRefresh ="+String.valueOf(updateIntervalProgressBarRefresh));
                Log.d(TAG, "timeToNextUpdateProgressBar.setProgress(Counter++ just happened): Counter=" + String.valueOf(Counter));
                timeToNextUpdateProgressBar.setProgress(Counter++);
                handlerURLRefresh.postDelayed(this, updateIntervalProgressBarRefresh); //updateIntervalURLRefresh/100
            }
        };
        handlerProgressBar.post(runnableProgressBarRefresh);

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

        Log.d(TAG, "onResume() - resetting full screen and hiding navigation bar");
    }

    public void RestorePrefrences() {
        // Restore preferences
        Log.d(TAG, "In RestorePrefences()");
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
        Log.d(TAG, "In StorePrefences()");
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
