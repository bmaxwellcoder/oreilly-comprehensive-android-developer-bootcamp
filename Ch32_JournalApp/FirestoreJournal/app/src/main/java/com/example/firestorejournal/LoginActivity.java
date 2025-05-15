package com.example.firestorejournal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
  private Button loginButton;
  private Button createAcctButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_login);

    loginButton = findViewById(R.id.email_sign_in_button);
    createAcctButton = findViewById(R.id.create_account_button);

    loginButton.setOnClickListener(
        view -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
    createAcctButton.setOnClickListener(
        view -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.login_layout),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
  }
}
