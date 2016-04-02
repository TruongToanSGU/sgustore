package com.sgulab.thongtindaotao.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.LinkedList;

public class MySharedPrefer {

    private static final String SHARED_PREF_LOGIN_USING_ACCOUNT = "SHARED_PREF_LOGIN_USING_ACCOUNT";
    private static final String SHARED_PREF_ACCOUNT_NAME = "SHARED_PREF_ACCOUNT_NAME";
    private static Helper sharedPreferencesHelper;

    private static MySharedPrefer INSTANCE;

    private MySharedPrefer(Helper helper) {
        sharedPreferencesHelper = helper;
    }

    public static MySharedPrefer getInstance(Application application) {
        if (sharedPreferencesHelper == null) {
            sharedPreferencesHelper = new Helper(PreferenceManager.getDefaultSharedPreferences(application));
            INSTANCE = new MySharedPrefer(sharedPreferencesHelper);
        }
        return INSTANCE;
    }

    static class Helper {
        private SharedPreferences sharedPreferences;

        public Helper(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        public void set(String key, String value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public void set(String key, boolean value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }

        public void set(String key, int value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }

        public void set(String key, long value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }

        public void set(String key, float value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            editor.apply();
        }

        public String get(String key, String defaultValue) {
            return sharedPreferences.getString(key, defaultValue);
        }

        public boolean get(String key, boolean defaultValue) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }

        public int get(String key, int defaultValue) {
            return sharedPreferences.getInt(key, defaultValue);
        }

        public float get(String key, float defaultValue) {
            return sharedPreferences.getFloat(key, defaultValue);
        }

        public long get(String key, long defaultValue) {
            return sharedPreferences.getLong(key, defaultValue);
        }

        public void removeKey(String key) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }

        public void clear() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public void setSharedPrefLoginUsingAccount(boolean isUse) {
        sharedPreferencesHelper.set(SHARED_PREF_LOGIN_USING_ACCOUNT, isUse);
    }

    public boolean getSharedPrefLoginUsingAccount() {
        return sharedPreferencesHelper.get(SHARED_PREF_LOGIN_USING_ACCOUNT, false);
    }

    public void setSharedPrefAccountName(String name) {
        sharedPreferencesHelper.set(SHARED_PREF_ACCOUNT_NAME, name);
    }

    public String getSharedPrefAccountName() {
        return sharedPreferencesHelper.get(SHARED_PREF_ACCOUNT_NAME, "Unknown");
    }

}
