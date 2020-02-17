package task.susmita.example.com.wheatherproject.info;

import java.util.ArrayList;
import java.util.Date;

import task.susmita.example.com.wheatherproject.DateUtility;


public class TemperatureList {

    private Integer dt;
    private Main main;
    private ArrayList<Weather> weather = new ArrayList<Weather>();
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Sys_ sys;
    private String dt_txt;
    private Date date;

    public Integer getDt() {
        return dt;
    }


    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }


    public void setMain(Main main) {
        this.main = main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }


    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Sys_ getSys() {
        return sys;
    }

    public void setSys(Sys_ sys) {
        this.sys = sys;
    }


    public String getDt_txt() {
        return dt_txt;
    }
    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;

    }

    public Date getDate() {
        if(date == null && dt_txt != null && !dt_txt.isEmpty())
            date = DateUtility.getDate(dt_txt);
        return date;
    }
}