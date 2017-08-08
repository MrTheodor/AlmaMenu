package com.jkrajniak.almamenu.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.preferences.AppPreferences;

import org.jsoup.helper.StringUtil;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by jakub on 31.01.17.
 */
public class SettingsAlmaViewAdapter extends RecyclerView.Adapter<SettingsAlmaViewAdapter.SettingsAlmaViewHolder>{
    private final AppPreferences appPreference;
    private final Context context;
    private ArrayList<String> almaNamesOrder;
    private HashMap<String, Boolean> selectedAlmas;
    private HashMap<String, Integer> almaName2AlmaId;

    private int numberOfCheckedEntries;

    public SettingsAlmaViewAdapter(Context context) {
        this.context = context;
        appPreference = new AppPreferences(context);
        int[] visibleAlmas = appPreference.getVisibleAlmas();

        numberOfCheckedEntries = visibleAlmas.length;

        almaNamesOrder = new ArrayList<String>();
        selectedAlmas = new HashMap<>();
        almaName2AlmaId = new HashMap<>();

        for (String s : AppPreferences.ALMA_NAMES) {
            selectedAlmas.put(s, false);
        }

        for (int almaIdx : visibleAlmas) {
            almaNamesOrder.add(AppPreferences.ALMA_NAMES[almaIdx]);
            selectedAlmas.put(AppPreferences.ALMA_NAMES[almaIdx], true);
        }

        for (int i=0; i < AppPreferences.ALMA_NAMES.length; i++) {
            almaName2AlmaId.put(AppPreferences.ALMA_NAMES[i], i);
            if (!selectedAlmas.get(AppPreferences.ALMA_NAMES[i]))
                almaNamesOrder.add(AppPreferences.ALMA_NAMES[i]);
        }
    }

    @Override
    public SettingsAlmaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SettingsAlmaViewHolder holder =  new SettingsAlmaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_almaname_settings, parent, false));

        holder.almaName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedAlmas.put((String) buttonView.getText(), isChecked);

                if (numberOfCheckedEntries == 1 && !isChecked) {
                    Toast.makeText(context, R.string.errorOneAlmaSelected, Toast.LENGTH_LONG).show();
                    buttonView.setChecked(true);
                } else {
                    updateSettings();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(SettingsAlmaViewHolder holder, int position) {
        String almaName = almaNamesOrder.get(position);
        holder.almaName.setText(almaName);
        if (selectedAlmas.get(almaName))
            holder.almaName.setChecked(true);
        else
            holder.almaName.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return almaNamesOrder.size();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(almaNamesOrder, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(almaNamesOrder, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateSettings();

        return true;
    }

    private void updateSettings() {
        SharedPreferences.Editor sp = appPreference.sp.edit();
        ArrayList<Integer> visibleAlm = new ArrayList<>();

        for (String s : almaNamesOrder) {
            if (selectedAlmas.get(s))
                visibleAlm.add(almaName2AlmaId.get(s));
        }

        numberOfCheckedEntries = visibleAlm.size();

        sp.putString(AppPreferences.ALMA_VISIBLES, StringUtil.join(visibleAlm, ","));


        sp.apply();
    }

    public static class  SettingsAlmaViewHolder extends RecyclerView.ViewHolder {
        CheckBox almaName;

        public SettingsAlmaViewHolder(View itemView) {
            super(itemView);
            almaName = (CheckBox) itemView.findViewById(R.id.almaNameItem);
        }
    }
}
