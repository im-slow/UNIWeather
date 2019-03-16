package it.univaq.mobileprogramming.uniweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.model.Sys;
import it.univaq.mobileprogramming.uniweather.model.WeatherResult;

public class AddCityActivity extends AppCompatActivity {

    private AdapterRecycler adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);

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
}
