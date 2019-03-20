package it.univaq.mobileprogramming.uniweather.activity;

import android.Manifest;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.utility.LocationGoogleService;
import it.univaq.mobileprogramming.uniweather.utility.VolleyRequest;

public class MainActivity extends AppCompatActivity implements LocationGoogleService.LocationListener {

    private ImageView icon_view;
    private TextView name_city, desc, temperature;
    private LocationGoogleService locationService;
    private RequestQueue queue;
    private ActualWeather actualWeather;
    private double actualLat;
    private double actualLon;

    //inizializza l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = VolleyRequest.getInstance(this).getRequestQueue();

        icon_view = findViewById(R.id.icon_view);
        name_city = findViewById(R.id.city_name);
        temperature = findViewById(R.id.temperature);
        desc = findViewById(R.id.condition);
        add_city();
        startLocalization();
        setTitle(null);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
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
        System.out.println("Lat: "+location.getLatitude());
        System.out.println("Lon: "+location.getLongitude());
        actualLat = location.getLatitude();
        actualLon = location.getLongitude();
        get_weather_by_coord(actualLat,actualLon);
        locationService.stopLocationUpdates(this);

    }

    //crea il menù all'avvio dell'app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //gestisce il menù
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo onClick per il bottone '+'
    public void add_city() {
        Button add_city_button = findViewById(R.id.add_city);

        add_city_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent newActivity = new Intent(v.getContext(), AddCityActivity.class);
                startActivity(newActivity);
            }
        });
    } // PD

    private void startLocalization(){
        int check = ContextCompat
                .checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if(check == PackageManager.PERMISSION_GRANTED){
            locationService = new LocationGoogleService();
            locationService.onCreate(this, this);
            locationService.requestLocationUpdates(this);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }
    }

    public void get_weather_by_coord(double latitude, double longitude) {

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&lang=it&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            ActualWeather tempWeather;
                            Log.d("Dati", response);
                            JSONObject root = new JSONObject(response);
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
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Dati","Nessuna città trovata");
            }
        });
                queue.add(stringRequest);
    }

    public void setIcon_view(String icon_name){
        if(icon_name.equals("01d"))
            icon_view.setImageResource(R.drawable.i01d);
        else if(icon_name.equals("01n"))
            icon_view.setImageResource(R.drawable.i01n);
        else if(icon_name.equals("02d"))
            icon_view.setImageResource(R.drawable.i02d);
        else if(icon_name.equals("02n"))
            icon_view.setImageResource(R.drawable.i02n);
        else if(icon_name.equals("03d"))
            icon_view.setImageResource(R.drawable.i03d);
        else if(icon_name.equals("03n"))
            icon_view.setImageResource(R.drawable.i03n);
        else if(icon_name.equals("04n"))
            icon_view.setImageResource(R.drawable.i04n);
        else if(icon_name.equals("04d"))
            icon_view.setImageResource(R.drawable.i04d);
        else if(icon_name.equals("09d"))
            icon_view.setImageResource(R.drawable.i09d);
        else if(icon_name.equals("09n"))
            icon_view.setImageResource(R.drawable.i09n);
        else if(icon_name.equals("10d"))
            icon_view.setImageResource(R.drawable.i10d);
        else if(icon_name.equals("10n"))
            icon_view.setImageResource(R.drawable.i10n);
        else if(icon_name.equals("11d"))
            icon_view.setImageResource(R.drawable.i11d);
        else if(icon_name.equals("11n"))
            icon_view.setImageResource(R.drawable.i11n);
        else if(icon_name.equals("13d"))
            icon_view.setImageResource(R.drawable.i13d);
        else if(icon_name.equals("13n"))
            icon_view.setImageResource(R.drawable.i13n);
        else if(icon_name.equals("50d"))
            icon_view.setImageResource(R.drawable.i50d);
        else if(icon_name.equals("50n"))
            icon_view.setImageResource(R.drawable.i50n);
    }


}
