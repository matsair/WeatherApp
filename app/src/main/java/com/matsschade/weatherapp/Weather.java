package com.matsschade.weatherapp;

/**
 * Created by Mats on 06/03/15.
 */
public class Weather {

    private String cityName;
    private String cityTemp;
    private String Desc;

    public Weather(String cityName, String cityTemp, String Desc){
        this.cityName = cityName;
        this.cityTemp = cityTemp;
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
}