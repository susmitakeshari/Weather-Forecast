package task.susmita.example.com.wheatherproject.info;

import java.util.List;
import java.util.ArrayList;


public class WeatherData {

    private City city;
    private String cod;
    private String message;
    private Integer cnt;
    private List<TemperatureList> list = new ArrayList<>();

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }


    public List<TemperatureList> getList() {
        return list;
    }

    public void setList(List<TemperatureList> list) {
        this.list = list;
    }
}