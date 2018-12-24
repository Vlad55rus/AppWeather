package onlyf.omgtu.ru.appwthr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import onlyf.omgtu.ru.appwthr.RequestParameters;
import onlyf.omgtu.ru.appwthr.WeatherCallback;
import onlyf.omgtu.ru.appwthr.WeatherResponse;
import onlyf.omgtu.ru.appwthr.WeatherUtils;

public class WeatherService extends Service implements WeatherCallback {

    WeatherUtils weatherUtils;
    WeatherResponse wr;
    RequestParameters requestParameters;
    SharedPreferences sharedPreferences;
    private final String LOG_TAG = "WeatherService";
    ExecutorService es;
    NotificationManager nm;

    public void onCreate(){
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service Started, id is - " + startId);

        MyRun mr = new MyRun(startId);
        es.execute(mr);

        weatherUtils = new WeatherUtils(this, this);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        RequestParameters requestParameters = new RequestParameters(this, sharedPreferences.getString("city", "Omsk"), sharedPreferences.getInt("units", 0));

        weatherUtils.makeRequest(requestParameters);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        wr = dataBaseHelper.getRecord(requestParameters);
        
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Weather is updated!")
                        .setContentText("It`s " + wr.getTemp() + " in " + wr.getName());

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onResponseCallback(WeatherResponse wr) {
        Log.d(LOG_TAG, "onResponseCallback: made request, status: " + !wr.getErrorFlag());
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "Service Destroyed");
        super.onDestroy();
    }
}