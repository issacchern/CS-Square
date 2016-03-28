package com.chernyee.cssquare.Utility;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.util.Log;

public class MyBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    static final String PREFS = "pref_file";

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "cs_prefs";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        Log.v("BackupAgentHelper" , "Backing up prefs file");
        SharedPreferencesBackupHelper helper =
                new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);
    }


}