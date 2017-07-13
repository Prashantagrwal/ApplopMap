package com.example.dell.applopmap.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.facebook.login.Login;
import com.example.dell.applopmap.location.GetLocation;
import com.example.dell.applopmap.sharePreference.LoginData;
import com.facebook.AccessToken;

import java.util.HashMap;

public class splash_screen extends AppCompatActivity
{




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(new CheckConnection(splash_screen.this).checkInternetConenction()) {
            final boolean check= isLoggedIn();

            int SPLASH_TIME_OUT = 3000;
            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                    if(check)
                    {
                        LoginData login=new LoginData(splash_screen.this);
                        HashMap<String,String> hash=login.getUserDetails();
                        Intent intent = new Intent(splash_screen.this, GetLocation.class);
                        intent.putExtra(LoginData.KEY_NAME,hash.get(LoginData.KEY_NAME));
                        intent.putExtra(LoginData.KEY_FB,login.getKeyId());
                        intent.putExtra(LoginData.KEY_PIC,hash.get(LoginData.KEY_PIC));
                        startActivity(intent);
                    }
                    else
                    {
                        startActivity(new Intent(splash_screen.this, Login.class));
                    }
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }
        else{
            startActivity(new Intent(splash_screen.this, Internet.class));
        }
    }
}
