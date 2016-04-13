package com.chernyee.cssquare.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.chernyee.cssquare.MainActivity;
import com.chernyee.cssquare.Question1;
import com.chernyee.cssquare.QuestionActivity;
import com.chernyee.cssquare.R;
import com.chernyee.cssquare.VarInit;

import org.parceler.Parcels;

import java.util.Random;

/**
 * Created by Issac on 4/1/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean notfication = sharedPreferences.getBoolean("csnotifcation", true);
        if(notfication ){

            if(VarInit.getSharedCodeListInstance().size() == 0){
                int databaseVersion = sharedPreferences.getInt("csdbversion", 7);
                new VarInit(context, databaseVersion).initializeVariable();

            }

            Question1 q ;
            int random = new Random().nextInt(VarInit.getSharedCodeListInstance().size());
            q = VarInit.getSharedCodeListInstance().get(random);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.app_logo)
                            .setContentTitle(q.getTitle())
                            .setContentText(q.getDescription()).setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
            if (Build.VERSION.SDK_INT >= 21) {
                mBuilder.setLargeIcon(
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo));
            }

            Intent resultIntent = new Intent(context, QuestionActivity.class);
            Bundle bundle = new Bundle();
            Parcelable wrapped = Parcels.wrap(q);
            bundle.putParcelable("data", wrapped);
            resultIntent.putExtras(bundle);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(12345, mBuilder.build());
        }
    }
}
