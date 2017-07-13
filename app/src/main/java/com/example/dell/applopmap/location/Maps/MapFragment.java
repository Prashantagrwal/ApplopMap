package com.example.dell.applopmap.location.Maps;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.applopmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;


public class MapFragment extends Fragment
{

    MapView mMapView;
    private GoogleMap googleMap;
    String poly_line;
    double user_lng,user_lat,dest_lng,dest_lat;

    public MapFragment(String poly_line, double user_lat,double user_lng, double dest_lat, double dest_lng) {
    this.poly_line=poly_line;
        this.user_lng = user_lng;
        this.user_lat = user_lat;
        this.dest_lng = dest_lng;
        this.dest_lat = dest_lat;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_maps,container,false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        MapShow();
        return view;
    }

    private void MapShow()
    {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap=mMap;
                    PolylineOptions poly=new PolylineOptions();
/*                    for(int i=0;i<poly_line.size();i++)
                    {
                      List<LatLng> lati;
                             lati= PolyUtil.decode(poly_line.get(i));
                            poly.add(lati.get(0));
                    }*/

                   poly.addAll(PolyUtil.decode(poly_line));
                   poly.width(10).color(Color.BLUE);
                    googleMap.addPolyline(poly);

                    LatLng user = new LatLng(user_lat,user_lng);
                    googleMap.addMarker(new MarkerOptions().position(user).title("User Position")).showInfoWindow();
                    MarkerOptions markerOptions = new MarkerOptions();


                    LatLng dest=new LatLng(dest_lat,dest_lng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markerOptions.position(dest).title("Dest Position");
                    // For zooming automatically to the location of the marker
                    googleMap.addMarker(markerOptions).showInfoWindow();


                    CameraPosition cameraPosition1 = new CameraPosition.Builder().target(dest).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(user).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
    }

}
