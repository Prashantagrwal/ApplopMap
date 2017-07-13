package com.example.dell.applopmap.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.applopmap.Model.PlaceType.Result;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.firebase.UserReview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

public class PlaceTypeAdapter extends RecyclerView.Adapter<PlaceTypeAdapter.ViewHolder>  {
    List<Result> list_result;
    Context context;
    String user_id,type;
    UserReview firebaseLogin;
    RecyclerViewClickListener recyclerViewClickListener;
    ValueEventListener childEventListener;

    public PlaceTypeAdapter(List<Result> list_result, Context context, String user_id,
                            String type,RecyclerViewClickListener recyclerViewClickListener)
    {
        this.recyclerViewClickListener=recyclerViewClickListener;
        this.list_result = list_result;
        this.context = context;
        this.user_id=user_id;
        this.type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.likeButtonOne.setBackgroundColor(Color.TRANSPARENT);
        holder.likeButtonTwo.setBackgroundColor(Color.TRANSPARENT);
        holder.likeButtonThree.setBackgroundColor(Color.TRANSPARENT);
        holder.likeButtonFour.setBackgroundColor(Color.TRANSPARENT);
        holder.likeButtonFive.setBackgroundColor(Color.TRANSPARENT);
        holder.likeButtonOne.setEnabled(true);
        holder.likeButtonTwo.setEnabled(true);
        holder.likeButtonThree.setEnabled(true);
        holder.likeButtonFour.setEnabled(true);
        holder.likeButtonFive.setEnabled(true);

        holder.tv_one.setText("NA");
        holder.tv_two.setText("NA");
        holder.tv_three.setText("NA");
        holder.tv_four.setText("NA");
        holder.tv_five.setText("NA");

      holder.tv_place_name.setText(list_result.get(position).getName());
        final String review;

  //      Boolean check=firebaseLogin.isReviewed(user_id,list_result.get(position).getPlaceId());
//Log.e("check", String.valueOf(check));

      /*  if(check)
        {
  //
            review=firebaseLogin.getReviewPlace();
//            Log.e("review",review);

        }

*/
        firebaseLogin=new UserReview();


   firebaseLogin.isReviewed(user_id,list_result.get(position).getPlaceId(),type,new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot)
       {
           boolean ch=false;Iterator<DataSnapshot> review=null;
           String child=null;
           if(dataSnapshot.exists()) {
               ch = true;


               for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
         {
             child  =dataSnapshot1.child("user_review").getValue().toString();
             break;
         }
           }
           if(ch)
           {
            /*   switch (child)
               {
                   case "ANGRY":
                       holder.likeButtonOne.setBackgroundColor(Color.RED);
                       break;
                   case "SAD":
                       holder.likeButtonTwo.setBackgroundColor(Color.RED);
                       break;
                   case "HAPPY":
                       holder.likeButtonThree.setBackgroundColor(Color.RED);
                       break;
                   case "COOL":
                       holder.likeButtonFour.setBackgroundColor(Color.RED);
                       break;
                   case "LOVED_IT":
                       holder.likeButtonFive.setBackgroundColor(Color.RED);
                       break;
               }*/
               holder.likeButtonOne.setEnabled(false);
               holder.likeButtonTwo.setEnabled(false);
               holder.likeButtonThree.setEnabled(false);
               holder.likeButtonFour.setEnabled(false);
               holder.likeButtonFive.setEnabled(false);
           }
       }
       @Override
       public void onCancelled(DatabaseError databaseError) {
           Log.e("Error inside isReviewed",databaseError.getMessage());
       }
   });

        firebaseLogin.TotalReview(type, list_result.get(position).getPlaceId(),
                childEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                              {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.exists()) {
                                Log.e("key", data.getKey());
                                long count = data.getChildrenCount();
                                double count_int = 0;
                                NumberFormat formatter = null;
                                if (count >= 1000) {
                                    count_int = (double) count / 1000;
                                    formatter = new DecimalFormat("#0.0");
                                }
                                switch (data.getKey()) {

                                    case "ANGRY":
                                        if (count < 1000)
                                            holder.tv_one.setText(String.valueOf(count));
                                        else
                                            holder.tv_one.setText(String.valueOf(formatter.format(count_int)));
                                        break;
                                    case "SAD":
                                        if (count < 1000)
                                            holder.tv_two.setText(String.valueOf(count));
                                        else
                                            holder.tv_two.setText(String.valueOf(formatter.format(count_int)));
                                        break;
                                    case "HAPPY":
                                        if (count < 1000)
                                            holder.tv_three.setText(String.valueOf(count));
                                        else
                                            holder.tv_three.setText(String.valueOf(formatter.format(count_int)));

                                        break;
                                    case "COOL":
                                        if (count < 1000)
                                            holder.tv_four.setText(String.valueOf(count));
                                        else
                                            holder.tv_four.setText(String.valueOf(formatter.format(count_int)));
                                        break;
                                    case "LOVED_IT":
                                        if (count < 1000)
                                            holder.tv_five.setText(String.valueOf(count));
                                        else
                                            holder.tv_five.setText(String.valueOf(formatter.format(count_int)));
                                        break;
                                }
                                Log.e("count", String.valueOf(count));
                            }
                        }
                    }
                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        final DecimalFormat formatter = new DecimalFormat("#0.0");

        holder.likeButtonOne.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //  holder.likeButton.setBackgroundColor(Color.RED);
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"ANGRY");
                String s_one=holder.tv_one.getText().toString();

                if(s_one.equals("NA")){
                    holder.tv_one.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_one);
                    if(value_int<1000){
                        holder.tv_one.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_one.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        holder.likeButtonTwo.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
        //        holder.likeButtonTwo.setBackgroundColor(Color.RED);

                String s_two=holder.tv_two.getText().toString();

                if(s_two.equals("NA")){
                    holder.tv_two.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_two);
                    if(value_int<1000){
                        holder.tv_two.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_two.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"SAD");


                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        holder.likeButtonThree.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
              //  holder.likeButtonThree.setBackgroundColor(Color.RED);

                String s_three=holder.tv_three.getText().toString();

                if(s_three.equals("NA")){
                    holder.tv_three.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_three);
                    if(value_int<1000){
                        holder.tv_three.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_three.setText(String.valueOf(formatter.format(check_double)));
                    }
                }

                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"HAPPY");

                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        holder.likeButtonFour.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
      //          holder.likeButtonFour.setBackgroundColor(Color.RED);
                String s_four=holder.tv_four.getText().toString();

                if(s_four.equals("NA")){
                    holder.tv_four.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_four);
                    if(value_int<1000){
                        holder.tv_four.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_four.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"COOL");

                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        holder.likeButtonFive.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
           //     holder.likeButtonFive.setBackgroundColor(Color.RED);
                String s_five=holder.tv_five.getText().toString();

                if(s_five.equals("NA")){
                    holder.tv_five.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_five);
                    if(value_int<1000){
                        holder.tv_five.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_five.setText(String.valueOf(formatter.format(check_double)));
                    }
                }

                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"LOVED_IT");
                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    /*    holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  holder.likeButton.setBackgroundColor(Color.RED);
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"ANGRY");
                String s_one=holder.tv_one.getText().toString();

                if(s_one.equals("NA")){
                    holder.tv_one.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_one);
                    if(value_int<1000){
                        holder.tv_one.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_one.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                holder.likeButton.setEnabled(false);
                holder.two.setEnabled(false);
                holder.three.setEnabled(false);
                holder.four.setEnabled(false);
                holder.five.setEnabled(false);

            }
        });

        holder.two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                holder.two.setBackgroundColor(Color.RED);

                String s_two=holder.tv_two.getText().toString();

                if(s_two.equals("NA")){
                    holder.tv_two.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_two);
                    if(value_int<1000){
                        holder.tv_two.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_two.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"SAD");


                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);
            }
        });
        holder.three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.three.setBackgroundColor(Color.RED);

                String s_three=holder.tv_three.getText().toString();

                if(s_three.equals("NA")){
                    holder.tv_three.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_three);
                    if(value_int<1000){
                        holder.tv_three.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_three.setText(String.valueOf(formatter.format(check_double)));
                    }
                }

                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"HAPPY");

                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);

            }
        });
        holder.four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.four.setBackgroundColor(Color.RED);
                String s_four=holder.tv_four.getText().toString();

                if(s_four.equals("NA")){
                    holder.tv_four.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_four);
                    if(value_int<1000){
                        holder.tv_four.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_four.setText(String.valueOf(formatter.format(check_double)));
                    }
                }
                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"COOL");

                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);  }
        });
        holder.five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.five.setBackgroundColor(Color.RED);
                String s_five=holder.tv_five.getText().toString();

                if(s_five.equals("NA")){
                    holder.tv_five.setText(String.valueOf(1));
                }
                else{
                    long value_int=Long.valueOf(s_five);
                    if(value_int<1000){
                        holder.tv_five.setText(String.valueOf(value_int+1));
                    }
                    else{
                        double check_double=(double)(value_int+1)/1000;
                        holder.tv_five.setText(String.valueOf(formatter.format(check_double)));
                    }
                }

                firebaseLogin.AddReview(user_id,type,list_result.get(position).getPlaceId(),"LOVED_IT");
                holder.likeButtonOne.setEnabled(false);
                holder.likeButtonTwo.setEnabled(false);
                holder.likeButtonThree.setEnabled(false);
                holder.likeButtonFour.setEnabled(false);
                holder.likeButtonFive.setEnabled(false);
            }
        });
    */
    }

    @Override
    public int getItemCount() {
        return list_result.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_place_name;
        TextView tv_one,tv_two,tv_three,tv_four,tv_five;
        LikeButton likeButtonOne,likeButtonTwo,likeButtonThree,likeButtonFour,likeButtonFive;
        ViewHolder(View itemView)
        {
            super(itemView);
            tv_place_name=(TextView) itemView.findViewById(R.id.place_name);

            likeButtonOne= (LikeButton) itemView.findViewById(R.id.angry);
            likeButtonTwo= (LikeButton) itemView.findViewById(R.id.b_sad);
            likeButtonThree= (LikeButton) itemView.findViewById(R.id.b_happy);
            likeButtonFour= (LikeButton) itemView.findViewById(R.id.b_good);
            likeButtonFive= (LikeButton) itemView.findViewById(R.id.b_loved_it);



            tv_one=(TextView) itemView.findViewById(R.id.tv_1);
            tv_two=(TextView) itemView.findViewById(R.id.tv_2);
            tv_three=(TextView) itemView.findViewById(R.id.tv_3);
            tv_four=(TextView) itemView.findViewById(R.id.tv_4);
            tv_five=(TextView) itemView.findViewById(R.id.tv_5);

        //    one= (ImageView) itemView.findViewById(R.id.b_angry);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
        recyclerViewClickListener.GetPosition(getAdapterPosition());
        }
    }
}
