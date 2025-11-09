package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.concurrent.Executor;

public class GeminiActivity extends AppCompatActivity implements View.OnClickListener {
    GenerativeModel ai;
    GenerativeModelFutures model;

    Button buttonAI;
    TextView textViewAI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gemini);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize the Gemini Developer API backend service
// Create a `GenerativeModel` instance with a model that supports your use case

        ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash");

        // Use the GenerativeModelFutures Java compatibility layer which offers
// support for ListenableFuture and Publisher APIs
        model = GenerativeModelFutures.from(ai);

        buttonAI = findViewById(R.id.buttonAI);
        textViewAI = findViewById(R.id.textViewAI);

        buttonAI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

// Initialize the Gemini Developer API backend service
// Create a `GenerativeModel` instance with a model that supports your use case
        GenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash");

// Use the GenerativeModelFutures Java compatibility layer which offers
// support for ListenableFuture and Publisher APIs
        GenerativeModelFutures model = GenerativeModelFutures.from(ai);

// Provide a prompt that contains text
        Content prompt = new Content.Builder()
                .addText("shortly describe a " + "dog")
                .build();

// To generate text output, call generateContent with the text input
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                Log.i("resultText", resultText);
                textViewAI.setText(resultText);
                System.out.println(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
                t.printStackTrace();
            }
        }, executor);
    }
}
