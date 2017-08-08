package com.jkrajniak.almamenu.activities;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.almaparser.AlmaMenu;
import com.jkrajniak.almamenu.almaparser.AlmaParser;
import com.jkrajniak.almamenu.preferences.AppPreferences;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task to pull current ALMA
 * menu.
 */
public class ALMAMenuPullService extends IntentService {

    public ALMAMenuPullService() {
        super("ALMAMenuPullService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Intent dataRefreshing = new Intent("dataRefreshing");
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataRefreshing);

        SharedPreferences.Editor spEditor = AppPreferences.getPreferences(getApplicationContext()).edit();
        Gson gson = new Gson();

        int almaIndex = 0;
        for (String almaUrl : AppPreferences.ALMA_URLS) {
            try {
                ArrayList<AlmaMenu> menu = AlmaParser.parse(almaUrl, almaIndex);
                if (menu.size() > 0) {
                    String prefKey = String.format("ALMA_%d", almaIndex);
                    spEditor.putString(prefKey, gson.toJson(menu));
                }
            } catch (IOException | URISyntaxException | ParseException e) {
                e.printStackTrace();
            }
            almaIndex++;
        }

        long unixTime = System.currentTimeMillis() / 1000L;
        spEditor.putString(AppPreferences.ALMA_LASTUPDATE, String.format("%d", unixTime));
        spEditor.commit();

        Intent dataReceived = new Intent("dataReceived");
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataReceived);
    }
}
