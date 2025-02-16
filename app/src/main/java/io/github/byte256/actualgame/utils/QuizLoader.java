package io.github.byte256.actualgame.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class QuizLoader {
    private InputStreamReader isr;

    public QuizLoader(Context context){

        try {
            InputStream is = context.getAssets().open("questions.csv");
            this.isr = new InputStreamReader(is);
        }
        catch (IOException e) {
            Log.e("ERROR IO", "QuizLoader: IO Failed");
        }
    }

    public List<QuizQuestion> getQuizzez(String level) throws IOException {
        List<QuizQuestion> quizzez = new ArrayList<>();
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3){
                if (parts[2].equals(level)){
                    QuizQuestion q = new QuizQuestion(parts[0], parts[1].replace(" ","").toUpperCase());
//                    Log.d("getQuizzez", parts[0]+"-"+parts[1]+"--"+parts[2]);
                    quizzez.add(q);
                }
            }
        }
        return quizzez;
    }
    public Set<String> getLevels() throws IOException {
        Set<String> levelList = new LinkedHashSet<>();
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null){
            String[] parts = line.split(",");
            levelList.add(parts[2]);
            Log.d("LevelList", "getLevels: "+parts[2]);
        }
        Log.d("QuizLoader", "getLevels: " + levelList);
        return levelList;
    }

}
