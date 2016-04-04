package com.chernyee.cssquare.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Issac on 4/1/2016.
 */
public class KeepAlarmLiveReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
        if (intent != null && Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            MyAlarmManager.register(context);
        }
    }

}
