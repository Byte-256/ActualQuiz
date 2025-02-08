package io.github.byte256.actualgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.byte256.actualgame.utils.Quiz;

public class GameActivity extends AppCompatActivity {

    private Quiz[] quizzes;
    private final int[] counter = {0};
    private int score = 0;

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



        TextView questionTextView = findViewById(R.id.question);
        RadioGroup optionsGroup = findViewById(R.id.optionsGroup);
        Button nextButton = findViewById(R.id.next);
        Button skipButton = findViewById(R.id.skip);
        ImageButton prevButton = findViewById(R.id.previous_question);


        quizzes = Quizloader();

        if (quizzes.length > 0) {
            displayQuestion(counter[0], questionTextView, optionsGroup);
        }

        prevButton.setOnClickListener(v -> {
            prev_quest(counter);
            displayQuestion(counter[0], questionTextView, optionsGroup);
            Log.d("Pagination", "Previous Question -> "+(counter[0]+1));
        });

        nextButton.setOnClickListener(v -> {
            validateAnswer(counter[0], optionsGroup);
            displayQuestion(counter[0], questionTextView, optionsGroup);
            Log.d("pagination", "Next Question -> "+(counter[0]+1));
        });

        skipButton.setOnClickListener(v -> {
            Log.d("pagination", "Skipped! Question -> "+(counter[0]+1));
            next_quest(counter);
            displayQuestion(counter[0], questionTextView, optionsGroup);
        });

    }

    private Quiz[] Quizloader() {
        String json = null;
        try {
            AssetManager assetManager = this.getAssets();
            InputStream inputStream = assetManager.open("quest.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
            reader.close();
        } catch (IOException e) {
            Log.e("Quiz Loader IO",e.toString());
        }

        // Parse the JSON data
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray quizzesArray = jsonObject.getJSONArray("quizzes");
                Quiz[] quizzes = new Quiz[quizzesArray.length()];
                for (int i = 0; i < quizzesArray.length(); i++) {
                    JSONObject quiz = quizzesArray.getJSONObject(i);
                    String question = quiz.getString("question");
                    JSONArray optionsArray = quiz.getJSONArray("options");
                    String[] options = new String[optionsArray.length()];
                    for (int j = 0; j < optionsArray.length(); j++) {
                        options[j] = optionsArray.getString(j);
                    }
                    String correctAnswer = quiz.getString("correctAnswer");
                    quizzes[i] = new Quiz(question, options, correctAnswer);
                }
                return quizzes;
            } catch (JSONException e) {
                Log.e("Parse Json",e.toString());
            }
        }
        return new Quiz[0];
    }

    private void validateAnswer(int c, RadioGroup options) {
        int selected_btn_id = options.getCheckedRadioButtonId();
        RadioButton selected_btn = findViewById(selected_btn_id);
        String selected_answer = selected_btn.getText().toString().trim();

        if (selected_btn_id != -1){
            if (selected_answer.equals(quizzes[c].getCorrectAnswer())) {
                score++;
                Log.d("validation", "correct");
                Log.d("validation", "scroe is " + score);
            }
            else {
               Log.d("validation", "Wrong!");
            }
            next_quest(counter);
        }
        else {
            Toast.makeText(this, "Please select an answer! or Skip the Question", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayQuestion(int i, TextView questionTextView, RadioGroup optionsGroup) {

        optionsGroup.removeAllViews(); // Clean the ground for new ones

        questionTextView.setText(quizzes[i].getQuestion());

        String[] options = quizzes[i].getOptions();

        for (String option : options){
            RadioButton btn = new RadioButton(this);
            btn.setText(option);
            optionsGroup.addView(btn); // Arrange the new ones
        }

    }

    public void prev_quest(int[] c){
        if(c[0] > 0){
            c[0]--;
        } else {
            Toast.makeText(this, "First Question!", Toast.LENGTH_SHORT).show();
        }
    }

    public void next_quest(int[] c){
        if (c[0] < quizzes.length - 1){
            c[0]++;
        }
        else {
            Toast.makeText(this, "Reached Final Question!", Toast.LENGTH_SHORT).show();
            final_screen();
        }
    }

    private void store_result() {
        int percentage = score / quizzes.length * 100;

        long now = System.currentTimeMillis();

        SharedPreferences pref = getSharedPreferences("QuizHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String history = pref.getString("history", "");
        String newEntry = "Date: " + now + " - Score: " + score + "/" + quizzes.length + " (" + percentage + "%)\n";

        editor.putString("history", history + newEntry);
        editor.apply();
    }

    private void final_screen() {
        store_result();
        int percentage =  score / quizzes.length * 100;

        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        intent.putExtra("percentage", percentage);

        startActivity(intent);
        finish();
    }
}