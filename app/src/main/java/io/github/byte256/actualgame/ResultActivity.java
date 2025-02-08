package io.github.byte256.actualgame;

import static android.graphics.Color.BLACK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int percentage = getIntent().getIntExtra("percentage", 0);

        TextView res = findViewById(R.id.result);
        res.setTextColor(BLACK);
        res.setText(String.format("%s%%", percentage));

        SharedPreferences prefs = getSharedPreferences("QuizHistory", MODE_PRIVATE);
        String history = prefs.getString("history", "No previous results.");
        TextView historyTextView = findViewById(R.id.historyText);
        historyTextView.setText(history);

        Button home = findViewById(R.id.home);

        home.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });


    }
}