package com.example.dell.applopmap.connectivity;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class MyAlertDialog
{
    Context context;

    MyAlertDialog(Context context)
    {
        this.context=context;
    }

    void NoConnection()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Try by clicking on it").setTitle("No connectivity")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                       if(new CheckConnection(context).checkInternetConenction()){

                       }
                    }

                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
