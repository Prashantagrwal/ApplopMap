package com.example.dell.applopmap.location.Maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dell.applopmap.Model.RouteClass.GetRoute;
import com.example.dell.applopmap.Model.RouteClass.Step;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.location.retrofit.GetDataRetro;

import java.util.ArrayList;
import java.util.List;


public class ShowMap extends AppCompatActivity implements InterfaceRoute
{

    Intent intent;
    double current_lat,current_lng,dest_lat,dest_lng;
     String origin,dest;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_frame);
        intent=getIntent();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        if(intent!=null)
        {
            current_lat=intent.getDoubleExtra("current_lat",0.0);
            current_lng=intent.getDoubleExtra("current_lng",0.0);
            dest_lat=intent.getDoubleExtra("dest_lat",0.0);
            dest_lng=intent.getDoubleExtra("dest_lng",0.0);
            origin=current_lat +","+current_lng;
            dest=dest_lat +","+dest_lng;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(new CheckConnection(ShowMap.this).checkInternetConenction()) {
            getRoute(origin,dest);
        }
        else{
            startActivity(new Intent(ShowMap.this, Internet.class));
        }
    }

    private void getRoute(String origin,String dest)
    {
              new GetDataRetro(this,1,1).getRouteData(
                      "AIzaSyCl7FXM7ivMukEv1wLidhVe56O4jqBAnRQ",
                      origin,
                      dest,
                      getResources().getString(R.string.base_url_direction));
    }

    @Override
    public void InterfaceR(GetRoute getRoute)
    {
        Log.e("data",getRoute.getStatus());
        List<Step> list_step=new ArrayList<>();
        list_step.addAll(getRoute.getRoutes().get(0).getLegs().get(0).getSteps());
      //  List<String> poly_line=new ArrayList<>();
        int i,size=getRoute.getRoutes().get(0).getLegs().get(0).getSteps().size();
        /*for(i=0;i<size;i++)
        {
            poly_line.add(getRoute.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getPolyline().getPoints());
        }*/
         String poly_line=getRoute.getRoutes().get(0).getOverviewPolyline().getPoints();
        MapFragment map=new MapFragment(poly_line,current_lat,current_lng,dest_lat,dest_lng);
        progressBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment,map,"Main").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
