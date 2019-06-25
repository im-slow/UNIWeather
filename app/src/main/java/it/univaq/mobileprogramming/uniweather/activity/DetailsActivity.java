package it.univaq.mobileprogramming.uniweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.database.Database;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;


public class DetailsActivity extends AppCompatActivity {

    private ImageView icon_view;
    private TextView name_city, desc, temperature, min_temp, max_temp,
            wind_speed, wind_degree, pressure, humidity;
    private Button star;
    private ActualWeather actualWeather;
    private ArrayList<ActualWeather> favourites = new ArrayList<>();
    private static final String TAG = "DetailsActivity";

    //inizializza l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.details_activity);

        star = findViewById(R.id.favourite_cities);

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
        temperature.setText(Integer.toString(((int) actualWeather.getTemp())) + "°");
        desc.setText(actualWeather.getDescription());
        min_temp.setText(actualWeather.getMin_temp() + "°C");
        max_temp.setText(actualWeather.getMax_temp() + "°C");
        wind_speed.setText(actualWeather.getWind_speed() + " km/h");
        wind_degree.setText(degToCompass(actualWeather.getWind_degree()));
        pressure.setText(actualWeather.getPressure() + "");
        humidity.setText(actualWeather.getHumidity() + "%");

        setTitle(null);
        loadDataFromDB();

        clickDB(favourites);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        favourites.clear();
        loadDataFromDB();
        clickDB(favourites);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DetailsActivity.this.finishAffinity();
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

    public void favourite_click(View v) {
        Log.d(TAG, "favourite_click");
            if(!(clickDB((ArrayList<ActualWeather>) favourites))) {
                System.out.println("salva preferito");
                saveDataInDB(actualWeather);
                star.setBackgroundResource(R.drawable.ic_star_gold_24dp);
            } else {
                System.out.println("cancella preferito");
                deleteDataInDB(actualWeather);
                star.setBackgroundResource(R.drawable.ic_star_border_empty_24dp);
        }
        favourites = new ArrayList<ActualWeather>();
        favourites.addAll(Database.getInstance(getApplicationContext()).getAllFavourites());
    }


    public boolean clickDB(ArrayList<ActualWeather> city) {
        Log.d(TAG, "clickDB");
        boolean find = false;
        if(!(favourites.isEmpty())) {
            for (ActualWeather a : city) {
                if (a.getLatitude() == actualWeather.getLatitude() && a.getLongitude() == actualWeather.getLongitude()) {
                    find = true;
                    star.setBackgroundResource(R.drawable.ic_star_gold_24dp);
                    return find;
                }
            }
        }
        star.setBackgroundResource(R.drawable.ic_star_border_empty_24dp);
        return find;
    }

    /**
     * Save forecast in the database.
     */
    private void saveDataInDB(final ActualWeather favourite) {
        Log.d(TAG, "saveDataInDB");
        Database.getInstance(getApplicationContext()).saveFavourite(favourite);
    }

    /**
     * delete one forecast in the database.
     */
    private void deleteDataInDB(ActualWeather favourite) {
        Log.d(TAG, "deleteDataInDB");
        Database.getInstance(getApplicationContext()).deleteFavourite(favourite);
    }

    /**
     * Load all forecast from database.
     */
    private void loadDataFromDB() {
        Log.d(TAG, "loadDataFromDB");
        favourites.addAll(Database.getInstance(getApplicationContext()).getAllFavourites());
    }

    public void map_click(View v){
        Log.d(TAG, "map_click");
        Intent intent = new Intent(v.getContext(), MapsActivity.class);
        intent.putExtra("ActualWeather", actualWeather);
        v.getContext().startActivity(intent);
    }

    public void five_days_click(View v){
        Log.d(TAG, "five_days_click");
        Intent intent = new Intent(v.getContext(), FiveDaysActivity.class);
        intent.putExtra("ActualWeather", actualWeather);
        v.getContext().startActivity(intent);
    }

    public void setIcon_view(String icon_name){
        if(icon_name.equals("01d"))
            icon_view.setImageResource(R.drawable.i01d);
        else if (icon_name.equals("01n"))
            icon_view.setImageResource(R.drawable.i01n);
        else if (icon_name.equals("02d"))
            icon_view.setImageResource(R.drawable.i02d);
        else if (icon_name.equals("02n"))
            icon_view.setImageResource(R.drawable.i02n);
        else if (icon_name.equals("03d"))
            icon_view.setImageResource(R.drawable.i03d);
        else if (icon_name.equals("03n"))
            icon_view.setImageResource(R.drawable.i03n);
        else if (icon_name.equals("04n"))
            icon_view.setImageResource(R.drawable.i04n);
        else if (icon_name.equals("04d"))
            icon_view.setImageResource(R.drawable.i04d);
        else if (icon_name.equals("09d"))
            icon_view.setImageResource(R.drawable.i09d);
        else if (icon_name.equals("09n"))
            icon_view.setImageResource(R.drawable.i09n);
        else if (icon_name.equals("10d"))
            icon_view.setImageResource(R.drawable.i10d);
        else if (icon_name.equals("10n"))
            icon_view.setImageResource(R.drawable.i10n);
        else if (icon_name.equals("11d"))
            icon_view.setImageResource(R.drawable.i11d);
        else if (icon_name.equals("11n"))
            icon_view.setImageResource(R.drawable.i11n);
        else if (icon_name.equals("13d"))
            icon_view.setImageResource(R.drawable.i13d);
        else if (icon_name.equals("13n"))
            icon_view.setImageResource(R.drawable.i13n);
        else if (icon_name.equals("50d"))
            icon_view.setImageResource(R.drawable.i50d);
        else if (icon_name.equals("50n"))
            icon_view.setImageResource(R.drawable.i50n);
    }

    public String degToCompass(int num) {
        String NaN = "NaN";
        if(num == -1) return NaN;
        int val = (int) Math.floor((num / 22.5) + 0.5);
        String[] arr = {"Nord", "Nord-nord-est", "Nord-est", "Est-nord-est", "Est", "Est-sud-est", "Sud-est", "Sud-sud-est", "Sud", "Sud-sud-ovest", "Sud-ovest", "ovest-sud-ovest", "ovest", "ovest-nord-ovest", "Nord-ovest", "Nord-nord-ovest"};
        return arr[(val % 16)];
    }

}
