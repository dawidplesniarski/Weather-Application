package com.example.weather;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Weather extends AppCompatActivity {

    ProgressDialog pd;
    TextView textView;
    TextView tempView;
    TextView pressureView;
    TextView tempMinView;
    TextView tempMaxView;
    TextView humidityView;
    String mCityName="";
    String MainWeather="";
    ImageView imageView;
    TextView timeView;
    Timer timer = new Timer();
    TimerTask timerTask;
    private static boolean run = true;

    @SuppressLint("CutPasteId")
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
        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        mCityName = intent.getStringExtra("cityName");
        String WeatherAdress="http://api.openweathermap.org/data/2.5/weather?q="+ mCityName +",pl&APPID=749561a315b14523a8f5f1ef95e45864&units=metric";
        TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(String.valueOf(mCityName));
        new JsonTask().execute(WeatherAdress);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        timeView = findViewById(R.id.TimeTextView);
        timeView.setText(dateFormat.format(date));
        AutoReload();
        run = true;

    }

    public void BackToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




    @SuppressLint("StaticFieldLeak")
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

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
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
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                //TextView textView = findViewById(R.id.textView);
                //textView.setText(result);

            if(result!=null)
            {
                wUpdate(result);
            }else{
                Toast.makeText(getApplicationContext(),"Podano błędne miasto!",Toast.LENGTH_LONG).show();
                BackToMain();
            }



        }

        @SuppressLint("SetTextI18n")
        void wUpdate(String result)
        {
            String tempMax;
            String tempMin = result.substring(result.indexOf("temp_min\":")+10,result.indexOf("temp_max\":")-2);

            if(result.contains("visibility")){
                tempMax = result.substring(result.indexOf("temp_max\":")+10,result.indexOf("visibility\":")-3);
            }else
                tempMax = result.substring(result.indexOf("temp_max\":")+10,result.indexOf("wind\":")-3);

            String pressure = result.substring(result.indexOf("pressure\":")+10,result.indexOf("humidity\":")-2);
            String humidity = result.substring(result.indexOf("humidity\":")+10,result.indexOf("temp_min\":")-2);
            String temperature = result.substring(result.indexOf("temp\":")+6,result.indexOf("pressure\":")-2);
            MainWeather = result.substring(result.indexOf("description\":")+14,result.indexOf("icon\":")-3);

            TextView test = findViewById(R.id.TEST);

            test.setText(MainWeather);
            tempView.setText(temperature + "°C");
            humidityView.setText(humidity + "%");
            tempMaxView.setText(tempMax + "°C");
            tempMinView.setText(tempMin + "°C");
            pressureView.setText(pressure+ "hPa");
            if(MainWeather.contains("cloud") || MainWeather.contains("few clouds"))
                imageView.setImageResource(R.drawable.cloudy_sky);

            if(MainWeather.contains("clear") || MainWeather.contains("sky"))
                imageView.setImageResource(R.drawable.clear_sky);

            if(MainWeather.contains("rain"))
                imageView.setImageResource(R.drawable.rain);


        }

    }
public void AutoReload()
{
     timerTask = new TimerTask() {
        @Override
        public void run() {
            if(run) {
           ReloadActivity();
            } else {
                timer.cancel();
                timer.purge();
            }
        }
    };
    timer.schedule(timerTask, 10000, 10000);
}

    public void onClickReload(View view)
    {
        ReloadActivity();
        run = false;
    }

    public void ReloadActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}



