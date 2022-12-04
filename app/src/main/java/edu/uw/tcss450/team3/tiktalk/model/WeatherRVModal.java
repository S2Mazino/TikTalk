package edu.uw.tcss450.team3.tiktalk.model;

public class WeatherRVModal {

    private String time;
    private String conditionIcon;
    private String temperature;

    public WeatherRVModal(String time, String conditionIcon, String temperature) {
        this.time = time;
        this.conditionIcon = conditionIcon;
        this.temperature = temperature;
    }


    public String getTime() {
        return time;
    }


    public String getConditionIcon() {
        return conditionIcon;
    }


    public String getTemperature() {
        return temperature;
    }

}
