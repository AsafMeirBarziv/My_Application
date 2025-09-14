package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListScoresActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listViewcScores;
    TextView textViewName;
    TextView editTextScore;
    Button buttonAdd;
    Button buttonDeleteScore;

    ArrayList<Score> scores = new ArrayList<>();
    ScoreItemAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_scores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewcScores = findViewById(R.id.listViewScores);
        textViewName = findViewById(R.id.EditTextName);
        editTextScore = findViewById(R.id.EditTextScore);
        buttonAdd = findViewById(R.id.buttonAdd);

        createSampleScores();

        // adapter = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1, scores);
        adapter = new com.example.myapplication.ScoreItemAdapter(this, R.layout.score_item, scores);

        listViewcScores.setAdapter(adapter);
        listViewcScores.setOnItemClickListener(this);
        listViewcScores.setOnItemLongClickListener(this);
        registerForContextMenu(listViewcScores);
        buttonAdd.setOnClickListener(this);


        buttonDeleteScore = findViewById(R.id.buttonDeleteScore);
        buttonDeleteScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteScore();
                    return;

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listViewScores) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.score_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position; // Position of the item in the ListView

        int itemId = item.getItemId();
        if (itemId == R.id.menu_edit) {
            // Handle edit action for item at 'position'
            return true;
        } else if (itemId == R.id.menu_delete) {
            // Handle delete action for item at 'position'
            confirmDeleteScore();

            return true;
        } else if (itemId == R.id.menu_share) {
            // Handle share action for item at 'position'
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void confirmDeleteScore() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete ?");

        // Set positive button (Yes)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed deletion, remove the item

                scores.remove(adapter.getSelectedPosition());
                adapter.notifyDataSetChanged();
                buttonDeleteScore.setEnabled(false);     }
        });

        // Set negative button (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User cancelled deletion, close the dialog
                dialog.dismiss();
                Toast.makeText(ListScoresActivity.this, "Deletion cancelled.", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        builder.create().show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState,persistentState);

    }

    private void createSampleScores() {
        scores = new ArrayList<>();
        scores.add(new Score("Asaf",134567));
        scores.add(new Score("Ben",4567));
        scores.add(new Score("Anat",1342567));
    }

    @Override
    public void onClick(View v) {
        String name = textViewName.getText().toString();
        Log.i("onClick name",name);
        String s = editTextScore.getText().toString();
        Log.i("onClick score string",s);

        if(s.equals("")){
            Log.e("onClick score error","is empty");
        }

        else {
            int score;
            try {
                score = Integer.parseInt(s);
            }
            catch (NumberFormatException e){
                Log.e("onClick score error",e.getMessage());
                editTextScore.setError("please enter decimal");
                return;
            }
            Log.i("onClick score", String.valueOf(score));
            scores.add(new Score(name,score));
            adapter.notifyDataSetChanged();
            Toast.makeText(this, name + " with score " + score + " added", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectedPosition(position);

        buttonDeleteScore.setEnabled(true);
        adapter.notifyDataSetChanged();
        return false;
    }
}