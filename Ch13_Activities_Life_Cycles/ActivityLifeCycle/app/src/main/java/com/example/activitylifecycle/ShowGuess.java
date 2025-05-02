package com.example.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowGuess extends AppCompatActivity {
    private TextView showGuessTextView; // TextView to display the guess

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_show_guess); // Set the layout for this activity

        showGuessTextView = findViewById(R.id.received_textview); // Initialize the TextView

        // Retrieve the extra data from the intent
        Bundle extra = getIntent().getExtras();

        if (extra != null) { // Check if there are extras
            showGuessTextView.setText(extra.getString("guess")); // Set the guess text in the TextView
            Log.d("Name extra", "onCreate: " + extra.getString("name")); // Log the name extra
            Log.d("Age extra", "onCreate: " + extra.getInt("age")); // Log the age extra
        }

        // Set an OnClickListener on the TextView
        showGuessTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent(); // Get the current Intent
                intent.putExtra("message_back", "From Second Activity"); // Add a message to the Intent
                setResult(RESULT_OK, intent); // Set the result with the Intent
                finish(); // Finish the activity
            }
        });

        // Set window insets listener to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}