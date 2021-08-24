package com.thedramaticcolumnist.app.Model.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class mSharedPreference {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    public mSharedPreference(Context mContext) {
        this.sharedPreferences = mContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.APP_SHARED_PREFS = "EasyButor";
    }

    public void clearAllPreferences() {
        editor.clear();
        editor.commit();
    }

    public void clearPreferences(String key) {
        editor.remove(key);
        editor.apply();

    }


    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }
}
