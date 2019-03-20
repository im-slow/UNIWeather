package it.univaq.mobileprogramming.uniweather.model;

public class ActualWeather {
    private int city_id;
    private String city_name;
    private double temp;
    private double humidity;
    private double min_temp;
    private double max_temp;
    private double wind_speed;
    private int wind_degree;
    private double latitude;
    private double longitude;
    private String icon_name;

    public ActualWeather(int city_id, String city_name, double temp, double humidity,
                         double min_temp, double max_temp, double wind_speed, int wind_degree,
                         double latitude, double longitude, String icon_name) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.temp = temp;
        this.humidity = humidity;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.wind_speed = wind_speed;
        this.wind_degree = wind_degree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon_name = icon_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getWind_degree() {
        return wind_degree;
    }

    public void setWind_degree(int wind_degree) {
        this.wind_degree = wind_degree;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }
}
