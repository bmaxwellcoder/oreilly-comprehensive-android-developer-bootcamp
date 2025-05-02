package com.example.nodo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class NewNoDoActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.reply";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_no_dao);

        editText = findViewById(R.id.edit_noDo);

        final Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String noDoString = editText.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, noDoString);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });


    }
}