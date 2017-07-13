package com.example.dell.applopmap.location.retrofit;


import com.example.dell.applopmap.Model.PlaceType.Place;
import com.example.dell.applopmap.Model.RouteClass.GetRoute;
import com.example.dell.applopmap.Model.SingleClass.SingleDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceRetro {
    @GET("json?")
    Call<Place> getPlaceDetailsWithoutToken(@Query("location") String location,@Query("radius") int radius,
                           @Query("type") String type,@Query("key") String key);
    @GET("json?")
    Call<Place> getPlaceDetailsWithToken(@Query("location") String location,@Query("radius") int radius,
                           @Query("type") String type,@Query("key") String key,
                                         @Query("pagetoken") String pageToken);

    @GET("json?")
    Call<SingleDetails> getSinglePlaceDetail(@Query("key")String key, @Query("reference") String reference);

    @GET("json?")
    Call<GetRoute> getRoutePlaceDetail(@Query("origin") String origin,@Query("destination") String destination
    ,@Query("mode") String mode,@Query("key")String key);
}

