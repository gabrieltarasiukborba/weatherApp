package com.example.weatherapp;

import android.widget.ImageView;

class Weather {
    private String name;
    private String temperature;
    private String icon;

    public String getName() {
        return name;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
