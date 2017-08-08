package com.jkrajniak.almamenu.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.preferences.AppPreferences;

/**
 * The configuration screen for the {@link AlmaMenuListWidget AlmaMenuListWidget} AppWidget.
 */
public class AlmaMenuWidgetConfigureActivity extends Activity {

    public static final String PREFS_NAME = "com.jkrajniak.almamenu.activities.AlmaMenuWidget";
    public static final String PREF_PREFIX_KEY = "almaMenuWidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AlmaMenuWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            int almaId = (int) spinner.getSelectedItemId();
            saveAlmaId(context, mAppWidgetId, almaId);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AlmaMenuListWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.almaMenuWidgetList);

            finish();
        }
    };

    private Spinner spinner;
    public AlmaMenuWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveAlmaId(Context context, int appWidgetId, int almaId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, almaId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static int getAlmaId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, AppPreferences.DEFAULT_ALMAID);
    }

    static void removeAlmaId(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.alma_menu_widget_configure);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.addAll(AppPreferences.ALMA_NAMES);
        spinner.setAdapter(arrayAdapter);

//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        spinner.setSelection(getAlmaId(AlmaMenuWidgetConfigureActivity.this, mAppWidgetId));
    }
}

