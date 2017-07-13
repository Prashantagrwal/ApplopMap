package com.example.dell.applopmap.location;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.applopmap.Adapter.RecyclerViewClickListener;
import com.example.dell.applopmap.Adapter.SubCategoryAdapter;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.squareup.picasso.Picasso;

public class SubCategory  extends AppCompatActivity implements RecyclerViewClickListener{

    RecyclerView recyclerView;
    ImageView imageView;
    TextView  textView;
    ProgressBar progressBar;
GridLayoutManager gridLayoutManager;
    Intent intent;
    String category_name,pic_url;
    int pos;
    String[] places,pic_url_array={"apa"};
    double latitude,longitude;

    int[] place_type_id={R.array.health_wellness,R.array.fashion_life_style,R.array.beauty_body_care
    ,R.array.enterainment,R.array.dining_lodging,R.array.services,R.array.education,R.array.pet_care};

    int[] place_type_url={R.array.health_pic_url,R.array.fashion_pic_url,R.array.beauty_pic_url
            ,R.array.enterainment_pic_url,R.array.dining_pic_url,R.array.service_pic_url
            ,R.array.education_pic_url,R.array.petcare_pic_url};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        if(intent!=null){
            category_name=intent.getStringExtra("places");
            pic_url=intent.getStringExtra("place_url");
            pos=intent.getIntExtra("position",0);
            latitude=intent.getDoubleExtra("latitude",0.0);
            longitude=intent.getDoubleExtra("longitude",0.0);

           places=SubCategory.this.getResources().getStringArray(place_type_id[pos]);
      //  pic_url_array=SubCategory.this.getResources().getStringArray(place_type_url[pos]);


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(new CheckConnection(SubCategory.this).checkInternetConenction()) {
            ids();
        }
        else{
            startActivity(new Intent(SubCategory.this, Internet.class));
        }
    }

    private void ids() {
        imageView= (ImageView) findViewById(R.id.imageView_palce_category);
        textView= (TextView) findViewById(R.id.text_place_type);
        progressBar= (ProgressBar) findViewById(R.id.progressBar_place);
        recyclerView= (RecyclerView) findViewById(R.id.recycleViewer_place);
        Picasso.with(SubCategory.this).load(pic_url).placeholder(R.drawable.beauty).into(imageView);
        textView.setText(category_name);
        gridLayoutManager= new GridLayoutManager(SubCategory.this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        SubCategoryAdapter sub=new SubCategoryAdapter(places,pic_url_array,SubCategory.this,this);
        recyclerView.setAdapter(sub);
    progressBar.setVisibility(View.GONE);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void GetPosition(int position)
    {

        if(String.valueOf(latitude)!=null && String.valueOf(longitude)!=null) {
            Intent intent = new Intent(SubCategory.this, GetPlaces.class);
            intent.putExtra("places", places[position]);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        }
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
