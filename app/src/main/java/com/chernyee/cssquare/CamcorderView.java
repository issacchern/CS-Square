package com.chernyee.cssquare;

/**
 * Created by Issac on 3/12/2016.
 * TODO: This is imcomplete version and it will be implemented any time in the future
 */


import java.io.IOException;

import android.content.Context;
;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CamcorderView extends SurfaceView implements
        SurfaceHolder.Callback {

    int width;
    int height;

    MediaRecorder recorder;
    SurfaceHolder holder;
    String outputFile = "";
    Camera camera;
    OrientationEventListener mOrientation;


    public CamcorderView(Context context, AttributeSet attrs) {
        super(context, attrs);




        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        outputFile += "/youraudiofile.mp4";
        holder = getHolder();
        holder.addCallback(this);

        recorder = new MediaRecorder();


        if(Camera.getNumberOfCameras() < 2) Toast.makeText(getContext(), "No front camera is found!",Toast.LENGTH_SHORT).show();


        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);


        camera.setDisplayOrientation(90);

        camera.lock();
        camera.unlock();

        recorder.setCamera(camera);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        recorder.setOrientationHint(270);




        // recorder.setVideoFrameRate(15);
        // recorder.setMaxDuration(10000);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        recorder.setOutputFile(outputFile);
        recorder.setPreviewDisplay(holder.getSurface());
        if (recorder != null) {
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                Log.e("IllegalStateException", e.toString());
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
        }


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        this.width = width;
        this.height = height;

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void setOutputFile(String filename)
    {
        outputFile = filename;
        recorder.setOutputFile(filename);
    }

    public void startRecording()
    {
        recorder.start();
    }

    public void stopRecording()
    {

        recorder.stop();
        recorder.release();

    }

    public void releaseCamera(){
        camera.release();
    }



}