package com.example.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button showGuess; // Button to trigger the guess activity
    private EditText enterGuess; // EditText to enter the guess
    private final int REQUEST_CODE = 2; // Request code for startActivityForResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_main); // Set the layout for this activity

        showGuess = findViewById(R.id.button_guess); // Initialize the button
        enterGuess = findViewById(R.id.guess_field); // Initialize the EditText

        // Set an OnClickListener on the button
        showGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guess = enterGuess.getText().toString().trim(); // Get the text from EditText

                if (!guess.isEmpty()) { // Check if the guess is not empty
                    Intent intent = new Intent(MainActivity.this, ShowGuess.class); // Create an Intent to start ShowGuess activity
                    intent.putExtra("guess", guess); // Add the guess to the Intent
                    intent.putExtra("name", "bond"); // Add a name to the Intent
                    intent.putExtra("age", 34); // Add an age to the Intent

                    startActivityForResult(intent, REQUEST_CODE); // Start the activity for result
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a guess", Toast.LENGTH_SHORT).show(); // Show a toast if the guess is empty
                }
            }
        });

        // Set window insets listener to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Handle the result from the started activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) { // Check if the request code matches
            assert data != null;
            String message = data.getStringExtra("message_back"); // Get the message from the result Intent
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show(); // Show the message in a toast
        }
    }
}