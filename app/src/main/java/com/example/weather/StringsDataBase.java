package com.example.weather;

public class StringsDataBase {
    private String cityName;
    private String smallDescription;
    private String mainTemp;
    private String mainDescription;
    private String minTemp;
    private String maxTemp;
    private String time;
    private String sunrise;
    private String sunset;
    private String clouds;
    private String humidity;
    private String windSpeed;
    private String pressure;
    private String windDeg;
    private String visibility;

    public String getCityName() {
        return cityName;
    }

    public String getMainDescription() {
        return mainDescription;
    }

    public String getMainTemp() {
        return mainTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getSmallDescription() {
        return smallDescription;
    }

    public String getClouds() {
        return clouds;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getTime() {
        return time;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public void setMainTemp(String mainTemp) {
        this.mainTemp = mainTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public void setSmallDescription(String smallDescription) {
        this.smallDescription = smallDescription;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
