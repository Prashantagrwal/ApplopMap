package com.example.dell.applopmap.location;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.applopmap.Adapter.RecentSearchAdapter;
import com.example.dell.applopmap.Adapter.RecyclerViewClickListener;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.firebase.UserReview;
import com.example.dell.applopmap.location.retrofit.LatLngClass;
import com.example.dell.applopmap.sharePreference.LoginData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class GooglePlacesAutocompleteActivity extends AppCompatActivity implements OnItemClickListener,
		View.OnClickListener,RecyclerViewClickListener {

	private static final String LOG_TAG = "ExampleApp";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
    private static final String USER_KEY_INT="int_value";
	public static final String LOCATION_NAME="location_name";
	public static final String LOACTION_LAT="location_lat";
	public static final String LOACTION_LNG="location_lng";
	Button button_location;
    Intent return_intent,intent_value;
	FrameLayout frameLayout;
	double lat,lng;
	List<LatLngClass> list_lat_lng=null;

	//------------ make your specific key ------------
	private static final String API_KEY = "AIzaSyCl7FXM7ivMukEv1wLidhVe56O4jqBAnRQ";
	LoginData loginData;
	String user_id;
	AutoCompleteTextView autoCompView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_search);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
		intent_value=getIntent();

	loginData=new LoginData(GooglePlacesAutocompleteActivity.this);
	user_id=loginData.getKeyId();

		if(intent_value!=null){
			lat=intent_value.getDoubleExtra("latitude",0.0);
			lng=intent_value.getDoubleExtra("longitude",0.0);
		frameLayout= (FrameLayout) findViewById(R.id.frame);

		}
		return_intent=new Intent();
        button_location= (Button) findViewById(R.id.b_location);
		button_location.setOnClickListener(this);


	}

	@Override
	protected void onStart() {
		super.onStart();
		if(new CheckConnection(GooglePlacesAutocompleteActivity.this).checkInternetConenction()) {
			recycle_view();
			autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_view));
			autoCompView.setOnItemClickListener(this);
		}
		else{
			startActivity(new Intent(GooglePlacesAutocompleteActivity.this, Internet.class));
		}
	}
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		Geocoder geocoder=new Geocoder(GooglePlacesAutocompleteActivity.this);
		try {
			List<Address> addressList=geocoder.getFromLocationName(str,5);

			for (Address address:addressList){
			String lat= String.valueOf(address.getLatitude());
			String lng= String.valueOf(address.getLongitude());
			Log.e("auto complete",String.valueOf(lat)+", "+String.valueOf(lng));


if(lat!=null && lng!=null){
			new UserReview(GooglePlacesAutocompleteActivity.this).AddRecentSearch(user_id,str,lat,lng);

				return_intent.putExtra(LOCATION_NAME,str);
				return_intent.putExtra(LOACTION_LAT,lat);
				return_intent.putExtra(LOACTION_LNG,lng);
				setResult(RESULT_OK,return_intent);
				finish();
		}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public static ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&components=country:in");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			
			System.out.println("URL: "+url);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
		
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());

		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.b_location){
			GetLocation.flag=0;
			setResult(RESULT_FIRST_USER,return_intent);
	     	finish();
		}
	}

	@Override
	public void GetPosition(int position) {

	}

	@Override
	public void GetPosition(String name, String Lat, String Lng) {
		new UserReview(GooglePlacesAutocompleteActivity.this).AddRecentSearch(user_id,name,Lat,Lng);
		return_intent.putExtra(LOCATION_NAME,name);
		return_intent.putExtra(LOACTION_LAT,Lat);
		return_intent.putExtra(LOACTION_LNG,Lng);
		setResult(RESULT_OK,return_intent);
		finish();
	}

	class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
		private ArrayList<String> resultList;

		public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}


		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View	v = inflater.inflate(R.layout.custom_array, null);

			TextView textView=(TextView) v.findViewById(R.id.text_auto_pace_name);
			textView.setText(resultList.get(position));
			return v;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();

					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
		  setResult(RESULT_CANCELED,return_intent);
				finish();
		}
		return true;
	}

	private void recycle_view() {

		LoginData data=new LoginData(GooglePlacesAutocompleteActivity.this);
		final String  user_id=data.getKeyId();

		list_lat_lng=new ArrayList<>();
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
					RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GooglePlacesAutocompleteActivity.this, 1);
					recyclerView.setLayoutManager(mLayoutManager);
					RecentSearchAdapter adapter=new RecentSearchAdapter(user_id,String.valueOf(lat)
							,String.valueOf(lng),list_lat_lng,GooglePlacesAutocompleteActivity.this);

								recyclerView.setAdapter(adapter);
				}
				else{
                    frameLayout.setVisibility(View.GONE);
				}
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});



	}
}