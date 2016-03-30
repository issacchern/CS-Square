package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        arrayOfItem.add(new SmallItem("Backup Data", "Data is backed up to the cloud automatically"));
        arrayOfItem.add(new SmallItem(getString(R.string.title_setting), getString(R.string.desc_setting)));

        ItemAdapter adapter = new ItemAdapter(getContext(), arrayOfItem);
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
                                String srt = EventRecurrenceFormatter.getRepeatString(getContext(), getResources(), recurrenceEvent, true);
                                Toast.makeText(getContext(), srt, Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), "But it's not functional yet :(", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "No occurence", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    recurrencePickerDialog.show(getFragmentManager(), "recurrencePicker");


                } else if (position == 1){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Reset data");
                    builder1.setMessage("Are you sure you want to reset data? That means all of your bookmarks and saved data will be lost.");
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

//                                    File pref_file = new File("/data/data/com.chernyee.cssquare/shared_prefs/pref_file.xml");
//                                    if (pref_file.exists()) {
//
//
//                                        pref_file.delete();
//
//                                        File pref_bak = new File("/data/data/com.chernyee.cssquare/shared_prefs/pref_file.bak");
//                                        if(pref_bak.exists()) pref_bak.delete();
//
//
//
//                                        File file = new File(SplashActivity.databasePath + "/" + "Questions.db");
//                                        if(file.exists()) file.delete();
//                                    }
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
