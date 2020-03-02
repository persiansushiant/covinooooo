package ir.technopedia.covino.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by uTheLoneWolf on 5/11/2016.
 */

public class SharedPreferencesManager {
    private static final String MY_APP_PREFERENCES = "ca7eed88-2409-4de7-b529-52598af76734";

    private SharedPreferences sharedPrefs;
    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        sharedPrefs = context.getApplicationContext().getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesManager(context);
        return instance;
    }

    public boolean getBooleanValue(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return sharedPrefs.getString(key, "");
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
