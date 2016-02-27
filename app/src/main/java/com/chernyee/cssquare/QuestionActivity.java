package com.chernyee.cssquare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private List<String> info;
    private Button codeButton;
    private int hintCount = 0;
    private CheckBox codeCheck;
    private String cliptoBoard;

    private SharedPreferences sharedPref;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.bookmark_item) {
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
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark, menu);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        info = extras.getStringArrayList("information");
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        final String [] split = info.get(5).split("\n");

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
        });

        codeCheck = (CheckBox) findViewById(R.id.codeCheckBox);
        codeCheck.setVisibility(View.INVISIBLE);

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
        }






        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
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

        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, cliptoBoard);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });


        final FloatingActionButton actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hintCount > split.length -1) hintCount = 0;

                Snackbar.make(view, split[hintCount++], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });



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
