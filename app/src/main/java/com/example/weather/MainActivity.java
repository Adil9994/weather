package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class MainActivity extends AppCompatActivity {

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }

    public void getJSON() {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                String API_KEY = "846f12aa31d2907a0bbb26f484c1c60f";
                String LOCATION = "Omsk";
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=metric";
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
                    System.out.println("temperature " + mainMap.get("temp"));
                    System.out.println("wind speed" + windMap.get("speed"));
                    return null;
                } catch (
                        IOException e) {
                    System.out.println(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {

            }
        }.execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJSON();
    }

}
