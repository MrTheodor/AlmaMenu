package com.jkrajniak.almamenu.fragments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.almaparser.AlmaMenu;
import com.jkrajniak.almamenu.almaparser.AlmaMenuItem;
import com.jkrajniak.almamenu.preferences.AppPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jakub on 30.01.17.
 */

public class AlmaMenuCardsViewAdapter extends RecyclerView.Adapter<AlmaMenuCardsViewAdapter.AlmaMenuCardsViewHolder> {

    private ArrayList<AlmaMenu> almaMenus;
    private final Context context;
    private final AppPreferences appPref;
    private final int almaId;
    private LinearLayoutManager llm;
    private AlmaMenuCardViewAdapter amc;

    public AlmaMenuCardsViewAdapter(Context context, int almaId) {
        this.context = context;
        this.almaId = almaId;
        appPref = new AppPreferences(context);
        loadData();
    }

    public void loadData() {
        almaMenus = appPref.getAlmaMenu(almaId);
    }

    @Override
    public AlmaMenuCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlmaMenuCardsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.almamenu_card, parent, false));
    }

    @Override
    public void onBindViewHolder(AlmaMenuCardsViewHolder holder, int position) {
        AlmaMenu almaMenu = almaMenus.get(position);
        if (almaMenu != null) {
            if (almaMenu.menuItems.size() == 0) {
                AlmaMenuItem ami = new AlmaMenuItem();
                ami.name = context.getResources().getString(R.string.closed);
                ami.isVeggie = false;
                ami.price = "";
                almaMenu.menuItems.add(ami);
            }

            llm = new LinearLayoutManager(context);
            llm.setAutoMeasureEnabled(true);
            holder.almaCards.setLayoutManager(llm);
            amc = new AlmaMenuCardViewAdapter(almaMenu.menuItems);
            holder.almaCards.setAdapter(amc);
            amc.notifyDataSetChanged();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM");
            holder.almaMenuCardDate.setText(sdf.format(almaMenu.date));
        }
    }

    @Override
    public int getItemCount() {
        return almaMenus.size();
    }

    public static class AlmaMenuCardsViewHolder extends RecyclerView.ViewHolder {
        RecyclerView almaCards;
        TextView almaMenuCardDate;

        public AlmaMenuCardsViewHolder(View viewItem) {
            super(viewItem);
            almaCards = (RecyclerView) viewItem.findViewById(R.id.almaMenuItemsList);
            almaMenuCardDate = (TextView) viewItem.findViewById(R.id.almaMenuDateView);
        }
    }
}
