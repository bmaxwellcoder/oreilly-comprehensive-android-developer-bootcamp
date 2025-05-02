package com.example.babyneeds;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddedItemActivity extends AppCompatActivity {
    private EditText itemNameEditText;
    private EditText itemQuantityEditText;
    private EditText itemColorEditText;
    private EditText itemSizeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        itemNameEditText = findViewById(R.id.enterItem_editText);
        itemQuantityEditText = findViewById(R.id.quantity_editText);
        itemColorEditText = findViewById(R.id.color_editText);
        itemSizeEditText = findViewById(R.id.size_editText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}