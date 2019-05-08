package com.jason.checkweather.gson;


public class City {

    public Basic basic;

    public String status;

    public class Basic{
        public String city;
        public String cnty;
        public String lat;
        public String lon;
        public String prov;
    }
}
