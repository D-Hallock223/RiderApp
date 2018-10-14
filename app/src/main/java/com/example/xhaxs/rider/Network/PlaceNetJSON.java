package com.example.xhaxs.rider.Network;

import android.util.Log;

import com.example.xhaxs.rider.Datatype.PlaceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PlaceNetJSON {

    private static final String LOG_CLASS = PlaceNetJSON.class.getName();
    private String mURLString;
    private HttpsURLConnection connection;
    private InputStream inputStream;

    public PlaceNetJSON(String urlString) {
        super();
        mURLString = urlString;
        connection = null;
        inputStream = null;
    }

    private URL generateURL() {
        URL url = null;
        try {
            url = new URL(mURLString);
        } catch (MalformedURLException e) {
            Log.d(LOG_CLASS, "Error Generating URL");
        } finally {
            Log.d(LOG_CLASS, "-----------------------------" + url.toString() + "--------------------------------");
            return url;
        }

    }

    public PlaceData[] openConnect() {
        cancelRequest();
        connection = null;
        inputStream = null;
        URL url = generateURL();
        String string = null;
        PlaceData[] apt = new PlaceData[0];
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(3000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("Error Establishing Connection!");
            }
            inputStream = connection.getInputStream();
            string = readInput(inputStream);
            if (string.isEmpty() == true) {
                throw new IOException("Error Reading Input From Stream!");
            }
            apt = parseJSON(string);
            return apt;
        } catch (IOException e) {
            Log.d(LOG_CLASS, "IOException Occurred...");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                } finally {
                    inputStream = null;
                }
            }
            connection.disconnect();
            return apt;
        }
    }

    private String readInput(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private PlaceData[] parseJSON(String string) {
        JSONObject jsonObject = null;
        PlaceData[] al = new PlaceData[0];
        try {
            jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("predictions");
            al = new PlaceData[jsonArray.length()];
            Log.d(LOG_CLASS, "Total Result Count returned # " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                Log.d(LOG_CLASS, "Count for # " + i);
                JSONObject structureFormatting = jsonArray.getJSONObject(i).getJSONObject("structured_formatting");
                String mainPlace, secPlace;
                try {
                    mainPlace = structureFormatting.getString("main_text");
                } catch (Exception e) {
                    Log.d(LOG_CLASS, e.toString());
                    mainPlace = "";
                }

                try {
                    secPlace = structureFormatting.getString("secondary_text");
                } catch (Exception e) {
                    Log.d(LOG_CLASS, e.toString());
                    secPlace = "";
                }
                PlaceData placeData = new PlaceData(mainPlace, secPlace);
                Log.d(LOG_CLASS, "<<<<<<<<<<<<<<<<<<<<<<" + placeData.toString() + ">>>>>>>>>>>-----------------------------");
                al[i] = placeData;
            }
        } catch (JSONException e) {
            Log.d(LOG_CLASS, "Error parsing JSON input" + e.toString());
        } finally {
            return al;
        }
    }

    public void cancelRequest() {
        Log.d(LOG_CLASS, "Cancel Request called................................PlaceNetJSON");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        } catch (IOException e) {
        }
    }

}
