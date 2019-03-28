package it.univaq.mobileprogramming.uniweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.database.Database;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;

public class FavouriteCitiesActivity extends AppCompatActivity {

    private AdapterRecycler adapter;
    private double actualLat;
    private double actualLon;
    private ArrayList<ActualWeather> favourites = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_cities_activity);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        setTitle(null);

        adapter = new AdapterRecycler(favourites);
        RecyclerView list = findViewById(R.id.city_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        loadDataFromDB();

        if (adapter != null) adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        favourites = new ArrayList<>();
        loadDataFromDB();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    /**
     * Load all forecast from database.
     */
    private void loadDataFromDB(){
        favourites.addAll(Database.getInstance(getApplicationContext()).getAllFavourites());
        if(adapter != null) adapter.notifyDataSetChanged();
    }


}
