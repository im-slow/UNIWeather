package it.univaq.mobileprogramming.uniweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import it.univaq.mobileprogramming.uniweather.activity.adapter.FiveDaysAdapter;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.model.Forecast;
import it.univaq.mobileprogramming.uniweather.utility.VolleyRequest;

public class FiveDaysActivity extends AppCompatActivity {

    private RequestQueue queue;
    private ActualWeather actualWeather;
    private FiveDaysAdapter adapter;
    private List<Forecast> forecastList = new ArrayList<>();
    private TextView city_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_days_activity);

        actualWeather = (ActualWeather) getIntent().getSerializableExtra("ActualWeather");

        city_name = findViewById(R.id.city_name5);
        city_name.setText(actualWeather.getCity_name());
        adapter = new FiveDaysAdapter(forecastList);

        RecyclerView list = findViewById(R.id.forecast_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        queue = VolleyRequest.getInstance(this).getRequestQueue();

        get_forecast_by_city_id(actualWeather.getCity_id());
        if (adapter != null) adapter.notifyDataSetChanged();

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        setTitle(null);
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

    public void get_forecast_by_city_id(int city_id) {

        String url = "http://api.openweathermap.org/data/2.5/forecast?id="+city_id+"&lang=it&units=metric&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Forecast tempForecast;
                    Log.d("Dati", response);
                    JSONObject info = new JSONObject(response);
                    JSONArray list = info.getJSONArray("list");
                    for (int i = 0; i < info.getInt("cnt"); i++) {
                        JSONObject item = list.getJSONObject(i);
                        JSONArray array = item.getJSONArray("weather");
                        JSONObject weather = array.getJSONObject(0);
                        JSONObject main = item.getJSONObject("main");
                        JSONObject wind = item.getJSONObject("wind");


                        tempForecast = new Forecast(actualWeather.getCity_id(), main.getDouble("temp"),
                                wind.getDouble("speed"), weather.getString("description"),
                                weather.getString("icon"), item.getString("dt_txt"));

                        forecastList.add(tempForecast);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                // Refresh list because the adapter data are changed
                if(adapter != null) adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Dati","Nessuna città trovata");
            }
        });
        queue.add(stringRequest);
    }
}
