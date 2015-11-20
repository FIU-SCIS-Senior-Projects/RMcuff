package marc.example.com.rmcuffv1_patient;

/**
 * Created by Davidb on 10/12/15.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ComplexPreferences {

    private static final String LOG_TAG = ComplexPreferences.class.getSimpleName();
    private static ComplexPreferences complexPreferences;
    //private final Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson GSON = new Gson();
    //Type T = new TypeToken<Object>(){}.getType();

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "defaultFile";
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();

        //Log.d(LOG_TAG, "LOADED PREFS FROM: " + namePreferences);
    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences, int mode) {
        if (complexPreferences == null) {
            complexPreferences = new ComplexPreferences(context,
                    namePreferences, mode);
        }
        Log.d(LOG_TAG, "LOADED PREFS FROM: " + complexPreferences + " : " + namePreferences);

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putString(key, GSON.toJson(object)).commit();

        Log.d(LOG_TAG, "STORING: " + GSON.toJson(object).toString());
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        }
        else {
            try {
                Log.d(LOG_TAG, "RETRIEVING: " + GSON.fromJson(gson, a).toString());
                return GSON.fromJson(gson, a);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "
                        + key + " is instance of other class");
            }
        }
    }



    public void removeObject(String key) {
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.remove(key);

        commit();
        Log.d(LOG_TAG, "REMOVING: " + key);
    }


    /*
    private void recalculateCaregivers(int position) {
        int count = getCount();

        for (int i = position + 1; i < count; i++) {
            Caregiver c = getObject(String.valueOf(i), Caregiver.class);
            c.setUserID(position);
            putObject(String.valueOf(position), c);
        }
    }
    */

    public void setCount(int value) {
        editor.putInt("userCount", value).commit();
    }

    public int getCount() {
        return preferences.getInt("userCount", 0);
    }

    public void wipePreferences(Context context, String fileName) {
        SharedPreferences wipePreferences = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor wipeEditor = wipePreferences.edit();
        wipeEditor.clear().commit();

        Log.d(LOG_TAG, "WIPING: " + wipePreferences.toString());
    }

    public String getAll()
    {
        return preferences.getAll().toString() ;
    }
}