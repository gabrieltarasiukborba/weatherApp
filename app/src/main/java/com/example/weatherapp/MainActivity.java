package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private JSONArray jsonCitiesData;
    private RadioGroup radioGroup;
    private Integer qtdFilterList = 0;
    private String[] listItems;
    private Map<String,Integer> hashMapCities = new HashMap<>();
    private boolean[] checkItems;
    private ArrayList<Integer> mCitiesItens = new ArrayList<>();
    private String searchCityTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radio_list);

        try {
            jsonCitiesData = new JSONArray(loadJSONFromAsset(this));
            listItems = new String[jsonCitiesData.length()];
            for(int i=0;i < jsonCitiesData.length();i++){
                JSONObject jsonObject = jsonCitiesData.getJSONObject(i);
                listItems[i] = jsonObject.getString("name");
                hashMapCities.put(jsonObject.getString("name"),jsonObject.getInt("id"));
            }
            //listItems = getResources().getStringArray(R.array.cities_item);
            checkItems = new boolean[listItems.length];
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onRadioButtonClicked(View view) {

        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radio_none:
                qtdFilterList = 0;
                break;
            case R.id.radio_3itm:
                qtdFilterList = 3;
                break;
            case R.id.radio_5itm:
                qtdFilterList = 5;
                break;
            case R.id.radio_10itm:
                qtdFilterList = 10;
                break;
        }
    }

    public void FilterCities(View view) {


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

    public Integer getQtdFilterList() {
        return qtdFilterList;
    }

    public void FilterCities2(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Selecione as cidades a serem pesquisadas");

        mBuilder.setMultiChoiceItems( listItems, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked){
                    if(!mCitiesItens.contains(position)){
                        mCitiesItens.add(position);
                    }else{
                        mCitiesItens.remove(position);
                    }
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for(int i = 0; i < mCitiesItens.size();i++){
                    item = item + listItems[mCitiesItens.get(i)];
                    if( i != mCitiesItens.size() -1){
                        item = item + ", ";

                    }
                }
                searchCityTag = item;
            }
        });

        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < checkItems.length; i++){
                    checkItems[i] = false;
                    mCitiesItens.clear();
                    searchCityTag = "";
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
