package com.example.dell.applopmap.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.applopmap.R;
import com.example.dell.applopmap.location.retrofit.LatLngClass;

import java.util.List;


public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    String Lat,Lng,user_id;
    List<LatLngClass> list;
    RecyclerViewClickListener recyclerViewClickListener;

    public RecentSearchAdapter(String user_id, String lat, String lng,List<LatLngClass> list,RecyclerViewClickListener recyclerViewClickListener) {
        Lat = lat;
        Lng = lng;
        this.user_id=user_id;
        this.list=list;
        this.recyclerViewClickListener=recyclerViewClickListener;
    }



    @Override
    public RecentSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_placesearch, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentSearchAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.GetPosition(list.get(position).getPlace_name(),list.get(position).getLat(),
                        list.get(position).getLng());
            }
        });
      holder.textView.setText(list.get(position).getPlace_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        RelativeLayout relativeLayout;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.address);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.predictedRow);
        }

    }
}
