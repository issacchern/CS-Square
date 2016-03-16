package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.chernyee.cssquare.Recording.RecordingSampler;
import com.chernyee.cssquare.Recording.VisualizerView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;


public class InterviewActivity extends AppCompatActivity implements
        RecordingSampler.CalculateVolumeListener{

    private Button interviewButton;
    private Button stopButton;
    private TextToSpeech tts;

    private VisualizerView visualizerView;

    private RecordingSampler mRecordingSampler;

    private CountdownView mCvCountdownView;

    private CustomPagerAdapter customAdapter;

    private IssacViewPager mPager;

    private TextView textView;

    private TextView littleTag;

    private SharedPreferences sharedPreferences;

    private int filter1 = 0;

    private int filter2= 0;

    private int filter3 = 0;

    private int seekTime = 0;

    private boolean endInterview = false;

    private HashMap<String, String> map = new HashMap<>();

    private int globalCount = 0;


    private ArrayList<String> interviewQuestionList = new ArrayList<>();


    private String [] HRQuestions = {
            "What do you know about our company?",
            "Tell me about your strength and weakness.",
            "Tell me why you choose your major.",
            "Where do you see yourself in 5 years?",
            "What companies are you interviewing with?",
            "Tell me about a time you dispute with someone.",
            "What is your expected salary?",
            "What are you looking for in terms of career development?",
            "Are you a team player?",
            "What would be your ideal working environment?",
            "Tell me about your project, internship, or any of your work experience."

    };

    private String [] TechnicalQuestions = {
            "Would you explain it to me the difference between iteration and recursion?",
            "How would you compare array and linked list?",
            "Explain to me what Bubble Sort is.",
            "Tell me the difference between stack and queue?",
            "Would you be able to design a stack using queue?",
            "Would you be able to design a queue using stack?",
            "Tell me the difference between singly linked list and doubly linked list.",
            "Explain what a binary search tree is.",
            "What is the difference between NULL and VOID?",
            "What is the primary advantage of using linked list?",
            "What is dynamic data structure? Give me an example of dynamic data structure. ",
            "Why would you choose Quick Sort over Merge Sort?",
            "What is a dequeue? How do we implement it?",
            "Given Bubble Sort, Selection Sort, and Insertion Sort, which one would you choose? Why?",
            "What is a Fibonacci search?",
            "Tell me the difference between breadth first search and depth first search?",
            "What is dynamic programming? When do we use it?",
            "How do you prevent deadlock in your program?",
            "What is the concept of object oriented programming?"


    };

    private String [] CodingQuestions = {
            "How do you find the second largest element in an array without allocating space?",
            "How do you find the third element from the end of linked list in one pass?",
            "How would you reverse a String?",
            "How to find the only duplicate element in an array? What about doing it in space?",
            "Write a program to compute Fibonnacci series, iteratively and recursively.",
            "How do you check if a string is palindrome?",
            "How to reverse linked list using iteration and recursion?",
            "Can you implement a stack using array or linked list?",
            "Find the smallest element in a binary search tree.",
            "Can you implement a square root function?",
            "How do you find the most frequent element in an Integer array?",
            "How do you write an effective program to check if a number is prime or not?"


    };





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final TextView countDownText = (TextView) findViewById(R.id.countDownText);

        new CountDownTimer(3900, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownText.setText(millisUntilFinished / 1000 + "");
            }

            public void onFinish() {
                countDownText.setVisibility(View.GONE);
                mPager.setAdapter(customAdapter);

                mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (globalCount < position) {
                            String s[] = interviewQuestionList.get(position).split("[.,?]");
                            for (int i = 0; i < s.length; i++) {
                                tts.speak(s[i], TextToSpeech.QUEUE_ADD, map);
                                tts.playSilence(500, TextToSpeech.QUEUE_ADD, map);
                            }

                            globalCount++;
                        }


                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                interviewButton.setText("End Interview");
                interviewButton.setEnabled(true);
                mRecordingSampler.startRecording();
                mCvCountdownView.start(seekTime * 60 * 1000);


                String s[] = interviewQuestionList.get(0).split("[.,?]");
                for(int i = 0; i < s.length; i++){
                    tts.speak(s[i], TextToSpeech.QUEUE_ADD, map);
                    tts.playSilence(500,TextToSpeech.QUEUE_ADD, map);
                }


            }
        }.start();



        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");



        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        boolean cscheck1 = sharedPreferences.getBoolean("cscheck1", false);
        boolean cscheck2 = sharedPreferences.getBoolean("cscheck2", false);
        boolean cscheck3 = sharedPreferences.getBoolean("cscheck3", false);
        seekTime = sharedPreferences.getInt("csseek", 10);
        int numQuestions = sharedPreferences.getInt("csplusminus", 10);


        interviewQuestionList.add("Hi, my name is Kimberley and I will be conducting your interview today. First of all, tell me about yourself.");

        Collections.shuffle(Arrays.asList(HRQuestions));
        Collections.shuffle(Arrays.asList(TechnicalQuestions));
        Collections.shuffle(Arrays.asList(CodingQuestions));

        if(cscheck1 && cscheck2 && cscheck3){

            filter1 = (numQuestions / 3 > HRQuestions.length) ? HRQuestions.length : numQuestions / 3 ;
            filter2 = (numQuestions - filter1 - numQuestions / 3 > TechnicalQuestions.length)? TechnicalQuestions.length : numQuestions - filter1 - numQuestions / 3 ;
            filter3 = (numQuestions - filter1 - filter2 > CodingQuestions.length) ? CodingQuestions.length : numQuestions - filter1 - filter2;

            for(int i = 0 ; i < filter1 ; i++){
                interviewQuestionList.add(HRQuestions[i]);
            }

            for(int i = 0 ; i < filter2 ; i++){
                interviewQuestionList.add(TechnicalQuestions[i]);
            }

            for(int i = 0 ; i < filter3 ; i++){
                interviewQuestionList.add(CodingQuestions[i]);
            }

        } else if((cscheck1 && cscheck2) || (cscheck2 && cscheck3) || cscheck1 && cscheck3){
            if(cscheck1 && cscheck2){
                filter1 = (numQuestions / 2 > HRQuestions.length) ? HRQuestions.length : numQuestions / 2 ;
                filter2 = (numQuestions - filter1 > TechnicalQuestions.length)? TechnicalQuestions.length : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewQuestionList.add(HRQuestions[i]);
                }

                for(int i = 0 ; i < filter2 ; i++){
                    interviewQuestionList.add(TechnicalQuestions[i]);
                }


            } else if (cscheck2 && cscheck3){
                filter2 = (numQuestions / 2 > TechnicalQuestions.length) ? TechnicalQuestions.length : numQuestions / 2 ;
                filter3 = (numQuestions - filter2 > CodingQuestions.length)? CodingQuestions.length : numQuestions - filter2;
                for(int i = 0 ; i < filter2 ; i++){
                    interviewQuestionList.add(TechnicalQuestions[i]);
                }

                for(int i = 0 ; i < filter3 ; i++){
                    interviewQuestionList.add(CodingQuestions[i]);
                }

            } else if (cscheck1 && cscheck3){
                filter1 = (numQuestions / 2 > HRQuestions.length) ? HRQuestions.length : numQuestions / 2 ;
                filter3 = (numQuestions - filter1 > CodingQuestions.length)? CodingQuestions.length : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewQuestionList.add(HRQuestions[i]);
                }

                for(int i = 0 ; i < filter3 ; i++){
                    interviewQuestionList.add(CodingQuestions[i]);
                }


            }


        } else if(cscheck1){
            filter1 = (numQuestions > HRQuestions.length) ? HRQuestions.length : numQuestions;
            for(int i = 0 ; i < filter1 ; i++){
                interviewQuestionList.add(HRQuestions[i]);
            }

        } else if(cscheck2){
            filter2 = (numQuestions > TechnicalQuestions.length) ? TechnicalQuestions.length : numQuestions;
            for(int i = 0 ; i < filter2 ; i++){
                interviewQuestionList.add(TechnicalQuestions[i]);
            }

        } else if(cscheck3){
            filter3 = (numQuestions > CodingQuestions.length) ? CodingQuestions.length : numQuestions;
            for(int i = 0 ; i < filter3 ; i++){
                interviewQuestionList.add(CodingQuestions[i]);
            }

        }

        interviewQuestionList.add("Do you have any question for me?");
        interviewQuestionList.add("Thank you for completing the mock interview. Please click the end interview button to proceed.");





        mPager = (IssacViewPager) findViewById(R.id.viewpager);
        customAdapter = new CustomPagerAdapter(this);




        tts = new TextToSpeech (this, new TextToSpeech.OnInitListener () {


            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                mPager.setPagingEnabled(false);
            }

            @Override
            public void onDone(String utteranceId) {
                mPager.setPagingEnabled(true);
            }

            @Override
            public void onError(String utteranceId) {

            }
        });


        visualizerView = (VisualizerView) findViewById(R.id.visualizer3);

        mRecordingSampler = new RecordingSampler();
        mRecordingSampler.setVolumeListener(this);
        mRecordingSampler.setSamplingInterval(100);
        mRecordingSampler.link(visualizerView);

        mCvCountdownView = (CountdownView)findViewById(R.id.cv_countdownViewTest1);

        mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {

                if(mRecordingSampler.isRecording()){
                    mRecordingSampler.release();
                    interviewButton.setText("View result");
                    mCvCountdownView.stop();
                }

            }
        });

        interviewButton = (Button) findViewById(R.id.interviewButton);
        interviewButton.setEnabled(false);
        interviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interviewButton.getText().equals("View result")) {
                    int remaingTime = (int) mCvCountdownView.getRemainTime();


                    Intent intent = new Intent(InterviewActivity.this, InterviewAfterActivity.class);
                    intent.putExtra("RemainingTime", remaingTime);
                    intent.putExtra("BeginningTime", seekTime * 60 * 1000);
                    intent.putExtra("QuestionList", interviewQuestionList);


                    startActivity(intent);

                } else if (mRecordingSampler.isRecording() && endInterview) {

                    mRecordingSampler.release();
                    interviewButton.setText("View result");
                    mCvCountdownView.stop();


                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewActivity.this);
                    builder1.setTitle("End Interview");
                    builder1.setMessage("You are at the middle of interview. Do you wish to end it now?");
                    builder1.setCancelable(false);


                    builder1.setPositiveButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    mRecordingSampler.release();
                                    interviewButton.setText("View result");
                                    mCvCountdownView.stop();


                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }


            }
        });




    }

    @Override
    protected void onDestroy() {

        if (mRecordingSampler.isRecording()) {
            mRecordingSampler.release();
        }

        tts.stop();
        tts.shutdown();

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        if(mRecordingSampler.isRecording()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewActivity.this);
            builder1.setTitle("End Interview");
            builder1.setMessage("You are at the middle of interview. Do you wish to end it now?");
            builder1.setCancelable(false);


            builder1.setPositiveButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            mRecordingSampler.release();
                            interviewButton.setText("Interview Ended");
                            interviewButton.setEnabled(false);
                            mCvCountdownView.stop();
                            finish();



                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else{
            super.onBackPressed();
        }


    }


    @Override
    public void onCalculateVolume(int volume) {

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.pager_interview,
                    container, false);
            container.addView(view);



            textView = (TextView) view.findViewById(R.id.text);
            littleTag = (TextView) view.findViewById(R.id.littleTag);

            textView.setText(interviewQuestionList.get(position));

            if(position <= filter1){

                view.setBackgroundResource(R.color.colorAccent);
                if(position == 0){
                    littleTag.setText("Introduction");
                } else{
                    littleTag.setText("Basic HR question");
                }

            } else if(position <= filter1 + filter2){
                view.setBackgroundResource(R.color.nice_green);
                littleTag.setText("Knowledge-based question");
            } else{
                view.setBackgroundResource(R.color.indicator_3);
                littleTag.setText("Coding-based question");

                if(position == interviewQuestionList.size() -2){
                    view.setBackgroundResource(R.color.indicator_4);
                    littleTag.setText("Wrapping up");
                }

                if(position == interviewQuestionList.size() -1){
                    view.setBackgroundResource(R.color.pink_pressed);
                    littleTag.setText("End of interview");
                    endInterview = true;
                }
            }




            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        public int getCount() {
            return interviewQuestionList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }



        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();



        }

    }


}