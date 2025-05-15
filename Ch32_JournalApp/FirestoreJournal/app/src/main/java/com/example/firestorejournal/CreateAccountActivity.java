package com.example.firestorejournal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {
  private Button createAcctButton;

  private FirebaseAuth firebaseAuth;
  private FirebaseAuth.AuthStateListener authStateListener;
  FirebaseUser currentUser;

  // Firestore connection
  private FirebaseFirestore databaseFF = FirebaseFirestore.getInstance();
  private CollectionReference collectionReference = databaseFF.collection("Users");

  private EditText emailEditText;
  private EditText passwordEditText;
  private ProgressBar progressBar;
  private EditText usernameEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_create_account);

    // Initialize Firebase Auth
    firebaseAuth = FirebaseAuth.getInstance();
    //

    createAcctButton = findViewById(R.id.create_account_button);
    progressBar = findViewById(R.id.create_account_progress);
    emailEditText = findViewById(R.id.email_account);
    passwordEditText = findViewById(R.id.password_account);
    usernameEditText = findViewById(R.id.username_account);

    authStateListener = firebaseAuth -> {
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //reload();
        } else {
            // no user yet
        }
    };



    createAcctButton.setOnClickListener(view -> {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(username)) {
            createUsernameEmailPassword(email,
                 password, username);
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }

    });

      ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.create_account_layout),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
  }

    private void createUsernameEmailPassword(String email, String password, String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            String currentUserID = currentUser.getUid();

                            // Create a user Map so we can create a user in the User collection
                            Map<String, String> userObj = new HashMap<>();
                            userObj.put("userId", currentUserID);
                            userObj.put("username", username);

                            // Save to Firestore database
                            collectionReference.add(userObj)
                                    .addOnSuccessListener(documentReference -> {
                                        // Handle success
                                        documentReference.get()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.getResult().exists()) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        String name = task1.getResult()
                                                                .getString("username");

                                                        Intent intent = new Intent(CreateAccountActivity.this, PostJournalActivity.class);
                                                        intent.putExtra("username", name);
                                                        intent.putExtra("userId", currentUserID);
                                                        startActivity(intent);
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Firestore write failed", e);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CreateAccountActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    });
        } else {
            // Handle empty fields
        }
    }

    @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    firebaseAuth.addAuthStateListener(authStateListener);
//    if (currentUser != null) {
//      reload();
//    }
  }



    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            Toast.makeText(this, "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT).show();
            // Navigate to the main activity or update the UI accordingly
        } else {
            // User is signed out
            Toast.makeText(this, "Please sign in to continue.", Toast.LENGTH_SHORT).show();
        }
    }
}
