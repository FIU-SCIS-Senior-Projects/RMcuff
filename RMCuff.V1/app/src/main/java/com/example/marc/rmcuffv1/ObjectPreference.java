package com.example.marc.rmcuffv1;

/**
 * Created by Davidb on 9/23/15.
 */

import android.app.Application;
import android.content.Context;

public class ObjectPreference extends Application {
    private static final String LOG_TAG = "ObjectPreference";
    private ComplexPreferences complexPrefenreces;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void createNewComplexFile(String fileName) {
        complexPrefenreces = complexPrefenreces.getComplexPreferences(getBaseContext(), fileName, MODE_PRIVATE);
        android.util.Log.i(LOG_TAG, "PREFERENCE LOADED: " + fileName);
    }

    public ComplexPreferences getComplexPreference() {
        if(complexPrefenreces != null) {
            return complexPrefenreces;
        }
        return null;
    }
}