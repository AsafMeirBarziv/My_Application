package com.example.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1000;
    TextView textViewTime;
    EditText editTextTime;
    Button buttonSetTime;
    Button buttonOpenActivityII;

    ImageView imageView;


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

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
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

            //startActivityForResult(intent, 0);

            activityResultLauncher.launch(intent);

        }
        else if (v.getId() == R.id.imageView){
            Toast.makeText(this, "imageView clicked", Toast.LENGTH_SHORT).show();
            Log.i("MainActivity", "imageView clicked");
            openImageChooser();

        }
    }



    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::handleResult
    );

    private void handleResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // Handle the result here, e.g., get data from result.getData()
            Intent data = result.getData();
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