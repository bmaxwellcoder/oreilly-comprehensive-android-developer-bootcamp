package com.example.earthquakewatcher;

import android.app.Application;
import android.util.Log;

import com.example.earthquakewatcher.Util.ApiKeyLoader;
import com.example.earthquakewatcher.Util.MapUtils;

/**
 * Custom Application class to initialize components early in the app lifecycle
 */
public class EarthquakeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Google Maps API key from properties file
        // This ensures the API key is loaded as early as possible
        try {
            MapUtils.initializeApiKey(getApplicationContext());
            String apiKey = ApiKeyLoader.getApiKey(getApplicationContext());
            if (apiKey != null && !apiKey.isEmpty()) {
                Log.d("EarthquakeApp", "API Key initialized successfully");
            } else {
                Log.e("EarthquakeApp", "Failed to load API key - key is empty");
            }
        } catch (Exception e) {
            Log.e("EarthquakeApp", "Error initializing API key: " + e.getMessage());
        }
    }
}