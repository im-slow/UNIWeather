package it.univaq.mobileprogramming.uniweather.model;

public class ActualWeather {
    private int city_id,wind_degree,humidity,pressure;
    private String city_name,description,icon_name, country;
    private double temp,min_temp,max_temp,wind_speed,latitude,longitude;


    @Override
    public String toString() {
        return "ActualWeather{" +
                "city_id=" + city_id +
                ", wind_degree=" + wind_degree +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", city_name='" + city_name + '\'' +
                ", description='" + description + '\'' +
                ", icon_name='" + icon_name + '\'' +
                ", country='" + country + '\'' +
                ", temp=" + temp +
                ", min_temp=" + min_temp +
                ", max_temp=" + max_temp +
                ", wind_speed=" + wind_speed +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public ActualWeather(double latitude, double longitude, String description, String icon_name,
                         double temp, int pressure, int humidity, double min_temp, double max_temp,
                         double wind_speed, int wind_degree, String country, int city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.wind_speed = wind_speed;
        this.wind_degree = wind_degree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon_name = icon_name;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
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
