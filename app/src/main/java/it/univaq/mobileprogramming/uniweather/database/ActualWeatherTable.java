package it.univaq.mobileprogramming.uniweather.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.model.ActualWeather;

public class ActualWeatherTable {
    // Name of the table
    static final String TABLE_NAME = "actualWeathers";
    static final String FAVORITE_TABLE = "favorites";

    // Columns name
    static final String ID = "id";
    static final String WIND_DEGREE = "wind_degree";
    static final String HUMIDITY = "humidity";
    static final String PRESSURE = "pressure";
    static final String NAME = "name";
    static final String DESCRIPTION = "description";
    static final String ICON_NAME = "icon_name";
    static final String COUNTRY = "country";
    static final String TEMP = "temp_";
    static final String MIN_TEMP = "min_temp";
    static final String MAX_TEMP = "max_temp";
    static final String WIND_SPEED = "wind_speed";
    static final String LATITUDE = "latitude";
    static final String LONGITUDE = "longitude";

    /**
     * The method handles the creation of the table
     * @param db the database instance
     */
    static void create(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WIND_DEGREE + " INTEGER, " +
                HUMIDITY + " INTEGER, " +
                PRESSURE + " INTEGER, " +
                NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                ICON_NAME + " TEXT, " +
                COUNTRY + " TEXT, " +
                TEMP + " REAL, " +
                MIN_TEMP + " REAL, " +
                MAX_TEMP + " REAL, " +
                WIND_SPEED + " REAL, " +
                LATITUDE + " REAL, " +
                LONGITUDE + " REAL " +
                ");";
        db.execSQL(sql);
    }

    /**
     * The method drops the table and loses all data
     * @param db the database instance
     */
    static void drop(SQLiteDatabase db){
        String sql = "DROP TABLE " + TABLE_NAME;
        db.execSQL(sql);
    }

    /**
     * The method do the upgrade of the table.
     * This implementation is a wrong way to do an upgrade because it loses all data
     */
    static void upgrade(SQLiteDatabase db){
        drop(db);
        create(db);
    }

    /**
     * The method save the City into the right table.
     *
     * @param db the database instance in writable mode
     * @param city the saved object containing the right id
     */
    static void insert(SQLiteDatabase db, ActualWeather city ){

        ContentValues values = new ContentValues();
        values.put(WIND_DEGREE, city.getWind_degree());
        values.put(HUMIDITY, city.getHumidity());
        values.put(PRESSURE, city.getPressure());
        values.put(NAME, city.getCity_name());
        values.put(DESCRIPTION, city.getDescription());
        values.put(ICON_NAME, city.getIcon_name());
        values.put(COUNTRY, city.getCountry());
        values.put(TEMP, city.getTemp());
        values.put(MIN_TEMP, city.getMin_temp());
        values.put(MAX_TEMP, city.getMax_temp());
        values.put(WIND_SPEED, city.getWind_speed());
        values.put(LATITUDE, city.getLatitude());
        values.put(LONGITUDE, city.getLongitude());
        long id = db.insert(TABLE_NAME, null, values);
        city.setCity_id((int) id);
    }

    /**
     * The method update all values of the City.
     *
     * @param db the database instance in writable mode
     * @param city the city to update with the new data
     * @return true if the update is successful, false otherwise
     */
    static boolean update(SQLiteDatabase db, ActualWeather city){

        ContentValues values = new ContentValues();
        values.put(WIND_DEGREE, city.getWind_degree());
        values.put(HUMIDITY, city.getHumidity());
        values.put(PRESSURE, city.getPressure());
        values.put(NAME, city.getCity_name());
        values.put(DESCRIPTION, city.getDescription());
        values.put(ICON_NAME, city.getIcon_name());
        values.put(COUNTRY, city.getCountry());
        values.put(TEMP, city.getTemp());
        values.put(MIN_TEMP, city.getMin_temp());
        values.put(MAX_TEMP, city.getMax_temp());
        values.put(WIND_SPEED, city.getWind_speed());
        values.put(LATITUDE, city.getLatitude());
        values.put(LONGITUDE, city.getLongitude());
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{ String.valueOf(city.getCity_id()) }) == 1;
    }

    /**
     * The method delete the City from the database
     *
     * @param db the database instance in writable mode
     * @param city the city to delete
     * @return true if the delete is successful, false otherwise
     */
    static boolean delete(SQLiteDatabase db, ActualWeather city){
        return db.delete(TABLE_NAME, ID + " = " + city.getCity_id(), null) == 1;

    }

    /**
     * The method delete all data inside the table of the cities.
     * Warning: the primary key is not reset.
     *
     * @param db the database instance in writable mode
     * @return true if the data are deleted, false otherwise
     */
    static boolean delete(SQLiteDatabase db){
        return db.delete(TABLE_NAME, null, null) > 0;

    }

    /**
     * The method get all cities from the database.
     *
     * @param db the database instance in readable mode
     * @return a list containing all cities, or an empty list
     */
    static List<ActualWeather> select(SQLiteDatabase db){

        List<ActualWeather> cities = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COUNTRY + " ASC;";

        Cursor cursor = null;
        try {
            // Perform the query
            cursor = db.rawQuery(sql, null);

            // Iterate on cursor to read all tuples
            while(cursor.moveToNext()){
                ActualWeather city = new ActualWeather();

                // Warning: get dynamically the column index
                city.setCity_id(cursor.getInt(cursor.getColumnIndex(ID)));
                city.setWind_degree(cursor.getInt(cursor.getColumnIndex(WIND_DEGREE)));
                city.setHumidity(cursor.getInt(cursor.getColumnIndex(HUMIDITY)));
                city.setPressure(cursor.getInt(cursor.getColumnIndex(PRESSURE)));
                city.setCity_name(cursor.getString(cursor.getColumnIndex(NAME)));
                city.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                city.setIcon_name(cursor.getString(cursor.getColumnIndex(ICON_NAME)));
                city.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                city.setTemp(cursor.getDouble(cursor.getColumnIndex(TEMP)));
                city.setMin_temp(cursor.getDouble(cursor.getColumnIndex(MIN_TEMP)));
                city.setMax_temp(cursor.getDouble(cursor.getColumnIndex(MAX_TEMP)));
                city.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
                city.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));

                cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Warning: always close the cursor
            if(cursor != null) cursor.close();
        }

        return cities;
    }
}
