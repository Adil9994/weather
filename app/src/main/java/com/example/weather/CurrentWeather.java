package com.example.weather;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
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
    String timezone;
    StringsDataBase values;
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

    public String timezoneConvertToHuman(String str) {
        long seconds = Long.parseLong(str);
        if (seconds / 3600 > 0) return "+" + seconds / 3600;
        return String.valueOf(seconds / 3600);
    }
    public String timeGetterForSun(String str){
        long unixSeconds = Long.parseLong(str);
        Date date = new java.util.Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss z");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT" + timezoneConvertToHuman(timezone)));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public String timeGetter(String str) {
        long unixSeconds = Long.parseLong(str);
        Date date = new java.util.Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT" + timezoneConvertToHuman(timezone)));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public void setSun(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        JSONObject sys  = jsonObject.getJSONObject("sys");
        String sunrise = String.valueOf(sys.getInt("sunrise"));
        String sunset = String.valueOf(sys.getInt("sunset"));
        values.setSunrise(timeGetterForSun(sunrise));
        values.setSunset(timeGetterForSun(sunset));
        this.sunrise.setText(values.getSunrise());
        this.sunset.setText(values.getSunset());
    }
    public void setClouds(String str) throws JSONException{
        JSONObject jsonObject = new JSONObject(str);
        JSONObject clouds = jsonObject.getJSONObject("clouds");
        String cloudiness = String.valueOf(clouds.getInt("all"));
        values.setClouds(cloudiness);
        this.clouds.setText(values.getClouds());
    }
    public void setWind(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        JSONObject wind = jsonObject.getJSONObject("wind");
        String windSpeed = String.valueOf(wind.getInt("speed"));
        int windDeg = wind.getInt("deg");
        values.setWindDeg(values.convertDegreeToCardinalDirection(windDeg));
        values.setWindSpeed(windSpeed);
        this.windDeg.setText(values.getWindDeg());
        this.windSpeed.setText(values.getWindSpeed());
    }
    public void setMainValues(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        JSONObject main = jsonObject.getJSONObject("main");
        String temperature = String.valueOf(main.getInt("temp"));
        String pressure = String.valueOf(main.getInt("pressure"));
        String humidity = String.valueOf(main.getInt("humidity"));
        String temp_min = String.valueOf(main.getInt("temp_min"));
        String temp_max = String.valueOf(main.getInt("temp_max"));
        values.setMainTemp(temperature);
        values.setPressure(pressure);
        values.setHumidity(humidity);
        values.setMinTemp(temp_min);
        values.setMaxTemp(temp_max);
        this.mainTemp.setText(values.getMainTemp());
        this.pressure.setText(values.getPressure());
        this.humidity.setText(values.getHumidity());
        this.minTemp.setText(values.getMinTemp());
        this.maxTemp.setText(values.getMaxTemp());
    }

    public void setStartValues(String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        String cityName = jsonObj.getString("name");
        String time = jsonObj.getString("dt");
        timezone = String.valueOf(jsonObj.getInt("timezone"));
        values.setTime(timeGetter(time));
        int visibility = jsonObj.getInt("visibility");
        values.setCityName(cityName);
        values.setVisibility(String.valueOf(visibility));
        this.cityName.setText(values.getCityName());
        this.time.setText(values.getTime());
        this.visibility.setText(values.getVisibility());
    }

    public void setWeather(String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        JSONArray weather = jsonObj.getJSONArray("weather");
        JSONObject description = weather.getJSONObject(0);
        String smallDescription = description.getString("main");
        String mainDescription = description.getString("description");
        values.setSmallDescription(smallDescription);
        values.setMainDescription(mainDescription);
        this.smallDescription.setText(values.getSmallDescription());
        this.mainDescription.setText(values.getMainDescription());
    }

    private class JsonGetter extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... args) {
            String API_KEY = "846f12aa31d2907a0bbb26f484c1c60f";
            String location = args[0];
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + "Tokio" + "&appid=" + API_KEY + "&units=metric";
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                return result.toString();
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (proDialog.isShowing())
                proDialog.dismiss();
            try {
                setStartValues(result);
                setWeather(result);
                setMainValues(result);
                setWind(result);
                setClouds(result);
                setSun(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Do things like hide the progress bar or change a TextView
        }
    }
}

