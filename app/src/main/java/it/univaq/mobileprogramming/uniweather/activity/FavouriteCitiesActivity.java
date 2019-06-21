package it.univaq.mobileprogramming.uniweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

public class FavouriteCitiesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private AdapterRecycler adapter;
    private RequestQueue queue;
    private ArrayList<ActualWeather> favourites = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "FavouriteCitiesActivity";
    private int LANG = R.string.LANG;

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

        swipeRefreshLayout = findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        queue = VolleyRequest.getInstance(this).getRequestQueue();

        if (adapter != null) adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        loadDataFromDB();
        if (adapter != null) adapter.notifyDataSetChanged();

        updateData();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");

        updateData();

        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public void updateData(){
        Log.d(TAG, "updateData");
        Log.d(TAG, "updateData: "+favourites.size());
        ArrayList<ActualWeather> tempFavourites = new ArrayList<>(favourites);
        favourites.clear();
        clearDataFromDB();
        for (ActualWeather a: tempFavourites){
            get_weather_by_cityid(a.getCity_id());
        }


    }

    /**
     * Load all forecast from database.
     */
    private void loadDataFromDB(){
        Log.d(TAG, "loadDataFromDB");
        favourites.clear();
        favourites.addAll(Database.getInstance(getApplicationContext()).getAllFavourites());
        Log.d(TAG, "loadDataFromDB: "+favourites);
        if(adapter != null) adapter.notifyDataSetChanged();

    }

    public void get_weather_by_cityid(int city_id) {
        Log.d(TAG, "get_weather_by_cityid");
        String url = "http://api.openweathermap.org/data/2.5/weather?id="+city_id+"&units=metric&cnt=25&lang="+getString(LANG)+"&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Dati", response);
                    swipeRefreshLayout.setRefreshing(true);
                    ActualWeather tempWeather;
                    JSONObject info = new JSONObject(response);

                    JSONObject coord = info.getJSONObject("coord");
                    JSONArray array = info.getJSONArray("weather");
                    JSONObject weather = array.getJSONObject(0);
                    JSONObject main = info.getJSONObject("main");
                    JSONObject wind = info.getJSONObject("wind");
                    JSONObject sys = info.getJSONObject("sys");

                    int windDegree = -1;
                    if(wind.length()>1){
                        windDegree = wind.getInt("deg");
                    }
                    tempWeather = new ActualWeather(coord.getDouble("lat"), coord.getDouble("lon"),
                            weather.getString("description"), weather.getString("icon"), main.getDouble("temp"),
                            main.getInt("pressure"), main.getInt("humidity"), main.getDouble("temp_min"),
                            main.getDouble("temp_max"), wind.getDouble("speed"), windDegree,
                            sys.getString("country"), info.getInt("id"), info.getString("name"));
                    favourites.add(tempWeather);
                    saveDataInDB(tempWeather);
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse tempWeather: "+tempWeather);
                } catch (Exception ex){
                    Log.d(TAG, "onResponse: error");
                    ex.printStackTrace();
                }

                if(adapter != null) adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Dati","Nessuna città trovata");
            }
        });
        queue.add(stringRequest);
        return;
    }

    private void saveDataInDB(ActualWeather favTemp){
        Log.d(TAG, "saveDataInDB");
        Database.getInstance(getApplicationContext()).saveFavourite(favTemp);
    }

    private void clearDataFromDB(){
        Log.d(TAG, "clearDataFromDB");
        favourites.clear();
        Database.getInstance(getApplicationContext()).deleteFavourite();
        if(adapter != null) adapter.notifyDataSetChanged();
    }

    //crea il menù all'avvio dell'app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //gestisce il menù
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){

            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_exit:
                showExitDialog();
                return true;

            default:
                return false;
        }
    }


    private void showExitDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteCitiesActivity.this);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
