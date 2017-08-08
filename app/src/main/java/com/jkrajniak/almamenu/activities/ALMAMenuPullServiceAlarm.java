package com.jkrajniak.almamenu.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by jakub on 25.01.17.
 */

public class ALMAMenuPullServiceAlarm extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.jkrajniak.almamenu.almamenupullservice.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ALMAMenuPullService.class);
        context.startService(i);
    }
}
