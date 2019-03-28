package it.univaq.mobileprogramming.uniweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.FiveDaysAdapter;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.model.Forecast;

public class FiveDaysActivity extends AppCompatActivity {

    private ActualWeather actualWeather;
    private FiveDaysAdapter adapter;
    private List<Forecast> forecastList = new ArrayList<>();
    private TextView day, time, temp, desc;
    private ImageView icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_days_activity);

        actualWeather = (ActualWeather) getIntent().getSerializableExtra("ActualWeather");

        adapter = new FiveDaysAdapter(forecastList);
        RecyclerView list = findViewById(R.id.forecast_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
    }
}
