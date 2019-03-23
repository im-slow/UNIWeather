package it.univaq.mobileprogramming.uniweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import it.univaq.mobileprogramming.uniweather.model.ActualWeather;

/**
 * This class create the database and manage the access to it.
 *
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int VERSION = 2;

    //... Singleton Pattern
    private static Database instance = null;

    public static Database getInstance(Context context){
        return instance == null ? instance = new Database(context) : instance;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    //... End Singleton Pattern


    @Override
    public void onCreate(SQLiteDatabase db) {
        ActualWeatherTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ActualWeatherTable.upgrade(db);
    }


    public void save(ActualWeather city){
        ActualWeatherTable.insert(getWritableDatabase(), city);
    }

    public List<ActualWeather> getAllCities(){
        return ActualWeatherTable.select(getReadableDatabase());
    }

    public void delete(){
        ActualWeatherTable.delete(getWritableDatabase());
    }

}

