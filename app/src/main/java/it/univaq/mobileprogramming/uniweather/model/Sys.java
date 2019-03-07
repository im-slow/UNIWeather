package it.univaq.mobileprogramming.uniweather.model;

public class Sys {
    public int type;
    public int id;
    public double message;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String country;
    public int sunrise;
    public int sunset;
}
