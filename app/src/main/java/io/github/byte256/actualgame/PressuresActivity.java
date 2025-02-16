package io.github.byte256.actualgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.byte256.actualgame.utils.LevelListAdapter;
import io.github.byte256.actualgame.utils.QuizLoader;

public class PressuresActivity extends AppCompatActivity {

    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pressures);

        ListView levelList = findViewById(R.id.levels);

        QuizLoader quizLoader = new QuizLoader(this);
        Set<String> levels;

        try {
            levels = quizLoader.getLevels();
            Log.d("Pressure", "onCreate: "+levels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        Log.d("Pressure", "getLevels: set" + levels);

        List<String> categories = new ArrayList<>(levels);

        LevelListAdapter adapter = new LevelListAdapter(this,categories);

//        Log.d("Pressure", "onCreateList: "+categories);
        levelList.setAdapter(adapter);
        levelList.setOnItemClickListener((parent, view, position, id) -> {
            level = categories.get(position);
            // Start game with selected category
//            Log.d("Pressure", "Category :" + level);
            gotoGame();
        });
    }
    private void gotoGame() {
        Intent intent = new Intent(PressuresActivity.this, GameActivity.class);
        intent.putExtra("level_name", level);
        startActivity(intent);
    }
}

