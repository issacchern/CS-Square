package com.chernyee.cssquare;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.chernyee.cssquare.Recording.RecordingSampler;
import com.chernyee.cssquare.Recording.VisualizerView;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.github.piasy.rxandroidaudio.StreamAudioPlayer;
import com.github.piasy.rxandroidaudio.StreamAudioRecorder;
import com.tbruyelle.rxpermissions.RxPermissions;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class InterviewActivity extends AppCompatActivity implements
        RecordingSampler.CalculateVolumeListener{

    private Button startButton;
    private Button stopButton;
    private TextToSpeech tts;

    private MediaRecorder mediaRecorder;

    private VisualizerView visualizerView;

    private RecordingSampler mRecordingSampler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tts = new TextToSpeech (this, new TextToSpeech.OnInitListener () {


            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
          //      tts.speak ("Tell me about yourself", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        visualizerView = (VisualizerView) findViewById(R.id.visualizer3);

        mRecordingSampler = new RecordingSampler();
        mRecordingSampler.setVolumeListener(this);
        mRecordingSampler.setSamplingInterval(100);
        mRecordingSampler.link(visualizerView);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/myrecording.mp3");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }









        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRecordingSampler.isRecording()) {
                    startButton.setText("START");
                    mRecordingSampler.release();




                } else {
                    startButton.setText("STOP");
                    mRecordingSampler.startRecording();


                }





            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                int random = (int) new Random().nextInt(220);
//                if(random == 0) random = 1;
//
//                String s = MainActivity.populateList.get(0).get(random).get(2);
//
//                tts.speak (s, TextToSpeech.QUEUE_FLUSH, null);




            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //         boolean ss = ((Visualizer) findViewById(R.id.visualizer)).checkThread();
                //         Toast.makeText(getApplicationContext(), "" + ss, Toast.LENGTH_SHORT).show();


            }
        });


        CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.cv_countdownViewTest1);
        mCvCountdownView.start(10 * 60 * 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCalculateVolume(int volume) {

    }
}