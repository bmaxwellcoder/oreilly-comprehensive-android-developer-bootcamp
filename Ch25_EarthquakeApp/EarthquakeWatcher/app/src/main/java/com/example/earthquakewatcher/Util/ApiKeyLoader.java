package com.example.earthquakewatcher.Util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiKeyLoader {
    private static final String TAG = "ApiKeyLoader";
    private static final String PROPERTIES_FILE = "apikeys.properties";
    private static final String API_KEY_PROPERTY = "MAPS_API_KEY";

    public static String getApiKey(Context context) {
        Properties properties = new Properties();
        try {
            // Try to load from assets folder
            InputStream inputStream = context.getAssets().open(PROPERTIES_FILE);
            properties.load(inputStream);
            String apiKey = properties.getProperty(API_KEY_PROPERTY);

            if (apiKey == null || apiKey.isEmpty()) {
                // If property is not found, try using the raw value from file
                inputStream = context.getAssets().open(PROPERTIES_FILE);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                apiKey = new String(buffer).trim();
            }

            return apiKey;
        } catch (IOException e) {
            Log.e(TAG, "Failed to load API key from properties file", e);
            return "";
        }
    }
}