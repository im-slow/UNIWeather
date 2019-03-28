package it.univaq.mobileprogramming.uniweather.model;

public class Forecast {
    private int id;
    private double temp, wind_speed;
    private String desc, icon, timestamp;

    public Forecast(int id, double temp, double wind_speed, String desc, String icon, String timestamp) {
        this.id = id;
        this.temp = temp;
        this.wind_speed = wind_speed;
        this.desc = desc;
        this.icon = icon;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
