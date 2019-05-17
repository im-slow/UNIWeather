package it.univaq.mobileprogramming.uniweather.utility;

import android.app.IntentService;
import android.content.Intent;

import static java.lang.Thread.sleep;

public class ForecastService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ForecastService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("Sono il service");
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
        }

    }
}
