package com.chernyee.cssquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences sharedPreferences;
    private PieChart mChart;
    private TextView header;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * All the onResume() method being overriden is to make sure the data is accurate and up-to-date
     */

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.listCompleted.clear();
        MainActivity.listEasy.clear();
        MainActivity.listMedium.clear();
        MainActivity.listHard.clear();
        for(int k = 0; k < MainActivity.populateList.get(0).size(); k++){

            String markString = "cse"+ MainActivity.listId.get(k);
            int markScore = sharedPreferences.getInt(markString, 0);
            if(markScore == 1){
                MainActivity.listCompleted.add(MainActivity.populateList.get(0).get(k));
            } else{
                if(MainActivity.populateList.get(0).get(k).get(8).contains("Easy")){
                    MainActivity.listEasy.add(MainActivity.populateList.get(0).get(k));
                } else if(MainActivity.populateList.get(0).get(k).get(8).contains("Medium")){
                    MainActivity.listMedium.add(MainActivity.populateList.get(0).get(k));
                } else if(MainActivity.populateList.get(0).get(k).get(8).contains("Hard")){
                    MainActivity.listHard.add(MainActivity.populateList.get(0).get(k));
                }
            }
        }


        header.setText("Completed: " + MainActivity.listCompleted.size() + "/" + MainActivity.listId.size());
        mChart.setCenterText(generateCenterText());
        mChart.setData(generatePieData());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity.listCompleted.clear();
        MainActivity.listEasy.clear();
        MainActivity.listMedium.clear();
        MainActivity.listHard.clear();
        for(int k = 0; k < MainActivity.populateList.get(0).size(); k++){

            String markString = "cse"+ MainActivity.listId.get(k);
            int markScore = sharedPreferences.getInt(markString, 0);
            if(markScore == 1){
                MainActivity.listCompleted.add(MainActivity.populateList.get(0).get(k));
            } else{
                if(MainActivity.populateList.get(0).get(k).get(8).contains("Easy")){
                    MainActivity.listEasy.add(MainActivity.populateList.get(0).get(k));
                } else if(MainActivity.populateList.get(0).get(k).get(8).contains("Medium")){
                    MainActivity.listMedium.add(MainActivity.populateList.get(0).get(k));
                } else if(MainActivity.populateList.get(0).get(k).get(8).contains("Hard")){
                    MainActivity.listHard.add(MainActivity.populateList.get(0).get(k));
                }
            }
        }






        View v = inflater.inflate(R.layout.fragment_home, container, false);


        header = (TextView) v.findViewById(R.id.header1);
        header.setText("Completed: " + MainActivity.listCompleted.size() + "/" + MainActivity.listId.size());

        mChart = (PieChart) v.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
        mChart.setUsePercentValues(true);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setData(generatePieData());








        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {



        if (e == null)
            return;

        Intent intent = new Intent(getActivity(), SearchableActivity.class);
        if(e.getXIndex() == 0){
            intent.putExtra("extraInfo", "Hard");
        } else if(e.getXIndex() == 1){
            intent.putExtra("extraInfo", "Medium");
        } else if(e.getXIndex() == 2){
            intent.putExtra("extraInfo", "Easy");
        } else{
            intent.putExtra("extraInfo", "Completed");
        }

        startActivity(intent);

        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private SpannableString generateCenterText() {

        double percentage = MainActivity.listCompleted.size() * 1.0 / MainActivity.listId.size() * 100;
        String pct = String.format("%.1f", percentage);

        SpannableString s = new SpannableString( pct + "%\nCompleted");
        s.setSpan(new RelativeSizeSpan(2f), 0, pct.length() + 1, 0);
        return s;
    }

    private PieData generatePieData() {

        int count = 4;

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Hard");
        xVals.add("Medium");
        xVals.add("Easy");
        xVals.add("Completed");
        entries1.add(new Entry(MainActivity.listHard.size(), 0));
        entries1.add(new Entry(MainActivity.listMedium.size(), 1));
        entries1.add(new Entry(MainActivity.listEasy.size(), 2));
        entries1.add(new Entry(MainActivity.listCompleted.size(), 3));



        PieDataSet ds1 = new PieDataSet(entries1, "www.chernyee.com");
        ds1.setColors(ColorTemplate.COLORFUL_COLORS);
        ds1.setValueFormatter(new PercentFormatter());
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(xVals,ds1);

        return d;
    }



}
