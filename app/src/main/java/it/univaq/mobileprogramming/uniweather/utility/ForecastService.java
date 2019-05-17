package it.univaq.mobileprogramming.uniweather.utility;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import static java.lang.Thread.sleep;

public class ForecastService extends JobIntentService {
    int i = 0;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        try {
            while (true) {
                i++;
                System.out.println("numero:" +i);
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
        }
    }
}
