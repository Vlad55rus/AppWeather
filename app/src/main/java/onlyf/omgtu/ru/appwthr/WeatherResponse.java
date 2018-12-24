package onlyf.omgtu.ru.appwthr;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeatherResponse {

    private final static String LOG_TAG = "WeatherResponse";
    private String
            description;
    private String name;
    private String errorDescription;
    private long
            temp;
    private long humidity;
    private long wind;
    private double pressure;
    private boolean errorFlag;
    private Date request_date = new Date();
    private String units;

    public WeatherResponse(JSONObject jsonObject, RequestParameters requestParameters) {

        if (jsonObject == null || requestParameters == null) {
            resetFieldsByDefault();
            errorFlag = true;
            errorDescription = "Invalid jsonObject or parameters";
            return;
        }

        try {
            JSONArray weather_array = jsonObject.getJSONArray("weather");
            JSONObject weather = weather_array.getJSONObject(0);
            this.description = weather.getString("description");

            JSONObject main = jsonObject.getJSONObject("main");

            this.temp = Math.round(main.getDouble("temp"));
            this.pressure = main.getDouble("pressure") * 0.75006375541921;
            this.humidity = Math.round(main.getDouble("humidity"));

            JSONObject wind = jsonObject.getJSONObject("wind");
            this.wind = Math.round(wind.getDouble("speed"));

            this.name = jsonObject.getString("name");

            request_date = new Date();
            units = requestParameters.getUnits();
        } catch (JSONException je) {
            resetFieldsByDefault();
            errorFlag = true;
            errorDescription = "Parsing error";
            Log.d(LOG_TAG, "Error while parsing JSON: " + je.getMessage());
        }
    }

    public WeatherResponse(Cursor cursor, RequestParameters requestParameters) {

        if (cursor == null || cursor.getCount() < 1) {
            resetFieldsByDefault();
            errorFlag = true;
            errorDescription = "Invalid cursor or no records found";
            return;
        }

        try {
            int cityColIndex = cursor.getColumnIndex("city_name");
            int request_dateColIndex = cursor.getColumnIndex("request_date");
            int descriptionColIndex = cursor.getColumnIndex("description");
            int temperatureColIndex = cursor.getColumnIndex("temperature");
            int humidityColIndex = cursor.getColumnIndex("humidity");
            int pressureColIndex = cursor.getColumnIndex("pressure");
            int windColIndex = cursor.getColumnIndex("wind");
            int unitsColIndex = cursor.getColumnIndex("units");
            if (!cursor.moveToNext()) {
                Log.d(LOG_TAG, "Trying to move to the next record. Cursor count: " + cursor.getCount());
                throw new Exception("Can't move to the next record");
            }
            name = cursor.getString(cityColIndex);
            request_date = DateHelper.getDateFromDbValue(cursor.getLong(request_dateColIndex));
            description = cursor.getString(descriptionColIndex);
            temp = cursor.getLong(temperatureColIndex);
            humidity = cursor.getLong(humidityColIndex);
            pressure = cursor.getDouble(pressureColIndex);
            wind = cursor.getLong(windColIndex);
            units = cursor.getString(unitsColIndex);
        } catch (IllegalArgumentException e) {
            resetFieldsByDefault();
            errorFlag = true;
            errorDescription = "Parsing error";
            Log.d(LOG_TAG, "Error while parsing cursor: " + e.getMessage());
        } catch (Exception e) {
            resetFieldsByDefault();
            errorFlag = true;
            errorDescription = "Cursor moving error";
            Log.d(LOG_TAG, "Error while moving cursor: " + e.getMessage());
        } finally {
            cursor.close();
        }
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public long getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public long getWind() {
        return wind;
    }

    public boolean getErrorFlag() {
        return errorFlag;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public String getUnits() {
        return units;
    }

    private void resetFieldsByDefault() {
        description = "";
        name = "";
        temp = 0;
        humidity = 0;
        wind = 0;
        pressure = 0;
        errorDescription = "";
        errorFlag = false;
        units = "metric";
    }
}
