package com.jkrajniak.almamenu.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.adapters.AlmaMenuPagerAdapter;
import com.jkrajniak.almamenu.preferences.AppPreferences;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private AlmaMenuPagerAdapter adapterViewPager;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mySwipeRefreshLayout != null) {
                mySwipeRefreshLayout.setRefreshing(false);
            }
            if (adapterViewPager != null)
                adapterViewPager.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTabs();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("dataReceived"));

        AppPreferences appPreferences = new AppPreferences(this);
        boolean firstTime = (appPreferences.sp.getBoolean(AppPreferences.ALMA_FIRSTRUN, true)
                && !appPreferences.hasData());
        if (firstTime) {
            SharedPreferences.Editor editor = appPreferences.sp.edit();
            editor.putBoolean(AppPreferences.ALMA_FIRSTRUN, false);
            editor.commit();
            Intent splashScreen = new Intent(MainActivity.this, SplashScreen.class);
            this.startActivity(splashScreen);
        }

        scheduleAlarm();
    }

    private void setupTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.view_pager);
        adapterViewPager = new AlmaMenuPagerAdapter(
                getSupportFragmentManager(), this);
        pager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_refresh:
                Intent i = new Intent(this, ALMAMenuPullService.class);
                startService(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), ALMAMenuPullServiceAlarm.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                this,
                ALMAMenuPullServiceAlarm.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup Periodic update the ALMA menu every 2 hours.
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                firstMillis,
                AppPreferences.UPDATE_FREQUENCE,
                pIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("dataReceived"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
