package com.example.earthquakewatcher.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.earthquakewatcher.Model.Earthquake;
import com.example.earthquakewatcher.R;
import com.example.earthquakewatcher.UI.CustomInfoWindow;
import com.example.earthquakewatcher.Util.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.earthquakewatcher.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue queue;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private BitmapDescriptor[] iconColors;
    private ActivityMapsBinding binding;
    private Button showListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        showListButton = findViewById(R.id.showListButton);

        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, QuakesListActivity.class));
            }
        });

        iconColors = new BitmapDescriptor[] {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
        };


        queue = Volley.newRequestQueue(this);

        getEarthquakes();
    }

    // Get all Earthquakes
    private void getEarthquakes() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.URL,
                null,
                response -> {
                    try {
                        JSONArray features = response.getJSONArray("features");
                        int count = Math.min(features.length(), Constants.LIMIT);
                        
                        for (int i = 0; i < count; i++) {
                            Earthquake earthquake = new Earthquake();
                            // Get properties
                            JSONObject properties = features.getJSONObject(i)
                                    .getJSONObject("properties");

                            // Get geometry object
                            JSONObject geometry = features.getJSONObject(i)
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

                            checkAndAddEarthquakeMarker(earthquake);
                        }
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

    private void checkAndAddEarthquakeMarker(Earthquake earthquake) {
        // First check if the earthquake has nearby cities
        JsonObjectRequest detailRequest = new JsonObjectRequest(
                Request.Method.GET,
                earthquake.getDetailLink(),
                null,
                response -> {
                    try {
                        JSONObject properties = response.getJSONObject("properties");
                        JSONObject products = properties.getJSONObject("products");

                        if (products.has("nearby-cities")) {
                            Object nearbyCities = products.get("nearby-cities");
                            
                            if (nearbyCities instanceof JSONArray) {
                                JSONArray nearbyCitiesArray = (JSONArray) nearbyCities;
                                if (nearbyCitiesArray.length() > 0) {
                                    // Only add marker if we have nearby cities
                                    addEarthquakeMarker(earthquake);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", "Error checking nearby cities: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("NetworkError", "Error checking nearby cities: " + error.getMessage());
                });

        queue.add(detailRequest);
    }

    private void addEarthquakeMarker(Earthquake earthquake) {
        // Format the date
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(earthquake.getTime()));

        Random random = new Random();
        // Create marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(iconColors[random.nextInt(iconColors.length)]);
        markerOptions.title(earthquake.getPlace());
        markerOptions.position(new LatLng(earthquake.getLat(), earthquake.getLon()));
        markerOptions.snippet("Magnitude: " + earthquake.getMagnitude() + "\n" +
                "Date: " + formattedDate);

        // Add circle to markers that have mag > x
        if (earthquake.getMagnitude() >= 2.0) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(earthquake.getLat(), earthquake.getLon()));
            circleOptions.radius(3000);
            circleOptions.strokeWidth(3.6f);
            circleOptions.fillColor(Color.RED);
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addCircle(circleOptions);
        }

        // Add marker to map
        Marker marker = mMap.addMarker(markerOptions);
        if (marker != null) {
            marker.setTag(earthquake.getDetailLink());

        }

        // Log earthquake details for debugging
        Log.d("Earthquake", "Added: " + earthquake.getPlace() + 
                " (Magnitude: " + earthquake.getMagnitude() + ")");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Location: ", location.toString());
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {
                LocationListener.super.onLocationChanged(locations);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }
        };

        if (
                (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener);

        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener);
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener);
            }
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        // API used changes response format
        getEarthquakeDetails(marker.getTag().toString());
//        Toast.makeText(getApplicationContext(), marker.getTag().toString(),
//                        Toast.LENGTH_LONG)
//                .show();
    }

    private void getEarthquakeDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject properties = response.getJSONObject("properties");
                        JSONObject products = properties.getJSONObject("products");

                        if (products.has("nearby-cities")) {
                            Object nearbyCities = products.get("nearby-cities");

                            if (nearbyCities instanceof JSONArray) {
                                JSONArray nearbyCitiesArray = (JSONArray) nearbyCities;
                                for (int i = 0; i < nearbyCitiesArray.length(); i++) {
                                    JSONObject nearbyCity = nearbyCitiesArray.getJSONObject(i);
                                    JSONObject contents = nearbyCity.getJSONObject("contents");
                                    JSONObject nearbyCitiesJson = contents.getJSONObject("nearby-cities.json");
                                    String nearbyCitiesUrl = nearbyCitiesJson.getString("url");
                                    
                                    getMoreDetails(nearbyCitiesUrl);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", "Error parsing earthquake details", e);
                    }
                },
                error -> {
                    Log.e("NetworkError", "Error fetching earthquake details", error);
                });

        queue.add(jsonObjectRequest);
    }

    public void getMoreDetails(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                    View view = getLayoutInflater().inflate(R.layout.popup, null);

                    Button dismissButton = view.findViewById(R.id.dismissPop);
                    Button dismissButtonTop = view.findViewById(R.id.dismissPopTop);
                    TextView popList = view.findViewById(R.id.popList);
                    WebView htmlPop = view.findViewById(R.id.htmlWebView);

                    StringBuilder stringBuilder = new StringBuilder();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject citiesObject = response.getJSONObject(i);

                            if (citiesObject.has("tectonicSummary")) {
                                if (citiesObject.getString("tectonicSummary") != null) {
                                    JSONObject tectonic = citiesObject.getJSONObject("tectonicSummary");
                                    if (tectonic.has("text") && tectonic.getString("text'") != null) {
                                       String text = tectonic.getString("text");
                                        htmlPop.loadDataWithBaseURL(null,
                                                text,
                                                "text/html",
                                                "UTF-8",
                                                null);
                                    }
                                };
                            }

                                stringBuilder.append("City: " + citiesObject.getString("name")
                                        + "\n" + "Distance: " + citiesObject.getString("distance")
                                        + "\n" + "Population: " + citiesObject.getString("population"));


                            stringBuilder.append("\n\n");
                        }

                        popList.setText(stringBuilder);

                        dismissButton.setOnClickListener(view1 -> {
                            dialog.dismiss();
                        });

                        dismissButtonTop.setOnClickListener(view2 -> {
                            dialog.dismiss();
                        });

                        dialogBuilder.setView(view);
                        dialog = dialogBuilder.create();
                        dialog.show();
                    } catch (JSONException e) {
                        Log.e("JSONError", "Error parsing city details", e);
                    }
                },
                error -> {
                    Log.e("NetworkError", "Error fetching city details", error);
                    Toast.makeText(this, "Failed to load city details", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonArrayRequest);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}