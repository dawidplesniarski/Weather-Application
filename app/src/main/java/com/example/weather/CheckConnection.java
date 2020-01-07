package com.example.weather;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class CheckConnection {

    static boolean isConnected(MainActivity mainActivity)
    {
        ConnectivityManager manager =(ConnectivityManager) mainActivity.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //połączenie WIFI
                 return true;
            }
            //włączone dane komórkowe
            return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;


        }
            return false;
    }


    static boolean isConnected(Weather weather)
    {
        ConnectivityManager manager =(ConnectivityManager) weather.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //połączenie WIFI
                return true;
            }
            //włączone dane komórkowe
            return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;


        }
        return false;
    }
}
