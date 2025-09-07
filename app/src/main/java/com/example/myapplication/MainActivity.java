package com.example.myapplication;

import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewTime;
    EditText editTextTime;
    Button buttonSetTime;
    Button buttonOpenActivityII;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewTime = findViewById(R.id.textViewTime);
        textViewTime.setText("12:48");
        editTextTime = findViewById(R.id.editTextTime);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        buttonSetTime.setOnClickListener(this);

        buttonOpenActivityII = findViewById(R.id.buttonOpenActivityII);
        buttonOpenActivityII.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewTime.setText("12:49");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSetTime){
            String time = String.valueOf(editTextTime.getText());
            textViewTime.setText(time);
        }
        else if (v.getId() == R.id.buttonOpenActivityII){
            Toast.makeText(this, "buttonOpenActivityII clicked", Toast.LENGTH_SHORT).show();
            Log.i("MainActivity", "buttonOpenActivityII clicked");

            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("time", editTextTime.getText().toString());
            startActivityForResult(intent, 0);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            String hour = data.getStringExtra("hour");
            textViewTime.setText(hour);

            // Create a Calendar object and set its time to the Date object
            Calendar calendar = Calendar.getInstance();

            // Set the hour of the day using Calendar
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));

            // Set the updated Date object from the Calendar
            textViewTime.setText(calendar.getTime().toString());
        }

    }
}