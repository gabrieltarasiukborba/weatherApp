package com.example.weatherapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;



public class Volley {

    private static JSONObject data;
    private static RequestQueue queue;
    private CustomListener listener;

    public Volley(JSONObject data, Context context, CustomListener listener){
        Volley.data = data;
        queue = com.android.volley.toolbox.Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public  void requestWheatherCities() throws JSONException {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, data.getString("url") + data.getString("id") + "&appid=" + data.getString("appid") + "&units=metric", response -> listener.onVolleyResponse(response),
                error -> {
                    Log.e("Volly Error",error.toString());
                    String errorJson = "'error':" + error.toString();
                    listener.onVolleyResponse(errorJson);
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }


}

