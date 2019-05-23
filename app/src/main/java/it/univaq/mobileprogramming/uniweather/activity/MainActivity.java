package it.univaq.mobileprogramming.uniweather.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.adapter.AdapterRecycler;
import it.univaq.mobileprogramming.uniweather.database.Database;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;
import it.univaq.mobileprogramming.uniweather.utility.AlarmReceiver;
import it.univaq.mobileprogramming.uniweather.utility.ForecastService;
import it.univaq.mobileprogramming.uniweather.utility.LocationGoogleService;
import it.univaq.mobileprogramming.uniweather.utility.Settings;
import it.univaq.mobileprogramming.uniweather.utility.VolleyRequest;

public class MainActivity extends AppCompatActivity implements LocationGoogleService.LocationListener, SwipeRefreshLayout.OnRefreshListener {

    private LocationGoogleService locationService;
    private RequestQueue queue;
    private AdapterRecycler adapter;
    private double actualLat;
    private double actualLon;
    private List<ActualWeather> cities = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int notification_id = 1;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            startLocalization();
            notifyLocation("Dati aggiornati");
        }
    };

    private void notifyLocation(String message) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myChannel", "Il Mio Canale", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.argb(255, 255, 0, 0));
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), "myChannel");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_launcher_use);
        builder.setContentText(message);
        builder.setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        Notification notify = builder.build();
        if(notificationManager != null) notificationManager.notify(notification_id, notify);
    }

    //inizializza l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Registering the receiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(myReceiver, new IntentFilter(ForecastService.FILTER_REQUEST_DOWNLOAD));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLocalization();

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        queue = VolleyRequest.getInstance(this).getRequestQueue();

        setTitle(null);

        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000*60*60, alarmIntent);

        adapter = new AdapterRecycler(cities);
        RecyclerView list = findViewById(R.id.city_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        TextView text = findViewById(R.id.main_text);
        long time = Settings.loadLong(getApplicationContext(), Settings.LAST_ACCESS, -1);
        if(time != -1){

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS", Locale.getDefault());
            String date = format.format(new Date(time));
            text.setText(date);
        }
        Settings.save(getApplicationContext(), Settings.LAST_ACCESS, System.currentTimeMillis());

        if (adapter != null) adapter.notifyDataSetChanged();
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
        if(Settings.loadBoolean(getApplicationContext(), Settings.FIRST_TIME, true)){

            clearDataFromDB();
            startLocalization();




        } else {
            // If is not the first time you open the app, get all saved data from Database

            loadDataFromDB();

        }
        Settings.save(getApplicationContext(), Settings.FIRST_TIME, false);

        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

        clearDataFromDB();

        get_weather_by_coord(actualLat, actualLon);

        if (adapter != null) adapter.notifyDataSetChanged();

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Lat: "+location.getLatitude());
        System.out.println("Lon: "+location.getLongitude());
        actualLat = location.getLatitude();
        actualLon = location.getLongitude();
        get_weather_by_coord(actualLat,actualLon);
        locationService.stopLocationUpdates(this);

        if (adapter != null) adapter.notifyDataSetChanged();


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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        cities.clear();

        String url = "http://api.openweathermap.org/data/2.5/find?lat="+latitude+"&lon="+longitude+"&units=metric&cnt=25&lang=it&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            swipeRefreshLayout.setRefreshing(true);
                            ActualWeather tempWeather;
                            Log.d("Dati", response);
                            JSONObject info = new JSONObject(response);
                            JSONArray list = info.getJSONArray("list");
                            for (int i = 0; i < info.getInt("count"); i++) {
                                JSONObject item = list.getJSONObject(i);
                                JSONObject coord = item.getJSONObject("coord");
                                JSONArray array = item.getJSONArray("weather");
                                JSONObject weather = array.getJSONObject(0);
                                JSONObject main = item.getJSONObject("main");
                                JSONObject wind = item.getJSONObject("wind");
                                JSONObject sys = item.getJSONObject("sys");

                                tempWeather = new ActualWeather(coord.getDouble("lat"), coord.getDouble("lon"),
                                        weather.getString("description"), weather.getString("icon"), main.getDouble("temp"),
                                        main.getInt("pressure"), main.getInt("humidity"), main.getDouble("temp_min"),
                                        main.getDouble("temp_max"), wind.getDouble("speed"), /*wind.getInt("deg")*/0,
                                        sys.getString("country"), item.getInt("id"), item.getString("name"));

                                System.out.println(tempWeather);
                                saveDataInDB(tempWeather);
                                cities.add(tempWeather);
                            }
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
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

    /**
     * Save forecast in the database.
     */
    private void saveDataInDB(final ActualWeather city){
        Database.getInstance(getApplicationContext()).save(city);
    }

    /**
     * Load all forecast from database.
     */
    private void loadDataFromDB(){
        cities.clear();
        cities.addAll(Database.getInstance(getApplicationContext()).getAllCities());
        if(adapter != null) adapter.notifyDataSetChanged();
    }

    /**
     * Clear data from database.
     */
    private void clearDataFromDB(){
        cities.clear();
        if(adapter != null) adapter.notifyDataSetChanged();
        Database.getInstance(getApplicationContext()).delete();

    }

    public void favourite_click(View v){
        startActivity(new Intent(MainActivity.this, FavouriteCitiesActivity.class));
    }
}
