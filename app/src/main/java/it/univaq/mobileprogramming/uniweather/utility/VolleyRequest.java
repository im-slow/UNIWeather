package it.univaq.mobileprogramming.uniweather.utility;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyRequest {

    private RequestQueue queue;

    private static VolleyRequest instance = null;

    public static VolleyRequest getInstance(Context context){
        return instance == null ? instance = new VolleyRequest(context) : instance;
    }

    private VolleyRequest(Context context){

        queue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue(){
        return queue;
    }

    public void downloadCities(Response.Listener<String> listener){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=7368b1dcdbc2b20401886a17908ac573",
                listener,
                null);
        queue.add(request);
    }
}
