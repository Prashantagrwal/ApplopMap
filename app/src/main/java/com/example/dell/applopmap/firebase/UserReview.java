package com.example.dell.applopmap.firebase;

import android.content.Context;
import android.util.Log;

import com.example.dell.applopmap.location.retrofit.LatLngClass;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserReview
{
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private static final String ACTION="user_action";
    private static final String REVIEW="user_review";
    private static final String RECENT_SEARCH="recent_search";
    private static final String CATEGORY="category";
    Context context;
    public UserReview()
    {
         mFirebaseInstance=FirebaseDatabase.getInstance();
         mFirebaseDatabase=mFirebaseInstance.getReference();
    }
    public UserReview(Context context)
    {
        this.context=context;
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase=mFirebaseInstance.getReference();
    }

public void AddReview(String user_id,String type,String place_id,String user_review)
{
    Log.e("onLog", "AddReview:");
    String key=mFirebaseDatabase.push().getKey();
    String key1=mFirebaseDatabase.push().getKey();
    mFirebaseDatabase.child(ACTION).child(user_id).child(REVIEW).child(type).child(key).child("plce_id").setValue(place_id);
    mFirebaseDatabase.child(ACTION).child(user_id).child(REVIEW).child(type).child(key).child("user_review").setValue(user_review);
    mFirebaseDatabase.child(CATEGORY).child(type).child(place_id).child(user_review).child(key1).child("user_id").setValue(user_id);
}

    public void AddRecentSearch(final String id, final String name, final String lat, final String lng)
    {
        Log.e("onLog", "AddReview:");
        final String key=mFirebaseDatabase.push().getKey();
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Log.e("user_location"," not exists");
                    LatLngClass latLngClass=new LatLngClass(name,lat,lng,String.valueOf(1));
                    mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).child(key).setValue(latLngClass);
            /*        mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).child(key).child("Latitude").setValue(lat);
                    mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).child(key).child("Longitude").setValue(lng);
                    mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).child(key).child("frequency").setValue(1);
                    mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).child(key).child("Place_Name").setValue(name);
               */ }
                else {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String count=String.valueOf(dataSnapshot1.child("frequency").getValue());
                        int c=Integer.parseInt(count)+1;
                        String child_key=dataSnapshot1.getKey();
                         Firebase.setAndroidContext(context);
                        Firebase m_objFireBaseRef = new Firebase("https://applopmap.firebaseio.com/");
                        Firebase objRef = m_objFireBaseRef.child(ACTION).child(id).child(RECENT_SEARCH);
                        objRef.child(child_key).child("frequency").setValue(String.valueOf(c));
                        Log.e("count",dataSnapshot1.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

       mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).orderByChild("place_name").equalTo(name)
               .addListenerForSingleValueEvent(valueEventListener);

    }

   public void GetRecentSearch(String id,ValueEventListener valueEventListener){
       Log.e("GetRecentSearch","inside");
        mFirebaseDatabase.child(ACTION).child(id).child(RECENT_SEARCH).addValueEventListener(valueEventListener);
    }




    public static final String MYTEST = "mytest";

    public void isReviewed(String user_id, final String placeId,String type,ValueEventListener valueEvent) {
/*

        final boolean[] b = {false};
        Log.d(MYTEST, "initial value : " + Arrays.toString(b));
*/


        mFirebaseDatabase.child(ACTION).child(user_id).child(REVIEW).child(type).
                orderByChild("plce_id").equalTo(placeId).addListenerForSingleValueEvent(valueEvent);

//        return b[0];
    }

   public  void TotalReview(String type,String place_id,ValueEventListener valueEventListener)
    {

        mFirebaseDatabase.child(CATEGORY).child(type).child(place_id).addValueEventListener(valueEventListener);
    }
}
