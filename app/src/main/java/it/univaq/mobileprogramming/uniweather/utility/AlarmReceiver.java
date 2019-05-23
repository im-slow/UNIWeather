package it.univaq.mobileprogramming.uniweather.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent();
        ForecastService.enqueueWork(context, ForecastService.class, 0 , serviceIntent);

    }
}
