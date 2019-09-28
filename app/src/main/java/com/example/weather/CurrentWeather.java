package com.example.weather;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

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
        new JsonGetter().execute("Omsk");
    }

    public String timeGetter(String str) {
        long epoch = Long.parseLong(str);
        @SuppressLint("SimpleDateFormat") String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(epoch * 1000));
        return date;
    }

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }

    private class JsonGetter extends AsyncTask<String, String, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected Map<String, Object> doInBackground(String... args) {
            String API_KEY = "846f12aa31d2907a0bbb26f484c1c60f";
            String location = args[0];
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + "Omsk" + "&appid=" + API_KEY + "&units=metric";
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
                Map<String, Object> firstMap = jsonToMap(result.toString());
                return firstMap;
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
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            Map<String, Object> mainMap = jsonToMap(map.get("main").toString());
            cityName.setText(mainMap.get("temp").toString());
            // Do things like hide the progress bar or change a TextView
        }
    }
}

