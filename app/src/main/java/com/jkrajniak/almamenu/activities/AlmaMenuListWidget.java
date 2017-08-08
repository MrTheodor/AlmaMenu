package com.jkrajniak.almamenu.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.activities.AlmaMenuWidgetConfigureActivity;
import com.jkrajniak.almamenu.almaparser.AlmaMenu;
import com.jkrajniak.almamenu.preferences.AppPreferences;
import com.jkrajniak.almamenu.services.AlmaMenuWidgetRemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class AlmaMenuListWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alma_menu_list_widget);
        views.setTextViewText(
                R.id.appwidget_text,
                AppPreferences.ALMA_NAMES[AlmaMenuWidgetConfigureActivity.getAlmaId(context, appWidgetId)]);

        Intent serviceIntent = new Intent(context, AlmaMenuWidgetRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.almaMenuWidgetList, serviceIntent);

        AppPreferences appPref = new AppPreferences(context);
        ArrayList<AlmaMenu> almaMenus = appPref.getAlmaMenu(AlmaMenuWidgetConfigureActivity.getAlmaId(context, appWidgetId));
        if (almaMenus.size() > 0) {
            AlmaMenu almaMenu = almaMenus.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM");
            views.setTextViewText(R.id.appwidget_date, sdf.format(almaMenu.date));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alma_menu_list_widget);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

