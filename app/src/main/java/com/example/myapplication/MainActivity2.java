package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    TextView textViewTime;
    EditText editTextHour;

    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        textViewTime = findViewById(R.id.textViewTime);
        String time = intent.getStringExtra("time");
        textViewTime.setText(time);

        editTextHour = findViewById(R.id.editTextHour);
        buttonOK = findViewById(R.id.buttonOK);

        buttonOK.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonOK) {
            if (editTextHour.getText().toString().isEmpty()) {
                editTextHour.setError("Please enter a value");
                return;
            }

            else if (Integer.parseInt(editTextHour.getText().toString()) > 23) {
                editTextHour.setError("Please enter a value between 0 and 23");
                return;
            }
            else if (Integer.parseInt(editTextHour.getText().toString()) < 0) {
                editTextHour.setError("Please enter a value between 0 and 23");
                return;
            }
            else {
                String hour = String.valueOf(editTextHour.getText());
                Intent intent = new Intent();
                intent.putExtra("hour",hour);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }


    }
}