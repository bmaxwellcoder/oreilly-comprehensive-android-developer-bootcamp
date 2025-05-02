package com.example.introfirestore;

import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_THOUGHT = "thought";

    private EditText titleEditText;
    private EditText thoughtEditText;
    private Button saveButton;
    private Button showButton;
    private Button updateTitleButton;
    private Button updateThought;
    private Button deleteTitleAndThought;
    private TextView receivedTitleTextView;
    private TextView receivedThoughtTextView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference = firebaseFirestore.collection("Journal")
            .document("First Thoughts");

    private CollectionReference collectionReference = firebaseFirestore.collection("Journal");
//    private DocumentReference documentReference = firebaseFirestore
//        .document("Journal/First Thoughts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.title_edit_text);
        thoughtEditText = findViewById(R.id.thoughts_edit_text);
        saveButton = findViewById(R.id.save_button);
        receivedTitleTextView = findViewById(R.id.received_title_text_view);
        receivedThoughtTextView = findViewById(R.id.received_thought_text_view);
        showButton = findViewById(R.id.show_button);
        updateTitleButton = findViewById(R.id.update_title_button);
        updateThought= findViewById(R.id.update_thought_button);
        deleteTitleAndThought = findViewById(R.id.delete_title_and_thought_button);

        updateTitleButton.setOnClickListener(this);
        updateThought.setOnClickListener(this);
        deleteTitleAndThought.setOnClickListener(this);

        saveButton.setOnClickListener(view -> {
//            String title = titleEditText.getText().toString().trim();
//            String thought = thoughtEditText.getText().toString().trim();
//
//            Journal journal = new Journal();
//            journal.setTitle(title);
//            journal.setThought(thought);

//            Map<String, Object> data = new HashMap<>();
//            data.put(KEY_TITLE, title);
//            data.put(KEY_THOUGHT, thought);

//            documentReference.set(journal)
//                    .addOnSuccessListener(unused -> {
//                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.d(TAG, "onFailure: " + e);
//                    });

          addThought();
        });

        showButton.setOnClickListener(view -> {
//            documentReference.get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//
//                            Journal journal = documentSnapshot.toObject(Journal.class);
//
//
////                            String title = documentSnapshot.getString(KEY_TITLE);
////                            String thought = documentSnapshot.getString(KEY_THOUGHT);
//
//                            if (journal != null) {
//                                receivedTitleTextView.setText(journal.getTitle());
//                                receivedThoughtTextView.setText(journal.getThought());
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, "No data exists",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.d(TAG, "onFailure" + e);
//                    });

            getThoughts();
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

//        documentReference.addSnapshotListener(MainActivity.this, (documentSnapshot, error) -> {
//            if (error != null) {
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//            if (documentSnapshot !=  null && documentSnapshot.exists()) {
//                String title = documentSnapshot.getString(KEY_TITLE);
//                String thought = documentSnapshot.getString(KEY_THOUGHT);
//
//                receivedTitleTextView.setText(title);
//                receivedThoughtTextView.setText(thought);
//            } else {
//                receivedTitleTextView.setText("");
//                receivedThoughtTextView.setText("");
//            }
//        });

        collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
                return;
            }

            if (queryDocumentSnapshots != null) {
                StringBuilder titles = new StringBuilder();
                StringBuilder thoughts = new StringBuilder();

                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Journal journal = snapshot.toObject(Journal.class);
                    titles.append(journal.getTitle()).append("\n");
                    thoughts.append(journal.getThought()).append("\n");
                }

                receivedTitleTextView.setText(titles.toString());
                receivedThoughtTextView.setText(thoughts.toString());
            }
        });
    }

    private void addThought() {
        String title = titleEditText.getText().toString().trim();
        String thought = thoughtEditText.getText().toString().trim();

        Journal journal = new Journal();
        journal.setTitle(title);
        journal.setThought(thought);

        collectionReference.add(journal);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_title_button) {
            updateTitle();
        } else if (view.getId() == R.id.update_thought_button) {
            updateThought();
        } else if (view.getId() == R.id.delete_title_and_thought_button) {
            deleteTitleAndThought();
        }
    }

    private void updateTitle() {
        String title = titleEditText.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);

        documentReference.update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(MainActivity.this, "Updated!",
                            Toast.LENGTH_LONG)
                            .show();
        }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Not updated!",
                                    Toast.LENGTH_LONG)
                            .show();
        });
    }

    private void updateThought() {
        String thought = thoughtEditText.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_THOUGHT, thought);

        documentReference.update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(MainActivity.this, "Thought updated!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Failed to update thought!", Toast.LENGTH_LONG).show();
                });
    }

    private void getThoughts() {
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    StringBuilder titles = new StringBuilder();
                    StringBuilder thoughts = new StringBuilder();

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Journal journal = snapshot.toObject(Journal.class);
                        titles.append(journal.getTitle()).append("\n");
                        thoughts.append(journal.getThought()).append("\n");
                    }

                    receivedTitleTextView.setText(titles.toString());
                    receivedThoughtTextView.setText(thoughts.toString());
                })
                .addOnFailureListener(e -> {

                });
    }

    public void deleteTitleAndThought() {
//        Map<String, Object> data = new HashMap<>();
//        data.put(KEY_TITLE, FieldValue.delete());
//        data.put(KEY_THOUGHT, FieldValue.delete());
//        documentReference.update(data);

        documentReference.delete();
    }

    public void deleteThought() {
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_THOUGHT, FieldValue.delete());
        documentReference.update(data);
    }
}