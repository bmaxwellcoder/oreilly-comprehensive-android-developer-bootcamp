package com.example.mymapsapp;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "Maps";
    private GoogleMap mMap;
    private LatLng mountEverest  = new LatLng ( 28.001377,  86.928129);
    private LatLng mountKilimanjaro = new LatLng( -3.075558, 37.344363);
    private LatLng alps = new LatLng(47.368955, 9.702579);

    // Todo: Create Markers for each mountain
    private MarkerOptions everestMarkerOptions;
    private MarkerOptions kilimanjaroMarkerOptions;
    private MarkerOptions alpsMarkerOptions;

    private List<MarkerOptions> markerOptionsList;
    private ArrayList<Marker> markerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        everestMarkerOptions = new MarkerOptions()
                .position(mountEverest)
                .title("Mt. Everest")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        kilimanjaroMarkerOptions = new MarkerOptions()
                .position(mountKilimanjaro)
                .title("Mt. Kilimanjaro")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


        alpsMarkerOptions = new MarkerOptions()
                .position(alps)
                .title("The Alps")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        // Add a marker in Sydney and move the camera
        LatLng binga = new LatLng(-19.7766655,33.0413444);
        LatLng sydney = new LatLng(-34, 151);
//         mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
         mMap.addMarker(new MarkerOptions()
                .position(binga)
                .title("Marker in Binga")
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                 .alpha(0.5f)
         );

        markerOptionsList = new ArrayList<>();
        markerOptionsList.add(everestMarkerOptions);
        markerOptionsList.add(kilimanjaroMarkerOptions);
        markerOptionsList.add(alpsMarkerOptions);

//         markerArrayList = new ArrayList<>();
//
//         markerArrayList.add();
//         markerArrayList.add();
//         markerArrayList.add();

         for (MarkerOptions options : markerOptionsList) {
             LatLng latLng = new LatLng(options.getPosition().latitude, options.getPosition().longitude);
             mMap.addMarker(options);
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
//             Log.d(TAG, "onMapReady: " + marker.getTitle());
         }

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(binga, 8));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(binga));
    }
} 