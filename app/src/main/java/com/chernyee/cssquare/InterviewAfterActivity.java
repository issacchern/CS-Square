package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import com.chernyee.cssquare.AlertDialog.SweetAlertDialog;
import com.chernyee.cssquare.Utility.SyntaxHighlighter;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.parceler.Parcels;

public class InterviewAfterActivity extends AppCompatActivity {

    private List<Question2> interviewList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private CountdownView countdownView;
    private FloatingActionButton fabReplay;
    private FloatingActionButton fabPlayPause;
    private FloatingActionButton fabSave;
    private boolean alertExit = true;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private InputStream in;
    private OutputStream out;

    private final String FILE_PATH = SplashActivity.RECORD_PATH + "/TempRecording.wav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_after);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_variant);
        mediaPlayer = MediaPlayer.create(this, Uri.parse(FILE_PATH));
        fabReplay = (FloatingActionButton) findViewById(R.id.fabReplay);
        fabPlayPause = (FloatingActionButton) findViewById(R.id.fabPlayPause);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        int remainingTime = getIntent().getIntExtra("RemainingTime", 1);
        int beginningTime = getIntent().getIntExtra("BeginningTime", 1);
        int spendingTime = beginningTime - remainingTime;
        countdownView = (CountdownView) findViewById(R.id.cv_countdownViewTest1);
        countdownView.updateShow(spendingTime);
        interviewList = Parcels.unwrap(getIntent().getParcelableExtra("data"));
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        fabReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    fabPlayPause.setImageResource(R.drawable.play_pause);
                    isPlaying = false;
                    mediaPlayer.stop();

                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                fabPlayPause.setImageResource(R.drawable.play_pause);

            }
        });


        fabPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    fabPlayPause.setImageResource(R.drawable.pause);
                    isPlaying = false;
                    mediaPlayer.pause();
                } else{
                    fabPlayPause.setImageResource(R.drawable.play);
                    isPlaying = true;
                    mediaPlayer.start();
                }


            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.edit_text, null);
                final MaterialEditText input = (MaterialEditText) dialoglayout.findViewById(R.id.edit);
                TextView text = (TextView) dialoglayout.findViewById(R.id.text);
                text.setText("Enter your file name");
                input.setText(".wav");

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InterviewAfterActivity.this);
                alertDialog.setView(dialoglayout);

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do something with it(
                        String s = input.getText().toString();
                        if (!s.endsWith(".wav")) {
                            Toast.makeText(InterviewAfterActivity.this, "File extension must be in .wav type!", Toast.LENGTH_LONG).show();
                        } else {
                            String temp = s.substring(0, s.indexOf(".wav"));
                            if (temp.matches("^[a-zA-Z0-9]+$") && temp.length() > 0) {
                                // check if file exists...

                                String wholePath = SplashActivity.RECORD_PATH + "/" + s;
                                File check = new File(wholePath);
                                if (check.exists()) {
                                    Toast.makeText(InterviewAfterActivity.this, "The file has already existed, please choose other name!", Toast.LENGTH_LONG).show();
                                } else {
                                    new MyAsyncTask(wholePath).execute();
                                    alertExit = false;
                                }

                            } else {
                                Toast.makeText(InterviewAfterActivity.this, "Invalid file name!", Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(249, 1877,
                        0)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Answer");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem shareItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                shareItem.setBackground(new ColorDrawable(Color.rgb(128,
                        128, 128)));
                // set item width
                shareItem.setWidth(dp2px(90));
                // set a icon
                shareItem.setIcon(R.drawable.share_variant);
                // add to menu
                menu.addMenuItem(shareItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item = interviewList.get(position).getAnswer();
                switch (index) {
                    case 0:

                        if(interviewList.get(position).getCategory().equals("Coding")){
                            SyntaxHighlighter sh1 = new SyntaxHighlighter(item);

                            new SweetAlertDialog(InterviewAfterActivity.this)
                                    .setTitleText("Answer")
                                    .setContentText(sh1.formatToHtml())
                                    .show();

                        } else{
                            new SweetAlertDialog(InterviewAfterActivity.this)
                                    .setTitleText("Answer")
                                    .setContentText(item)
                                    .show();
                        }

                        break;
                    case 1:
                        String messageToCopy = interviewList.get(position).getQuestion() + "\n\n" +
                                interviewList.get(position).getAnswer();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, messageToCopy);
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share the answer"));

                        break;
                }
                return false;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (alertExit) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewAfterActivity.this);
                    builder1.setTitle("Exiting");
                    builder1.setMessage("Your recorded audio will be deleted. Are you sure you want to continue?");
                    builder1.setCancelable(false);


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
                                    dialog.cancel();
                            //        Intent intent = new Intent(InterviewAfterActivity.this, MainActivity.class);
                             //       startActivity(intent);
                                    finish();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return true;


                } else {
                    return super.onOptionsItemSelected(item);
                }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if(alertExit){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewAfterActivity.this);
            builder1.setTitle("Exiting");
            builder1.setMessage("Your recorded audio will be deleted. Are you sure you want to continue?");
            builder1.setCancelable(false);


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
                            dialog.cancel();
                   //         Intent intent = new Intent(InterviewAfterActivity.this, MainActivity.class);
                   //         startActivity(intent);
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        } else{
            super.onBackPressed();
        }

    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return interviewList.size();
        }

        @Override
        public String getItem(int position) {
            return interviewList.get(position).getQuestion();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            holder.tv_name.setText(item);

            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListView.smoothOpenMenu(position);
              //      Toast.makeText(InterviewAfterActivity.this,"Swipe to the left",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        String fileAddress;

        public MyAsyncTask(String fileAddress){
            this.fileAddress = fileAddress;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(InterviewAfterActivity.this);
            progressDialog.setTitle("Copying file");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Toast.makeText(InterviewAfterActivity.this, "Your file has successfully saved!", Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                in = new FileInputStream(FILE_PATH);
                out = new FileOutputStream(fileAddress);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error saving file!", Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
