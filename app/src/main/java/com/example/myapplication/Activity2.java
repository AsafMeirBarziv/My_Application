package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {

    Button buttonBack;

    @Override
    protected void onStart() {
        Log.i("Activity2", "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i("Activity2", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("Activity2", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i("Activity2", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("Activity2", "onResume");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity2", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            // here is my click listner
            // v is the view
            Intent intent = new Intent(this, Activity1.class);
            startActivity(intent);
        });



    }
}