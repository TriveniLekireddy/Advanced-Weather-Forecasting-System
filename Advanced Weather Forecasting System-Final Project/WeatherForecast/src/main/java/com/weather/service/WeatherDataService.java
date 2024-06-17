package com.weather.service;

import com.weather.dao.DatabaseManager;
import com.weather.model.WeatherData;

import java.util.List;

public class WeatherDataService {
    private DatabaseManager dbManager;

    public WeatherDataService() {
        this.dbManager = new DatabaseManager();
    }

    public void addWeatherData(WeatherData data) {
        dbManager.insertWeatherData(data);
    }

    public List<WeatherData> getAllWeatherData() {
        return dbManager.getAllWeatherData();
    }

    public void updateWeatherData(WeatherData data) {
        dbManager.updateWeatherData(data);
    }

    public void deleteWeatherData(int id) {
        dbManager.deleteWeatherData(id);
    }

    public String forecastBasedOnData(String location) {
        List<WeatherData> allData = dbManager.getAllWeatherData();
        double sum = 0;
        int count = 0;

        for (WeatherData data : allData) {
            if (data.getLocation().equalsIgnoreCase(location)) {
                sum += data.getTemperature();
                count++;
            }
        }

        if (count == 0) {
            return "No historical data available for this location.";
        }

        double averageTemp = sum / count;
        return String.format("Predicted average temperature for %s: %.2f°C", location, averageTemp);
    }
}