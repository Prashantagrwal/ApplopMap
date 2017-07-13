package com.example.dell.applopmap.connectivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.dell.applopmap.R;
import com.example.dell.applopmap.splash.splash_screen;


public class Internet extends AppCompatActivity
{  Button retry;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet);
    retry = (Button) findViewById(R.id.b_retry);

        retry.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if(new CheckConnection(Internet.this).checkInternetConenction())
            {
             intent=new Intent(Internet.this,splash_screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                intent=new Intent(Internet.this,Internet.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    });

    }

}
