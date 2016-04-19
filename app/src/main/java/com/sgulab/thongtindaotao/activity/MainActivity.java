package com.sgulab.thongtindaotao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.fragment.FitFragment;
import com.sgulab.thongtindaotao.fragment.InfoFragment;
import com.sgulab.thongtindaotao.fragment.MarkFragment;
import com.sgulab.thongtindaotao.fragment.MarkTermFragment;
import com.sgulab.thongtindaotao.fragment.SGUFragment;
import com.sgulab.thongtindaotao.fragment.WeekScheduleFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SGUFragment markView;
    private SGUFragment markTermView;
    private SGUFragment infoView;
    private SGUFragment weekScheduleView;
    private SGUFragment fitView;

    private SGUFragment currentFragment;

    private EditText mMssv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mMssv = (EditText) findViewById(R.id.ed_mssv);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        markView = new MarkFragment();
        markTermView = new MarkTermFragment();
        infoView = new InfoFragment();
        weekScheduleView = new WeekScheduleFragment();
        fitView = new FitFragment();

        configUserType();
        showInfoFragment();
    }

    private void configUserType() {
        if (sharedPreferences.getSharedPrefLoginUsingAccount()) {
            mMssv.setFocusable(false);
            mMssv.setText(sharedPreferences.getSharedPrefAccountName());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_go) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            currentFragment.onSearch(mMssv.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mark_all) {
            showMarkFragment();
        } else if (id == R.id.nav_mark_by_term) {
            showMarkTermFragment();
        } else if (id == R.id.nav_info) {
            showInfoFragment();
        } else if (id == R.id.nav_tkb_week) {
            showWeekScheduleFragment();
        }else if (id == R.id.nav_feed_fit) {
            showFitView();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFitView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, fitView);
        transaction.commitAllowingStateLoss();
        currentFragment = fitView;
    }

    private void showMarkFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, markView);
        transaction.commitAllowingStateLoss();
        currentFragment = markView;
        currentFragment.onShow(mMssv.getText().toString());
    }

    private void showMarkTermFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, markTermView);
        transaction.commitAllowingStateLoss();
        currentFragment = markTermView;
        currentFragment.onShow(mMssv.getText().toString());
    }

    private void showInfoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, infoView);
        transaction.commitAllowingStateLoss();
        currentFragment = infoView;
        currentFragment.onShow(mMssv.getText().toString());
    }

    private void showWeekScheduleFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, weekScheduleView);
        transaction.commitAllowingStateLoss();
        currentFragment = weekScheduleView;
        currentFragment.onShow(mMssv.getText().toString());
    }
}
