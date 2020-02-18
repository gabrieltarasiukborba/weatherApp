package com.example.weatherapp;

class Utils {

    private static final String header = "https://api.openweathermap.org/data/2.5/group?id=";
    private static final String appid = "2922ee550b51489c4e66277a5d5936a3";

    public static String getHeader(){
        return header;
    }

    public String getAppid(){
        return appid;
    }
}
