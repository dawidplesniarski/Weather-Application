package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Normalizer;


public class MainActivity extends AppCompatActivity {

    EditText cityName;
    boolean connected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();


    }

    public void onClickCheckWeather(View view)
    {
        cityName = findViewById(R.id.typeCity);
        String mCityName = cityName.getText().toString();


        mCityName =
                Normalizer
                        .normalize(mCityName, Normalizer.Form.NFD)      // Zamiana wszystkich liter specjalnych typu ą ć ź itp.. na regularne litery
                        .replaceAll("[^\\p{ASCII}]", "");

        Intent data = new Intent(this,Weather.class);
        data.putExtra("cityName", mCityName);



        ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //połączenie WIFI
                connected = true;
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                //włączone dane komórkowe
                connected = true;
            }
        } else{
            //brak połączenia
            connected = false;
        }


        if(connected)
        {
            startActivity(data);
            saveData(mCityName);
        }
        if(!connected)
        {
            Toast.makeText(getApplicationContext(),"Brak połączenia internetowego !",Toast.LENGTH_LONG).show();
        }
        //data.removeExtra(mCityName);
    }
    private void saveData(String city)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city",city);
        editor.apply();
    }
    public void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String city = sharedPreferences.getString("city", "");
        cityName = findViewById(R.id.typeCity);
        cityName.setText(city);
    }


    }
