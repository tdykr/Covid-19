package com.example.covid19tracker;

public class APIConfig {

    public final static String URL_SUMMARY = "https://corona.lmao.ninja/all";
    public final static String URL_LIST_COUNTRIES = "https://corona.lmao.ninja/countries";
    public final static String URL_LIST_COUNTRIES_SEARCH(String country){
        String url = "https://corona.lmao.ninja/countries/"+country;
        return url;
    }

}
