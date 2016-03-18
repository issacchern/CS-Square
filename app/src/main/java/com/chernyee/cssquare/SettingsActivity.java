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
import android.support.v4.app.FragmentActivity;
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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import be.billington.calendar.recurrencepicker.EventRecurrence;
import be.billington.calendar.recurrencepicker.EventRecurrenceFormatter;
import be.billington.calendar.recurrencepicker.RecurrencePickerDialog;


public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int value = 1;
    private ListView listView;
    private String recurrenceRule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.list);

        ArrayList<SmallItem> arrayOfItem = new ArrayList<SmallItem>();
        arrayOfItem.add(new SmallItem("Friendly Reminder", "Enable CS-Square to send notification"));
        arrayOfItem.add(new SmallItem("Reset Data", "All of your data will be deleted"));
        arrayOfItem.add(new SmallItem("Backup Data", "Data is backed up to the cloud automatically"));
        arrayOfItem.add(new SmallItem(getString(R.string.title_setting), getString(R.string.desc_setting)));


        ItemAdapter adapter = new ItemAdapter(this, arrayOfItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    RecurrencePickerDialog recurrencePickerDialog = new RecurrencePickerDialog();

                    if (recurrenceRule != null && recurrenceRule.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(RecurrencePickerDialog.BUNDLE_RRULE, recurrenceRule);
                        recurrencePickerDialog.setArguments(bundle);
                    }

                    recurrencePickerDialog.setOnRecurrenceSetListener(new RecurrencePickerDialog.OnRecurrenceSetListener() {
                        @Override
                        public void onRecurrenceSet(String rrule) {
                            recurrenceRule = rrule;

                            if (recurrenceRule != null && recurrenceRule.length() > 0) {
                                EventRecurrence recurrenceEvent = new EventRecurrence();
                                recurrenceEvent.setStartDate(new Time("" + new Date().getTime()));
                                recurrenceEvent.parse(rrule);
                                String srt = EventRecurrenceFormatter.getRepeatString(SettingsActivity.this, getResources(), recurrenceEvent, true);
                                Toast.makeText(SettingsActivity.this, srt, Toast.LENGTH_LONG).show();
                                Toast.makeText(SettingsActivity.this, "But it's not functional yet :(", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "No occurence", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    recurrencePickerDialog.show(getSupportFragmentManager(), "recurrencePicker");


                } else if (position == 1){
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




                } else if(position == 2){
                    BackupManager bm = new BackupManager(SettingsActivity.this);
                    bm.dataChanged();
                    Toast.makeText(SettingsActivity.this, "Make sure you have already enabled Backup & Restore options in Settings > Backup & reset", Toast.LENGTH_LONG).show();


                } else if (position == 3){
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.chernyee.cssquarepaid")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.chernyee.cssquarepaid")));
                    }

                }
            }
        });

    }




    public class SmallItem{
        public String title;
        public String description;

        public SmallItem(String title, String description) {
            this.title = title;
            this.description = description;
        }

    }

    public class ItemAdapter extends ArrayAdapter<SmallItem> {
        public ItemAdapter(Context context, ArrayList<SmallItem> smallItems) {
            super(context, 0, smallItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SmallItem smallItem = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(android.R.id.text1);
            TextView tvDesc = (TextView) convertView.findViewById(android.R.id.text2);
            tvTitle.setText(smallItem.title);
            tvDesc.setText(smallItem.description);
            return convertView;
        }
    }


}
