package com.example.weather;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class CurrentWeather extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.CurrentWeather);
        new JsonGetter().execute("Omsk");
    }

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }

    private static class JsonGetter extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... location) {
            String API_KEY = "846f12aa31d2907a0bbb26f484c1c60f";
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY + "&units=metric";
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
                System.out.println(result);
                Map<String, Object> respMap = jsonToMap(result.toString());
                Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
                Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());
                return "City detected";
            } catch (
                    IOException e) {
                System.out.println(e.getMessage());
            }
            return "Error,city not founded";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }
}

