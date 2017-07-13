package com.example.dell.applopmap.location;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.applopmap.Adapter.PlaceTypeAdapter;
import com.example.dell.applopmap.Adapter.RecyclerViewClickListener;
import com.example.dell.applopmap.Model.PlaceType.Place;
import com.example.dell.applopmap.Model.PlaceType.Result;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.location.retrofit.Data;
import com.example.dell.applopmap.location.retrofit.GetDataRetro;
import com.example.dell.applopmap.sharePreference.LoginData;

import java.util.ArrayList;
import java.util.List;

public class GetPlaces extends AppCompatActivity implements Data,RecyclerViewClickListener
{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Intent intent;
    double lat,lng;String type,lat_lng;

    List<Result> list_result=null;
    GridLayoutManager mLayoutManager;
    PlaceTypeAdapter adapter;
    String user_id;LoginData data;
    TextView text_zero;

    GetDataRetro get;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    static int count_place=0;
int radius;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        if (intent!=null)
        {
            type=intent.getStringExtra("places");
            lat=intent.getDoubleExtra("latitude",0.0);
            lng=intent.getDoubleExtra("longitude",0.0);
            lat_lng=lat+","+lng;
            ids();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(new CheckConnection(GetPlaces.this).checkInternetConenction()) {
    ids();
        }
        else{
            startActivity(new Intent(GetPlaces.this, Internet.class));
        }
    }



    private void ids()
    { text_zero=(TextView) findViewById(R.id.text_zero_result);
        data=new LoginData(GetPlaces.this);
         radius=data.getDistance();
        radius*=1000;
      Log.e("radius",String.valueOf(radius));

         get =new GetDataRetro(this);
        get.getValue(1,lat_lng,radius,type,"AIzaSyCl7FXM7ivMukEv1wLidhVe56O4jqBAnRQ",
                getResources().getString(R.string.base_url_place_name)
                , "xyt");
        progressBar= (ProgressBar) findViewById(R.id.progressBar_place);
        recyclerView= (RecyclerView) findViewById(R.id.recycleViewer_place);

        list_result=new ArrayList<>();
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        user_id=new LoginData(GetPlaces.this).getKeyId();
        Log.e("is",user_id);
        adapter = new PlaceTypeAdapter(list_result,GetPlaces.this,user_id,type,this);

    }


    @Override
    public void PassData(Place place,int count)
    {
          SetAdapter(place,count);
    }

    public void SetAdapter(final Place place, int count)
    {

        if(count==1) {
            if (place.getStatus().equals("OK")) {
                list_result.addAll(place.getResults());
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            } else if (place.getStatus().equals("ZERO_RESULTS")) {
                progressBar.setVisibility(View.GONE);
                text_zero.setVisibility(View.VISIBLE);
            }
        }
        else if(count==2){
                list_result.addAll(place.getResults());
                adapter.notifyDataSetChanged();
            }

       // FirebaseLogin firebase=new FirebaseLogin();
       // firebase.getUserReview(place.getResults());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    if (place.getNextPageToken() != null) {
                            Log.e("at","last");
                        get.getValue(2, lat_lng, radius, type, "AIzaSyCl7FXM7ivMukEv1wLidhVe56O4jqBAnRQ",
                                getResources().getString(R.string.base_url_place_name)
                                ,place.getNextPageToken());
                    }
                    loading = true;
                }

                }

        });

    }

    @Override
    public void GetPosition(int position)
    {
           Intent intent=new Intent(GetPlaces.this,PlaceDetail.class);
           intent.putExtra("reference",list_result.get(position).getReference());
           intent.putExtra("current_lat",lat);
           intent.putExtra("current_lng",lng);
         Log.e("latlng",lat+" ,"+lng);
           startActivity(intent);
    }

    @Override
    public void GetPosition(String name, String Lat, String Lng) {

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
