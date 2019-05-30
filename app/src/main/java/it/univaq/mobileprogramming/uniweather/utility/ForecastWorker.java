package it.univaq.mobileprogramming.uniweather.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.MainActivity;
import it.univaq.mobileprogramming.uniweather.database.Database;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ForecastWorker extends Worker {

    private static final int notification_id = 55;
    private static final String TAG = "Worker";
    private RequestQueue queue;


    public ForecastWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        queue = VolleyRequest.getInstance(context).getRequestQueue();
    }

    @NonNull
    @Override
    public Result doWork() {
        double lat = (double)Settings.lastLatitude(getApplicationContext(), Settings.LAST_LATITUDE, -1);
        double lon = (double)Settings.lastLongitude(getApplicationContext(), Settings.LAST_LONGITUDE, -1);
        get_weather_by_coord(lat,lon);
        notifyLocation("Database Aggiornato");
        Log.d(TAG, "doWork: Work Done");
        return Result.success();
    }

    public void get_weather_by_coord(double latitude, double longitude) {
        if (latitude == 0 && longitude == 0)
            return;

        clearDataFromDB();
        String url = "http://api.openweathermap.org/data/2.5/find?lat="+latitude+"&lon="+longitude+"&units=metric&cnt=25&lang=it&appid=7368b1dcdbc2b20401886a17908ac573";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
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

                        System.out.println("Worker"+tempWeather);
                        saveDataInDB(tempWeather);
                    }
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
        queue.add(stringRequest);
    }

    /**
     * Save forecast in the database.
     */
    private void saveDataInDB(final ActualWeather city){
        Database.getInstance(getApplicationContext()).save(city);
    }

    private void clearDataFromDB(){
        Database.getInstance(getApplicationContext()).delete();

    }

    private void notifyLocation(String message) {

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myChannel", "Il Mio Canale", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.argb(255, 255, 0, 0));
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder bbuilder = new NotificationCompat.Builder(
                getApplicationContext(), "myChannel");
        bbuilder.setContentTitle(getApplicationContext().getString(R.string.app_name));
        bbuilder.setSmallIcon(R.drawable.ic_launcher_use);
        bbuilder.setContentText(message);
        bbuilder.setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);

        bbuilder.setContentIntent(pendingIntent);

        Notification notify = bbuilder.build();
        if(notificationManager != null) notificationManager.notify(notification_id, notify);
    }
}


