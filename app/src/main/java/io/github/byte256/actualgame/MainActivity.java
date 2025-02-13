package io.github.byte256.actualgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button start, exit, start2;

        start = findViewById(R.id.start_game);
        exit = findViewById(R.id.close);
        exit = findViewById(R.id.close);
        start2 = findViewById(R.id.start_game_2);

        start.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Started!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        start2.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Started!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Game2Activity.class);
            startActivity(intent);
            finish();
        });
        exit.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Exited!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}