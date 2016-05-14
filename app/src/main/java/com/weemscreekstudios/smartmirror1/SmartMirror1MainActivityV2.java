package com.weemscreekstudios.smartmirror1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.weemscreekstudios.smartmirror1.JSON_HTTP_Retrieval.JSON_HTTP_Retrieval;
import com.weemscreekstudios.smartmirror1.apiFixerIOJSON.apiFixerIOBase;
import com.weemscreekstudios.smartmirror1.FiveDayForecastJSON.openWeatherMapAPI5DayForcast;
import com.weemscreekstudios.smartmirror1.model.Weather;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/* Preferences
    AnnapolisOpenWeatherMapAPIHTML  = HTML returned for Annapolis, MD
    LondonOpenWeatherMapAPIHTML = HTML returned for London, UK
    VancouverOpenWeatherMapAPIHTML = HTML returned for Vancouver, WA
    CheltenhamOpenWeatherMapAPIHTML = HTML returned for Cheltenham, UK
 */

public class SmartMirror1MainActivityV2 extends Activity {

    public String PREFS_NAME = "test";


    TextView textViewLastUpdateTime, textViewNextUpdateTime;
    ProgressBar timeToNextUpdateProgressBar;

    TextView textViewCityName, textViewCurrentTemp;
    TextView textViewSunset, textViewSunrise, textViewDataTime, textViewPercipitationTotal3Hrs, textViewVersion, textViewNetConnectStatus;
    TextView textViewGBPDollars, textViewEuroDollars;

    TextView[][] textViewWeatherForecast = new TextView[5][6];

    ImageView imageViewWeatherIcon;
    DecimalFormat df = new DecimalFormat("#.#");
    DecimalFormat dfXX = new DecimalFormat("#.##");

    Handler handlerURLRefresh = new Handler();    //handlerURLRefresh for URL refresh timer updates (15 minutes +)
    Handler handlerProgressBar = new Handler();    //handler progress bar refresh timer updates (15 minutes +)

    int Counter = 1; //counter for loop to show timer is work
    boolean timerFlag = false;  //flag to know if timer was activitated the first time
    long updateIntervalURLRefresh = 1800000;   //600,000 millsec is 10 minutes, 900,000 is 15 mins.
    long updateIntervalProgressBarRefresh = updateIntervalURLRefresh/100; //progress bar is 0 to 100


    private static final String TAG = SmartMirror1MainActivityV2.class.getSimpleName(); //set tag for debug logs

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    int netConnectStatusCount = 0;  //variable to hold the number of updates successful
    boolean netConnectStatus = false; //false = no network connection

    apiFixerIOBase apiCurrencyExchangeData = new apiFixerIOBase();  //object to store the currency exchange ratees
    JSON_HTTP_Retrieval exchangeData = new JSON_HTTP_Retrieval();   //object to handle retrieving the information from the internet

    openWeatherMapAPI5DayForcast FiveDayForecastData = new openWeatherMapAPI5DayForcast();
    JSON_HTTP_Retrieval FiveDayWeatherData = new JSON_HTTP_Retrieval();      //object to handle retrieving the information from the internet


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() - resetting full screen and hiding navigation bar");
        requestWindowFeature(Window.FEATURE_NO_TITLE); //run in fulls screen mode
        //setContentView(R.layout.activity_main_json_layoutv4);
        setContentView(R.layout.activity_main_json_layoutv5_draft);

        // Hide the status bar and the navigation bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        PREFS_NAME = this.getString(R.string.preferenceName);  //set the PREFS_NAME at create time

        textViewLastUpdateTime = (TextView) findViewById(R.id.textViewLastUpdateTime);
        textViewNextUpdateTime = (TextView) findViewById(R.id.textViewNextUpdateTime);
        timeToNextUpdateProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        textViewCityName = (TextView) findViewById(R.id.textViewCityName);
        textViewCurrentTemp = (TextView) findViewById(R.id.textViewCurrentTemp);
        textViewSunrise = (TextView) findViewById(R.id.textViewSunrise);
        textViewSunset = (TextView) findViewById(R.id.textViewSunset);
        textViewDataTime = (TextView) findViewById(R.id.textViewDataTime);
        textViewPercipitationTotal3Hrs = (TextView) findViewById(R.id.textViewPercipitationTotal3Hrs);
        textViewVersion = (TextView)findViewById(R.id.textViewVersionNumber);
        textViewNetConnectStatus = (TextView) findViewById(R.id.textViewNetConnectStatus);
        imageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);

        textViewWeatherForecast[0][0] = (TextView)findViewById(R.id.textView12);  //current time
        textViewWeatherForecast[0][1] = (TextView)findViewById(R.id.textView23);  //current 'F (temperature)
        textViewWeatherForecast[0][2] = (TextView)findViewById(R.id.textView24);  //current W (wind)
        textViewWeatherForecast[0][3] = (TextView)findViewById(R.id.textView25);  //current H (humidity)
        textViewWeatherForecast[0][4] = (TextView)findViewById(R.id.textView26);  //current C (clouds)
        textViewWeatherForecast[0][5] = (TextView)findViewById(R.id.textViewWeatherDescription);  //current Conditions

        textViewWeatherForecast[1][0] = (TextView)findViewById(R.id.textView13);  //+3H time
        textViewWeatherForecast[1][1] = (TextView)findViewById(R.id.textView28);  //+3H  'F (temperature)
        textViewWeatherForecast[1][2] = (TextView)findViewById(R.id.textView29);  //+3H  W (wind)
        textViewWeatherForecast[1][3] = (TextView)findViewById(R.id.textView30);  //+3H  H (humidity)
        textViewWeatherForecast[1][4] = (TextView)findViewById(R.id.textView31);  //+3H  C (clouds)
        textViewWeatherForecast[1][5] = (TextView)findViewById(R.id.textView32);  //+3H  Conditions

        textViewWeatherForecast[2][0] = (TextView)findViewById(R.id.textView14);  //+6H  time
        textViewWeatherForecast[2][1] = (TextView)findViewById(R.id.textView33);  //+6H  'F (temperature)
        textViewWeatherForecast[2][2] = (TextView)findViewById(R.id.textView34);  //+6H  W (wind)
        textViewWeatherForecast[2][3] = (TextView)findViewById(R.id.textView35);  //+6H  H (humidity)
        textViewWeatherForecast[2][4] = (TextView)findViewById(R.id.textView36);  //+6H  C (clouds)
        textViewWeatherForecast[2][5] = (TextView)findViewById(R.id.textView37);  //+6H  Conditions

        textViewWeatherForecast[3][0] = (TextView)findViewById(R.id.textView15);  //+9H  time
        textViewWeatherForecast[3][1] = (TextView)findViewById(R.id.textView38);  //+9H 'F (temperature)
        textViewWeatherForecast[3][2] = (TextView)findViewById(R.id.textView39);  //+9H W (wind)
        textViewWeatherForecast[3][3] = (TextView)findViewById(R.id.textView40);  //+9H  H (humidity)
        textViewWeatherForecast[3][4] = (TextView)findViewById(R.id.textView41);  //+9H  C (clouds)
        textViewWeatherForecast[3][5] = (TextView)findViewById(R.id.textView42);  //+9H  Conditions

        textViewWeatherForecast[4][0] = (TextView)findViewById(R.id.textView16);  //+12H  time
        textViewWeatherForecast[4][1] = (TextView)findViewById(R.id.textView43);  //+12H 'F (temperature)
        textViewWeatherForecast[4][2] = (TextView)findViewById(R.id.textView44);  //+12H W (wind)
        textViewWeatherForecast[4][3] = (TextView)findViewById(R.id.textView45);  //+12H  H (humidity)
        textViewWeatherForecast[4][4] = (TextView)findViewById(R.id.textView46);  //+12H  C (clouds)
        textViewWeatherForecast[4][5] = (TextView)findViewById(R.id.textView47);  //+12H  Conditions

        textViewGBPDollars = (TextView) findViewById(R.id.textViewGBPDollars);
        textViewEuroDollars = (TextView) findViewById(R.id.textViewEuroDollars);
        Log.d(TAG, "onCreate():exchangeData.LoadDataFromAssets(\"apiexchangedata.txt\")");
        exchangeData.setContext(this);
        apiCurrencyExchangeData.apiFixerDeserializer(exchangeData.LoadDataFromAssets("apiexchangedata.txt"));//get default exchange data to display from assets file

        FiveDayWeatherData.setContext(this);


        Runnable runnableURLRefresh = new Runnable() { //update weather data
             @Override
             public void run() {
                 Log.d(TAG, "RunnableURLRefresh.run() - in the time function");
                 Date dateTimeStampNow = new Date(); //initializes to current time
                 long dateTimeNowMillisec = dateTimeStampNow.getTime(); //get current time in millisecs
                 SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); //set format to AM/PM
                 textViewLastUpdateTime.setText(sdf.format(dateTimeStampNow)); //display last update time from a date
                 textViewNextUpdateTime.setText(sdf.format(dateTimeNowMillisec + updateIntervalURLRefresh)); //next update time

                 Counter = 1; //reset the progress bar counter
                 timeToNextUpdateProgressBar.setProgress(Counter);  //reset progress bar
                 Log.d(TAG, "RunnableURLRefresh timeToNextUpdateProgressBar.setProgress(1) just happened");

                 RetrieveExchangeData();  //get the currency exchange data
                 System.out.println("RunnableURLRefresh RetrieveExchangeData() just happened" );  //print out JSON - comes out in alphabetical order

                 RetrieveFiveDayWeatherForecastData(); //get the forecast from OpenWeatherMapAIP
                 System.out.println("RunnableURLRefresh RetrieveFiveDayWeatherForecastData() just happened" );  //print out JSON - comes out in alphabetical order

                 handlerURLRefresh.postDelayed(this, updateIntervalURLRefresh); //20 minutes - 1200 sec = 1,200,000 millisec
             }
         };
        handlerURLRefresh.post(runnableURLRefresh);

        Runnable runnableProgressBarRefresh = new Runnable() {
            @Override
            public void run() {    //update progress bar
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

    protected void onWindowFocusChanged(){
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

        Log.d(TAG, "onWindowFocusChanged() - resetting full screen and hiding navigation bar");
    }
    public void RestorePrefrences() {
        // Restore preferences
        Log.d(TAG, "In RestorePrefences()");
        String defaultText = getString(R.string.loadErrorURLHTML);   //simple HTML to show load error

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //AnnapolisOpenWeatherMapAPIHTML = settings.getString(AnnapolisOpenWeatherMapAPIHTML,defaultText);

        //test

    }

    public void StorePrefrences() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        Log.d(TAG, "In StorePrefences()");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

     /*   editor.putString(VancouverOpenWeatherMapAPIHTML,); //store the HTML values for reuse
        editor.putString(CheltenhamOpenWeatherMapAPIHTML,); //store the HTML values for reuse
        */
        // Commit the edits!
        editor.commit();

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
    public void Update_Current_Weather_Display(){
        System.out.println("Update_Display(): updating text fields");  //print out JSON - comes out in alphabetical order

       //textViewSunrise.setText(weather.location.getSunriseDateFormated("hh:mm aa"));  //display sunrise time formatted as above
        //textViewSunset.setText(weather.location.getSunsetDateFormated("hh:mm aa"));  //display sunrise time formatted as above

        textViewSunrise.setText("05:14+ AM");  //display sunrise time formatted as above
        textViewSunset.setText(" 08:55+ PM");  //display sunrise time formatted as above


        System.out.println("Update_Display():" + versionName + "-" + String.valueOf(versionCode));
        textViewVersion.setText(versionName + "-" + String.valueOf(versionCode));

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa"); //set format to AM/PM
        //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); //set format to AM/PM
        //String dataupdatetime = String.valueOf(sdf.format(weather.getdt()*1000+3600));
        //System.out.println("Update_Display(): textViewDataTime.setText() with "+String.valueOf(weather.getdt()));
        //System.out.println("Update_Display():" + dataupdatetime);
        //textViewDataTime.setText(FiveDayForecastData);

        if (netConnectStatusCount >60 && netConnectStatus){
            textViewNetConnectStatus.setVisibility(View.INVISIBLE); //hide the status flag
            netConnectStatusCount = 0;  //reset counter until the next comms error
        }

        if (netConnectStatus)
            textViewNetConnectStatus.setVisibility(View.INVISIBLE); //hide the status flag because comms are present
        else
            textViewNetConnectStatus.setVisibility(View.VISIBLE); //light up  the status flag because comms are not present
    }

    //method to display data without weather being passed
    public void Update_Exchange_Data_Display(){
        System.out.println("Update_Display()");

        DecimalFormat df = new DecimalFormat("#.0000");

        //update currency info
        //textViewEuroDollars.setText(String.valueOf(String.valueOf(1.0f/apiCurrencyExchangeData.rates.getEUR())));
        //textViewGBPDollars.setText(String.valueOf(String.valueOf(1.0f/apiCurrencyExchangeData.rates.getGBP())));

        textViewEuroDollars.setText(String.valueOf(df.format(1.0f/apiCurrencyExchangeData.rates.getEUR())));
        textViewGBPDollars.setText(String.valueOf(df.format(1.0f/apiCurrencyExchangeData.rates.getGBP())));

    }

    //method to display data without weather being passed
    public void Update_Weather_Forecast_Display(){
        System.out.println("Update_Weather_Forecast_Display()");

        DecimalFormat df = new DecimalFormat("#.0");
        //update currency info
        //textViewEuroDollars.setText(String.valueOf(df.format(1.0f/apiCurrencyExchangeData.rates.getEUR())));

        textViewWeatherForecast[0][0].setText("Now");  //set the future forecast times, [rows][columns]
        textViewWeatherForecast[1][0].setText("+3 Hr");
        textViewWeatherForecast[2][0].setText("+6 Hr");
        textViewWeatherForecast[3][0].setText("+9 Hr");
        textViewWeatherForecast[4][0].setText("+12 Hr");

        textViewCurrentTemp.setText(String.valueOf(FiveDayForecastData.list[0].main.getTemp())+"'F");  //display the more detailed descriptions

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa"); //set format to AM/PM
        //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); //set format to AM/PM
        //String dataupdatetime = String.valueOf(sdf.format(FiveDayForecastData.list[0].dt_text)*1000+3600));
        textViewDataTime.setText(FiveDayForecastData.list[0].getDt_text());


        textViewCityName.setText(FiveDayForecastData.getCity().getname());  //set city name

        textViewPercipitationTotal3Hrs.setText("∑rain " + FiveDayForecastData.list[0].rain.toString3Hr() + "″/3hrs");

        imageViewWeatherIcon.setImageDrawable(Get_Weather_Icon(FiveDayForecastData.list[0].weather[0].getIcon()));

        //textViewOutput.setText(forcastData.list[0].toString()+"\n\n"+forcastData.list[1].weather[0].toString());  example of how to access forecast data

        for (int x = 0; x < 5; x++){  //iterate through the rows: Temperature, Wind, Humidity, Clouds, Description
            textViewWeatherForecast[x][1].setText(String.valueOf(FiveDayForecastData.list[x].main.getTemp())+"'F"); //set the temperature
            textViewWeatherForecast[x][2].setText(String.valueOf(FiveDayForecastData.list[x].wind.getSpeed())+"mph");  //set the wind
            textViewWeatherForecast[x][3].setText(String.valueOf(FiveDayForecastData.list[x].main.gethumidity())+"%");  //set the humidity
            textViewWeatherForecast[x][4].setText(FiveDayForecastData.list[x].clouds.getPerc()+"'%");  //set the humidity
            textViewWeatherForecast[x][5].setText(FiveDayForecastData.list[x].weather[0].getDescription());  //set the description
        }

    }

    public void RetrieveExchangeData(){
        System.out.println("RetrieveExchangeData()");
        //Before your app attempts to connect to the network, it should check to see whether a network connection is available using getActiveNetworkInfo() and isConnected().
        // Remember, the device may be out of range of a network, or the user may have disabled both Wi-Fi and mobile data acces


        if(exchangeData.Check_Network_Connection()){
            System.out.println("URLJSON_OnClick(): Good network connectivity");
            DownloadExchangeJSONTask webpageTask = new DownloadExchangeJSONTask(); //get ready to download exchange data
            //string for apiFixer
            //String apiFixerURL = "http://api.fixer.io/latest?symbols=USD,GBP";
            String apiFixerURL = "http://api.fixer.io/latest?base=USD";
            webpageTask.execute(apiFixerURL);
            System.out.println("URLJSON_OnClick(): webpageTask.execute(apiFixerURL) was initiated");
        }
        else {
            // display error
            System.out.println("URLJSON_OnClick(): Error on network connectivity");
        }
    }


    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadExchangeJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                System.out.println("DownloadExchangeJSONTask.doInBackground(): try{}");
                //return downloadUrl(urls[0]);
                return exchangeData.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            apiCurrencyExchangeData.apiFixerDeserializer(result);  //deserialize the returned JSON
            System.out.println("DownloadExchangeJSONTask,onPostExecute():result = "+result);
            System.out.println("DownloadExchangeJSONTask,onPostExecute():apiCurrencyExchangeData = "+apiCurrencyExchangeData.toString());
            Update_Exchange_Data_Display();
        }
    }

    public void RetrieveFiveDayWeatherForecastData(){
        System.out.println("RetrieveFiveDayWeatherForecastData()");
        //Before your app attempts to connect to the network, it should check to see whether a network connection is available using getActiveNetworkInfo() and isConnected().
        // Remember, the device may be out of range of a network, or the user may have disabled both Wi-Fi and mobile data acces


        if(FiveDayWeatherData.Check_Network_Connection()){
            System.out.println("RetrieveFiveDayWeatherForecastData(): Good network connectivity");
            DownloadFiveDayWeatherForecastDataJSONTask webpageTask = new DownloadFiveDayWeatherForecastDataJSONTask(); //get ready to download exchange data
            //string for apiFixer
            //String apiFixerURL = "http://api.fixer.io/latest?symbols=USD,GBP";
            String JSON_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Cheltenham&units=imperial&appid=40ccc628e578669ca8c47e31599b0d04";
            webpageTask.execute(JSON_URL);
            System.out.println("RetrieveFiveDayWeatherForecastData(): webpageTask.execute(JSON_URL) was initiated");
        }
        else {
            // display error
            System.out.println("RetrieveFiveDayWeatherForecastData(): Error on network connectivity");
        }


    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadFiveDayWeatherForecastDataJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                System.out.println("DownloadFiveDayWeatherForecastDataJSONTask.doInBackground(): try{}");
                //return downloadUrl(urls[0]);
                return FiveDayWeatherData.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            FiveDayForecastData.openWeatherMapAPI5DayForcastDeserializer(result);
            System.out.println("DownloadFiveDayWeatherForecastDataJSONTask,onPostExecute():result = "+result);
            Update_Weather_Forecast_Display();
            Update_Current_Weather_Display();
        }
    }

}
