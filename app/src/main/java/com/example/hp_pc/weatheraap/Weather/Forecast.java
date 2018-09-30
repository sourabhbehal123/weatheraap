package com.example.hp_pc.weatheraap.Weather;

/**
 * Created by hp-pc on 6/21/2018.
 */

public class Forecast {
    private CurrentWeather currentWeather;
    private Hour[] hours;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public Hour[] getHours() {
        return hours;
    }

    public void setHours(Hour[] hours) {
        this.hours = hours;
    }
}
