package com.chernyee.cssquare.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Issac on 4/1/2016.
 */
public class MyAlarmManager {

    public static void register(Context context) {
        Calendar today = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 14);
        today.set(Calendar.MINUTE, 50);
        today.set(Calendar.SECOND, 20);

        if (now.after(today)) {
            return;
        }

        Intent intent = new Intent("com.chernyee.cssquare.alarm");
        intent.setClass(context, AlarmReceiver.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(context, 520, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), broadcast);
    }
}
