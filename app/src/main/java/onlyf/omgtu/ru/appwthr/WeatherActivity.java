package onlyf.omgtu.ru.appwthr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity implements WeatherCallback {

    private WeatherUtils utils;
    private TextView
            mainTemp,
            humidity,
            city,
            description,
            wind,
            pressure,
            cachedDate;
    private String
            pendingStateLabel,
            notAvailableLabel,
            unitsDegreeMark,
            unitsSpeedMark;
    private final static String LOG_TAG = "WeatherActivity";
    private RequestParameters
            requestParameters;
    private DataBaseHelper
            dataBaseHelper;
    private LinearLayout
            cachedBlock;
    private WeatherResponse
            weatherResponse;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mainTemp = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        cachedBlock = findViewById(R.id.cached_block);
        cachedDate = findViewById(R.id.cachedDate);
        pendingStateLabel = getString(R.string.pending_state);
        notAvailableLabel = getString(R.string.not_available);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Intent intent = getIntent();
        requestParameters = new RequestParameters(getApplicationContext(), intent.getStringExtra("city"), intent.getIntExtra("units", RequestParameters.UNITS_DEFAULT));


        unitsDegreeMark = requestParameters.getUnitsDegreeMark();
        unitsSpeedMark = requestParameters.getUnitsSpeedMark();

        weatherResponse = dataBaseHelper.getRecord(requestParameters);

        if (intent.getBooleanExtra("useCache", false)) {
            cachedDate.setText(String.format("%s %s", getString(R.string.cached_date), DateHelper.formatDate(weatherResponse.getRequest_date())));
            cachedBlock.setVisibility(View.VISIBLE);
        }

        onResponseCallback(weatherResponse);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void onClick(View view) {
        mainTemp.setText(pendingStateLabel);
        humidity.setText(pendingStateLabel);
        description.setText(pendingStateLabel);
        wind.setText(pendingStateLabel);
        pressure.setText(pendingStateLabel);
    }

    @SuppressLint("DefaultLocale")
    public void onResponseCallback(WeatherResponse wr) {
        if (wr.getErrorFlag()) {
            city.setText(getString(R.string.city));
            mainTemp.setText(wr.getErrorDescription());
            description.setText(getString(R.string.description));
            humidity.setText(notAvailableLabel);
            wind.setText(notAvailableLabel);
            pressure.setText(notAvailableLabel);
        } else {
            city.setText(wr.getName());
            mainTemp.setText(String.format("%d%s", wr.getTemp(), unitsDegreeMark));
            description.setText(wr.getDescription());
            humidity.setText(String.format("%s%%", wr.getHumidity()));
            wind.setText(String.format("%d %s", wr.getWind(), unitsSpeedMark));
            pressure.setText(String.format("%.2f %s", wr.getPressure(), getString(R.string.pressure_units)));
        }
    }
}
