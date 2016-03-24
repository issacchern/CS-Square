package com.chernyee.cssquare;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class QuestionActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private AdView adView;
    private AdRequest adRequest;

    private TagGroup mTagGroup;
    private List<String> info;
    private Button codeButton;
    private int hintCount = 0;
    private CheckBox codeCheck;
    private String cliptoBoard;
    private long tStart = 0;
    private long tDelta = 0;
    private long tEnd = 0;
    private double elapsedSeconds = 0;
    private boolean weirdToggle = false;
    private TextView noteTitle;
    private TextView note;
    private FloatingActionButton actionA;
    private FloatingActionButton actionB;
    private FloatingActionButton actionC;
    private FloatingActionButton actionD;

    private SharedPreferences sharedPref;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if(taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                Intent i = new Intent(QuestionActivity.this, MainActivity.class);
                startActivity(i);
            } else{
                finish();
            }


        }

        else if (id == R.id.bookmark_item) {
            String markString = "cs"+info.get(0);
            int markScore = sharedPref.getInt(markString, 0);
            if(markScore == 0){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(markString, 1);
                editor.commit();
                item.setIcon(getResources().getDrawable(R.drawable.star));
                Toast.makeText(this, "Bookmarked!", Toast.LENGTH_SHORT).show();
            } else{
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(markString,0);
                editor.commit();
                item.setIcon(getResources().getDrawable(R.drawable.star_outline));
                Toast.makeText(this, "Remove bookmarked!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if(id == R.id.hot){
            Toast.makeText(this, "This question is hot!",Toast.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark, menu);
        MenuItem menuItem = menu.findItem(R.id.hot);
        if(info.get(9).contains("hot")){

        } else{
            menuItem.setVisible(false);
        }



        MenuItem bedMenuItem = menu.findItem(R.id.bookmark_item);
        String markString = "cs"+info.get(0);
        int markScore = sharedPref.getInt(markString, 0);
        if(markScore == 0){
            bedMenuItem.setIcon(getResources().getDrawable(R.drawable.star_outline));
        } else{
            bedMenuItem.setIcon(getResources().getDrawable(R.drawable.star));
        }


        return super.onCreateOptionsMenu(menu);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
  //              .addTestDevice("5F99E784008077D0C0404F50AF1A4B44")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tStart = System.currentTimeMillis();

        Bundle extras = getIntent().getExtras();

        info = extras.getStringArrayList("information");
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        final String [] split = info.get(5).split("\n");

        int ads = sharedPref.getInt("csnoads",0);

        if(ads == 0){
            adView = (AdView) findViewById(R.id.adView);

            adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("5F99E784008077D0C0404F50AF1A4B44")  // An example device ID
                    .build();
            adView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-6737882167206424/5868773590");

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });
            requestNewInterstitial();
        } else{
            weirdToggle = true;
        }

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        String tag = info.get(6);
        String [] tags = tag.split(",");
        for(int i = 0; i < tags.length ; i++){
            tags[i] = tags[i].trim();
        }
        mTagGroup.setTags(tags);


        TextView titleView = (TextView) findViewById(R.id.codeTitle);
        titleView.setText(info.get(1));
        TextView descriptionView = (TextView) findViewById(R.id.codeDescription);
        descriptionView.setText(info.get(2));

        updateCodeAndNumber(info.get(3));



        cliptoBoard = info.get(3);

        codeButton = (Button) findViewById(R.id.codeButton);
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String markString = "cse"+info.get(0);
                int markScore = sharedPref.getInt(markString, 0);
                if(markScore == 1 || weirdToggle){
                    if(codeButton.getText().equals("Hide Solution")){
                        updateCodeAndNumber(info.get(3));
                        cliptoBoard = info.get(3);
                        codeButton.setText("Show Solution");
                        codeCheck.setVisibility(View.VISIBLE);

                    } else{
                        updateCodeAndNumber(info.get(4));
                        cliptoBoard = info.get(4);
                        codeButton.setText("Hide Solution");
                        codeCheck.setVisibility(View.VISIBLE);

                    }

                } else{

                    tEnd = System.currentTimeMillis();
                    tDelta = tEnd - tStart;
                    elapsedSeconds = tDelta / 1000.0;

                    if(elapsedSeconds < 120){


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionActivity.this);
                        builder1.setTitle("Show Solution");
                        builder1.setMessage("Really? You spent less than 2 minutes and you already give up? ");
                        builder1.setCancelable(true);


                        builder1.setPositiveButton(
                                "Keep trying",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "See answer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //TODO:show ads
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        }


                                        weirdToggle = true;

                                        if(codeButton.getText().equals("Hide Solution")){
                                            updateCodeAndNumber(info.get(3));
                                            cliptoBoard = info.get(3);
                                            codeButton.setText("Show Solution");
                                            codeCheck.setVisibility(View.VISIBLE);

                                        } else{
                                            updateCodeAndNumber(info.get(4));
                                            cliptoBoard = info.get(4);
                                            codeButton.setText("Hide Solution");
                                            codeCheck.setVisibility(View.VISIBLE);

                                        }
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    } else{

                        if(codeButton.getText().equals("Hide Solution")){
                            updateCodeAndNumber(info.get(3));
                            cliptoBoard = info.get(3);
                            codeButton.setText("Show Solution");
                            codeCheck.setVisibility(View.VISIBLE);

                        } else{
                            updateCodeAndNumber(info.get(4));
                            cliptoBoard = info.get(4);
                            codeButton.setText("Hide Solution");
                            codeCheck.setVisibility(View.VISIBLE);

                        }

                    }

                }


            }
        });

        codeCheck = (CheckBox) findViewById(R.id.codeCheckBox);
        codeCheck.setVisibility(View.GONE);

        codeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String markString = "cse"+info.get(0);
                int markScore = sharedPref.getInt(markString, 0);
                if(markScore == 0){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(markString, 1);
                    editor.commit();
                    int ss = sharedPref.getInt("cscomplete",0) + 1;
                    editor.putInt("cscomplete", ss);
                    editor.commit();


                } else{
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(markString, 0);
                    editor.commit();
                    int ss = sharedPref.getInt("cscomplete",0) - 1;
                    editor.putInt("cscomplete", ss);
                    editor.commit();
                }
            }
        });

        String markString = "cse"+info.get(0);
        int markScore = sharedPref.getInt(markString, 0);
        if(markScore == 1){
            codeCheck.setVisibility(View.VISIBLE);
            codeCheck.setChecked(true);
        } else {
            codeCheck.setVisibility(View.GONE);
            codeCheck.setChecked(false);
        }



        actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(actionA.getTitle().equals("Code copied!")){
                    actionA.setTitle("Code uncopied!");
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", "nanana");
                    clipboard.setPrimaryClip(clip);
                }else{
                    actionA.setTitle("Code copied!");
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", cliptoBoard);
                    clipboard.setPrimaryClip(clip);
                }


            }
        });

        actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, cliptoBoard);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share the code"));

            }
        });

        actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.edit_text, null);
                final EditText input = (EditText) dialoglayout.findViewById(R.id.edit);

                String str = "csef" + info.get(0);
                String none = sharedPref.getString(str,"None");
                if(none.equals("None") || none.equals("")){

                } else {
                    input.setText(none);
                }

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuestionActivity.this);
                alertDialog.setView(dialoglayout);

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                });

                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do something with it()
                        String str = "csef" + info.get(0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(str, input.getText().toString());
                        editor.commit();


                        if(input.getText().toString().equals("None") || input.getText().toString().equals("")){
                            note.setVisibility(View.GONE);
                            noteTitle.setVisibility(View.GONE);
                        } else{
                            noteTitle.setVisibility(View.VISIBLE);
                            note.setVisibility(View.VISIBLE);
                            note.setText(input.getText().toString());
                        }

                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });


        actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hintCount > split.length -1) hintCount = 0;
                Snackbar.make(view, split[hintCount++], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        note = (TextView) findViewById(R.id.codeNotes);
        String str = "csef" + info.get(0);
        String none = sharedPref.getString(str,"None");

        noteTitle = (TextView) findViewById(R.id.codeNotesTitle);


        if(none.equals("None") || none.equals("")){
            note.setVisibility(View.GONE);
            noteTitle.setVisibility(View.GONE);
        } else{
            note.setText(none);
        }
    }



    public void updateCodeAndNumber(String str){

        SyntaxHighlighter sh1 = new SyntaxHighlighter(str);
        TextView codeView = (TextView) findViewById(R.id.code);
        codeView.setText(sh1.formatToHtml());

        int lineNumber = MainActivity.countLines(str);
        String printLine = "";
        for(int i = 1; i <= lineNumber; i++){
            if(i < lineNumber)
                printLine += (i < 10) ? " " + i + "\n" : "" + i + "\n" ;
            else printLine += (i < 10) ? " " + i : "" + i;
        }

        TextView codeNumberView = (TextView) findViewById(R.id.codeNumber);
        codeNumberView.setText(printLine);
    }


}