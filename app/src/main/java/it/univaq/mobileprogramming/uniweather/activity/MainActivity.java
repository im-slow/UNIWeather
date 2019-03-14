package it.univaq.mobileprogramming.uniweather.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.model.Sys;
import it.univaq.mobileprogramming.uniweather.model.WeatherResult;

import static android.Manifest.permission.INTERNET;


public class MainActivity extends AppCompatActivity {

    ImageView icon_view;
    TextView name_city, desc, temperature;

    private List<WeatherResult> cities = new ArrayList<>();
    private AdapterRecycler adapter;

    //inizializza l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        icon_view = findViewById(R.id.icon_view);
        name_city = findViewById(R.id.city_name);
        temperature = findViewById(R.id.temperature);
        desc = findViewById(R.id.condition);
        add_city();
        get_weather_by_name("Castel di Ieri");

        setTitle(null);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        // oggetti di esempio
        Sys s1 = new Sys("Lazio");
        Sys s2 = new Sys("Abruzzo");
        Sys s3 = new Sys("Toscana");
        Sys s4 = new Sys("Campania");
        Sys s5 = new Sys("Piemonte");
        Sys s6 = new Sys("Lombardia");

        List<WeatherResult> data = new ArrayList<WeatherResult>();

        WeatherResult c1 = new WeatherResult("Roma", s1);
        data.add(c1);

        WeatherResult c2 = new WeatherResult("L'Aquila", s2);
        data.add(c2);

        WeatherResult c3 = new WeatherResult("Firenze", s3);
        data.add(c3);

        WeatherResult c4 = new WeatherResult("Napoli", s4);
        data.add(c4);

        WeatherResult c5 = new WeatherResult("Torino", s5);
        data.add(c5);

        WeatherResult c6 = new WeatherResult("Milano", s6);
        data.add(c6);

        //fine esempio
        adapter = new AdapterRecycler(data);
        RecyclerView list = findViewById(R.id.main_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
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
    private void add_city() {
        Button add_city_button = findViewById(R.id.add_city);

        add_city_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent newActivity = new Intent(v.getContext(), AddCityActivity.class);
                startActivity(newActivity);
            }
        });
    } // PD

    public void get_weather_by_name(String input){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://api.openweathermap.org/data/2.5/weather?q="+input+"&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Dati", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject main_object = jsonObject.getJSONObject("main");
                            JSONArray array = jsonObject.getJSONArray("weather");
                            JSONObject object = array.getJSONObject(0);
                            int tempo = (int)main_object.getDouble("temp")-273;
                            String description = object.getString("description");
                            String city = jsonObject.getString("name");
                            String icon = "i"+object.getString("icon");

                            System.out.println("Città: " + city);
                            System.out.println("Meteo: " + description);
                            System.out.println("Temperatura: "+tempo+"°");
                            System.out.println("Icona: " + icon);

                            setIcon_view(icon);
                            temperature.setText(String.valueOf(tempo));
                            name_city.setText(city);
                            desc.setText(description);


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
                requestQueue.add(stringRequest);
    }

    public void setIcon_view(String icon_name){
        if(icon_name == "i01d")
            icon_view.setImageResource(R.drawable.i01d);
        else if(icon_name == "i01n")
            icon_view.setImageResource(R.drawable.i01n);
        else if(icon_name == "i02d")
            icon_view.setImageResource(R.drawable.i02d);
        else if(icon_name == "i02n")
            icon_view.setImageResource(R.drawable.i02n);
        else if(icon_name == "i03d")
            icon_view.setImageResource(R.drawable.i03d);
        else if(icon_name == "i03n")
            icon_view.setImageResource(R.drawable.i03n);
        else if(icon_name == "i04n")
            icon_view.setImageResource(R.drawable.i04n);
        else if(icon_name == "i09d")
            icon_view.setImageResource(R.drawable.i09d);
        else if(icon_name == "i09n")
            icon_view.setImageResource(R.drawable.i09n);
        else if(icon_name == "i10d")
            icon_view.setImageResource(R.drawable.i10d);
        else if(icon_name == "i10n")
            icon_view.setImageResource(R.drawable.i10n);
        else if(icon_name == "i11d")
            icon_view.setImageResource(R.drawable.i11d);
        else if(icon_name == "i11n")
            icon_view.setImageResource(R.drawable.i11n);
        else if(icon_name == "i13d")
            icon_view.setImageResource(R.drawable.i13d);
        else if(icon_name == "i13n")
            icon_view.setImageResource(R.drawable.i13n);
        else if(icon_name == "i50d")
            icon_view.setImageResource(R.drawable.i50d);
        else if(icon_name == "i50n")
            icon_view.setImageResource(R.drawable.i50n);


    }

}