package io.github.byte256.actualgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button start, exit;

        exit = findViewById(R.id.close);
        start = findViewById(R.id.start_game);

        start.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Started!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, PressuresActivity.class);
            startActivity(intent);
        });
        exit.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Exited!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}