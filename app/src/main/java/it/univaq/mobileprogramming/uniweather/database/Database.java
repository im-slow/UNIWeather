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
    private static final int VERSION = 4;

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
        FavouriteTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ActualWeatherTable.upgrade(db);
        FavouriteTable.upgrade(db);
    }

    public void save(ActualWeather city){
        ActualWeatherTable.insert(getWritableDatabase(), city);
    }

    // salva preferito
    public void saveFavourite(ActualWeather favourite){ FavouriteTable.insert(getWritableDatabase(), favourite);}

    public List<ActualWeather> getAllCities(){
        return ActualWeatherTable.select(getReadableDatabase());
    }

    // ritorna lista preferiti
    public List<ActualWeather> getAllFavourites() {
        return FavouriteTable.select(getReadableDatabase());
    }

    public void delete(){
        ActualWeatherTable.delete(getWritableDatabase());
    }

    // cancella preferiti
    public void deleteFavourite() { FavouriteTable.delete(getWritableDatabase()); }

    //cancella un preferito
    public void deleteFavourite(ActualWeather favourite) { FavouriteTable.delete(getWritableDatabase(), favourite); }

}

