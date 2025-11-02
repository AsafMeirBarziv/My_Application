package com.example.myapplication;


// CustomItemAdapter.java
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import java.util.List;

public class ScoreItemAdapter extends ArrayAdapter<Score> {

    private Context context;
    private int resource;
    private List<Score> items;
    private int selectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public ScoreItemAdapter(Context context, int resource, List<Score> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Score currentItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        int color = Color.parseColor(sharedPreferences.getString("list_avg_color", "#F44336"  ) );
        //int color = Color.parseColor( "#FF03DAC5"  );


        // Lookup view for data population
        TextView scoreDescription = convertView.findViewById(R.id.textViewScoreDescription);

        // Populate the data into the template view using the data object
        if (currentItem != null) {
            scoreDescription.setText(currentItem.toString());

            if (currentItem.getScore() > scoresAvg())
                color = context.getColor( R.color.white);
            boolean bold = sharedPreferences.getBoolean("check_box_bold",  false);

            if (bold)
                scoreDescription.setTypeface(null, Typeface.BOLD);

            if (position == selectedPosition)
                scoreDescription.setTypeface(null, Typeface.BOLD_ITALIC);


            convertView.setBackgroundColor(color);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private int scoresAvg() {
        int avg = 0;
        for (Score score:items)
        {
            avg += score.getScore();
        }
        avg /= items.size();
        return avg;
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }
}