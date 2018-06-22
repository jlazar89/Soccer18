package com.bluetag.wc.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Dream Land on 2/27/2018.
 */

public class Settings {

    public static void setSettings(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getSettings(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, true);
    }

}
