package com.example.dell.applopmap.location.retrofit;

/**
 * Created by DELL on 10/07/2017.
 */

public class LatLngClass {

String lat;
    String lng;
    String place_name;
    String frequency;
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }



   public LatLngClass(){}

   public LatLngClass(String place_name, String lat, String lng, String frequency){
       this.frequency=frequency;
       this.lat=lat;
       this.lng=lng;
       this.place_name=place_name;
   }
}
