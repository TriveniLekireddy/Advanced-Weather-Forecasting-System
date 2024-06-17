package com.weather.main;

import com.weather.model.WeatherData;
import com.weather.service.WeatherDataService;

public class Main {
    public static void main(String[] args) {
        WeatherDataService service = new WeatherDataService();
        WeatherData data = new WeatherData("2024-06-12", "New York", 25.0);
        service.addWeatherData(data);
        System.out.println("Weather data added successfully");

        // Fetch and display all weather data
        service.getAllWeatherData().forEach(wd -> {
            System.out.println("Date: " + wd.getDate() + ", Location: " + wd.getLocation() + ", Temp: " + wd.getTemperature());
        });
    }
}