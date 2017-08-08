package com.jkrajniak.almamenu.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jkrajniak.almamenu.preferences.AppPreferences;


public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Intent pullServiceIntent = new Intent(context, ALMAMenuPullServiceAlarm.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(
                    context,
                    ALMAMenuPullServiceAlarm.REQUEST_CODE,
                    pullServiceIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis();
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    firstMillis,
                    AppPreferences.UPDATE_FREQUENCE,
                    pIntent);

        }
    }
}
