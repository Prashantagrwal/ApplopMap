package com.example.dell.applopmap.firebase;


import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.dell.applopmap.sharePreference.LoginData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseLogin {
    private DatabaseReference mFirebaseDatabase,mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private String fb_id=null,name=null,userId,profile_pic;
    private Context context;
    private static final String USER_TABLE="user_data";
    private static final String USER_NAME="user_name";
    private static final String USER_FB="user_fb_id";
    private ProgressBar progressBar;
    static boolean check=false;
    String review;

    public FirebaseLogin(String name, String fb_id, String profile_pic, Context context) {
        this.profile_pic=profile_pic;
        this.context=context;
        this.progressBar=progressBar;
        this.fb_id=fb_id;this.name=name;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(USER_TABLE);
    }

    public FirebaseLogin() {}

    public FirebaseLogin(String type,int i) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(type);
    }

   public  void getUserData() {


     ValueEventListener valueEventListener=new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             if (!dataSnapshot.exists()) {
                 Log.e("is","Not Exists");
                 userId=mFirebaseDatabase.push().getKey();
                 Log.e("is",userId);
                 mFirebaseDatabase.child(userId).child(USER_NAME).setValue(name);
                 mFirebaseDatabase.child(userId).child(USER_FB).setValue(fb_id);
             }
             else {
                 Log.e("is","Exists");
             }


         }
         @Override
         public void onCancelled(DatabaseError databaseError) {
             Log.e("Error",databaseError.getMessage());
         }
     };

       mFirebaseDatabase.orderByChild(USER_FB).equalTo(fb_id).addListenerForSingleValueEvent(valueEventListener);

   }
}
