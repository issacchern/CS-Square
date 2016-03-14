package com.chernyee.cssquare.Recording;

import android.util.Log;

/**
 * Created by Issac on 3/14/2016.
 */
public class AppLog {
    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message){
        return Log.i(APP_TAG, message);
    }
}

