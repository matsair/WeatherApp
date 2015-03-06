package com.matsschade.weatherapp;

public class Weather {

    private String cityName;
    private int cityTempInt;
    private String cityTemp;
    private String Desc;

    public Weather(String cityName, double cityTempDouble, String Desc){
        this.cityName = cityName;
        this.cityTempInt = (int) cityTempDouble;
        this.cityTemp = Integer.toString(cityTempInt) + " \u2103";
        this.Desc = Desc;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(String cityTemp) {
        this.cityTemp = cityTemp;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getCityTempInt() {
        return cityTempInt;
    }

    public void setCityTempInt(int cityTempInt) {
        this.cityTempInt = cityTempInt;
    }
}
