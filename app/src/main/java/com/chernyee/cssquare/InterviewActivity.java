package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
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

import org.parceler.Parcels;

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
    private boolean endInterview = false;
    private HashMap<String, String> map = new HashMap<>();
    private int globalCount = 0;
    private boolean onCountDown = true;
    private String firstViewPager = "Hi, my name is Kimberley and I will be conducting your interview today. First of all, tell me about yourself.";
    private String secondLastViewPager = "Do you have any question for me?";
    private String lastViewPager = "Thank you for completing the mock interview and please click the end interview button to proceed.";

    private List<Question2> interviewList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final TextView countDownText = (TextView) findViewById(R.id.countDownText);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean cscheckHR = sharedPreferences.getBoolean("cscheck1", false);
        boolean cscheckKnowledge = sharedPreferences.getBoolean("cscheck2", false);
        boolean cscheckJava = sharedPreferences.getBoolean("cscheck2Java", false);
        boolean cscheckAndroid = sharedPreferences.getBoolean("cscheck2Android", false);
        boolean cscheckCode = sharedPreferences.getBoolean("cscheck3", false);
        final int seekTime = sharedPreferences.getInt("csseek", 10);
        int numQuestions = sharedPreferences.getInt("csplusminus", 10);


        interviewList = new ArrayList<>();
        interviewList.add(new Question2(firstViewPager));
        List<Question2> HRList = new ArrayList<>(QuestionList.getInterviewList("HR"));
        List<Question2> knowledgeList = new ArrayList<>(QuestionList.getInterviewList("Knowledge"));
        List<Question2> javaList = new ArrayList<>(QuestionList.getInterviewList("Java"));
        List<Question2> androidList = new ArrayList<>(QuestionList.getInterviewList("Android"));
        List<Question2> codeList = new ArrayList<>(QuestionList.getInterviewList("Coding"));
        if(cscheckJava) knowledgeList.addAll(javaList);
        if(cscheckAndroid) knowledgeList.addAll(androidList);
        Collections.shuffle(HRList);
        Collections.shuffle(codeList);
        Collections.shuffle(knowledgeList);



        if(cscheckHR && cscheckKnowledge && cscheckCode){
            filter1 = (numQuestions / 3 > HRList.size()) ? HRList.size() : numQuestions / 3 ;
            filter2 = (numQuestions - filter1 - numQuestions / 3 > knowledgeList.size())? knowledgeList.size() : numQuestions - filter1 - numQuestions / 3 ;
            filter3 = (numQuestions - filter1 - filter2 > codeList.size()) ? codeList.size() : numQuestions - filter1 - filter2;

            for(int i = 0 ; i < filter1 ; i++){
                interviewList.add(HRList.get(i));
            }
            for(int i = 0 ; i < filter2 ; i++){
                interviewList.add(knowledgeList.get(i));
            }
            for(int i = 0 ; i < filter3 ; i++){
                interviewList.add(codeList.get(i));
            }

        } else if((cscheckHR && cscheckKnowledge) || (cscheckKnowledge && cscheckCode) || cscheckHR && cscheckCode){
            if(cscheckHR && cscheckKnowledge){
                filter1 = (numQuestions / 2 >  HRList.size()) ? HRList.size() : numQuestions / 2 ;
                filter2 = (numQuestions - filter1 > knowledgeList.size())? knowledgeList.size() : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewList.add(HRList.get(i));
                }
                for(int i = 0 ; i < filter2 ; i++){
                    interviewList.add(knowledgeList.get(i));
                }

            } else if (cscheckKnowledge && cscheckCode){
                filter2 = (numQuestions / 2 > knowledgeList.size()) ? knowledgeList.size() : numQuestions / 2 ;
                filter3 = (numQuestions - filter2 > codeList.size())? codeList.size() : numQuestions - filter2;
                for(int i = 0 ; i < filter2 ; i++){
                    interviewList.add(knowledgeList.get(i));
                }
                for(int i = 0 ; i < filter3 ; i++){
                    interviewList.add(codeList.get(i));
                }

            } else if (cscheckHR && cscheckCode){
                filter1 = (numQuestions / 2 > HRList.size()) ? HRList.size() : numQuestions / 2 ;
                filter3 = (numQuestions - filter1 > codeList.size())? codeList.size() : numQuestions - filter1;
                for(int i = 0 ; i < filter1 ; i++){
                    interviewList.add(HRList.get(i));
                }
                for(int i = 0 ; i < filter3 ; i++){
                    interviewList.add(codeList.get(i));
                }
            }

        } else if(cscheckHR){
            filter1 = (numQuestions > HRList.size()) ? HRList.size() : numQuestions;
            for(int i = 0 ; i < filter1 ; i++){
                interviewList.add(HRList.get(i));
            }

        } else if(cscheckKnowledge){
            filter2 = (numQuestions > knowledgeList.size()) ? knowledgeList.size() : numQuestions;
            for(int i = 0 ; i < filter2 ; i++){
                interviewList.add(knowledgeList.get(i));
            }

        } else if(cscheckCode){
            filter3 = (numQuestions > codeList.size()) ? codeList.size() : numQuestions;
            for(int i = 0 ; i < filter3 ; i++){
                interviewList.add(codeList.get(i));
            }
        }

        interviewList.add(new Question2(secondLastViewPager));
        interviewList.add(new Question2(lastViewPager));

        new CountDownTimer(3200, 1000) {

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
                            String[] s = interviewList.get(position).getQuestion().split("[.,?]");
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


                String s[] = firstViewPager.split("[.,?]");
                for(int i = 0; i < s.length; i++){
                    tts.speak(s[i], TextToSpeech.QUEUE_ADD, map);
                    tts.playSilence(400,TextToSpeech.QUEUE_ADD, map);
                }
                onCountDown = false;
            }
        }.start();





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
                    interviewList.remove(0); // remove first
                    interviewList.remove(interviewList.size() - 1); // remove second last
                    interviewList.remove(interviewList.size() - 1); // remove last
                    Parcelable wrapped = Parcels.wrap(interviewList);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", wrapped);
                    intent.putExtras(bundle);
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
            textView.setText(interviewList.get(position).getQuestion());
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

                if(position == interviewList.size() -2){
                    view.setBackgroundResource(R.color.indicator_4);
                    littleTag.setText("Wrapping up");
                }
                if(position == interviewList.size() -1){
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
            return interviewList.size();
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