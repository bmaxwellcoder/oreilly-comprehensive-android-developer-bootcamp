package com.example.contactmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contactmanager.data.DatabaseHandler;
import com.example.contactmanager.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        contactArrayList = new ArrayList<>();

        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        // Clear database
//        databaseHandler.clearDatabase();

//        databaseHandler.addContact(new Contact("James", "213986"));
//        databaseHandler.addContact(new Contact("Greg", "098765"));
//        databaseHandler.addContact(new Contact("Helena", "40678765"));
//        databaseHandler.addContact(new Contact("Carimo", "768345"));
//
//
//        databaseHandler.addContact(new Contact("Silo", "3445"));
//        databaseHandler.addContact(new Contact("Santos", "6687"));
//        databaseHandler.addContact(new Contact("Litos", "5344"));
//        databaseHandler.addContact(new Contact("Karate", "96534"));
//        databaseHandler.addContact(new Contact("Guerra", "158285"));
//        databaseHandler.addContact(new Contact("Gema", "78130"));

        List<Contact> contactList = databaseHandler.getAllContacts();
        for (Contact contact : contactList) {
            Log.d("MainActivity", "onCreate: " + contact.getName());
            contactArrayList.add(contact.getName());
        }

        // create array adapter
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                contactArrayList
        );

        // add to our listview
        listView.setAdapter(arrayAdapter);

        // Attach eventListener to listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("List", "onItemClick: " + contactArrayList.get(position));
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}