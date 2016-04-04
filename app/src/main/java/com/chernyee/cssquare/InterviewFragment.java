package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class InterviewFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CheckedTextView checkedTextViewHR;
    private CheckedTextView checkedTextViewKnowledge;
    private CheckedTextView checkedTextViewCode;
    private CheckedTextView checkedTextViewAll;
    private CheckedTextView checkedTextViewJava;
    private CheckedTextView checkedTextViewAndroid;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private ImageView minusButton;
    private ImageView plusButton;
    private TextView questionText;
    private SeekBar seekBar;
    private TextView seekValue;
    private Button startButton;
    private int initialNumberQuestion = 10;

    public InterviewFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        // default settings
        editor.putBoolean("cscheck1", true);
        editor.putBoolean("cscheck2", true);
        editor.putBoolean("cscheck2Java", false);
        editor.putBoolean("cscheck2Android", false);
        editor.putBoolean("cscheck3", true);
        editor.putInt("csradio", 1);
        editor.putInt("csseek", 10);
        editor.putInt("csplusminus", 10);
        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_interview, container, false);

        minusButton = (ImageView) v.findViewById(R.id.minusButton);
        plusButton = (ImageView) v.findViewById(R.id.plusButton);
        questionText = (TextView) v.findViewById(R.id.questionText);
        seekBar = (SeekBar) v.findViewById(R.id.seekbar);
        seekValue = (TextView) v.findViewById(R.id.seekvalue);
        startButton = (Button) v.findViewById(R.id.startButton);
        checkedTextViewHR = (CheckedTextView) v.findViewById(R.id.checkedTextViewHR);
        checkedTextViewKnowledge = (CheckedTextView) v.findViewById(R.id.checkedTextViewKnowledge);
        checkedTextViewCode = (CheckedTextView) v.findViewById(R.id.checkedTextViewCode);
        checkedTextViewAll = (CheckedTextView) v.findViewById(R.id.checkedTextViewAll);
        checkedTextViewJava = (CheckedTextView) v.findViewById(R.id.checkedTextViewJava);
        checkedTextViewAndroid = (CheckedTextView) v.findViewById(R.id.checkedTextViewAndroid);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) radioGroup.findViewById(R.id.radio1);
        radioButton2 = (RadioButton) radioGroup.findViewById(R.id.radio2);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initialNumberQuestion > 0){
                    initialNumberQuestion--;
                    editor = sharedPreferences.edit();
                    editor.putInt("csplusminus", initialNumberQuestion);
                    editor.commit();
                    questionText.setText(initialNumberQuestion + " Questions total");
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialNumberQuestion++;
                editor = sharedPreferences.edit();
                editor.putInt("csplusminus", initialNumberQuestion);
                editor.commit();
                questionText.setText(initialNumberQuestion + " Questions total");
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue.setText(progress + " mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkedTextViewHR.isChecked() && !checkedTextViewKnowledge.isChecked() && !checkedTextViewCode.isChecked()
                        && !checkedTextViewAndroid.isChecked() && !checkedTextViewJava.isChecked()) {
                    Toast.makeText(getContext(), "Please select at least one of the categories", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (seekBar.getProgress() == 0) {
                    Toast.makeText(getContext(), "Length of interview must not be 0!", Toast.LENGTH_SHORT).show();
                    return;
                }

                editor = sharedPreferences.edit();

                if (radioButton1.isChecked()) {
                    editor.putInt("csradio", 1);
                } else if (radioButton2.isChecked()) {
                    editor.putInt("csradio", 2);
                }

                editor.putInt("csseek", seekBar.getProgress());
                editor.commit();


                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                startActivity(intent);

            }
        });

        checkedTextViewHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewHR.isChecked()){
                    checkedTextViewHR.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", false);
                    editor.commit();
                    checkedTextViewAll.setChecked(false);
                } else{
                    checkedTextViewHR.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", true);
                    editor.commit();
                }
            }
        });

        checkedTextViewKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewKnowledge.isChecked()){
                    checkedTextViewKnowledge.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2", false);
                    editor.putBoolean("cscheck2Java", false);
                    editor.putBoolean("cscheck2Android", false);
                    editor.commit();
                    checkedTextViewJava.setVisibility(View.GONE);
                    checkedTextViewAndroid.setVisibility(View.GONE);
                    checkedTextViewAll.setChecked(false);
                } else{
                    checkedTextViewKnowledge.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2", true);
                    editor.putBoolean("cscheck2Java", true);
                    editor.putBoolean("cscheck2Android", true);
                    editor.commit();
                    checkedTextViewJava.setVisibility(View.VISIBLE);
                    checkedTextViewAndroid.setVisibility(View.VISIBLE);
                    checkedTextViewJava.setChecked(true);
                    checkedTextViewAndroid.setChecked(true);
                }
            }
        });

        checkedTextViewJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewJava.isChecked()){
                    checkedTextViewJava.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2Java", false);
                    editor.commit();
                } else{
                    checkedTextViewJava.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2Java", true);
                    editor.commit();
                }
            }
        });

        checkedTextViewAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewAndroid.isChecked()){
                    checkedTextViewAndroid.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2Android", false);
                    editor.commit();
                } else{
                    checkedTextViewAndroid.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck2Android", true);
                    editor.commit();
                }
            }
        });

        checkedTextViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewCode.isChecked()){
                    checkedTextViewCode.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck3", false);
                    editor.commit();
                    checkedTextViewAll.setChecked(false);
                } else{
                    checkedTextViewCode.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck3", true);
                    editor.commit();
                }
            }
        });

        checkedTextViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextViewAll.isChecked()){
                    checkedTextViewHR.setChecked(false);
                    checkedTextViewKnowledge.setChecked(false);
                    checkedTextViewCode.setChecked(false);
                    checkedTextViewAll.setChecked(false);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", false);
                    editor.putBoolean("cscheck2", false);
                    editor.putBoolean("cscheck3", false);
                    editor.putBoolean("cscheck2Java", false);
                    editor.putBoolean("cscheck3Android", false);
                    editor.commit();
                    checkedTextViewJava.setVisibility(View.GONE);
                    checkedTextViewAndroid.setVisibility(View.GONE);
                } else{
                    checkedTextViewHR.setChecked(true);
                    checkedTextViewKnowledge.setChecked(true);
                    checkedTextViewCode.setChecked(true);
                    checkedTextViewAll.setChecked(true);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("cscheck1", true);
                    editor.putBoolean("cscheck2", true);
                    editor.putBoolean("cscheck3", true);
                    editor.putBoolean("cscheck2Java", true);
                    editor.putBoolean("cscheck3Android", true);
                    editor.commit();
                    checkedTextViewJava.setVisibility(View.VISIBLE);
                    checkedTextViewAndroid.setVisibility(View.VISIBLE);
                    checkedTextViewJava.setChecked(true);
                    checkedTextViewAndroid.setChecked(true);
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Function Not Supported")
                        .setMessage("Video recording is still at early development stage and is unavailable now." +
                                " Meanwhile, you can try audio recording and see how it turns out.")
                        .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.android)
                        .show();
                radioButton2.setChecked(false);
                radioButton1.setChecked(true);
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }
}
