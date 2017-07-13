package com.example.dell.applopmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.applopmap.R;

/**
 * Created by DELL on 12/07/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder> {

    String[] place_type;
    String[] pic_url;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;

    public SubCategoryAdapter(String[] place_type, String[] pic_url, Context context,RecyclerViewClickListener
                              recyclerViewClickListener) {
        this.place_type = place_type;
        this.pic_url = pic_url;
        this.context = context;
        this.recyclerViewClickListener=recyclerViewClickListener;
    }

    @Override
    public SubCategoryAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_adapter,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.viewHolder holder, int position) {
        //Picasso.with(context).load(pic_url[position]).placeholder(R.drawable.ic_account_circle_white_24dp)
          //      .into(holder.imageView);
         holder.textView.setText(place_type[position]);
    }

    @Override
    public int getItemCount() {
        return place_type.length;
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;
        public viewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image_view_url);
            textView= (TextView) itemView.findViewById(R.id.tv_place_type);
        itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.GetPosition(getLayoutPosition());
        }
    }
}
