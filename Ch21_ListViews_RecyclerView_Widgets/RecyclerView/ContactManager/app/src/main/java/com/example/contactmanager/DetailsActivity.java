package com.example.contactmanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {
    private TextView detailsName; // TextView to display the name
    private TextView detailsPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        detailsName = findViewById(R.id.details_name_textView);
        detailsPhone = findViewById(R.id.details_phone_textView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String name = bundle.getString("name");
            String phone = bundle.getString("number");

            detailsName.setText(name);
            detailsPhone.setText(phone);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}