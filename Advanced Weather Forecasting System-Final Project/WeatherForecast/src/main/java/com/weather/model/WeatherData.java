package com.weather.model;

public class WeatherData {
    private int id;
    private String date;
    private String location;
    private double temperature;

    public WeatherData(String date, String location, double temperature) {
        this.date = date;
        this.location = location;
        this.temperature = temperature;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
}