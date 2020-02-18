package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeatherAdapter extends BaseAdapter {

    private final List<Weather> weathers;
    private final Context context;

    public WeatherAdapter(Context context, List<Weather> weathers) {
        this.weathers = weathers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return weathers.size();
    }

    @Override
    public Object getItem(int position) {
        return weathers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather weather = weathers.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_layout, parent, false);
        }

        ImageView campoIcon = view.findViewById(R.id.item_icon);
        String iconUrl = "https://openweathermap.org/img/w/" + weather.getIcon() + ".png";
        Glide.with(context).load(iconUrl).into(campoIcon);

        TextView campoNome = view.findViewById(R.id.item_nome);
        campoNome.setText(weather.getName());

        TextView campoTemperatura = view.findViewById(R.id.item_temp);
        campoTemperatura.setText(weather.getTemperature() + " ºC");

        return view;
    }
}
