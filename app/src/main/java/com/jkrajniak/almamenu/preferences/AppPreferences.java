package com.jkrajniak.almamenu.preferences;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkrajniak.almamenu.almaparser.AlmaMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** AppPreferences for preferences. */
public class AppPreferences {
    public static final String APPLOG = "AlmaMenu";
    public static final String[] ALMA_URLS = {
            "http://www.alma.be/nl/restaurant/alma-1",
            "http://www.alma.be/nl/restaurant/alma-2",
            "http://www.alma.be/nl/restaurant/alma-3",
            "http://www.alma.be/nl/restaurant/groep-t",
            "http://www.alma.be/nl/restaurant/alma-gasthuisberg"
    };
    public static final String[] ALMA_NAMES = {
            "ALMA 1",
            "ALMA 2",
            "ALMA 3",
            "GROEP T",
            "ALMA Gasthuisberg"
    };
    public static final String ALMA_FIRSTRUN = "almaFirstRun";
    public static final long UPDATE_FREQUENCE = AlarmManager.INTERVAL_HALF_DAY;

    /// Main preferece file.
    public static String PREF = "com.jkrajniak.almamenu.PREF";
    public static String SELECTED_ALMA_WIDGET = "selectedAlmaId";
    public static String ALMA_VISIBLES = "almaVisibles";
    public static String ALMA_LASTUPDATE = "almaLastUpdate";
    public static int DEFAULT_ALMAID = 0;

    public SharedPreferences sp;
    public AppPreferences(Context c) {
        sp = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static void resetPreferences(Context context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().commit();
    }

    // Helper methods to retrieve data from SharedPreferences.

    public boolean hasData() {
        String val = sp.getString("ALMA_0", null);
        return val != null;
    }

    public int[] getVisibleAlmas() {
        String almaIdList = sp.getString(ALMA_VISIBLES, null);
        if (almaIdList == null) {
            int[] returns = new int[ALMA_NAMES.length];
            for (int i = 0; i < ALMA_NAMES.length; i++) {
                returns[i] = i;
            }
            return returns;
        } else {
            String[] ret = almaIdList.split(",");
            int[] returns = new int[ret.length];
            for (int i = 0; i < ret.length; i++) {
                returns[i] = Integer.parseInt(ret[i]);
            }
            return returns;
        }
    }

    public ArrayList<AlmaMenu> getAlmaMenu(int almaId) {
        ArrayList<AlmaMenu> ret = new ArrayList<>();
        String json = sp.getString(String.format("ALMA_%d", almaId), "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            ret = gson.fromJson(json, new TypeToken<ArrayList<AlmaMenu>>() {}.getType());
        }
        return ret;
    }
}
