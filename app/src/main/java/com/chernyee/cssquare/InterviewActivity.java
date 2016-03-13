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


import com.chernyee.cssquare.Visualizer.Visualizer;
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


public class InterviewActivity extends AppCompatActivity{

    Visualizer v1;
    private Button startButton;
    private Button stopButton;

    private StreamAudioRecorder mStreamAudioRecorder;
    private StreamAudioPlayer mStreamAudioPlayer;

    private File mAudioFile;
    private FileOutputStream mFileOutputStream;

    private boolean mIsRecording = false;

    private byte[] mBuffer;
    boolean toggle = true;

    private TextToSpeech tts;

    static final int BUFFER_SIZE = 4096;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStreamAudioRecorder = StreamAudioRecorder.getInstance();
        mStreamAudioPlayer = StreamAudioPlayer.getInstance();
        mBuffer = new byte[BUFFER_SIZE];

        tts = new TextToSpeech (this, new TextToSpeech.OnInitListener () {


            @Override
            public void onInit(int status) {
                tts.setLanguage (Locale.US);
                tts.speak ("Tell me about yourself", TextToSpeech.QUEUE_FLUSH, null);
            }
        });




        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //         ((Visualizer) findViewById(R.id.visualizer)).startListening();


                if (mIsRecording) {
                    stopRecord();
                    startButton.setText("Start");
                    mIsRecording = false;
                } else {

                    boolean isPermissionsGranted = RxPermissions.getInstance(getApplicationContext())
                            .isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            RxPermissions.getInstance(getApplicationContext())
                                    .isGranted(Manifest.permission.RECORD_AUDIO);

                    if (!isPermissionsGranted) {
                        RxPermissions.getInstance(getApplicationContext())
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean granted) {
                                        // not record first time to request permission
                                        if (granted) {
                                            Toast.makeText(getApplicationContext(), "Permission granted",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Permission not granted",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                });
                    } else {
                        startRecord();
                        startButton.setText("Stop");
                        mIsRecording = true;

                    }

                }


            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //             ((Visualizer) findViewById(R.id.visualizer)).stopListening();

                int random = (int) new Random().nextInt(220);
                if(random == 0) random = 1;

                String s = MainActivity.populateList.get(0).get(random).get(2);

                tts.speak (s, TextToSpeech.QUEUE_FLUSH, null);

            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //         boolean ss = ((Visualizer) findViewById(R.id.visualizer)).checkThread();
                //         Toast.makeText(getApplicationContext(), "" + ss, Toast.LENGTH_SHORT).show();

                play();
            }
        });


        CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.cv_countdownViewTest1);
        mCvCountdownView.start(10 * 60 * 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void stopRecord() {
        mStreamAudioRecorder.stop();
        try {
            mFileOutputStream.close();
            mFileOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecord() {
        try {
            mAudioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "ISSACTESTING" + ".mp3");
            mAudioFile.createNewFile();
            mFileOutputStream = new FileOutputStream(mAudioFile);
            mStreamAudioRecorder.start(new StreamAudioRecorder.AudioDataCallback() {
                @Override
                public void onAudioData(byte[] data, int size) {
                    if (mFileOutputStream != null) {
                        try {
                            mFileOutputStream.write(data, 0, size);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError() {
                    startButton.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Record fail",
                                    Toast.LENGTH_SHORT).show();
                            startButton.setText("Start");
                            mIsRecording = false;
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void play() {
        Observable.just(mAudioFile).subscribeOn(Schedulers.io()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                try {
                    mStreamAudioPlayer.init();
                    FileInputStream inputStream = new FileInputStream(file);
                    int read;
                    while ((read = inputStream.read(mBuffer)) > 0) {
                        mStreamAudioPlayer.play(mBuffer, read);
                    }
                    inputStream.close();
                    mStreamAudioPlayer.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

}