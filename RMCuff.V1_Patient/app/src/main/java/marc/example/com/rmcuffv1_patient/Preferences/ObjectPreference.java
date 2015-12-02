package marc.example.com.rmcuffv1_patient.Preferences;

import android.app.Application;

public class ObjectPreference extends Application {
    private static final String LOG_TAG = "ObjectPreference";
    private ComplexPreferences complexPrefenreces;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void createNewComplexFile(String fileName) {
        complexPrefenreces = ComplexPreferences.getComplexPreferences(getBaseContext(), fileName, MODE_PRIVATE);
        android.util.Log.i(LOG_TAG, "PREFERENCE LOADED: " + fileName);
    }

    public ComplexPreferences getComplexPreference() {
        if (complexPrefenreces != null) {
            return complexPrefenreces;
        }
        return null;
    }
}