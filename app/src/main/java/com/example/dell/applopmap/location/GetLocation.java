package com.example.dell.applopmap.location;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.applopmap.Adapter.NearByPlacesNameAdapter;
import com.example.dell.applopmap.Adapter.RecyclerViewClickListener;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.firebase.FirebaseLogin;
import com.example.dell.applopmap.sharePreference.LoginData;
import com.example.dell.applopmap.splash.MyAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GetLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        RecyclerViewClickListener, View.OnClickListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    //private boolean mRequestingLocationUpdates = false;

    public static int flag = 0;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    double latitude, longitude;
    Intent intent;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    String[] place_category = {"hospital", "bus_station", "cafe", "restaurant", "accounting", "airport", "amusement_park",
            "aquarium", "art_gallery", "atm", "bakery", "bank", "bar"};

String[] image_url;
String[] places={
        "Health & Wellness","Fashion & LifeStyle ","Beauty & Bodycare","Entertainment",
        "Dining & Lodging",
        "Services","Education","Petcare"};



    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private final static int MY_REQUEST_LOCATION = 16;
    private final static int REQUEST_PERMISSION_SETTING = 101;
    private final static int REQUEST_AUTO_COMPLETE= 1001;
    private final static int REQUEST_MY_ACCOUNT= 120;
    boolean sentToSettings;
    SharedPreferences permissionStatus;
    TextView text_address;
    ImageView imageView;
    String[] place_pic;
CheckConnection checkConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.location);

        text_address= (TextView) findViewById(R.id.textView_location);

       // new CurrentLocation(GetLocation.this);
        text_address.setOnClickListener(this);



        imageView= (ImageView) findViewById(R.id.image_account);

    imageView.setOnClickListener(this);




       place_pic =GetLocation.this.getResources().getStringArray(R.array.category_url);
        intent = getIntent();

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

          checkConnection=new CheckConnection(GetLocation.this);
        if(checkConnection.checkInternetConenction()){
            GetIntent();
            if (checkPlayServices()) {

                // Building the GoogleApi client
                buildGoogleApiClient();

                createLocationRequest();
            }
            permissionLocation();
        }
        else
        {
            startActivity(new Intent(GetLocation.this, Internet.class));
        }

    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(GetLocation.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }



    @Override
    protected void onStart() {
        super.onStart();
if(!checkConnection.checkInternetConenction()) {
    startActivity(new Intent(GetLocation.this, Internet.class));
}
    }

    private void GetIntent() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
       // progressBar.setVisibility(View.VISIBLE);
       // progressBar.setIndeterminate(false);
        if (intent != null) {

            Picasso.with(GetLocation.this).load(intent.getStringExtra(LoginData.KEY_PIC))
                    .placeholder(R.drawable.ic_account_circle_white_24dp)
                    .into(imageView);
            new FirebaseLogin(intent.getStringExtra(LoginData.KEY_NAME), intent.getStringExtra(LoginData.KEY_FB),
                    intent.getStringExtra(LoginData.KEY_PIC), GetLocation.this).getUserData();
        }
        Adapter();
    }

    private void Adapter() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewer);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        NearByPlacesNameAdapter adapter = new NearByPlacesNameAdapter(GetLocation.this, places,place_pic,this);
        recyclerView.setAdapter(adapter);

    }

    private void permissionLocation() {

        if (ContextCompat.checkSelfPermission(GetLocation.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GetLocation.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(GetLocation.this);
                builder.setTitle("mandatory Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(GetLocation.this, new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        permissionLocation();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission

                AlertDialog.Builder builder = new AlertDialog.Builder(GetLocation.this);
                builder.setTitle("mandatory Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to permission and enable location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(GetLocation.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.ACCESS_FINE_LOCATION, true);
            editor.commit();
        }

        else
            {

                Log.e("value","outside");
                if (mGoogleApiClient != null)
                {
                    Log.e("value","inside");
                    mGoogleApiClient.connect();
                }


                if (mGoogleApiClient.isConnected() && permissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION,true)) {
                    startLocationUpdates();
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (mGoogleApiClient != null)
                {
                    mGoogleApiClient.connect();
                }

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                permissionLocation();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(GetLocation.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (mGoogleApiClient != null)
                {
                    mGoogleApiClient.connect();
                }
            }
        }
        else if(requestCode==REQUEST_AUTO_COMPLETE)
        {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else  if(requestCode==MY_REQUEST_LOCATION){
            if(resultCode==RESULT_OK){
                latitude=Double.valueOf(data.getStringExtra(GooglePlacesAutocompleteActivity.LOACTION_LAT));
                longitude=Double.valueOf(data.getStringExtra(GooglePlacesAutocompleteActivity.LOACTION_LNG));
                text_address.setText(data.getStringExtra(GooglePlacesAutocompleteActivity.LOCATION_NAME));
            }
            else if(resultCode==RESULT_FIRST_USER) {
                startLocationUpdates();
            }
        }
        else if(requestCode==REQUEST_MY_ACCOUNT){
            if(resultCode==RESULT_OK){
                latitude=Double.valueOf(data.getStringExtra(GooglePlacesAutocompleteActivity.LOACTION_LAT));
                longitude=Double.valueOf(data.getStringExtra(GooglePlacesAutocompleteActivity.LOACTION_LNG));
                text_address.setText(data.getStringExtra(GooglePlacesAutocompleteActivity.LOCATION_NAME));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_in) {
            startActivity(new Intent(GetLocation.this, MyAccount.class));
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
if(mGoogleApiClient.isConnected())
        stopLocationUpdates();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
          if(mGoogleApiClient.isConnected())
           {
               displayLocation();
           }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.e("inside", "onConnectionSuspended");
        mGoogleApiClient.connect();
    }



    private void displayLocation() {


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null)
        {
      if(flag==0){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Geocoder geocoder = new Geocoder(GetLocation.this, Locale.getDefault());

            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);


                if(addresses.get(0).getSubLocality()!=null && addresses.get(0).getLocality()!=null) {
                    text_address.setText(addresses.get(0).getSubLocality() + "," + addresses.get(0).getLocality());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        }else
        {
            Log.e("value","null");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }


    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }



    @Override
    public void GetPosition(int position)
    {

       // Toast.makeText(GetLocation.this,"item clicked"+position,Toast.LENGTH_LONG).show();
    if(String.valueOf(latitude)!=null && String.valueOf(longitude)!=null) {
        Intent intent = new Intent(GetLocation.this, SubCategory.class);
        intent.putExtra("places", places[position]);
        intent.putExtra("place_url", place_pic[position]);
        intent.putExtra("position",position);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        Log.e("latlng", latitude + " ," + longitude);
        startActivity(intent);
    }
    }

    @Override
    public void GetPosition(String name, String Lat, String Lng) {

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        switch(id){

            case R.id.textView_location:

            Intent  intent_text=new Intent(GetLocation.this,GooglePlacesAutocompleteActivity.class);
            intent_text.putExtra("latitude",latitude);
            intent_text.putExtra("longitude",longitude);
            startActivityForResult(intent_text,MY_REQUEST_LOCATION);

                break;
            case R.id.image_account:
                Intent  intent_account=new Intent(GetLocation.this, MyAccount.class);
                startActivityForResult(intent_account,REQUEST_MY_ACCOUNT);
                break;
        }
    }
}
