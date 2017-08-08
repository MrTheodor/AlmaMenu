package com.jkrajniak.almamenu.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkrajniak.almamenu.R;
import com.jkrajniak.almamenu.activities.ALMAMenuPullService;
import com.jkrajniak.almamenu.preferences.AppPreferences;

public class AlmaMenuFragment extends Fragment {
    public int almaId;
    private AlmaMenuCardsViewAdapter almaMenuCardsViewAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            almaMenuCardsViewAdapter.loadData();
            almaMenuCardsViewAdapter.notifyDataSetChanged();
            mySwipeRefreshLayout.setRefreshing(false);
        }
    };

    private final BroadcastReceiver broadcastReceiverUpdateData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mySwipeRefreshLayout.setRefreshing(true);
        }
    };

    private SwipeRefreshLayout mySwipeRefreshLayout;


    public static AlmaMenuFragment newInstance(int almaId) {
        AlmaMenuFragment almaFragment = new AlmaMenuFragment();
        Bundle args = new Bundle();
        args.putInt("almaId", almaId);
        almaFragment.setArguments(args);
        return almaFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        almaId = getArguments().getInt("almaId", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_fragment, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        almaMenuCardsViewAdapter = new AlmaMenuCardsViewAdapter(getContext(), almaId);
        rv.setAdapter(almaMenuCardsViewAdapter);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter("dataReceived"));

        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        Intent i = new Intent(getContext(), ALMAMenuPullService.class);
                        getContext().startService(i);
                    }
                }
        );

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                broadcastReceiverUpdateData, new IntentFilter("dataRefreshing"));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                broadcastReceiverUpdateData, new IntentFilter("dataRefreshing"));
    }
}
