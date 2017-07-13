package com.example.dell.applopmap.sharePreference;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginData
{
   private SharedPreferences pref;

    // Editor for Shared preferences
  private   SharedPreferences.Editor editor;

    // Context
   private Context _context;

    // Shared pref mode
   private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Applop";


    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_FB = "fb_id";
    public static final String KEY_PIC = "profile_pic";
    public static final String KEY_KM= "user_range";
    public static final String KEY_LAT= "user_lat";
    public static final String KEY_LNG= "user_lng";

    // Constructor
    public LoginData(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }


    public void LoginDetails(String name,String profile_pic)
    {
        editor.putString(KEY_NAME, name);
        // commit changes

    editor.putString(KEY_PIC,profile_pic);
        editor.commit();
    }

    public void UserId(String id)
    {
        editor.putString(KEY_FB,id);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        // user email id

        user.put(KEY_PIC,pref.getString(KEY_PIC,null));
        return user;
    }

    public String getKeyId()
    {
        return pref.getString(KEY_FB,null);
    }

    public void setDistance(int km) {
        editor.putInt(KEY_KM,km);
        editor.commit();
    }

    public int getDistance(){
        return pref.getInt(KEY_KM,2);
    }

    public void setLatLng(String lat,String lng){
        editor.putString(KEY_LAT,lat);
        editor.putString(KEY_LNG,lng);
        editor.commit();
    }

    public HashMap<String,String> getLatLng(){
        HashMap<String,String> user_LatLng =new HashMap<String, String>();
        user_LatLng.put(KEY_LAT,pref.getString(KEY_LAT,null));
        user_LatLng.put(KEY_LNG,pref.getString(KEY_LNG,null));
        return user_LatLng;
    }
}
