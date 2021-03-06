package com.weemscreekstudios.smartmirror1;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.weemscreekstudios.smartmirror1.model.Location;
import com.weemscreekstudios.smartmirror1.model.Weather;



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

    TextView textViewCityName, textViewWeatherDescription, textViewCloudPercentage, textViewWindSpeed, textViewCurrentTemp, textViewHiTemp, textViewLowTemp, textViewHumidity;
    TextView textViewSunset, textViewSunrise, textViewDataTime, textViewPercipitationTotal3Hrs, textViewPressure;
    Weather liveWeather = new Weather(); //global to hold the returned weather
    ImageView imageViewWeatherIcon;
    DecimalFormat df = new DecimalFormat("#.#");

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
        //setContentView(R.layout.activity_smart_mirror1_main);  //main activity
        //setContentView(R.layout.activity_main_json_layoutv2);
        setContentView(R.layout.activity_main_json_layoutv3);


        // Hide the status bar and the navigation bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        PREFS_NAME = this.getString(R.string.preferenceName);  //set the PREFS_NAME at create time

        textViewLastUpdateTime = (TextView) findViewById(R.id.textViewLastUpdateTime);
        textViewNextUpdateTime = (TextView) findViewById(R.id.textViewNextUpdateTime);
        timeToNextUpdateProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        textViewCityName = (TextView) findViewById(R.id.textViewCityName);
        textViewWeatherDescription = (TextView) findViewById(R.id.textViewWeatherDescription);
        textViewCloudPercentage = (TextView) findViewById(R.id.textViewCloudPercentage);
        textViewWindSpeed = (TextView)findViewById(R.id.textViewWindSpeed);
        textViewCurrentTemp = (TextView) findViewById(R.id.textViewCurrentTemp);
        textViewHiTemp = (TextView) findViewById(R.id.textViewHiTemp);
        textViewLowTemp = (TextView) findViewById(R.id.textViewLowTemp);
        textViewSunrise = (TextView) findViewById(R.id.textViewSunrise);
        textViewSunset = (TextView) findViewById(R.id.textViewSunset);
        textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
        textViewDataTime = (TextView) findViewById(R.id.textViewDataTime);
        textViewPercipitationTotal3Hrs = (TextView) findViewById(R.id.textViewPercipitationTotal3Hrs);
        textViewPressure = (TextView)findViewById(R.id.textViewPressure);
        imageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);
/*
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
        Log.d(TAG, "webview2.loadUrl(file:///android_asset/openweatherapi-london-black.htm) - just happended");  */

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
                 //webview.reload();  //refresh the page - API only allows once per 10 minutes
                 //Log.d(TAG, "webview.reload() just happened");
                 JSONWeatherTask task = new JSONWeatherTask();  //retrieve the data from OpenWeatherMap API on internet
                 task.execute(new String[]{"test"}); //city is actually hardcoded at the moment
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

        //read in JSON data from string.xml that has been modifed to escape the quotation marks
        //pare the JSON download
        Weather weatherJSON = new Weather();  //create weather object
        String weatherJSONFullString = this.getString(R.string.CairnsJSONPlus);  //retrieve JSON with escaped quotes from string.xml
        try {
            weatherJSON = JSONWeatherParser.getWeather(weatherJSONFullString);
            Update_Weather_Display(weatherJSON);  //update display with canned Cairns data
            System.out.println("weatherJSON updated display with canned Cairns weather " );  //print out JSON - comes out in alphabetical order
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("weatherJSONFullString.getDescr(): " + weatherJSON.currentCondition.getDescr() + ", temp = " + String.valueOf(weatherJSON.temperature.getTemp()));  //print out JSON - comes out in alphabetical order


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

    public void Update_Weather_OnClick(View v){
        //handle the buttonUpdateWeather Clicks
        System.out.println("Update_Weather_OnClick");  //print out JSON - comes out in alphabetical order
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{"test"}); //city is acutally hardcoded at the moment
        System.out.println("Update_Weather_OnClick: task.execute(new String[]{\"test\"})");  //print out JSON - comes out in alphabetical order
    }

    public Drawable Get_Weather_Icon(String weatherIconName){
        //member function to help get the correct image based on the returned icon name
        Drawable iconresourceID = getResources().getDrawable(R.drawable.brokenicon, getTheme());
        System.out.println("Get_Weather_Icon(): "+ weatherIconName);  //print out JSON - comes out in alphabetical order

        switch(weatherIconName){
            case "01d":{
                iconresourceID = getResources().getDrawable(R.drawable.weather01d, getTheme());;
                break;
            }
            case "01n":{
                iconresourceID = getResources().getDrawable(R.drawable.weather01n, getTheme());;
                break;
            }
            case "02n":{
                iconresourceID = getResources().getDrawable(R.drawable.weather02n, getTheme());;
                break;
            }
            case "02d":{
                iconresourceID = getResources().getDrawable(R.drawable.weather02d, getTheme());;
                break;
            }
            case "03n":{
                iconresourceID = getResources().getDrawable(R.drawable.weather03n, getTheme());;
                break;
            }
            case "03d":{
                iconresourceID = getResources().getDrawable(R.drawable.weather03d, getTheme());;
                break;
            }
            case "04d":{
                iconresourceID = getResources().getDrawable(R.drawable.weather04d, getTheme());;
                break;
            }
            case "04n":{
                iconresourceID = getResources().getDrawable(R.drawable.weather04n, getTheme());;
                break;
            }
            case "09d": {
                iconresourceID = getResources().getDrawable(R.drawable.weather09d, getTheme());;
                break;
            }
            case "09n": {
                iconresourceID = getResources().getDrawable(R.drawable.weather09n, getTheme());;
                break;
            }
            case "10d": {
                iconresourceID = getResources().getDrawable(R.drawable.weather10d, getTheme());;
                break;
            }
            case "10n": {
                iconresourceID = getResources().getDrawable(R.drawable.weather10n, getTheme());;
                break;
            }
            case "11d": {
                iconresourceID = getResources().getDrawable(R.drawable.weather11d, getTheme());;
                break;
            }
            case "11n": {
                iconresourceID = getResources().getDrawable(R.drawable.weather11n, getTheme());;
                break;
            }
            case "50n":{
                iconresourceID = getResources().getDrawable(R.drawable.weather50n, getTheme());;
                break;
            }
            case "50d":{
                iconresourceID = getResources().getDrawable(R.drawable.weather50d, getTheme());;
                break;
            }
            default:{
            }
        }
        return iconresourceID;

    }


    //updates the activity with the weather data
    public void Update_Weather_Display(Weather weather){
        System.out.println("Update_Weather_Display(): updating text fields");  //print out JSON - comes out in alphabetical order
        textViewCityName.setText(weather.location.getCity());  //display city name
        textViewWeatherDescription.setText(weather.currentCondition.getCondition() + ", " + weather.currentCondition.getDescr());  //display the more detailed descriptions
        textViewCloudPercentage.setText(String.valueOf(weather.clouds.getPerc())+ "\u0025");  //display the cloud percentage data
        textViewWindSpeed.setText("W " +String.valueOf(weather.wind.getSpeed())+ " mph\u2196");  //display the wind speed
        textViewCurrentTemp.setText(String.valueOf(weather.temperature.getTemp())+ " \u2109");  //display the more detailed descriptions
        textViewHiTemp.setText("H "+String.valueOf(weather.temperature.getMaxTemp())+ " \u2109");  //display the more detailed descriptions
        textViewLowTemp.setText("L "+String.valueOf(weather.temperature.getMinTemp())+ " \u2109");  //display the more detailed descriptions
        textViewHumidity.setText("H " + String.valueOf(weather.currentCondition.getHumidity()) + "\u0025");  //display the more detailed descriptions

        textViewSunrise.setText(weather.location.getSunriseDateFormated("hh:mm aa"));  //display sunrise time formatted as above
        textViewSunset.setText(weather.location.getSunsetDateFormated("hh:mm aa"));  //display sunrise time formatted as above

        textViewPressure.setText("P " + String.valueOf(df.format(weather.currentCondition.getPressureImperial())) + "\u2033\u2196");
        textViewPercipitationTotal3Hrs.setText("∑rain " + String.valueOf(df.format(weather.rain.getAmmount())) + "″/3hrs");
        System.out.println("Update_Weather_Display(): updating text fields" + "P " + String.valueOf(df.format(weather.currentCondition.getPressureImperial())) + "\u2033\u2196");  //print out JSON - comes out in alphabetical order
        System.out.println("Update_Weather_Display(): updating text fields" + "∑rain " + String.valueOf(df.format(weather.rain.getAmmount())) + "″/3hrs");  //print out JSON - comes out in alphabetical order


        imageViewWeatherIcon.setImageDrawable(Get_Weather_Icon(weather.currentCondition.getIcon()));
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            //String data = "http://api.openweathermap.org/data/2.5/weather?q=Cheltenham&units=imperial&appid=40ccc628e578669ca8c47e31599b0d04";
            //String data = ((new WeatherHttpClient()).getWeatherData(params[0]));  //original passes array of city names
            String data = ((new WeatherHttpClient()).getWeatherData("Cheltenham&units=imperial", getString(R.string.OpenWeatherMapBaseURL), getString(R.string.OpenWeatherMApAppIDPrefix)+getString(R.string.OpenWeatherMapAppID)));  //new single city, base url, and appID

            System.out.println("JSONWeatherTask: data string = " + data);  //print out JSON - comes out in alphabetical order
            try {
                System.out.println("JSONWeatherTask: try{}");
                weather = JSONWeatherParser.getWeather(data);
                System.out.println("JSONWeatherTask: "+ weather.currentCondition.getCondition() + ", " + weather.currentCondition.getDescr());

                // Let's retrieve the icon
               // weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));  do not retrieve the icon

            } catch (JSONException e) {
                System.out.println("JSONWeatherTask: catch{}");
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {

       /* if (weather.iconData != null && weather.iconData.length > 0) {
            System.out.println("JSONWeatherTask: onPostExecute: retrieving weather icon");
            Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
            imageViewWeatherIcon.setImageBitmap(img);
        } */
        /*else{
            imageViewWeatherIcon.setImageResource(R.drawable.brokenicon); //show broken image icon
            System.out.println("JSONWeatherTask: onPostExecute: weather icon did not retrieve");
        }*/
            String returnedWeatherData = weather.location.getCity() + "," + weather.location.getCountry()+ weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C";
            System.out.println("JSONWeatherTask: onPostExecute: " + returnedWeatherData);  //print out JSON - comes out in alphabetical order

            System.out.println("JSONWeatherTask: onPostExecute: Updated text fields");  //print out JSON - comes out in alphabetical order
            Update_Weather_Display(weather);

        }
    }

}
