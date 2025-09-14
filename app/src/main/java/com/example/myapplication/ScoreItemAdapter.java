package com.example.myapplication;


// CustomItemAdapter.java
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Lookup view for data population
        TextView scoreDescription = convertView.findViewById(R.id.textViewScoreDescription);

        // Populate the data into the template view using the data object
        if (currentItem != null) {
            scoreDescription.setText(currentItem.toString());
            int color;
            if (currentItem.getScore() > scoresAvg())
                color = R.color.white;
            else
                color = R.color.red;;

            if (position == selectedPosition)
                scoreDescription.setTypeface(null, Typeface.BOLD);
            else
                scoreDescription.setTypeface(null, Typeface.NORMAL);

            convertView.setBackgroundColor(context.getColor(color));
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