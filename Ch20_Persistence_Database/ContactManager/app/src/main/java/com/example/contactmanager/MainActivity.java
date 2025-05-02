package com.example.contactmanager;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contactmanager.data.DatabaseHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

        // Clear database
        databaseHandler.clearDatabase();

        // Create contact object first
        Contact jeremy = new Contact();
        jeremy.setName("Jeremy");
        jeremy.setPhoneNumber("5551234567");

        Contact jason = new Contact();
        jason.setName("Jason");
        jason.setPhoneNumber("5553210987");

        databaseHandler.addContact(jeremy);
        databaseHandler.addContact(jason);

        Log.d("Count", "onCreate: " + databaseHandler.getCount());

        // Get 1 contact
//        Contact c = databaseHandler.getContact(1);
//        c.setName("NewJeremy");
//        c.setPhoneNumber("5555551234");

//        int updateRow = databaseHandler.updateContact(c);
//        Log.d("RowId", "onCreate: " + updateRow);
//
//        Log.d("Main", "onCreate: " + c.getName() + ", " + c.getPhoneNumber());

//        databaseHandler.deleteContact(c);
        // once deleted, do not try to extract

        List<Contact> contactList = databaseHandler.getAllContacts();
        for (Contact contact : contactList) {
            Log.d("MainActivity", "onCreate: " + contact.getName());
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}