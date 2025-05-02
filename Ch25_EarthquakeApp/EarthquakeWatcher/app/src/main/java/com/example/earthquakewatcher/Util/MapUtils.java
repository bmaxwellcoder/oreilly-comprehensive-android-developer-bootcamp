package com.example.earthquakewatcher.Util;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.lang.reflect.Field;

/**
 * Utility class for handling Google Maps API key initialization
 */
public class MapUtils {

    /**
     * Initialize Google Maps API key at runtime
     * This method uses reflection to set the API key programmatically
     * before the map view is created
     * 
     * @param context Application context to load the API key
     */
    public static void initializeApiKey(Context context) {
        try {
            // Get API key from properties file
            String apiKey = ApiKeyLoader.getApiKey(context);
            if (apiKey != null && !apiKey.isEmpty()) {
                // Use reflection to set the API key at runtime
                // This must be done before any MapView is created
                Field field = MapsInitializer.class.getDeclaredField("API_KEY");
                field.setAccessible(true);
                field.set(null, apiKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Alternative method to set the API key directly
     * on a specific MapView instance
     * 
     * @param mapView The MapView to initialize
     * @param context Application context to load the API key
     */
    public static void setApiKey(MapView mapView, Context context) {
        try {
            String apiKey = ApiKeyLoader.getApiKey(context);
            if (apiKey != null && !apiKey.isEmpty()) {
                Field field = mapView.getClass().getDeclaredField("mApiKey");
                field.setAccessible(true);
                field.set(mapView, apiKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}