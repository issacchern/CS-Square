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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chernyee.cssquare.Recording.RecordingSampler;
import com.chernyee.cssquare.Recording.VisualizerView;
import com.chernyee.cssquare.UI.IssacViewPager;
import com.chernyee.cssquare.UI.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import cn.iwgang.countdownview.CountdownView;


public class InterviewActivity extends AppCompatActivity implements
        RecordingSampler.CalculateVolumeListener{

    private Button interviewButton;
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
    private boolean onCountDown = true;

    private ArrayList<String> interviewQuestionList = new ArrayList<>();
    private ArrayList<String> interviewAnswerList = new ArrayList<>();
    private ArrayList<String> interviewCategoryList = new ArrayList<>();
    private List<List<String>> interviewKnowledgeAll = new ArrayList<List<String>>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final TextView countDownText = (TextView) findViewById(R.id.countDownText);
        new CountDownTimer(3900, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownText.setText(millisUntilFinished / 1000 + "");
            }

            public void onFinish() {
                countDownText.setVisibility(View.GONE);
                mPager.setAdapter(customAdapter);
                mPager.setPageTransformer(true, new ZoomOutPageTransformer());
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
                                tts.playSilence(400, TextToSpeech.QUEUE_ADD, map);
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
                    tts.playSilence(400,TextToSpeech.QUEUE_ADD, map);
                }

                onCountDown = false;

            }
        }.start();

        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean cscheck1 = sharedPreferences.getBoolean("cscheck1", false);
        boolean cscheck2 = sharedPreferences.getBoolean("cscheck2", false);
        boolean cscheck2Java = sharedPreferences.getBoolean("cscheck2Java", false);
        boolean cscheck2Android = sharedPreferences.getBoolean("cscheck2Android", false);
        boolean cscheck3 = sharedPreferences.getBoolean("cscheck3", false);
        seekTime = sharedPreferences.getInt("csseek", 10);
        int numQuestions = sharedPreferences.getInt("csplusminus", 10);
        interviewKnowledgeAll.addAll(MainActivity.interviewKnowledge);
        if(cscheck2Java) interviewKnowledgeAll.addAll(MainActivity.interviewKnowledgeJava);
        if(cscheck2Android) interviewKnowledgeAll.addAll(MainActivity.interviewKnowledgeAndroid);

        Collections.shuffle(MainActivity.interviewHR);
        Collections.shuffle(MainActivity.interviewCoding);
        Collections.shuffle(interviewKnowledgeAll);

        interviewAnswerList.clear();
        interviewQuestionList.clear();
        interviewCategoryList.clear();
        interviewQuestionList.add("Hi, my name is Kimberley and I will be conducting your interview today. First of all, tell me about yourself.");

        if(cscheck1 && cscheck2 && cscheck3){
            filter1 = (numQuestions / 3 > MainActivity.interviewHR.size()) ? MainActivity.interviewHR.size() : numQuestions / 3 ;
            filter2 = (numQuestions - filter1 - numQuestions / 3 > interviewKnowledgeAll.size())? interviewKnowledgeAll.size() : numQuestions - filter1 - numQuestions / 3 ;
            filter3 = (numQuestions - filter1 - filter2 > MainActivity.interviewCoding.size()) ? MainActivity.interviewCoding.size() : numQuestions - filter1 - filter2;

            for(int i = 0 ; i < filter1 ; i++){
                interviewQuestionList.add(MainActivity.interviewHR.get(i).get(1));
                interviewAnswerList.add(MainActivity.interviewHR.get(i).get(2));
                interviewCategoryList.add(MainActivity.interviewHR.get(i).get(3));
            }

            for(int i = 0 ; i < filter2 ; i++){
                interviewQuestionList.add(interviewKnowledgeAll.get(i).get(1));
                interviewAnswerList.add(interviewKnowledgeAll.get(i).get(2));
                interviewCategoryList.add(interviewKnowledgeAll.get(i).get(3));
            }

            for(int i = 0 ; i < filter3 ; i++){
                interviewQuestionList.add(MainActivity.interviewCoding.get(i).get(1));
                interviewAnswerList.add(MainActivity.interviewCoding.get(i).get(2));
                interviewCategoryList.add(MainActivity.interviewCoding.get(i).get(3));
            }

        } else if((cscheck1 && cscheck2) || (cscheck2 && cscheck3) || cscheck1 && cscheck3){
            if(cscheck1 && cscheck2){
                filter1 = (numQuestions / 2 >  MainActivity.interviewHR.size()) ?  MainActivity.interviewHR.size() : numQuestions / 2 ;
                filter2 = (numQuestions - filter1 > interviewKnowledgeAll.size())? interviewKnowledgeAll.size() : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewQuestionList.add(MainActivity.interviewHR.get(i).get(1));
                    interviewAnswerList.add(MainActivity.interviewHR.get(i).get(2));
                    interviewCategoryList.add(MainActivity.interviewHR.get(i).get(3));
                }

                for(int i = 0 ; i < filter2 ; i++){
                    interviewQuestionList.add(interviewKnowledgeAll.get(i).get(1));
                    interviewAnswerList.add(interviewKnowledgeAll.get(i).get(2));
                    interviewCategoryList.add(interviewKnowledgeAll.get(i).get(3));
                }


            } else if (cscheck2 && cscheck3){
                filter2 = (numQuestions / 2 > interviewKnowledgeAll.size()) ? interviewKnowledgeAll.size() : numQuestions / 2 ;
                filter3 = (numQuestions - filter2 > MainActivity.interviewCoding.size())? MainActivity.interviewCoding.size() : numQuestions - filter2;
                for(int i = 0 ; i < filter2 ; i++){
                    interviewQuestionList.add(interviewKnowledgeAll.get(i).get(1));
                    interviewAnswerList.add(interviewKnowledgeAll.get(i).get(2));
                    interviewCategoryList.add(interviewKnowledgeAll.get(i).get(3));
                }

                for(int i = 0 ; i < filter3 ; i++){
                    interviewQuestionList.add(MainActivity.interviewCoding.get(i).get(1));
                    interviewAnswerList.add(MainActivity.interviewCoding.get(i).get(2));
                    interviewCategoryList.add(MainActivity.interviewCoding.get(i).get(3));
                }

            } else if (cscheck1 && cscheck3){
                filter1 = (numQuestions / 2 > MainActivity.interviewHR.size()) ?MainActivity.interviewHR.size() : numQuestions / 2 ;
                filter3 = (numQuestions - filter1 > MainActivity.interviewCoding.size())? MainActivity.interviewCoding.size() : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewQuestionList.add(MainActivity.interviewHR.get(i).get(1));
                    interviewAnswerList.add(MainActivity.interviewHR.get(i).get(2));
                    interviewCategoryList.add(MainActivity.interviewHR.get(i).get(3));
                }

                for(int i = 0 ; i < filter3 ; i++){
                    interviewQuestionList.add(MainActivity.interviewCoding.get(i).get(1));
                    interviewAnswerList.add(MainActivity.interviewCoding.get(i).get(2));
                    interviewCategoryList.add(MainActivity.interviewCoding.get(i).get(3));
                }


            }


        } else if(cscheck1){
            filter1 = (numQuestions > MainActivity.interviewHR.size()) ? MainActivity.interviewHR.size() : numQuestions;
            for(int i = 0 ; i < filter1 ; i++){
                interviewQuestionList.add(MainActivity.interviewHR.get(i).get(1));
                interviewAnswerList.add(MainActivity.interviewHR.get(i).get(2));
                interviewCategoryList.add(MainActivity.interviewHR.get(i).get(3));
            }

        } else if(cscheck2){
            filter2 = (numQuestions > interviewKnowledgeAll.size()) ? interviewKnowledgeAll.size() : numQuestions;
            for(int i = 0 ; i < filter2 ; i++){
                interviewQuestionList.add(interviewKnowledgeAll.get(i).get(1));
                interviewAnswerList.add(interviewKnowledgeAll.get(i).get(2));
                interviewCategoryList.add(interviewKnowledgeAll.get(i).get(3));
            }

        } else if(cscheck3){
            filter3 = (numQuestions > MainActivity.interviewCoding.size()) ? MainActivity.interviewCoding.size() : numQuestions;
            for(int i = 0 ; i < filter3 ; i++){
                interviewQuestionList.add(MainActivity.interviewCoding.get(i).get(1));
                interviewAnswerList.add(MainActivity.interviewCoding.get(i).get(2));
                interviewCategoryList.add(MainActivity.interviewCoding.get(i).get(3));
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

                if (mRecordingSampler.isRecording()) {
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
                    intent.putExtra("AnswerList", interviewAnswerList);
                    intent.putExtra("CategoryList", interviewCategoryList);
                    startActivity(intent);
                    finish();

                } else if (mRecordingSampler.isRecording() && endInterview) {

                    mRecordingSampler.release();
                    interviewButton.setText("View result");
                    mCvCountdownView.stop();


                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewActivity.this);
                    builder1.setTitle("End Interview");
                    builder1.setMessage("You are at the middle of interview. Do you wish to end it now?");
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

        if(!onCountDown){
            if(mRecordingSampler.isRecording()){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(InterviewActivity.this);
                builder1.setTitle("End Interview");
                builder1.setMessage("You are at the middle of interview. Do you wish to end it now?");
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