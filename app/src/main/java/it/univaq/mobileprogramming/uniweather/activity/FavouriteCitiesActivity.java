package it.univaq.mobileprogramming.uniweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.database.Database;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.utility.VolleyRequest;

public class FavouriteCitiesActivity extends AppCompatActivity {

    private AdapterRecycler adapter;
    private RequestQueue queue;
    private ArrayList<ActualWeather> favourites = new ArrayList<>();
    private static final String TAG = "FavouriteCitiesActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.favourite_cities_activity);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        setTitle(null);

        adapter = new AdapterRecycler(favourites);
        RecyclerView list = findViewById(R.id.city_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        queue = VolleyRequest.getInstance(this).getRequestQueue();

        loadDataFromDB();
        if (adapter != null) adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        loadDataFromDB();
        //updateFavourites(favourites);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    /**
     * Load all forecast from database.
     */
    private void loadDataFromDB(){
        Log.d(TAG, "loadDataFromDB");
        //updateFavourites(favourites);
        favourites.clear();
        favourites.addAll(Database.getInstance(getApplicationContext()).getAllFavourites());
        Log.d(TAG, "loadDataFromDB: "+favourites);
        if(adapter != null) adapter.notifyDataSetChanged();

    }

    public ActualWeather get_weather_by_cityid(int city_id) {
        Log.d(TAG, "get_weather_by_cityid");
        //favourites.clear();
        ActualWeather tempWeather = new ActualWeather();
        String url = "http://api.openweathermap.org/data/2.5/weather?id="+city_id+"&units=metric&cnt=25&lang=it&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    //swipeRefreshLayout.setRefreshing(true);
                    ActualWeather tempWeather;
                    Log.d("Dati", response);
                    JSONObject info = new JSONObject(response);

                        JSONObject coord = info.getJSONObject("coord");
                        JSONArray array = info.getJSONArray("weather");
                        JSONObject weather = array.getJSONObject(0);
                        JSONObject main = info.getJSONObject("main");
                        JSONObject wind = info.getJSONObject("wind");
                        JSONObject sys = info.getJSONObject("sys");

                        tempWeather = new ActualWeather(coord.getDouble("lat"), coord.getDouble("lon"),
                                weather.getString("description"), weather.getString("icon"), main.getDouble("temp"),
                                main.getInt("pressure"), main.getInt("humidity"), main.getDouble("temp_min"),
                                main.getDouble("temp_max"), wind.getDouble("speed"), /*wind.getInt("deg")*/0,
                                sys.getString("country"), info.getInt("id"), info.getString("name"));

                        System.out.println(tempWeather);
                        //saveDataInDB(tempWeather);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                //swipeRefreshLayout.setRefreshing(false);
                // Refresh list because the adapter data are changed
                //if(adapter != null) adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Dati","Nessuna città trovata");
            }
        });
        queue.add(stringRequest);
        return tempWeather;
    }

    private void saveDataInDB(final ActualWeather city){
        Log.d(TAG, "saveDataInDB");
        Database.getInstance(getApplicationContext()).saveFavourite(city);
    }

    private void clearDataFromDB(){
        Log.d(TAG, "clearDataFromDB");
        favourites.clear();
        Database.getInstance(getApplicationContext()).deleteFavourite();
        if(adapter != null) adapter.notifyDataSetChanged();
    }

   private void updateFavourites(ArrayList<ActualWeather> paramfavourites){
        Log.d(TAG, "updateFavourites");
        ArrayList<ActualWeather> temp = new ArrayList<>(paramfavourites);
        Log.d(TAG, "updateFavourites: "+temp);
        ActualWeather favTemp;
        clearDataFromDB();
        for (ActualWeather t: temp) {
            favTemp = get_weather_by_cityid(t.getCity_id());
            Log.d(TAG, "updateFavourites: "+favTemp);
            favourites.add(favTemp);
            Log.d(TAG, "updateFavourites: "+favTemp);
            System.out.println("oggetto nuovo"+": "+favTemp.getCity_name()+","+favTemp.getTemp());
            saveDataInDB(favTemp);
        }
        loadDataFromDB();
        if(adapter != null) adapter.notifyDataSetChanged();
    }
}
