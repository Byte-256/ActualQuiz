package io.github.byte256.actualgame;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.github.byte256.actualgame.utils.QuizLoader;
import io.github.byte256.actualgame.utils.QuizQuestion;

public class GameActivity extends AppCompatActivity {
    private TextView questionText;
    private TextView questionLabel;
    private GridLayout letterGrid;      // Grid for scrambled letters
    private GridLayout answerGrid;      // Grid for answer spaces
    private Button checkButton, clearButton, skipButton;
    private ImageButton closeButton;
    private String currentWord;
    private String scrambledWord;
    private List<TextView> letterViews;
    private List<TextView> answerSpaces;

    private List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        level = getIntent().getStringExtra("level_name");

        if(level == null || level.equals(" ")){
            level = "easy1";
        }

        QuizLoader loader = new QuizLoader(this);
        try {
            questions = loader.getQuizzez(level);
        } catch (IOException e) {
            Log.e("onCreate: ",e.toString() );
        }

        questionText = findViewById(R.id.questionText);
        questionLabel = findViewById(R.id.question_label);
        TextView pressureLabel = findViewById(R.id.pressure_text);

        letterGrid = findViewById(R.id.letterGrid);
        answerGrid = findViewById(R.id.answerGrid);

        checkButton = findViewById(R.id.checkButton);
        skipButton = findViewById(R.id.skipButton);
        clearButton = findViewById(R.id.clearButton);
        closeButton = findViewById(R.id.close_icon);


        letterViews = new ArrayList<>();
        answerSpaces = new ArrayList<>();

        pressureLabel.setText(String.format("Pressure: %s", level));
        setupButtons();
        loadQuestion(currentQuestionIndex);
    }

    private void setupButtons() {
        checkButton.setOnClickListener(v -> checkAnswer());
        clearButton.setOnClickListener(v -> clearAnswer());
        closeButton.setOnClickListener(v -> finish());
        skipButton.setOnClickListener(v -> { currentQuestionIndex++;
            loadQuestion(currentQuestionIndex); });
    }

    private void loadQuestion(int index) {
        if (index < questions.size()) {
            questionLabel.setText(String.format(Locale.ENGLISH,"Question: %d", index+1));

            QuizQuestion question = questions.get(index);
            questionText.setText(question.getQuestion());
            currentWord = question.getAnswer();
            scrambledWord = scrambleWord(currentWord);
            createLetterSpaces();
            createAnswerSpaces();
        } else {
            Toast.makeText(this, "Quiz completed!", Toast.LENGTH_LONG).show();
            final_screen();
        }
    }

    private void store_result() {
        int total_questions = questions.size();
        int correct_answers = score;

        float percentage = (float) correct_answers / total_questions * 100;
        int percentage_int = (int) Math.floor(percentage);


        long now = System.currentTimeMillis();

//Saving the Score
        SharedPreferences pref = getSharedPreferences("QuizHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String history = pref.getString("history", "");
        String newEntry = "Date: " + now + " - Score: " + score + "/" + questions.size() + " (" + percentage_int + "%)\n";
        editor.putString("history", history + newEntry);
        editor.apply();

//Saving the Level Progress
        SharedPreferences level_pref = getSharedPreferences("LevelHistory", MODE_PRIVATE);
        SharedPreferences.Editor level_editor = level_pref.edit();
        level_editor.putBoolean(level, true);
        level_editor.apply();
    }
    private void final_screen() {
        store_result();
        int total_questions = questions.size();
        int correct_answers = score;
        float percentage = (float) correct_answers / total_questions * 100;
        int percentage_int = (int) Math.floor(percentage);

        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        intent.putExtra("percentage", percentage_int);

        startActivity(intent);
        finish();
    }

    public int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    private void createLetterSpaces() {
        letterGrid.removeAllViews();
        letterViews.clear();

        for (char letter : scrambledWord.toCharArray()) {
            TextView letterView = new TextView(this);
            letterView.setText(String.valueOf(letter));
            letterView.setTextSize(24);
            letterView.setTextColor(Color.parseColor("#438F82"));
            letterView.setGravity(Gravity.CENTER);
            letterView.setPadding(8, 8, 8, 8);
            letterView.setBackgroundResource(R.drawable.circle_outline);

            letterView.setWidth(dpToPx(48));
            letterView.setHeight(dpToPx(48));

            // Setup drag functionality
            letterView.setOnLongClickListener(v -> {
                ClipData.Item item = new ClipData.Item(letterView.getText().toString());
                ClipData dragData = new ClipData(
                        letterView.getText().toString(),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );

                View.DragShadowBuilder shadow = new View.DragShadowBuilder(letterView);
                letterView.startDragAndDrop(dragData, shadow, letterView, 0);
                letterView.setVisibility(View.INVISIBLE);
                return true;
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);

            letterGrid.addView(letterView, params);
            letterViews.add(letterView);
        }
    }

    private void createAnswerSpaces() {
        answerGrid.removeAllViews();
        answerSpaces.clear();

        for (int i = 0; i < currentWord.length(); i++) {
            TextView space = new TextView(this);
            space.setText("_");
            space.setTextSize(24);
            space.setGravity(Gravity.CENTER);
            space.setTextColor(Color.parseColor("#438F82")); // set color

            space.setPadding(8, 8, 8, 8);

            space.setBackgroundResource(R.drawable.circle_outline);

            space.setWidth(dpToPx(48));
            space.setHeight(dpToPx(48));

            space.setTag("empty");

            // Setup drop functionality
            space.setOnDragListener(this::handleDrag);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(12, 12, 12, 12);

            answerGrid.addView(space, params);
            answerSpaces.add(space);
        }
    }

    private boolean handleDrag(View view, DragEvent event) {
        TextView target = (TextView) view;

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            case DragEvent.ACTION_DRAG_ENTERED:
                view.setBackgroundResource(R.drawable.circle_outline_selected);
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                view.setBackgroundResource(R.drawable.circle_outline);
                return true;

            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String draggedLetter = item.getText().toString();

                if (target.getTag().equals("empty")) {
                    target.setText(draggedLetter);
                    target.setTag("filled");
                    View draggedView = (View) event.getLocalState();
                    draggedView.setVisibility(View.INVISIBLE);
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENDED:
                view.setBackgroundResource(R.drawable.circle_outline);
                View draggedView = (View) event.getLocalState();
                if (!event.getResult()) {
                    draggedView.setVisibility(View.VISIBLE);
                }
                return true;

            default:
                return false;
        }
    }

    private void checkAnswer() {
        StringBuilder answer = new StringBuilder();
        for (TextView space : answerSpaces) {
            if (space.getTag().equals("filled")) {
                answer.append(space.getText());
            }
        }

        if (answer.toString().equals(currentWord)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            score++;
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            clearAnswer();
        }
    }

    private void clearAnswer() {
        for (TextView letterView : letterViews) {
            letterView.setVisibility(View.VISIBLE);
        }
        for (TextView space : answerSpaces) {
            space.setText("_");
            space.setTag("empty");
        }
    }

    private String scrambleWord(String word) {
        List<Character> chars = new ArrayList<>();
        for (char c : word.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder scrambled = new StringBuilder();
        for (char c : chars) {
            scrambled.append(c);
        }
        return scrambled.toString();
    }
}