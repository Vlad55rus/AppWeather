package onlyf.omgtu.ru.appwthr;

import android.app.Activity;
import android.os.Bundle;

public class DBRecordsActivity extends Activity {

    private final static String LOG_TAG = "DBRecordsActivity";
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbrecords);
//        dataBaseHelper = new DataBaseHelper(getApplicationContext());
//        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
//        Cursor c = database.query("weather_log", null, null, null, null, null, null);
//        Log.d(LOG_TAG, "Records in 'weather_log' table: " + c.getCount());
//        if (c.moveToFirst()) {
//            int idColIndex = c.getColumnIndex("id");
//            int cityColIndex = c.getColumnIndex("city_name");
//            int request_dateColIndex = c.getColumnIndex("request_date");
//            int descriptionColIndex = c.getColumnIndex("description");
//            int temperatureColIndex = c.getColumnIndex("temperature");
//            int humidityColIndex = c.getColumnIndex("humidity");
//            int pressureColIndex = c.getColumnIndex("pressure");
//            int windColIndex = c.getColumnIndex("wind");
//            do {
//                Log.d(LOG_TAG,
//                        "ID = " + c.getInt(idColIndex) +
//                                ", city = " + c.getString(cityColIndex) +
//                                ", request_date = " + c.getString(request_dateColIndex) +
//                                ", description = " + c.getString(descriptionColIndex) +
//                                ", temperature = " + c.getString(temperatureColIndex) +
//                                ", humidity = " + c.getString(humidityColIndex) +
//                                ", pressure = " + c.getString(pressureColIndex) +
//                                ", wind = " + c.getString(windColIndex) + ";"
//                );
//            } while (c.moveToNext());
//        }
//        c.close();
//        database.close();
    }
}
