package com.jkrajniak.almamenu.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.almaparser.AlmaMenuItem;
import com.jkrajniak.almamenu.preferences.AppPreferences;

import java.util.ArrayList;

public class AlmaMenuCardViewAdapter extends RecyclerView.Adapter<AlmaMenuCardViewAdapter.AlmaMenuCardViewHolder> {

    private ArrayList<AlmaMenuItem> almaMenuItems;
    private boolean emptyList;

    public AlmaMenuCardViewAdapter(ArrayList<AlmaMenuItem> almaMenuItems) {
        this.almaMenuItems = almaMenuItems;
    }

    @Override
    public AlmaMenuCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlmaMenuCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_almamenuitem, parent, false));
    }

    @Override
    public void onBindViewHolder(AlmaMenuCardViewHolder holder, int position) {
        AlmaMenuItem almaMenuItem = almaMenuItems.get(position);
        holder.name.setText(almaMenuItem.name);
        holder.price.setText(almaMenuItem.price);
        if (almaMenuItem.isVeggie)
            holder.vegetarianImage.setVisibility(ImageView.VISIBLE);
        else
            holder.vegetarianImage.setVisibility(ImageView.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return almaMenuItems.size();
    }

    public static class AlmaMenuCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView vegetarianImage;
        protected TextView price;

        public AlmaMenuCardViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.tvName);
            vegetarianImage = (ImageView) v.findViewById(R.id.vegetarianImage);
            price = (TextView) v.findViewById(R.id.tvPrice);
        }
    }
}
