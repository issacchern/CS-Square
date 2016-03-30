package com.chernyee.cssquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements OnChartValueSelectedListener {


    private SharedPreferences sharedPreferences;
    private PieChart mChart;
    private TextView header;
    private List<Question> listCompleted;
    private List<Question> listBeginner;
    private List<Question> listEasy;
    private List<Question> listMedium;
    private List<Question> listHard;

    public HomeFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        listBeginner = new ArrayList<>(QuestionList.getDifficulty("Beginner", getContext(), false));
        listEasy = new ArrayList<>(QuestionList.getDifficulty("Easy",getContext(), false));
        listMedium = new ArrayList<>(QuestionList.getDifficulty("Medium",getContext(), false));
        listHard = new ArrayList<>(QuestionList.getDifficulty("Hard",getContext(),false));
        listCompleted = new ArrayList<>(QuestionList.getDifficulty("", getContext(),true));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cscomplete", listCompleted.size());
        editor.commit();


        header.setText("Completed: " + listCompleted.size() + "/" + SplashActivity.sharedCodeList.size());
        mChart.setCenterText(generateCenterText());
        mChart.setData(generatePieData());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        listBeginner = new ArrayList<>(QuestionList.getDifficulty("Beginner", getContext(), false));
        listEasy = new ArrayList<>(QuestionList.getDifficulty("Easy",getContext(), false));
        listMedium = new ArrayList<>(QuestionList.getDifficulty("Medium",getContext(), false));
        listHard = new ArrayList<>(QuestionList.getDifficulty("Hard",getContext(),false));
        listCompleted = new ArrayList<>(QuestionList.getDifficulty("", getContext(),true));

        header = (TextView) v.findViewById(R.id.header1);
        header.setText("Completed: " + listCompleted.size() + "/" + SplashActivity.sharedCodeList.size());

        mChart = (PieChart) v.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
        mChart.setUsePercentValues(true);
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setData(generatePieData());
        return v;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null) return;
        Intent intent = new Intent(getActivity(), SearchableActivity.class);
        if(e.getXIndex() == 0){
            intent.putExtra("data","Hard");
        } else if(e.getXIndex() == 1){
            intent.putExtra("data","Medium");
        } else if(e.getXIndex() == 2){
            intent.putExtra("data","Easy");
        } else if(e.getXIndex() == 3) {
            intent.putExtra("data","Beginner");
        } else {
            intent.putExtra("data","Completed");
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

    private SpannableString generateCenterText() {

        double percentage = listCompleted.size() * 1.0 / SplashActivity.sharedCodeList.size() * 100;
        String pct = String.format("%.1f", percentage);
        SpannableString s = new SpannableString( pct + "%\nCompleted");
        s.setSpan(new RelativeSizeSpan(2f), 0, pct.length() + 1, 0);
        return s;
    }

    private PieData generatePieData() {

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Hard");
        xVals.add("Medium");
        xVals.add("Easy");
        xVals.add("Beginner");;
        xVals.add("Completed");
        entries1.add(new Entry(listHard.size(), 0));
        entries1.add(new Entry(listMedium.size(), 1));
        entries1.add(new Entry(listEasy.size(), 2));
        entries1.add(new Entry(listBeginner.size(), 3));
        entries1.add(new Entry(listCompleted.size(), 4));
        PieDataSet ds1 = new PieDataSet(entries1, "CS Square 2016");
        ds1.setColors(ColorTemplate.COLORFUL_COLORS);
        ds1.setValueFormatter(new PercentFormatter());
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        PieData d = new PieData(xVals,ds1);
        return d;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
    }
}
