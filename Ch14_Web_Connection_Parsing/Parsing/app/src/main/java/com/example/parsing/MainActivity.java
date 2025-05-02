package com.example.parsing;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //         https://jsonplaceholder.typicode.com/todos/1
//    private RequestQueue requestQueue;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        requestQueue = Volley.newRequestQueue(this);
        queue = ValleySingleton.getInstance(this.getApplicationContext())
                .getRequestQueue(); // gets one request queue


        // JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (
                        Request.Method.GET,
                        "https://jsonplaceholder.typicode.com/todos/1",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("JSON Obj response", "onResponse: " + response.getString("title"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON Obj error", "onErrorResponse: " + error.getMessage());
                            }
                        });

        // JSON Array Request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        "https://jsonplaceholder.typicode.com/todos",
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        Log.d
                                                ("JSON Arr response ",
                                                        "onResponse: "
                                                                + jsonObject.getString("id") + " " // can also use getInt()
                                                                + jsonObject.getString("title")
                                                );
                                       boolean d =  jsonObject.getBoolean("completed");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON Arr error", "onErrorResponse: " + error.getMessage());
                            }
                        }
                );
//        requestQueue.add(jsonObjectRequest);
//        requestQueue.add(jsonArrayRequest);
        queue.add(jsonArrayRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}