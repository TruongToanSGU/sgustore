package com.sgulab.thongtindaotao.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.utils.CmUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A login screen that offers login via mssv/password.
 */
public class LoginActivity extends BaseActivity{

  // UI references.
  private AutoCompleteTextView mEmailView;
  private EditText mPasswordView;
  private View mProgressView;
  private View mLoginFormView;

  private WebView webView;

  private AtomicInteger step = new AtomicInteger();
  private String mssv;
  private String password;

  interface LoginHandler {
    void onSuccess(String name);
    void onFail();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // Set up the login form.
    mEmailView = (AutoCompleteTextView) findViewById(R.id.ed_mssv);
    webView = (WebView) findViewById(R.id.webView);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setLoadsImagesAutomatically(false);
    webView.setWebViewClient(new HelloWebViewClient());
    webView.addJavascriptInterface(new MyJavaScriptInterface(new LoginHandler() {
      @Override
      public void onSuccess(final String name) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            onLoginSuccess(name);
          }
        });
      }

      @Override
      public void onFail() {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            onLoginFail();
          }
        });
      }
    }), "HTMLOUT");

    CookieManager.getInstance().removeAllCookie();

    mPasswordView = (EditText) findViewById(R.id.password);
    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });

    Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
    mEmailSignInButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });

    mLoginFormView = findViewById(R.id.login_form);
    mProgressView = findViewById(R.id.login_progress);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private class HelloWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url)
    {
      super.onPageFinished(view, url);
      switch (step.getAndIncrement()) {
        case 0:
          String js = "javascript:(function(){" +
                  "username=document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtTaiKhoa');" +
                  "password=document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_txtMatKhau');" +
                  "btlogin=document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_btnDangNhap');" +
                  "username.value='" + mssv + "';" +
                  "password.value='" + password + "';" +
                  "e=document.createEvent('HTMLEvents');" +
                  "e.initEvent('click',true,true);" +
                  "btlogin.dispatchEvent(e);" +
                  "})()";
          webView.loadUrl(js);
          break;
        case 1:
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              webView.invalidate();
              webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
            }
          }, 1000);
          break;
      }
    }
  }

  class MyJavaScriptInterface
  {
    private LoginHandler mLoginHandler;

    public MyJavaScriptInterface(LoginHandler mLoginHandler) {
      this.mLoginHandler = mLoginHandler;
    }

    @JavascriptInterface
    public void processHTML(String html)
    {
      Document document = Jsoup.parse(html);
      final Element userLoginInfo = document.getElementById("ctl00_Header1_ucLogout_lblNguoiDung");
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (userLoginInfo == null || !userLoginInfo.hasText()) {
            mLoginHandler.onFail();
          } else {
            String userName = userLoginInfo.html();
            try {
              userName = userName.replace("Chào ", "");
              userName = userName.substring(0, userName.indexOf("(") - 1);
            } catch (Exception e) {
              e.printStackTrace();
            }
            mLoginHandler.onSuccess(userName);
          }
        }
      });
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  protected void onLoginSuccess(String name) {
    saveLoginData();
    sharedPreferences.setSharedPrefLoginUsingAccount(true);
    sharedPreferences.setSharedPrefAccountName(name);
    startActivity(new Intent(LoginActivity.this, MainActivity.class));
    finish();
  }

  protected void saveLoginData() {

  }

  protected void onLoginFail() {
    showProgress(false);
    new AlertDialog.Builder(this).setTitle("Login failed").setMessage("Please try again!").show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void attemptLogin() {

    if (!CmUtils.isOnline(context)) return;

    // Reset errors.
    mEmailView.setError(null);
    mPasswordView.setError(null);

    // Store values at the time of the login attempt.
    mssv = mEmailView.getText().toString();
    password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      mPasswordView.setError(getString(R.string.error_invalid_password));
      focusView = mPasswordView;
      cancel = true;
    }

    if (TextUtils.isEmpty(mssv)) {
      mEmailView.setError(getString(R.string.error_field_required));
      focusView = mEmailView;
      cancel = true;
    } else if (!isEmailValid(mssv)) {
      mEmailView.setError(getString(R.string.error_invalid_email));
      focusView = mEmailView;
      cancel = true;
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView.requestFocus();
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.

      showProgress(true);
      webView.loadUrl("http://thongtindaotao.sgu.edu.vn/");
      step.set(0);
    }
  }

  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
//    return mssv.contains("@");
    return true;
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      mLoginFormView.animate().setDuration(shortAnimTime).alpha(
          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }
}

