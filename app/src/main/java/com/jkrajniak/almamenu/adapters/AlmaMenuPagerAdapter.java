package com.jkrajniak.almamenu.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.jkrajniak.almamenu.fragments.AlmaMenuFragment;
import com.jkrajniak.almamenu.preferences.AppPreferences;

/**
 * Created by jakub on 28.01.17.
 */

public class AlmaMenuPagerAdapter extends FragmentStatePagerAdapter {
    private final Context context;
    private final AppPreferences apppref;
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;

    public AlmaMenuPagerAdapter(FragmentManager fm, Context cxt) {
        super(fm);
        context = cxt;
        apppref = new AppPreferences(context);

        preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(AppPreferences.ALMA_VISIBLES)) {
                    notifyDataSetChanged();
                }
            }
        };
        apppref.sp.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    @Override
    public Fragment getItem(int position) {
        int[] visibleAlmas = apppref.getVisibleAlmas();
        int almaId = visibleAlmas[position];

        return AlmaMenuFragment.newInstance(almaId);
    }

    @Override
    public int getCount() {
        int[] visibleAlmas = apppref.getVisibleAlmas();
        return visibleAlmas.length;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        int[] visibleAlmas = apppref.getVisibleAlmas();
        int almaId = visibleAlmas[position];
        return AppPreferences.ALMA_NAMES[almaId];
    }

    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
