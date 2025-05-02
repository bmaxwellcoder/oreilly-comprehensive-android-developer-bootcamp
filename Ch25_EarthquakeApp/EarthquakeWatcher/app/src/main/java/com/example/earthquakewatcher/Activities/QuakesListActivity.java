package com.example.earthquakewatcher.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.earthquakewatcher.Model.Earthquake;
import com.example.earthquakewatcher.R;
import com.example.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    private List<Earthquake> earthquakeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quakes_list);

        earthquakeList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        queue = Volley.newRequestQueue(this);

        arrayList = new ArrayList<>();

        getAllQuakes(Constants.URL);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void getAllQuakes(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.URL,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("features");

                        for (int i = 0; i < Constants.LIMIT; i++) {
                            Earthquake earthquake = new Earthquake();
                            // Get properties
                            JSONObject properties = jsonArray.getJSONObject(i)
                                    .getJSONObject("properties");

                            // Get geometry object
                            JSONObject geometry = jsonArray.getJSONObject(i)
                                    .getJSONObject("geometry");

                            // Get coordinates array
                            JSONArray coordinates = geometry.getJSONArray("coordinates");

                            double lon = coordinates.getDouble(0);
                            double lat = coordinates.getDouble(1);

                            // Set earthquake properties
                            earthquake.setMagnitude(properties.getDouble("mag"));
                            earthquake.setPlace(properties.getString("place"));
                            earthquake.setTime(properties.getLong("time"));
                            earthquake.setDetailLink(properties.getString("detail"));
                            earthquake.setType(properties.getString("types"));
                            earthquake.setLat(lat);
                            earthquake.setLon(lon);

                            arrayList.add(earthquake.getPlace());

                        }

                        arrayAdapter = new ArrayAdapter<>(
                                QuakesListActivity.this,
                                android.R.layout.simple_list_item_1,
                        android.R.id.text1, arrayList);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Toast.makeText(getApplicationContext(),
                                        "Clicked " + position,
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                        arrayAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("JSONError", "Error parsing earthquake data: " + e.getMessage());
                        Toast.makeText(this, "Error loading earthquake data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("NetworkError", "Error fetching earthquake data: " + error.getMessage());
                    Toast.makeText(this, "Error connecting to earthquake service", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }
}