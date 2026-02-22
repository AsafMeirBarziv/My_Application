package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.ai.type.GenerationConfig;
import com.google.firebase.ai.type.Schema;
import com.google.firebase.vertexai.FirebaseVertexAI;

import java.util.Map;
import java.util.concurrent.Executor;

public class AI_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonRequestJson = findViewById(R.id.buttonRequestJson);
        buttonRequestJson.setOnClickListener(v -> fetchQuartetCategories());

        textViewResponse = findViewById(R.id.textViewResponse);

    }

    TextView textViewResponse;

    // Define schema for a single Category object
    Schema categorySchema = Schema.obj(Map.of(
            "category_name", Schema.str("Name of the quartet"),
            "members", Schema.array(Schema.str("Cards in the quartet")),
            "description", Schema.str("A brief summary of the quartet")
    ));


    // Define the final response as an array of these objects
    Schema responseSchema = Schema.array(categorySchema);

    GenerationConfig config = new GenerationConfig.Builder()
            .setResponseMimeType("application/json")
            .setResponseSchema(responseSchema)
            .build();

    GenerativeModel gm = FirebaseAI.getInstance()
            .generativeModel("gemini-2.5-flash", config);

    GenerativeModelFutures model = GenerativeModelFutures.from(gm);

    public void fetchQuartetCategories() {
        Content prompt = new Content.Builder()
                .addText("List 5 distinct categories of quartets for the quartets cards game, and supply 4 cards for each quartet.")
                .build();
        ;
        ListenableFuture<GenerateContentResponse> responseFuture = model.generateContent(prompt);

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        Futures.addCallback(responseFuture, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // The AI returns a valid JSON string ready for parsing
                String jsonOutput = result.getText();
                Log.i("JSON Response", jsonOutput);
                textViewResponse.setText(jsonOutput);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        }, executor); // Or a custom background executor
    }

}
