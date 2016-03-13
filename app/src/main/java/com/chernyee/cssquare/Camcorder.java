package com.chernyee.cssquare;

/**
 * Created by Issac on 3/12/2016.
 */


        import android.app.Activity;
        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.Button;

public class Camcorder extends Activity {

    private CamcorderView camcorderView;
    private boolean recording = false;
    private Button click;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.camcorder_preview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        click = (Button) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recording) {
                    camcorderView.stopRecording();
                    recording = false;

                } else {
                    recording = true;
                    click.setText("Stop recording");
                    camcorderView.startRecording();
                }

            }
        });
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        camcorderView = (CamcorderView) findViewById(R.id.camcorder_preview);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (recording) {
            camcorderView.stopRecording();
        }
        camcorderView.releaseCamera();

    }
}