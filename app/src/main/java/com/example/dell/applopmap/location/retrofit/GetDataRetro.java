package com.example.dell.applopmap.location.retrofit;


import android.util.Log;

import com.example.dell.applopmap.Model.PlaceType.Place;
import com.example.dell.applopmap.Model.RouteClass.GetRoute;
import com.example.dell.applopmap.Model.SingleClass.SingleDetails;
import com.example.dell.applopmap.location.Maps.InterfaceRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataRetro
{

    Data data;
    int check;
    SingleData singleData;
    InterfaceRoute interfaceRoute;
    public GetDataRetro(Data data)
    {
          this.data=data;
    }

    public GetDataRetro(SingleData singleData,int value) {
        this.singleData=singleData;
    }

    public GetDataRetro(InterfaceRoute interfaceRoute,int x,int y){ this.interfaceRoute=interfaceRoute;}

   public void getValue(final int check, String location, int radius, String type, String key,
                        String base_url, final String pageToken) {
this.check=check;
        Call<Place> call=null;
        InterfaceRetro apiService =
                RetroCall.getClient(base_url).create(InterfaceRetro.class);


        if(check==1)
        {

        Log.e("url",apiService.getPlaceDetailsWithoutToken(location,radius,type,key).request().url().toString());
               call= apiService.getPlaceDetailsWithoutToken(location,radius,type,key);
        }
        else if(check==2)
        {
            Log.e("url",apiService.getPlaceDetailsWithToken(location,radius,type,key,pageToken).request().url().toString());
            call = apiService.getPlaceDetailsWithToken(location,radius,type,key,pageToken);
        }


        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response)
            {
                Place place=response.body();
//                Log.e("result",place.getResults().get(0).getName());
                data.PassData(place,check);
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t)
            {
                Log.e("error",t.getMessage());
            }
        });

    }

   public void getSingleValue(String key, final String reference, String base_url_place)
   {
    Call<SingleDetails> call=null;
       Log.e("string",base_url_place);
       InterfaceRetro interfaceRetro=RetroCall.getClientDetail(base_url_place).create(InterfaceRetro.class);
       Log.e("url",interfaceRetro.getSinglePlaceDetail(key,reference).request().url().toString());
       call=interfaceRetro.getSinglePlaceDetail(key,reference);


       call.enqueue(new Callback<SingleDetails>() {
                   @Override
                   public void onResponse(Call<SingleDetails> call, Response<SingleDetails> response) {
                   SingleDetails singleDetails=response.body();
                      Log.e("data",singleDetails.getResult().toString());
                       singleData.getSingleDetailData(singleDetails);
                   }

                   @Override
                   public void onFailure(Call<SingleDetails> call, Throwable t) {

                   }
               });
   }

   public void getRouteData(String key,String origin,String dest,String base_url)
   {
       Call<GetRoute> call=null;
       final InterfaceRetro interfaceRetro=RetroCall.getRouteDetails(base_url).create(InterfaceRetro.class);
     Log.e("url",interfaceRetro.getRoutePlaceDetail(origin,dest,"driving",key).request().url().toString());
       call=interfaceRetro.getRoutePlaceDetail(origin,dest,"driving",key);
       call.enqueue(new Callback<GetRoute>() {
           @Override
           public void onResponse(Call<GetRoute> call, Response<GetRoute> response) {

              GetRoute getRoute=response.body();
                  interfaceRoute.InterfaceR(getRoute);
           }

           @Override
           public void onFailure(Call<GetRoute> call, Throwable t) {

           }
       });
   }
}
