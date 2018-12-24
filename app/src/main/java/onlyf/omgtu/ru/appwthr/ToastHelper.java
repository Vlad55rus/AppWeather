package onlyf.omgtu.ru.appwthr;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    public static void showToast(final Context context, final String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showToast(final Context context, final String text, final int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
