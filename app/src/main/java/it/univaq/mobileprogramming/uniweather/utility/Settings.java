package it.univaq.mobileprogramming.uniweather.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Settings class provides to save and load some values by SharedPreferences.
 */
public class Settings {

    public static final String LAST_ACCESS = "last_access_time"; // Used to save the last timestamp when the user open the app
    public static final String FIRST_TIME = "first_time"; // Used to remember if is the first time that the user open the app
    public static final String LAST_LATITUDE = "last_latitude"; // Used to remember last accessible latitude
    public static final String LAST_LONGITUDE = "last_longitude"; // Used to remember last accessible longitude
    public static final String BACKGROUND_WORK = "background_work"; // Used to switch on and off background downloads

    public static void save(Context context, String key, long value) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, boolean value) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, float value) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static long loadLong(Context context, String key, long fallback) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getLong(key, fallback);
    }

    public static boolean loadBoolean(Context context, String key, boolean fallback) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, fallback);
    }


    public static float loadFloat(Context context, String key, float fallback) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getFloat(key, fallback);
    }
}

