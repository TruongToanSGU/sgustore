package com.sgulab.thongtindaotao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sgulab.thongtindaotao.R;

public class SplashActivity extends BaseActivity {

  private Button btLogin;
  private Button btGuest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    btLogin = (Button) findViewById(R.id.button_splash_account);
    btGuest = (Button) findViewById(R.id.button_splash_guest);

    btLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
      }
    });

    btGuest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    });
  }
}
