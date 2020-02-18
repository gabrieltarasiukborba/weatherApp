package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private JSONArray jsonCitiesData;
    private ListView list;
    private String[] listItems;
    private Map<String,Integer> hashMapCities = new HashMap<>();
    private boolean[] checkItems;
    private ArrayList<Integer> mCitiesItens = new ArrayList<>();
    private String searchCityTag;
    private Volley volley;
    private JSONObject data,answer;
    private Utils messager = new Utils();
    private List<Weather> weatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.weather_list);

        try {
            jsonCitiesData = new JSONArray(loadJSONFromAsset(this));
            listItems = new String[jsonCitiesData.length()];
            for(int i=0;i < jsonCitiesData.length();i++){
                JSONObject jsonObject = jsonCitiesData.getJSONObject(i);
                listItems[i] = jsonObject.getString("name");
                hashMapCities.put(jsonObject.getString("name"),jsonObject.getInt("id"));
            }
            checkItems = new boolean[listItems.length];
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("city_brazil.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void FilterCities(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Selecione as cidades a serem pesquisadas");

        mBuilder.setMultiChoiceItems( listItems, checkItems, (dialog, position, isChecked) -> {
            if(isChecked){
                if(!mCitiesItens.contains(position)){
                    mCitiesItens.add(position);
                }else{
                    mCitiesItens.remove(position);
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", (dialog, which) -> {
            String item = "";
            for(int i = 0; i < mCitiesItens.size();i++){
                item = item + hashMapCities.get(listItems[mCitiesItens.get(i)]);
                if( i != mCitiesItens.size() -1){
                    item = item + ",";
                }
            }
            searchCityTag = item;
            Toast.makeText(this,"Cities Selected, Click on Search to get Weather Information",Toast.LENGTH_LONG).show();
        });

        mBuilder.setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());

        mBuilder.setNeutralButton("Clear All", (dialog, which) -> {
            for(int i = 0; i < checkItems.length; i++){
                checkItems[i] = false;
                mCitiesItens.clear();
                searchCityTag = "";
                list.setAdapter(null);
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void RequestWeather(View view) {

        if((mCitiesItens.size() > 20) || (mCitiesItens.size() == 0)){
            Toast.makeText(this,"Maximum limit of cities exceeded",Toast.LENGTH_LONG).show();
        }else{
            data = new JSONObject();
            try{
                data.put("id",searchCityTag);
                data.put("appid",messager.getAppid());
                data.put("url",messager.getHeader());

                volley = new Volley(data, getApplicationContext(), response -> {
                    try {
                        answer = new JSONObject(response);
                        carregalista(answer);

                    } catch (JSONException e) {
                        Log.e("LOG_ERROR", Arrays.toString(e.getStackTrace()));
                        Toast.makeText(this,"Problem to Search Cities",Toast.LENGTH_LONG).show();
                    }
                });

                volley.requestWheatherCities();
            } catch (JSONException e) {
                Log.e("LOG_ERROR", Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void carregalista(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for(int i = 0; i < jsonArray.length();i++)
            {
                Weather weather = new Weather();
                JSONObject aux = jsonArray.getJSONObject(i);
                weather.setName(aux.getString("name"));
                JSONObject main = aux.getJSONObject("main");
                weather.setTemperature(main.getString("temp"));
                JSONArray weatherImages = aux.getJSONArray("weather");
                JSONObject weatherIcons = weatherImages.getJSONObject(0);
                weather.setIcon(weatherIcons.getString("icon"));
                weatherList.add(weather);
            }

        } catch (JSONException e) {
            Log.e("MNU_ERROR", Arrays.toString(e.getStackTrace()));
        }

        WeatherAdapter weatherAdapter = new WeatherAdapter(getApplicationContext(),weatherList);
        list.setAdapter(weatherAdapter);
    }
}
