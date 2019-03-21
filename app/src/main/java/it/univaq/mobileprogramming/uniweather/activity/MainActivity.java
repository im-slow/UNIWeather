package it.univaq.mobileprogramming.uniweather.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.model.Sys;
import it.univaq.mobileprogramming.uniweather.model.WeatherResult;
import it.univaq.mobileprogramming.uniweather.utility.LocationGoogleService;
import it.univaq.mobileprogramming.uniweather.utility.VolleyRequest;

public class MainActivity extends AppCompatActivity implements LocationGoogleService.LocationListener {

    private List<ActualWeather> data = new ArrayList<ActualWeather>();
    private AdapterRecycler adapter;
    private LocationGoogleService locationService;
    private RequestQueue queue;
    private ActualWeather actualWeather;
    private double actualLat;
    private double actualLon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        startLocalization();

        adapter = new AdapterRecycler(data);
        RecyclerView list = findViewById(R.id.main_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        queue = VolleyRequest.getInstance(this).getRequestQueue();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationService = new LocationGoogleService();
                locationService.onCreate(this, this);
                locationService.requestLocationUpdates(this);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.stopLocationUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Lat: " + location.getLatitude());
        System.out.println("Lon: " + location.getLongitude());
        actualLat = location.getLatitude();
        actualLon = location.getLongitude();
        get_weather_by_coord(actualLat, actualLon);
        locationService.stopLocationUpdates(this);

    }

    private void startLocalization() {
        int check = ContextCompat
                .checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (check == PackageManager.PERMISSION_GRANTED) {
            locationService = new LocationGoogleService();
            locationService.onCreate(this, this);
            locationService.requestLocationUpdates(this);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

            public void get_weather_by_coord(double latitude, double longitude) {
                //http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10
                String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&cnt=20&units=metric&lang=it&appid=7368b1dcdbc2b20401886a17908ac573";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonRoot = new JSONArray(response);

                            for(int i = 0; i < jsonRoot.length(); i++) {

                                ActualWeather tempWeather;
                                Log.d("Dati", response);
                                JSONObject root = jsonRoot.getJSONObject(i);
                                JSONObject coord = root.getJSONObject("coord");
                                JSONArray array = root.getJSONArray("weather");
                                JSONObject weather = array.getJSONObject(0);
                                JSONObject main = root.getJSONObject("main");
                                JSONObject wind = root.getJSONObject("wind");
                                JSONObject sys = root.getJSONObject("sys");

                                tempWeather = new ActualWeather(coord.getDouble("lon"), coord.getDouble("lat"),
                                        weather.getString("description"), weather.getString("icon"), main.getDouble("temp"),
                                        main.getInt("pressure"), main.getInt("humidity"), main.getDouble("temp_min"),
                                        main.getDouble("temp_max"), wind.getDouble("speed"), wind.getInt("deg"),
                                        sys.getString("country"), root.getInt("id"), root.getString("name"));

                                System.out.println(tempWeather);
                                data.add(tempWeather);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Dati", "Nessuna città trovata");
                    }
                });
                queue.add(stringRequest);
            }


}

