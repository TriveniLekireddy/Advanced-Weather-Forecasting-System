package com.weather.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.weather.model.WeatherData;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "triveni@19";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public void createTables() {
        String createWeatherTableSQL = "CREATE TABLE IF NOT EXISTS weather_data ("
            + "id SERIAL PRIMARY KEY,"
            + "date VARCHAR(50) NOT NULL,"
            + "location VARCHAR(50) NOT NULL,"
            + "temperature DOUBLE PRECISION NOT NULL"
            + ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createWeatherTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password) {
        return "postgres".equals(username) && "triveni@19".equals(password);
    }
    
    public void insertWeatherData(WeatherData data) {
        String insertSQL = "INSERT INTO weather_data(id, date, location, temperature) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, data.getId()); // Custom ID
            pstmt.setString(2, data.getDate());
            pstmt.setString(3, data.getLocation());
            pstmt.setDouble(4, data.getTemperature());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherData> getAllWeatherData() {
        List<WeatherData> weatherDataList = new ArrayList<>();
        String selectSQL = "SELECT * FROM weather_data";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                WeatherData data = new WeatherData(
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getDouble("temperature")
                );
                data.setId(rs.getInt("id"));
                weatherDataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherDataList;
    }

    public void updateWeatherData(WeatherData data) {
        String updateSQL = "UPDATE weather_data SET date = ?, location = ?, temperature = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, data.getDate());
            pstmt.setString(2, data.getLocation());
            pstmt.setDouble(3, data.getTemperature());
            pstmt.setInt(4, data.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWeatherData(int id) {
        String deleteSQL = "DELETE FROM weather_data WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}