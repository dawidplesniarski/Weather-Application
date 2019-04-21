package com.example.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.Normalizer;


public class MainActivity extends AppCompatActivity {

    EditText cityName;
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
        startActivity(data);
        saveData(mCityName);
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
