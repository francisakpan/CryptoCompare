package com.francis.cryptocompare.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Francis on 02/11/2017.
 *
 */

public class Network {
    //Log tag string
    private static final String LOG_TAG = Network.class.getSimpleName();

    /**
     * This function fetches for data using network call to the given url
     * @param requestUrl url to query for data
     * @return data from queried url
     */
    public static String fetchJSONData(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
            Log.i("Json string", jsonResponse);
        }catch (Exception e){
            Log.e(LOG_TAG, "Problem making http request", e);
        }

        return jsonResponse;
    }

    /**
     *
     * @param stringUrl the url string to convert to a url object
     * @return a url object of the string given
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the url", e);
        }
        return url;
    }

    /**
     *
     * @param url url to make http request
     * @return return data from http request
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null ){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromJson(inputStream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake Json file");
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromJson(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
