package com.matsschade.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

        // Lookup the view that is to be populated
        TextView cityName = (TextView) convertView.findViewById(R.id.cityName);
        TextView cityTemp = (TextView) convertView.findViewById(R.id.cityTemp);
        ImageView icon = (ImageView)   convertView.findViewById(R.id.icon);

        // Populate the data into list_single
        cityName.setText(weather.getCityName());
        cityTemp.setText(weather.getCityTemp());
        // Thanks to Icons8 for the cloud icon. See http://icons8.com/web-app/649/Clouds
        icon.setImageResource(R.drawable.cloud);

        // Return the completed view to render on screen
        return convertView;
    }
}
