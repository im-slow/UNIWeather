package it.univaq.mobileprogramming.uniweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;


public class DetailsActivity extends AppCompatActivity {

    private ImageView icon_view;
    private TextView name_city, desc, temperature, min_temp, max_temp,
                    wind_speed, wind_degree, pressure, humidity;
    private ActualWeather actualWeather;

    //inizializza l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        actualWeather = (ActualWeather) getIntent().getSerializableExtra("ActualWeather");

        icon_view = findViewById(R.id.icon_view);
        name_city = findViewById(R.id.city_name);
        temperature = findViewById(R.id.temperature);
        desc = findViewById(R.id.condition);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        wind_speed = findViewById(R.id.wind_speed);
        wind_degree = findViewById(R.id.wind_degree);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);

        setIcon_view(actualWeather.getIcon_name());
        name_city.setText(actualWeather.getCity_name());
        temperature.setText(Integer.toString(((int) actualWeather.getTemp())));
        desc.setText(actualWeather.getDescription());
        min_temp.setText(actualWeather.getMin_temp()+"°C");
        max_temp.setText(actualWeather.getMax_temp()+"°C");
        wind_speed.setText(actualWeather.getWind_speed()+" km/h");
        wind_degree.setText(actualWeather.getWind_degree()+"°");
        pressure.setText(actualWeather.getPressure()+"");
        humidity.setText(actualWeather.getHumidity()+"%");

        setTitle(null);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void favourite_click(View v){
        startActivity(new Intent(DetailsActivity.this, FavouriteCitiesActivity.class));
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
