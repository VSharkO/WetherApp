package com.example.vsharko.jsonexemple;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String city;
    TextView rez;

    EditText text;

    public void search(View view){
        DownloadTask task = new DownloadTask();
        text = findViewById(R.id.editText);
        city = text.getText().toString();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=f1176f6e069cc58d6df2ba5bd7bd93af");

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                rez = findViewById(R.id.rez);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject temperature = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");
                temperature = jsonObject.getJSONObject("main");
                double temp = temperature.getDouble("temp")-272.15;
                Log.i("Weather content: ", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i=0;i<arr.length();i++){

                    JSONObject jsonPart = arr.getJSONObject(i);
                    rez.setText(city+":\n Wether: "+ jsonPart.getString("main")+"\nTemperature: "+temp+"°C");
                    Log.i("Main:",jsonPart.getString("main"));
                    Log.i("Description:",jsonPart.getString("description"));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}