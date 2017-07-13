package com.example.dell.applopmap.location;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.applopmap.Model.SingleClass.SingleDetails;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.location.Maps.ShowMap;
import com.example.dell.applopmap.location.retrofit.GetDataRetro;
import com.example.dell.applopmap.location.retrofit.SingleData;

public class PlaceDetail extends AppCompatActivity implements SingleData, View.OnClickListener {

    String formatted_phone_number;
    TextView name,phone,address;
    Button navigate;
    Intent intent;
    ImageView imageView;
    String reference;
    double dest_lat=0.0,dest_lng=0.0,current_lat=0.0,current_lng=0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.single_place);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent =getIntent();
        if(intent!=null)
        {
            current_lat=intent.getDoubleExtra("current_lat",0.0);
            current_lng=intent.getDoubleExtra("current_lng",0.0);
reference=intent.getStringExtra("reference");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(new CheckConnection(PlaceDetail.this).checkInternetConenction()) {
            getServerCall(reference);
        }
        else{
            startActivity(new Intent(PlaceDetail.this, Internet.class));
        }
    }

    private void getServerCall(String reference)
    {
        new GetDataRetro(this,1).getSingleValue("AIzaSyCl7FXM7ivMukEv1wLidhVe56O4jqBAnRQ", reference,
                getResources().getString(R.string.base_url_single_place_details));
    }

    @Override
    public void getSingleDetailData(SingleDetails singleDetails)
    {
          String string_name=singleDetails.getResult().getName();
          String formatted_address=singleDetails.getResult().getFormattedAddress();
        formatted_phone_number=singleDetails.getResult().getFormattedPhoneNumber();
         final String NOT_AVAILABLE="Not Available";

        dest_lat=singleDetails.getResult().getGeometry().getLocation().getLat();
        dest_lng=singleDetails.getResult().getGeometry().getLocation().getLng();
        string_name= string_name==null?NOT_AVAILABLE:string_name;
       formatted_address=formatted_address==null?NOT_AVAILABLE:formatted_address;
       formatted_phone_number=formatted_phone_number==null?NOT_AVAILABLE:formatted_phone_number;
        name= (TextView) findViewById(R.id.name);
        phone= (TextView) findViewById(R.id.phone);
        address= (TextView) findViewById(R.id.address);
        navigate= (Button) findViewById(R.id.button_show_map);
        imageView = (ImageView) findViewById(R.id.image_phone);

       name.setText(string_name);
       phone.setText(formatted_phone_number);
       address.setText(formatted_address);
if(!formatted_phone_number.equals(NOT_AVAILABLE)){
    imageView.setVisibility(View.VISIBLE);
}

    navigate.setOnClickListener(this);
    imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id=view.getId();
     if(id==R.id.button_show_map)
     {
         Intent show_map=new Intent(PlaceDetail.this,ShowMap.class);
          show_map.putExtra("dest_lat",dest_lat);
          show_map.putExtra("dest_lng",dest_lng);
          show_map.putExtra("current_lat",current_lat);
          show_map.putExtra("current_lng",current_lng);
          startActivity(show_map);
     }
     else if(id==R.id.image_phone){
         Intent i = new Intent(Intent.ACTION_DIAL);
         i.setData(Uri.parse("tel:"+formatted_phone_number));
         startActivity(i);
     }
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
