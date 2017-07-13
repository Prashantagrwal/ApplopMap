package com.example.dell.applopmap.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.applopmap.R;
import com.squareup.picasso.Picasso;

public class NearByPlacesNameAdapter extends RecyclerView.Adapter<NearByPlacesNameAdapter.ViewHolder> {

    Context context;
    String[] places,place_url;
    RecyclerViewClickListener recyclerViewClickListener;
    public NearByPlacesNameAdapter(Context context, String[] places,String[] place_url,RecyclerViewClickListener recyclerViewClickListener)
    {
        this.recyclerViewClickListener=recyclerViewClickListener;
        this.context = context;
        this.places = places;
        this.place_url=place_url;
    }

    @Override
    public NearByPlacesNameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type, parent, false);

      return  new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NearByPlacesNameAdapter.ViewHolder holder, int position)
    {

      holder.textView.setText(places[position]);
        Picasso.with(context).load(place_url[position]).placeholder(R.drawable.beauty).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
ImageView imageView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.text_place_type);
            imageView= (ImageView) itemView.findViewById(R.id.imageView_palce_category);
           itemView.setOnClickListener(this);
         }

        @Override
        public void onClick(View view)
        {
            recyclerViewClickListener.GetPosition(getLayoutPosition());

        }
    }
}
