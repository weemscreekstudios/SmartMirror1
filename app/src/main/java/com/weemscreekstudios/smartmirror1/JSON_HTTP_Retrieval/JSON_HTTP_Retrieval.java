package com.weemscreekstudios.smartmirror1.JSON_HTTP_Retrieval;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Parents on 4/10/2016.
 */
public class JSON_HTTP_Retrieval {
    Context mContext;
    Activity activity;

    //need to have the Context in order to get system information
    //in the calling class, create the object by
    // JSON_HTTP_Retrieval exchangedata = new JSON_HTTP_Retrieval(this); //Here the context is passed
    public void setContext(Context mContext){
        this.mContext = mContext;
    }


    public boolean Check_Network_Connection(){
        //Before your app attempts to connect to the network, it should check to see whether a network connection is available using getActiveNetworkInfo() and isConnected().
        // Remember, the device may be out of range of a network, or the user may have disabled both Wi-Fi and mobile data acces

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            System.out.println("JSON_HTTP_Retrieval.Check_Network_Connection(): Good network connectivity");
            return true;
        } else {
            // display error
            System.out.println("JSON_HTTP_Retrieval.Check_Network_Connection(): Error on network connectivity");
            return false;
        }

    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            System.out.println("downloadUrl(): try{}");
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);
            String contentAsString = iStreamToString(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            System.out.println("downloadUrl(): finally{}");
            if (is != null) {
                is.close();
            }
        }
    }


    public String iStreamToString(InputStream is1)
    {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }

    public String LoadDataFromAssets(String inFile) {
        String tContents = "";

        try {
            InputStream stream = mContext.getAssets().open(inFile);;

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }
}
