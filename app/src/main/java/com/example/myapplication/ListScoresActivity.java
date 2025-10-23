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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListScoresActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnSuccessListener<DocumentReference>, OnFailureListener, SeekBar.OnSeekBarChangeListener {

    ListView listViewcScores;
    TextView textViewName;
    TextView editTextScore;
    Button buttonAdd;
    Button buttonDeleteScore;
    Button buttonFilter;

    ArrayList<Score> scores = new ArrayList<>();
    ScoreItemAdapter adapter;

    FirebaseFirestore db;

    SeekBar seekBarScore;

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
        seekBarScore = findViewById(R.id.seekBarScore);

        seekBarScore.setOnSeekBarChangeListener(this);

        //createSampleScores();

        // adapter = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1, scores);
        adapter = new com.example.myapplication.ScoreItemAdapter(this, R.layout.score_item, scores);
        listViewcScores.setAdapter(adapter);
        ;
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


        db = FirebaseFirestore.getInstance();
        db.collection("scores").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, 
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Firestore", "Listen failed.", error);
                    return;
                }
                Log.i("Firestore", "Listen QuerySnapshot succeeded.");
                scores.clear();
                for (QueryDocumentSnapshot document : value) {
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                    Score score = document.toObject(Score.class);
                    scores.add(score);
                }
                adapter.notifyDataSetChanged();
            }});

                                                    
        //getSpecificDocument("scores","Gur");
        //loadScoresFromDB(Integer.MAX_VALUE);
        //addScoresToDB(db);
        buttonFilter = findViewById(R.id.buttonFilter);
        buttonFilter.setOnClickListener(this);
    }

    public void getSpecificDocument(String collectionName, String documentId) {
        Log.i("getSpecificDocument", "getSpecificDocument :" +collectionName + ":" + documentId);
        db.collection(collectionName).document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document found, you can now access its data
                                Log.d("Firestore", "Document data: " + document.getData());
                                // Example: Get a specific field
                                long score = (long) document.get("score");
                                Log.d("Firestore", "My field value: " + score);
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.d("Firestore", "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    private void loadScoresFromDB(int maxScore) {
        Log.i("loadScoresFromDB", "loadScoresFromDB");

        db.collection("scores").
                //whereLessThan("score" , maxScore).
                whereGreaterThanOrEqualTo("name",  "A").
                whereLessThanOrEqualTo("name",  "A\uf7ff"). // The \uf7ff here is just the last known Unicode character, so that the query stops returning results after dealing with every “Th”
                orderBy("name", Query.Direction.ASCENDING).
                get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i("onSuccess", "loadScoresFromDB onSuccess");
                        scores.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.i("onSuccess", document.getId() + " => " + document.getData());
                            Score score = document.toObject(Score.class);
                            scores.add(score);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("onFailure", "Error getting documents.", e);
                    }
                });

    }

    void addScoresToDB(FirebaseFirestore db){
        // Create a new Map with the scores
        Map<String, ArrayList<Score>> data = new HashMap<>();
        data.put("scores", scores);


// Add a new document with a generated ID
        db.collection("scores")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FB OnSuccessListener", "scores added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FB OnFailureListener", "Error adding scores", e);
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
                db.collection("scores").
                        document(scores.get(adapter.getSelectedPosition()).getName()).
                        delete().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("onSuccess", "document deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("onFailure", "Error deleting document", e);
                            }
                        });


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


    private void createSampleScores() {
        scores = new ArrayList<>();
        scores.add(new Score("Asaf",45));
        scores.add(new Score("Ben",55));
        scores.add(new Score("Anat",51));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            String name = textViewName.getText().toString();
            Log.i("onClick name", name);
            String s = editTextScore.getText().toString();
            Log.i("onClick score string", s);

            if (s.equals("")) {
                Log.e("onClick score error", "is empty");
            } else {
                int score;
                try {
                    score = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    Log.e("onClick score error", e.getMessage());
                    editTextScore.setError("please enter decimal");
                    return;
                }
                Log.i("onClick score", String.valueOf(score));
                db.collection("scores").
                        document(name).
                        set(new Score(name, score)).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("onSuccess", "document added");
                            }
                        }).addOnFailureListener(this);

                scores.add(new Score(name, score));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, name + " with score " + score + " added", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId()== R.id.buttonFilter) {

            String s = editTextScore.getText().toString();
            Log.i("onClick score string", s);
            int score;
            try {
                score = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                Log.e("onClick score error", e.getMessage());
                editTextScore.setError("please enter decimal");
                return;
            }
            loadScoresFromDB(score);
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

    @Override
    public void onSuccess(DocumentReference documentReference) {
        Log.i("onSuccess", "document added with ID: " + documentReference.getId());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.w("onFailure", "Error adding document", e);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextScore.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}