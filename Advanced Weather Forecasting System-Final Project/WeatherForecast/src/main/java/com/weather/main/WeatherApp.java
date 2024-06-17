package com.weather.main;

import com.weather.dao.DatabaseManager;
import com.weather.model.WeatherData;
import com.weather.service.ForecastService;
import com.weather.service.WeatherDataService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherApp {
    private WeatherDataService service;
    private ForecastService forecastService;
    private DefaultListModel<String> listModel;
    private JList<String> weatherList;
    private JTextField dateField, locationField, temperatureField, idField;
    private JFrame mainFrame;
    private DatabaseManager dbManager;

    public WeatherApp() {
        dbManager = new DatabaseManager();
        dbManager.createTables();
        service = new WeatherDataService();
        forecastService = new ForecastService();
        showLoginGUI();
    }

    private void showLoginGUI() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel()); // Empty cell
        panel.add(loginButton);

        loginFrame.add(panel);
        loginFrame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (dbManager.authenticateUser(username, password)) {
                    loginFrame.dispose();
                    createAndShowMainGUI();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void createAndShowMainGUI() {
        mainFrame = new JFrame("Weather Forecasting System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();
        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField();
        JLabel locationLabel = new JLabel("Location:");
        locationField = new JTextField();
        JLabel temperatureLabel = new JLabel("Temperature:");
        temperatureField = new JTextField();

        JButton addButton = new JButton("Add Weather Data");
        JButton updateButton = new JButton("Update Weather Data");
        JButton deleteButton = new JButton("Delete Weather Data");
        JButton forecastDataButton = new JButton("Forecast Based on Data");
        JButton forecastApiButton = new JButton("Forecast Using API");

        listModel = new DefaultListModel<>();
        weatherList = new JList<>(listModel);

        panel.add(idLabel);
        panel.add(idField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(temperatureLabel);
        panel.add(temperatureField);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(forecastDataButton);
        panel.add(forecastApiButton);

        mainFrame.add(panel, BorderLayout.NORTH);
        mainFrame.add(new JScrollPane(weatherList), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String date = dateField.getText();
                    String location = locationField.getText();
                    double temperature = Double.parseDouble(temperatureField.getText());
                    WeatherData data = new WeatherData(date, location, temperature);
                    data.setId(id);
                    service.addWeatherData(data);

                    updateWeatherList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String date = dateField.getText();
                    String location = locationField.getText();
                    double temperature = Double.parseDouble(temperatureField.getText());
                    WeatherData data = new WeatherData(date, location, temperature);
                    data.setId(id);
                    service.updateWeatherData(data);

                    updateWeatherList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    service.deleteWeatherData(id);

                    updateWeatherList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        forecastDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                String forecast = service.forecastBasedOnData(location);
                JOptionPane.showMessageDialog(mainFrame, forecast, "Forecast Based on Data", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        forecastApiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                String forecast = forecastService.get5DayWeatherForecast(location);
                JOptionPane.showMessageDialog(mainFrame, forecast, "5-Day Weather Forecast", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        updateWeatherList();
        mainFrame.setVisible(true);
    }

    private void updateWeatherList() {
        listModel.clear();
        for (WeatherData data : service.getAllWeatherData()) {
            String weatherInfo = String.format("ID: %d, Date: %s, Location: %s, Temp: %.2f",
                    data.getId(), data.getDate(), data.getLocation(), data.getTemperature());
            listModel.addElement(weatherInfo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeatherApp::new);
    }
}