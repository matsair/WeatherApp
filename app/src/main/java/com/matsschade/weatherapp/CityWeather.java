package com.matsschade.weatherapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CityWeather extends ActionBarActivity {

    private static String IMG_URL = "http://openweathermap.org/img/w/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");

        setContentView(R.layout.activity_city_weather);

        final TextView mResponse = (TextView) findViewById(R.id.temp);
        final TextView nResponse = (TextView) findViewById(R.id.cityname);
        final TextView oResponse = (TextView) findViewById(R.id.description);


        //setContentView(R.layout.activity_city_weather);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String partOne = "http://api.openweathermap.org/data/2.5/find?lat=";
        String partTwo = latitude;
        String partThree = "&lon=";
        String partFour = longitude;
        String partFive = "&cnt=10";
        String url =  partOne + partTwo + partThree + partFour + partFive;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Access the elements in the JSON response
                        try {
                            JSONObject a = new JSONObject(response);
                            JSONArray b = a.getJSONArray("list");
                            JSONObject c = b.getJSONObject(1);
                            nResponse.setText(c.getString("name"));
                            JSONObject d = c.getJSONObject("main");
                            mResponse.setText(d.getString("temp"));

                            JSONArray e = c.getJSONArray("weather");
                            JSONObject f = e.getJSONObject(0);
                            oResponse.setText(f.getString("main"));
                        }
                        catch (JSONException e) {
                            Log.d("JSON", "Error");
                        }

                        Log.d("Response is: ", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("That didn't work!", "");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
