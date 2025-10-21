package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class msgActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView textViewMessage;
    Button buttonSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_msg);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textViewMessage = findViewById(R.id.textView);
        buttonSet = findViewById(R.id.buttonSet);

        buttonSet.setOnClickListener(v -> {
            // here is my click listner
            // v is the view
            String message = textViewMessage.getText().toString();
            db.collection("messages").
                    document("message").
                    set(new Message(message)).
                    addOnSuccessListener(unused -> {
                        Toast.makeText(this, "message added", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Log.e("onFailure", "Error adding document", e);
                        Toast.makeText(this, "message not added", Toast.LENGTH_SHORT).show();
                    });
        });

        db.collection("messages").addSnapshotListener((value, error) -> {
            for (DocumentSnapshot document : value) {
                textViewMessage.setText(document.getString("message"));
            }
        });
    }
}