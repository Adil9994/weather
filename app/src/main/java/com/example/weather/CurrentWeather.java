package com.example.weather;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrentWeather extends AppCompatActivity {
    TextView cityName;
    TextView smallDescription;
    TextView mainTemp;
    TextView mainDescription;
    TextView minTemp;
    TextView maxTemp;
    TextView time;
    TextView sunrise;
    TextView sunset;
    TextView clouds;
    TextView humidity;
    TextView windSpeed;
    TextView pressure;
    TextView windDeg;
    TextView visibility;
    StringsDataBase values;
    StringBuilder result;
    ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_weather);
        cityName = findViewById(R.id.cityName);
        smallDescription = findViewById(R.id.smallDescription);
        mainTemp = findViewById(R.id.mainTemp);
        mainDescription = findViewById(R.id.mainDescription);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        time = findViewById(R.id.time);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        clouds = findViewById(R.id.clouds);
        humidity = findViewById(R.id.humidity);
        windSpeed = findViewById(R.id.windSpeed);
        windDeg = findViewById(R.id.windDeg);
        pressure = findViewById(R.id.pressure);
        visibility = findViewById(R.id.visibility);
        values = new StringsDataBase();
        new JsonGetter().execute("Omsk");
    }

    public String timeGetter(String str) {
        long unixSeconds = Long.parseLong(str);
// convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public void setMainValues(String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        String cityName = jsonObj.getString("name");
        String time = jsonObj.getString("dt");
        values.setTime(timeGetter(time));
        this.time.setText(values.getTime());
        int visibility = jsonObj.getInt("visibility");
        values.setCityName(cityName);
        this.cityName.setText(values.getCityName());
        values.setVisibility(String.valueOf(visibility));
        this.visibility.setText(values.getVisibility());
    }

    public static String getSmallDescription(String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        JSONArray weather = jsonObj.getJSONArray("weather");
        JSONObject description = weather.getJSONObject(0);
        String smallDescription = description.getString("main");
        return smallDescription;
    }

    private class JsonGetter extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(CurrentWeather.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected Void doInBackground(String... args) {
            String API_KEY = "846f12aa31d2907a0bbb26f484c1c60f";
            String location = args[0];
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + "Omsk" + "&appid=" + API_KEY + "&units=metric";
            try {
                result = new StringBuilder();
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(String... args) {
            super.onProgressUpdate(args);
            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            if (proDialog.isShowing())
                proDialog.dismiss();
            try {
                setMainValues(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Do things like hide the progress bar or change a TextView
        }
    }
}

