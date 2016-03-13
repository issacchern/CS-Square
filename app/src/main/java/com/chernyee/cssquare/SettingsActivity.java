package com.chernyee.cssquare;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private SharedPreferences sharedPreferences;
    private int value = 1;
    private SwitchPreference friendlyReminder;

    private NotificationManager notificationManager;

    private boolean isNotificationActive = false;
    private int notifiID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.pref_general);

        Preference resetData = (Preference) findPreference("reset_data");
        resetData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here


                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingsActivity.this);
                builder1.setTitle("Reset data");
                builder1.setMessage("Are you sure you want to reset data? That means all of your bookmarks and saved data will be lost.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                File pref_file = new File("/data/data/com.chernyee.cssquare/shared_prefs/pref_file.xml");
                                if (pref_file.exists()) {
                                    pref_file.delete();

                                }

                                System.exit(0);


                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


                return true;

            }


        });


        Preference disableAds = (Preference) findPreference("disable_ads");

        disableAds.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.chernyee.cssquarepaid")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.chernyee.cssquarepaid")));
                }

                return false;
            }
        });




        friendlyReminder = (SwitchPreference) findPreference("friendly_reminder");



        friendlyReminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Toast.makeText(getApplication(), "Let me know what kind of notification you would like to receive!", Toast.LENGTH_LONG).show();
                if (friendlyReminder.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("csswitch", 0);
                    editor.commit();






                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("csswitch", 1);
                    editor.commit();
//                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
//                    NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(SettingsActivity.this).setContentTitle("Practice coding everyday")
//                            .setContentText("[Leetcode] Maximum Subarray").setSmallIcon(R.drawable.app_logo)
//                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND).setTicker("Alert new message")
//                            .setColor(Color.parseColor("#F9BB00"));
//
//                    Intent moreInforIntent = new Intent(SettingsActivity.this, QuestionActivity.class);
//
//
//                    moreInforIntent.putExtra("information",new ArrayList<>(MainActivity.populateList.get(0).get(0)));
//
//                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(SettingsActivity.this);
//
//                    taskStackBuilder.addNextIntent(moreInforIntent);
//                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                    notiBuilder.setContentIntent(pendingIntent);
//
//                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(notifiID, notiBuilder.build());



                    Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

                    // Define our intention of executing AlertReceiver
                    Intent alertIntent = new Intent(SettingsActivity.this, AlertReceiver.class);

                    // Allows you to schedule for your application to do something at a later date
                    // even if it is in he background or isn't active
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    // set() schedules an alarm to trigger
                    // Trigger for alertIntent to fire in 5 seconds
                    // FLAG_UPDATE_CURRENT : Update the Intent if active
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                            PendingIntent.getBroadcast(SettingsActivity.this, 1, alertIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));


                }
                return true;
            }
        });

        value = sharedPreferences.getInt("csswitch", 0);

        if(value == 1){
            friendlyReminder.setChecked(true);
            //TODO: enable set notification
        } else{
            friendlyReminder.setChecked(false);
            //TODO: disable set notification
        }


    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


}
