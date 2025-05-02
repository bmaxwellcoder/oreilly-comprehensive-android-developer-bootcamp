package com.example.makeitrain;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    //private Button showMoney;
//private Button showTag;
    private TextView moneyText;
    private int moneyCounter = 0;
    private Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.another_layout);
//        newButton = findViewById(R.id.button);
//        newButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("New button", "onClick: Hola");
//            }
//                                     });


        setContentView(R.layout.activity_main);
        moneyText = findViewById(R.id.money_text);
        EdgeToEdge.enable(this);
        // Used with the instance variables
//        showMoney = findViewById(R.id.button_make_rain);
//        showTag = findViewById(R.id.button_show_tag);

//        showMoney.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("MYTAG", "onClick: Show Money");
//            }
//        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void showTag(View view) { // Example of using a Toast
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT)
                .show();
        Log.d("MYTAG", "onClick: Show Money");
    }

    public void makeItRain(View view) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        moneyCounter += 1000;
        moneyText.setText(numberFormat.format(moneyCounter));
        // Get colors from colors.xml resource
//        moneyText.setTextColor(
//                moneyCounter >= 10_000 ?
//                        ContextCompat.getColor(this, R.color.green) :
//                        ContextCompat.getColor(this, R.color.black));
//        // show a message
//        if (moneyCounter >= 10_000) {
//            Toast.makeText(getApplicationContext(), "Wow, you are rich!" , Toast.LENGTH_LONG)
//                    .show();
//        }

        switch (moneyCounter) {
            case 20_000:
                moneyText.setTextColor(Color.BLACK);
                break;
            case 40_000:
                moneyText.setTextColor(Color.YELLOW);
                break;
            case 60_000:
                moneyText.setTextColor(Color.GREEN);
                break;
            default:
                break;
        }
        Log.d("MIR", "makeItRain: Tapped " + moneyCounter);
    }

}