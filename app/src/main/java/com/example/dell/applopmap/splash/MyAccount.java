package com.example.dell.applopmap.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell.applopmap.Adapter.RecentSearchAdapter;
import com.example.dell.applopmap.Adapter.RecyclerViewClickListener;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.firebase.UserReview;
import com.example.dell.applopmap.location.GooglePlacesAutocompleteActivity;
import com.example.dell.applopmap.location.retrofit.LatLngClass;
import com.example.dell.applopmap.sharePreference.LoginData;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyAccount extends AppCompatActivity implements RecyclerViewClickListener
{
    TextView name,search;
    ImageView imageView;
    Button Log_out;
    SeekBar seekBar;
    LoginData data;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        data=new LoginData(MyAccount.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   ids();
    }



  void ids()
{
    linearLayout= (LinearLayout) findViewById(R.id.recycle_recent_search);
    name= (TextView) findViewById(R.id.textViewName);
    search= (TextView) findViewById(R.id.text_view_search_within);
     frameLayout= (FrameLayout) findViewById(R.id.frame);
    imageView=(ImageView) findViewById(R.id.imageView);
progressBar= (ProgressBar) findViewById(R.id.progress);
    Log_out= (Button) findViewById(R.id.button_log_out);
    seekBar= (SeekBar) findViewById(R.id.button_seekBar);
    HashMap<String,String> userData=new HashMap<>();
    userData=data.getUserDetails();
    name.setText(userData.get(LoginData.KEY_NAME));

    seekBar.incrementProgressBy(2);
    seekBar.setMax(10);
    int km=data.getDistance();
    search.setText(String.valueOf(km)+" km");
    seekBar.setProgress(km);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            progress=progress/2;
            progress=progress*2;

            if(progress>=2){
                data.setDistance(progress);
                search.setText(String.valueOf(progress)+" km");
            }
            else{
                data.setDistance(2);
                search.setText(String.valueOf(2)+" km");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }


    });
    Log.e("pic",userData.get(LoginData.KEY_PIC));

    Picasso.with(MyAccount.this).load(userData.get(LoginData.KEY_PIC))
            .placeholder(R.drawable.ic_account_circle_black_48dp)
            .into(imageView);

    Log_out= (Button) findViewById(R.id.button_log_out);
   Log_out.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           LoginManager.getInstance().logOut();
           startActivity(new Intent(MyAccount.this,splash_screen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
       }
   });
}


    @Override
    protected void onStart() {
        super.onStart();
if(new CheckConnection(MyAccount.this).checkInternetConenction()) {
    reycle_view();
}
else{
startActivity(new Intent(MyAccount.this, Internet.class));
}
    }

    private void reycle_view() {

        final String  user_id=data.getKeyId();
        final HashMap<String,String> user_lat_lng=data.getLatLng();
        final List<LatLngClass> list_lat_lng=new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        new UserReview().GetRecentSearch(user_id, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        LatLngClass lat_lng_class=dataSnapshot1.getValue(LatLngClass.class);
		/*				Log.e("Place_Name",String.valueOf(dataSnapshot1.child("Place_Name").getValue()));
						Log.e("Latitude",String.valueOf(dataSnapshot1.child("Latitude").getValue()));
						Log.e("Longitude",String.valueOf(dataSnapshot1.child("Longitude").getValue()));
						Log.e("frequency",String.valueOf(dataSnapshot1.child("frequency").getValue()));


						lat_lng_class.setLat(String.valueOf(dataSnapshot1.child("Latitude").getValue()));
						lat_lng_class.setLng(String.valueOf(dataSnapshot1.child("Longitude").getValue()));
						lat_lng_class.setPlace_name(String.valueOf(dataSnapshot1.child("Place_Name").getValue()));
						lat_lng_class.setFrequency(String.valueOf(dataSnapshot1.child("frequency").getValue()));
		*/

                        list_lat_lng.add(lat_lng_class);
                    }

                    String temp_lat,temp_lng,temp_frequency,temp_name;
                    LatLngClass temp;
                    int LatLng_size=list_lat_lng.size(),i,j,first_value,second_value;

                    for(i=0;i<LatLng_size;i++){
                        first_value=Integer.parseInt(list_lat_lng.get(i).getFrequency());

                        for(j=0;j<=i;j++){
                            second_value=Integer.parseInt(list_lat_lng.get(j).getFrequency());
                            if(first_value>=second_value){

                                temp=list_lat_lng.get(j);
                                list_lat_lng.set(j,list_lat_lng.get(i));
                                list_lat_lng.set(i,temp);
                            }
                        }
                    }

                    for(i=0;i<list_lat_lng.size();i++)
                    {
                        Log.e("value",list_lat_lng.get(i).getFrequency());
                    }

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_recent);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MyAccount.this, 1);
                    recyclerView.setLayoutManager(mLayoutManager);
                    RecentSearchAdapter adapter=new RecentSearchAdapter(user_id,"2.2222"
                            ,"22.004614",list_lat_lng,MyAccount.this);
progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }
                else{
                 frameLayout.setVisibility(View.GONE);
   progressBar.setVisibility(View.GONE);
                }        }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            }
    return true;
    }

    @Override
    public void GetPosition(int position) {

    }

    @Override
    public void GetPosition(String name, String Lat, String Lng) {
       Intent return_intent=new Intent();
        new UserReview(MyAccount.this).AddRecentSearch(data.getKeyId(),name,Lat,Lng);
        return_intent.putExtra(GooglePlacesAutocompleteActivity.LOCATION_NAME,name);
        return_intent.putExtra(GooglePlacesAutocompleteActivity.LOACTION_LAT,Lat);
        return_intent.putExtra(GooglePlacesAutocompleteActivity.LOACTION_LNG,Lng);
        setResult(RESULT_OK,return_intent);
        finish();
    }
}
