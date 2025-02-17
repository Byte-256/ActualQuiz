package io.github.byte256.actualgame.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import io.github.byte256.actualgame.R;


public class LevelListAdapter extends ArrayAdapter<String> {
    private final List<String> items;
    private final Context context;

    public LevelListAdapter(@NonNull Context context, List<String> items) {
        super(context, R.layout.list_pressure_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_pressure_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.list_pressure_text_view);
        textView.setText(items.get(position));
        SharedPreferences prefs = context.getSharedPreferences("LevelHistory", MODE_PRIVATE);
        boolean is_complete = prefs.getBoolean(items.get(position), false);
        switch (items.get(position)){
            case "easy 1":
            case "easy 2":
                textView.setTextColor(Color.parseColor("#57E61E"));
                break;
            case "medium 1":
            case "medium 2":
                textView.setTextColor(Color.parseColor("#FFC107"));
                break;
            case "intense":
                textView.setTextColor(Color.parseColor("#F44336"));
                break;
            default:
                textView.setTextColor(Color.parseColor("#000000"));
                break;
        }
        if (is_complete) {
            textView.setBackground(AppCompatResources.getDrawable(context, R.drawable.pressure_btn_completed));
            textView.setTextColor(Color.argb(255, 80,80,80));
        }
        return convertView;
    }
}
