package io.github.byte256.actualgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    String[] quest = {
        "What is your name?",
        "How old are you?",
        "What is your favorite color?",
        "What is your hobby?",
        "Where do you live?",
    };
    char[] answer = {
        'A',
        'B',
        'A',
        'C',
        'D'
    };
    public String getQuest(int no) {
        if(no > 4){
            return "Invalid Move Bruh!";
        }
        return quest[no];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int[] counter = {0};

        Button next, skip;
        TextView question = findViewById(R.id.question);

        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_quest(counter);
                question.setText(quest[counter[0]]);
                Toast.makeText(GameActivity.this, "Next Question -> "+(counter[0]+1), Toast.LENGTH_SHORT).show();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameActivity.this, "Skipped! Question -> "+(counter[0]+1), Toast.LENGTH_SHORT).show();
                next_quest(counter);
                question.setText(quest[counter[0]]);
            }
        });

    }
    public void next_quest(int[] c){
        if (c[0] < 4){
            c[0]++;
        }
        else {
            Toast.makeText(this, "Reached Final Question!", Toast.LENGTH_SHORT).show();
            final_screen();
        }
    }

    private void final_screen() {
        Toast.makeText(this, "bruh! just restart it!", Toast.LENGTH_SHORT).show();
    }
}