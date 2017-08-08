package com.jkrajniak.almamenu.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.activities.AlmaMenuWidgetConfigureActivity;
import com.jkrajniak.almamenu.almaparser.AlmaMenu;
import com.jkrajniak.almamenu.almaparser.AlmaMenuItem;
import com.jkrajniak.almamenu.preferences.AppPreferences;

import java.util.ArrayList;

/**
 * Created by jakub on 28.01.17.
 */
public class AlmaMenuWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final int appWidgetId;
    private final int almaId;
    private final Context context;
    private final AppPreferences appPref;

    private final ArrayList<AlmaMenuItem> almaMenuItemList;

    public AlmaMenuWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        almaId = AlmaMenuWidgetConfigureActivity.getAlmaId(applicationContext, appWidgetId);
        context = applicationContext;
        appPref = new AppPreferences(applicationContext);

        almaMenuItemList = new ArrayList<AlmaMenuItem>();
        loadData();
    }

    private void loadData() {
        ArrayList<AlmaMenu> almaMenus = appPref.getAlmaMenu(almaId);
        almaMenuItemList.clear();
        if (almaMenus.size() > 0) {
            ArrayList<AlmaMenuItem> alms = almaMenus.get(0).menuItems;
            if (alms.size() == 0) {
                AlmaMenuItem alm = new AlmaMenuItem();
                alm.name = context.getResources().getString(R.string.closed);
                alm.isVeggie = false;
                alm.price = "";
                almaMenuItemList.add(alm);
            } else {
                almaMenuItemList.addAll(alms);
            }
        }
    }

    @Override
    public void onCreate() {
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return almaMenuItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(),
                R.layout.item_almamenuitem_widget);
        
        AlmaMenuItem almaMenuItem = almaMenuItemList.get(position);
        remoteView.setTextViewText(R.id.tvName_widget, almaMenuItem.name);
        remoteView.setTextColor(R.id.tvName_widget, Color.BLACK);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
