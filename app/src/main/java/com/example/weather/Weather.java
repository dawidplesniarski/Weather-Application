package com.example.weather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Weather extends AppCompatActivity {

    ProgressDialog pd;
    TextView textView;
    TextView tempView;
    TextView pressureView;
    TextView tempMinView;
    TextView tempMaxView;
    TextView humidityView;
    private String mCityName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        textView=findViewById(R.id.cityNameTextView);
        tempView=findViewById(R.id.tempTextView);
        tempMinView=findViewById(R.id.tempMinTextView);
        tempMaxView=findViewById(R.id.tempMaxTextView);
        humidityView=findViewById(R.id.humidityTextView);
        pressureView=findViewById(R.id.pressureTextView);
        Intent intent = getIntent();
        mCityName = intent.getStringExtra("cityName");
        String WeatherAdress="http://api.openweathermap.org/data/2.5/weather?q="+mCityName+",pl&APPID=749561a315b14523a8f5f1ef95e45864&units=metric";
        TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(String.valueOf(mCityName));
        new JsonTask().execute(WeatherAdress);
        mCityName ="";
    }




    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Weather.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            //TextView textView = findViewById(R.id.textView);
            //textView.setText(result);
            wUpdate(result);
        }

        void wUpdate(String result)
        {
            String temperature = result.substring(result.indexOf("temp\":")+10,result.indexOf("pressure\":")-2);
            String tempMin = result.substring(result.indexOf("temp_min\":")+10,result.indexOf("temp_max\":")-2);
            String tempMax = result.substring(result.indexOf("temp_max\":")+10,result.indexOf("visibility\":")-3);
            //String cityName = result.substring(result.indexOf("name\":")+10,result.indexOf("cod\":")-3);
            String pressure = result.substring(result.indexOf("pressure\":")+10,result.indexOf("humidity\":")-2);
            String humidity = result.substring(result.indexOf("humidity\":")+10,result.indexOf("temp_min\":")-2);
            tempView.setText(temperature);
            humidityView.setText(humidity);
            tempMaxView.setText(tempMax);
            tempMinView.setText(tempMin);
            pressureView.setText(pressure);

        }
    }


    }



