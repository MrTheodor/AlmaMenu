package com.jkrajniak.almamenu.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.adapters.SettingsAlmaViewAdapter;
import com.jkrajniak.almamenu.fragments.AlmaMenuCardsViewAdapter;

import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity {

    private SettingsAlmaViewAdapter almaMenuCardsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.preferedAlmasList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        almaMenuCardsViewAdapter = new SettingsAlmaViewAdapter(this);
        rv.setAdapter(almaMenuCardsViewAdapter);

        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();

                SettingsAlmaViewAdapter sava = (SettingsAlmaViewAdapter) recyclerView.getAdapter();
                return sava.onItemMove(fromPos, toPos);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        ith.attachToRecyclerView(rv);
    }
}
