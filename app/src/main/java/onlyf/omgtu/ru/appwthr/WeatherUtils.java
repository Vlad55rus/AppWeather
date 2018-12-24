package onlyf.omgtu.ru.appwthr;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class WeatherUtils {

    private WeatherResponse weatherResponse;
    private WeatherCallback callback;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private final static String LOG_TAG = "WeatherUtils";

    public WeatherUtils(final Context context, WeatherCallback callback) {
        this.callback = callback;
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    public void makeRequest(final RequestParameters requestParameters) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                buildUrl(context, requestParameters),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        weatherResponse = new WeatherResponse(response, requestParameters);
                        Log.d("HTTP", "Response success: " + response.toString());
                        dataBaseHelper.addRecord(weatherResponse);
                        callback.onResponseCallback(weatherResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weatherResponse = new WeatherResponse((JSONObject) null, null);
                        Log.d("HTTP", "Response error: " + error);
                        weatherResponse.setErrorDescription(error.toString());
                        callback.onResponseCallback(weatherResponse);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private String buildUrl(Context context, RequestParameters rp) {
        String units = rp.getUnits();
        String url;
        if (units.isEmpty()) {
            url = context.getString(R.string.base_url) +
                    "?q=" + rp.getCity() +
                    "&appid=" + context.getString(R.string.weather_app_key) +
                    "&lang=" + context.getString(R.string.language_code);
        } else {
            url = context.getString(R.string.base_url) +
                    "?q=" + rp.getCity() +
                    "&appid=" + context.getString(R.string.weather_app_key) +
                    "&lang=" + context.getString(R.string.language_code) +
                    "&units=" + rp.getUnits();
        }
        Log.d("HTTP", "URL built: " + url);
        return url;
    }
}
