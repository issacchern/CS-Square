package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import be.billington.calendar.recurrencepicker.EventRecurrence;
import be.billington.calendar.recurrencepicker.EventRecurrenceFormatter;
import be.billington.calendar.recurrencepicker.RecurrencePickerDialog;


public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private ListView listView;
    private String recurrenceRule;
    private Switch sw;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        listView = (ListView) v.findViewById(R.id.list);

        ArrayList<SmallItem> arrayOfItem = new ArrayList<SmallItem>();
        arrayOfItem.add(new SmallItem("Friendly Reminder", "Enable CS-Square to send notification"));
        arrayOfItem.add(new SmallItem("Reset Data", "All of your data will be deleted"));
        arrayOfItem.add(new SmallItem("Backup Data", "Data is backed up to the cloud periodically"));
        arrayOfItem.add(new SmallItem(getString(R.string.title_setting), getString(R.string.desc_setting)));

        ItemAdapter adapter = new ItemAdapter(getContext(), arrayOfItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    // empty
                } else if (position == 1){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("WARNING!");
                    builder1.setMessage(
                            "By resetting data, it means all of your saved data will be gone forever! " +
                            "Are you sure you want to do that?");
                    builder1.setCancelable(true);

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int db = sharedPreferences.getInt("csdbversion", 7);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    editor.putInt("csdbversion", db);
                                    editor.commit();
                                    dialog.cancel();

                                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();


                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else if(position == 2){
                    BackupManager bm = new BackupManager(getActivity());
                    bm.dataChanged();
                    Toast.makeText(getContext(), "Make sure you have already enabled Backup & " +
                            "Restore options in Settings > Backup & reset for backup function to work properly.", Toast.LENGTH_LONG).show();

                } else if (position == 3){
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.chernyee.cssquarepaid")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.chernyee.cssquarepaid")));
                    }
                }
            }
        });



        return v;
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_list, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.text_desc);
            tvTitle.setText(smallItem.title);
            tvDesc.setText(smallItem.description);
            sw = (Switch) convertView.findViewById(R.id.sw);
            if(position == 0){
                boolean notification = sharedPreferences.getBoolean("csnotification", true);
                if(notification) sw.setChecked(true);
                else sw.setChecked(false);
                sw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sw.isChecked()){
                            sw.setChecked(false);
                            Toast.makeText(getContext(), "Disable notification!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("csnotification", false);
                            editor.commit();
                        } else{
                            sw.setChecked(true);
                            Toast.makeText(getContext(), "Enable notification!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("csnotification", true);
                            editor.commit();
                        }
                    }
                });
            } else{
                sw.setVisibility(View.GONE);
            }

            return convertView;
        }


    }
}
