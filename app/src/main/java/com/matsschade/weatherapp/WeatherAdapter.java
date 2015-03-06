package com.matsschade.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mats on 06/03/15.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {
    public WeatherAdapter(Context context, ArrayList<Weather> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Weather weather = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_single, parent, false);
        }
        // Lookup view for data population
        TextView cityName = (TextView) convertView.findViewById(R.id.cityName2);
        TextView cityTemp = (TextView) convertView.findViewById(R.id.cityTemp);
        // Populate the data into the template view using the data object
        cityName.setText(weather.getCityName());
        cityTemp.setText(weather.getCityTemp());
        // Return the completed view to render on screen
        return convertView;
    }
}
