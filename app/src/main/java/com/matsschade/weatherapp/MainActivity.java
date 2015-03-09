package com.matsschade.weatherapp;

import android.app.ProgressDialog;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected GoogleApiClient mClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;

    protected WeatherAdapter wAdapter;

    protected String LATITUDE;
    protected String LONGITUDE;
    protected String mLastUpdateTime;

    protected ArrayList<Weather> arrayOfWeather = new ArrayList<Weather>();

    // Progress dialog
    private ProgressDialog pDialog;
    private ProgressDialog lDialog;

    // The OpenWeatherAPI URL to request data from
    String url;
    private String BASE_URL = "http://api.openweathermap.org/data/2.5/find?lat=";
    private String MID_URL = "&lon=";
    private String END_URL = "&cnt=10&units=metric";

    // The OpenWeatherAPI URL to request image data from
    private String imgUrl = "http://10.0.2.2:8080/TestAndroid/DownloadServlet?name=logo.png";

    public static final String TAG = "WeatherApp";

    protected Boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherlist);
        setTitle(R.string.app_title);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Dont't panic...");
        pDialog.setCancelable(false);

        lDialog = new ProgressDialog(this);
        lDialog.setMessage("Getting your location...");
        lDialog.setCancelable(false);

        // Get current weather data based on current location
        buildGoogleApiClient();

        wAdapter = new WeatherAdapter(this, arrayOfWeather);

        ListView lv = (ListView) findViewById(R.id.cityNameList);

        lv.setAdapter(wAdapter);
    }

    private void getWeatherData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray b = response.getJSONArray("list");

                    // Get weather data from OpenWeatherAPI and save them into Weather objects
                    for (int i = 0; i < b.length(); i++) {
                        JSONObject c = b.getJSONObject(i);
                        JSONObject d = c.getJSONObject("main");
                        JSONArray e = c.getJSONArray("weather");
                        JSONObject f = e.getJSONObject(0);
                        Weather weather = new Weather(c.getString("name"), d.getDouble("temp"), f.getString("main"));

                        // Populate the WeatherAdapter with the weather objects
                        wAdapter.add(weather);
                        Log.d(TAG, f.getString("main"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                hidepDialog();
                wAdapter.notifyDataSetChanged();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.volley_error), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Refresh the list with current location and current weather data
        if (id == R.id.refresh) {
            wAdapter.clear();
//            mClient.disconnect();
//            mClient.connect();
            createLocationRequest();
            startLocationUpdates();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("Connection", "Connection Successful");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        LATITUDE =  String.valueOf(mLastLocation.getLatitude());
        LONGITUDE = String.valueOf(mLastLocation.getLongitude());
        url = BASE_URL + LATITUDE + MID_URL + LONGITUDE + END_URL;
        getWeatherData();

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        if (mLastLocation != null) {
            LATITUDE =  String.valueOf(mLastLocation.getLatitude());
            LONGITUDE = String.valueOf(mLastLocation.getLongitude());
            url = BASE_URL + LATITUDE + MID_URL + LONGITUDE + END_URL;
            getWeatherData();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Connection", "Connection suspended");
        mClient.connect();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection", "Connection Failed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect to the Location API
        Log.i(TAG, "Connecting...");
        wAdapter.clear();
        mClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect from Location API
        stopLocationUpdates();
        if (mClient.isConnected()) {
            mClient.disconnect();
            Log.i(TAG, "Disconnected onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        stopLocationUpdates();
        // Disconnect from Location API
        if (mClient.isConnected()) {
            mClient.disconnect();
            Log.i(TAG, "Disconnected onDestroy");
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void showlDialog() {
        if (!lDialog.isShowing())
            lDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
