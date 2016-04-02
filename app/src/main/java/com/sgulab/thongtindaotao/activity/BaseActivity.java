package com.sgulab.thongtindaotao.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.sgulab.thongtindaotao.utils.MySharedPrefer;

public class BaseActivity extends AppCompatActivity {
    protected final Context context = this;
    protected MySharedPrefer sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = MySharedPrefer.getInstance(getApplication());
    }
}
