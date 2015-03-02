package com.matsschade.weatherapp;

import android.app.Activity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mats on 02/03/15.
 */

public class getData extends Activity {

    final TextView mResponse = (TextView) findViewById(R.id.response);

    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://openweathermap.org/data/getrect?type=city&lat1=55.75&lat2=60.75&lng1=32.61&lng2=38.61";

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    mResponse.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mResponse.setText("That didn't work!");
        }
    });
// Add the request to the RequestQueue.
   // queue.add(stringRequest);

}
