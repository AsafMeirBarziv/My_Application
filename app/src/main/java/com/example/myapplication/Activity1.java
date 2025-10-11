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



public class Activity1 extends AppCompatActivity {
    Button buttonOpen2;

    @Override
    protected void onStart() {
        Log.i("Activity1", "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i("Activity1", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("Activity1", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i("Activity1", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("Activity1", "onResume");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity1", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonOpen2 = findViewById(R.id.buttonOpen2);
        buttonOpen2.setOnClickListener(v -> {
            // here is my click listner
            // v is the view
                Intent intent = new Intent(this, Activity2.class);
                startActivity(intent);
            }
        );


    }
}