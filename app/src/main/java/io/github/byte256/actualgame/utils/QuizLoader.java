package io.github.byte256.actualgame.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizLoader {

    private final Context context;

    public QuizLoader(Context context){
        this.context = context;
    }

    public List<QuizQuestion> getQuizzez() throws IOException {
        List<QuizQuestion> quizzez = new ArrayList<QuizQuestion>();
        try {
            InputStream is = context.getAssets().open("questions.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2){
                    QuizQuestion q = new QuizQuestion(parts[0], parts[1].replace(" ","").toUpperCase());
                    Log.d("getQuizzez", parts[0]+"-"+parts[1]);
                    quizzez.add(q);
                }
            }
        }
        catch (IOException e) {
            Log.d("getQuizzez: ", e.toString());
        }
        return quizzez;
    }

}
