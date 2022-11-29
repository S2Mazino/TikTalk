package edu.uw.tcss450.team3.tiktalk.ui.weather;

public class WeatherDailyForecastItem {

    private String dailyForecastIcon;
    private String dailyForecastDay;
    private String dailyForecastTemp;

    public WeatherDailyForecastItem(String dailyForecastDay, String dailyForecastIcon, String dailyForecastTemp) {
        this.dailyForecastIcon = dailyForecastIcon;
        this.dailyForecastDay = dailyForecastDay;
        this.dailyForecastTemp = dailyForecastTemp;
    }

    public String getDailyForecastIcon() {
        return dailyForecastIcon;
    }

    public String getDailyForecastDay() {
        return dailyForecastDay;
    }

    public String getDailyForecastTemp() {
        return dailyForecastTemp;
    }
}
