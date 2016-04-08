package com.chernyee.cssquare.Utility;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.chernyee.cssquare.FlashCardActivity;

import java.io.IOException;

public class MyBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    static final String PREFS = "pref_file";
    static final String DBS = "db_file";

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "cs_prefs";
    static final String DB_BACKUP_KEY = "cs_db";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        Log.v("BackupAgentHelper" , "Backing up prefs file");
        SharedPreferencesBackupHelper prefHelper =
                new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, prefHelper);

        FileBackupHelper dbHelper = new FileBackupHelper(this,DBS);
        addHelper(DB_BACKUP_KEY, dbHelper);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper performs backup
        synchronized (FlashCardActivity.sDataLock) {
            super.onBackup(oldState, data, newState);
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode,
                          ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper restores the file
        synchronized (FlashCardActivity.sDataLock) {
            super.onRestore(data, appVersionCode, newState);
        }
    }


}