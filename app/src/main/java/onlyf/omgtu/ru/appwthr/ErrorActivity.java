package onlyf.omgtu.ru.appwthr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Objects;

public class ErrorActivity extends AppCompatActivity {

    private TextView errorMessage;
    private final static String TAG = "ErrorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        errorMessage = findViewById(R.id.error_message);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        errorMessage.setText(intent.getStringExtra("errorDescription"));
    }
}
