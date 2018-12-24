package onlyf.omgtu.ru.appwthr;

import android.util.Log;
import static java.lang.Thread.sleep;

public class MyRun implements Runnable {

    private final String LOG_TAG = "WeatherService";
    int startId;

    public MyRun(int startId){
        this.startId = startId;
    }

    @Override
    public void run() {
        try{
            Log.d(LOG_TAG, "Thread is sleeping :) for 3 sec \n");
            sleep(3000);
        }
        catch (InterruptedException e){
            Log.d(LOG_TAG, "IExptn -- " + e.getMessage());
        }
    }
}
