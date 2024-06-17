package com.weather.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastService {
    private static final String API_KEY ="d8f984b8fe1d2cb75023bad48cb7243a"; // Replace with your API key
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s";

    public String get5DayWeatherForecast(String location) {
        try {
            String urlString = String.format(API_URL, location, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            return parse5DayWeatherData(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather data.";
        }
    }

    private String parse5DayWeatherData(String response) {
        JSONObject json = new JSONObject(response);
        JSONArray list = json.getJSONArray("list");
        StringBuilder forecast = new StringBuilder("5-Day Forecast:\n");
        
        for (int i = 0; i < list.length(); i += 8) { // Taking data every 24 hours (8 * 3-hour intervals)
            JSONObject dayForecast = list.getJSONObject(i);
            String date = dayForecast.getString("dt_txt");
            double temp = dayForecast.getJSONObject("main").getDouble("temp");
            String description = dayForecast.getJSONArray("weather").getJSONObject(0).getString("description");
            forecast.append(String.format("%s - %s: %.2f°C\n", date, description, temp));
        }
        
        return forecast.toString();
    }
    
}