package com.example.testlocation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.testlocation.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
// TODO: fix issue that causes location to only update after app is restarted
    private static final int ALL_PERMISSIONS_RESULT = 1111;
    private GoogleApiClient client;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private TextView locationTextView;
    private LocationRequest locationRequest;

    public static final long UPDATE_INTERVAL = 5000;
    public static final long FASTEST_INTERVAL = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        locationTextView = findViewById(R.id.textview_first);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


        // let's add permission we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                // Check if we should show rationale
                if (shouldShowRequestPermissionRationale(permissionsToRequest.get(0))) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("This app needs location permissions to show your current location on the map")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsToRequest.toArray(
                                            new String[permissionsToRequest.size()]),
                                            ALL_PERMISSIONS_RESULT
                                    );
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                } else {
                    // Request permissions directly if we don't need to show rationale
                    requestPermissions(permissionsToRequest.toArray(
                            new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT
                    );
                }
            }
        }

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private void checkPlayServices() {
        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);


        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(MainActivity.this, "No services",
                                            Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        }
                    });
            errorDialog.show();
        } else {
            Toast.makeText(MainActivity.this, "All is good!",
                            Toast.LENGTH_LONG)
                    .show();
        }
        Log.d("GooglePlayServices", "Error Code: " + errorCode);
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // want to keep in case on lower vesion of Android
            return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        if (client != null) {
            client.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkPlayServices();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (client != null && client.isConnected()) {
            LocationServices.getFusedLocationProviderClient(this)
                    .removeLocationUpdates(new LocationCallback() {

                    });
            client.disconnect();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            locationTextView.setText(MessageFormat.format(
                    "Latitude: {0}, Longitude: {1}",
                    location.getLatitude(), location.getLongitude()));
            // Get last known location.
            Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: "
                    + location.getLongitude());
    }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (
                (ActivityCompat.checkSelfPermission
                        (
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission
                        (
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED)
        ) {
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        locationTextView.setText(MessageFormat.format(
                                "Latitude: {0}, Longitude: {1}",
                                location.getLatitude(), location.getLongitude()));
                        // Get last known location.
                        Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    }
                });

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (
                (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        ) {

            Toast.makeText(this, "You need to " +
                    "enable permission to display location",
                    Toast.LENGTH_LONG)
                    .show();
        }

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    locationTextView.setText(MessageFormat.format(
                            "Latitude: {0}, Longitude: {1}",
                            location.getLatitude(), location.getLongitude()));

                    Log.d("Location", "Latitude: "
                            + location.getLatitude() + ", Longitude: "
                            + location.getLongitude());
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        }, null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perm : permissionsToRequest) {
                if (!hasPermission(perm)) {
                    permissionsRejected.add(perm);
                }
            }

            if (permissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("These permissions are mandatory to display location")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(permissionsRejected.toArray(
                                                    new String[permissionsRejected.size()]),
                                                    ALL_PERMISSIONS_RESULT);
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                }
            } else {
                if (client != null) {
                    client.connect();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}