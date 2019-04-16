package com.example.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClickCheckWeather(View view)
    {
        EditText cityName = findViewById(R.id.typeCity);
        String mCityName = cityName.getText().toString();
        Intent data = new Intent(this,Weather.class);
        data.putExtra("cityName", mCityName);
        startActivity(data);
        //data.removeExtra(mCityName);
    }


    }
