package it.univaq.mobileprogramming.uniweather.utility;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Date;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.MainActivity;

import static java.lang.Thread.sleep;

public class ForecastService extends JobIntentService {

    private final int notification_id = 1;
    int i = 0;
    public static final String TAG = "ExampleJobINtentService";
    public static final String REQUEST = "request";
    public static final String FILTER_REQUEST_DOWNLOAD = "filter_request_download";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");

        try {
            Intent newIntent = new Intent(FILTER_REQUEST_DOWNLOAD);
            newIntent.putExtra(REQUEST, 0);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(newIntent);
            i++;
            System.out.println("numero:" +i);
            notifyLocation("Meteo Aggiornato");
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Publish a notify.
     *
     * @param message
     */
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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }
}
