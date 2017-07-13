package com.example.dell.applopmap.facebook.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.dell.applopmap.Model.LoginClass.LoginDetail;
import com.example.dell.applopmap.R;
import com.example.dell.applopmap.connectivity.CheckConnection;
import com.example.dell.applopmap.connectivity.Internet;
import com.example.dell.applopmap.location.GetLocation;
import com.example.dell.applopmap.sharePreference.LoginData;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Collections;

public class Login extends AppCompatActivity
{
    LoginButton loginButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.login);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    if(new CheckConnection(Login.this).checkInternetConenction())
              LogIn();
        else
        {
          startActivity(new Intent(Login.this, Internet.class));
        }
    }

    void LogIn()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginButton= (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Collections.singletonList("public_profile"));

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginButton.setVisibility(View.GONE);
                        GraphRequest request = GraphRequest.newMeRequest(
  loginResult.getAccessToken(),
  new GraphRequest.GraphJSONObjectCallback() {
    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        LoginDetail loginDetail = gson.fromJson(object.toString(), LoginDetail.class);
         if (loginDetail != null) {

             String id=loginDetail.getId();
             String name=loginDetail.getName();
             String profile_pic= loginDetail.getPicture().getData().getUrl();
             LoginData LoginData =new LoginData(Login.this);
             LoginData.UserId(id);
             LoginData.LoginDetails(name,profile_pic);
            Intent intent = new Intent(Login.this, GetLocation.class);
            intent.putExtra(LoginData.KEY_NAME,name);
            intent.putExtra(LoginData.KEY_FB,id);
            intent.putExtra(LoginData.KEY_PIC,profile_pic);
            startActivity(intent);
            finish();
        } else {
               Log.e("Error","something went wrong");
        }
    }
});

          Bundle parameters = new Bundle();
          parameters.putString("fields", "name,email,picture.width(200).height(200)");
          request.setParameters(parameters);
          request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("inside","OnActivityCreated");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
