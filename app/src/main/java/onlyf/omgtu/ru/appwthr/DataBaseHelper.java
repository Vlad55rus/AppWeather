package onlyf.omgtu.ru.appwthr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String LOG_TAG = "DataBaseHelper";

    public DataBaseHelper(Context context) {
        super(context, "weather_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "DBH_onCreate");
        db.execSQL(
                "create table weather_log ("
                        + "id integer primary key autoincrement,"
                        + "city_name text,"
                        + "request_date long,"
                        + "units text,"
                        + "description text,"
                        + "temperature integer,"
                        + "humidity integer,"
                        + "wind integer,"
                        + "pressure real"
                        + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRecord(WeatherResponse wr) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase database = this.getWritableDatabase();
        contentValues.put("city_name", wr.getName());
        contentValues.put("request_date", DateHelper.getCurrentTimeStamp());
        contentValues.put("description", wr.getDescription());
        contentValues.put("temperature", wr.getTemp());
        contentValues.put("humidity", wr.getTemp());
        contentValues.put("pressure", wr.getTemp());
        contentValues.put("wind", wr.getWind());
        contentValues.put("units", wr.getUnits());

        long rowID = database.insert("weather_log", null, contentValues);
        Log.d(LOG_TAG, "Record inserted @ ID = " + rowID);
    }

    public WeatherResponse getRecord(RequestParameters parameters) {
        SQLiteDatabase database = this.getWritableDatabase();
        String rawQuery = "SELECT * FROM weather_log WHERE city_name = '" + parameters.getCity() + "' AND units = '" + parameters.getUnits() + "' ORDER BY request_date DESC";
        Log.d(LOG_TAG, "Query: {" + rawQuery + "};");
        Cursor cursor = database.rawQuery(rawQuery, null);
        return new WeatherResponse(cursor, parameters);
    }

    public int logRecords(Context context) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.query("weather_log", null, null, null, null, null, null);
        int recordCount = c.getCount();
        Log.d(LOG_TAG, "Records in 'weather_log' table: " + recordCount);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int cityColIndex = c.getColumnIndex("city_name");
            int request_dateColIndex = c.getColumnIndex("request_date");
            int descriptionColIndex = c.getColumnIndex("description");
            int temperatureColIndex = c.getColumnIndex("temperature");
            int humidityColIndex = c.getColumnIndex("humidity");
            int pressureColIndex = c.getColumnIndex("pressure");
            int windColIndex = c.getColumnIndex("wind");
            int unitsColIndex = c.getColumnIndex("units");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", city = " + c.getString(cityColIndex) +
                                ", request_date = " + c.getString(request_dateColIndex) +
                                ", description = " + c.getString(descriptionColIndex) +
                                ", temperature = " + c.getString(temperatureColIndex) +
                                ", humidity = " + c.getString(humidityColIndex) +
                                ", pressure = " + c.getString(pressureColIndex) +
                                ", wind = " + c.getString(windColIndex) +
                                ", units = " + c.getString(unitsColIndex) + ";"
                );
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return recordCount;
    }

    public int wipeDB() {
        return this.getWritableDatabase().delete("weather_log", null, null);
    }
}
