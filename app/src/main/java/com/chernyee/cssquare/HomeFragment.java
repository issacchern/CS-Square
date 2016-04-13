package com.chernyee.cssquare;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.ApiData.QodApi;
import com.chernyee.cssquare.ApiData.QodData;
import com.chernyee.cssquare.UI.SlideAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import so.orion.slidebar.GBSlideBar;
import so.orion.slidebar.GBSlideBarListener;


public class HomeFragment extends Fragment {

    private String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    private static final String SCENERY_URL = "http://www.chernyee.com/scenery/";
    private static final String BASE_URL = "http://quotes.rest/";
    private SharedPreferences sharedPreferences;
    private GBSlideBar gbSlideBar;
    private SlideAdapter mAdapter;
    private PieChart mPieChart;
    private BarChart mBarChart;
    private ScrollView ll;
    private TextView header;
    private int qCompleted;
    private int qBeginner;
    private int qEasy;
    private int qMedium;
    private int qHard;

    private TextView quoteView;
    private ImageView backgroundImage;
    private TextView authorView;
    private Retrofit retrofit;
    private TextView imageTextButton;
    private TextView flashcardTextButton;
    private TextView bookmarkTextButton;

    public HomeFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        qBeginner = QuestionList.getRemaining("Beginner", getContext(), false);
        qEasy = QuestionList.getRemaining("Easy", getContext(), false);
        qMedium = QuestionList.getRemaining("Medium", getContext(), false);
        qHard = QuestionList.getRemaining("Hard", getContext(), false);
        qCompleted = QuestionList.getRemaining("Complete", getContext(), true);


        header.setText("Completed: " + qCompleted + "/" + VarInit.getSharedCodeListInstance().size());
        mPieChart.setCenterText(generateCenterText());
        mPieChart.setData(generatePieData());
        mBarChart.setData(generateBarData());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayDate = sdf.format(date);

        if(todayDate.equals(sharedPreferences.getString("csdate", ""))){
            // do nothing
        } else{
            if(MainActivity.isNetworkAvailable(getContext())){
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                QodApi qodApi = retrofit.create(QodApi.class);
                Call<QodData> call = qodApi.getQodApi("inspire");
                call.enqueue(new Callback<QodData>() {
                    @Override
                    public void onResponse(Call<QodData> call, Response<QodData> response) {

                        if(response.isSuccessful()){
                            String title = response.body().getContents().getQuotes().get(0).getQuote();

                            Calendar c = Calendar.getInstance();
                            String url = SCENERY_URL + c.get(Calendar.DAY_OF_MONTH) +".jpg";
                            String author = response.body().getContents().getQuotes().get(0).getAuthor();
                            quoteView.setText(title);
                            authorView.setText(author);
                            Picasso.with(getContext()).load(url).into(backgroundImage);

                            // update shared preference
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("csdate", todayDate);
                            editor.putString("csqobtitle", title);
                            editor.putString("csqobimage", url);
                            editor.putString("csqobauthor", author);
                            editor.commit();

                        } else{
                            Toast.makeText(getContext(), "Error on the server side!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<QodData> call, Throwable t) {

                    }
                });

            }
        }

        qBeginner = QuestionList.getRemaining("Beginner", getContext(), false);
        qEasy = QuestionList.getRemaining("Easy", getContext(), false);
        qMedium = QuestionList.getRemaining("Medium", getContext(), false);
        qHard = QuestionList.getRemaining("Hard", getContext(), false);
        qCompleted = QuestionList.getRemaining("Complete", getContext(), true);
    }

    public void GeneratePie(){

        header.setText("Completed: " + qCompleted + "/" + VarInit.getSharedCodeListInstance().size());
        mPieChart.setDescription("");
        mPieChart.setCenterText(generateCenterText());
        mPieChart.setCenterTextSize(10f);
        mPieChart.setUsePercentValues(true);
        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(50f);
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                if (e == null) return;
                Intent intent = new Intent(getActivity(), SearchableActivity.class);
                if (e.getXIndex() == 0) {
                    intent.putExtra("data", "Hard");
                } else if (e.getXIndex() == 1) {
                    intent.putExtra("data", "Medium");
                } else if (e.getXIndex() == 2) {
                    intent.putExtra("data", "Easy");
                } else if (e.getXIndex() == 3) {
                    intent.putExtra("data", "Beginner");
                } else {
                    intent.putExtra("data", "Completed");
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
        });
        mPieChart.setData(generatePieData());
    }

    public void GenerateBar(){

        mBarChart.setDescription("");


        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(true);
        mBarChart.setScaleEnabled(true);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);

        YAxisValueFormatter myFormatter = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf((int)value);
            }
        };



        YAxis left = mBarChart.getAxisLeft();
        left.setAxisMinValue(0);
        left.setAxisMaxValue(10);
        left.setStartAtZero(true);
        left.setValueFormatter(myFormatter);


        YAxis right = mBarChart.getAxisRight();
        right.setAxisMinValue(0);
        right.setAxisMaxValue(10);
        right.setStartAtZero(true);
        right.setValueFormatter(myFormatter);

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        mBarChart.setData(generateBarData());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        ll = (ScrollView) v.findViewById(R.id.home);
        mPieChart = (PieChart) v.findViewById(R.id.chart1);
        mBarChart = (BarChart) v.findViewById(R.id.chart2);
        header = (TextView) v.findViewById(R.id.header1);
        quoteView = (TextView) v.findViewById(R.id.quote);
        backgroundImage = (ImageView) v.findViewById(R.id.backgroundImage);
        authorView = (TextView) v.findViewById(R.id.author);

        imageTextButton = (TextView) v.findViewById(R.id.imageTextButton);
        flashcardTextButton =(TextView) v.findViewById(R.id.flashcardTextButton);
        bookmarkTextButton = (TextView) v.findViewById(R.id.bookmarkTextButton);

        imageTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), JokesActivity.class);
                startActivity(intent);
            }
        });

        flashcardTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FlashCardActivity.class);
                startActivity(intent);
            }
        });

        bookmarkTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchableActivity.class);
                intent.putExtra("data", "Bookmarked");
                startActivity(intent);
            }
        });


        quoteView.setText(sharedPreferences.getString("csqobtitle", "You are awesome the way you are!"));
        Picasso.with(getContext()).load(sharedPreferences.getString("csqobimage",
                "http://i.imgur.com/DvpvklR.png")).into(backgroundImage);
        authorView.setText(sharedPreferences.getString("csqobauthor", "Anonymous"));

        backgroundImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                String names[] ={"Share the quote","Download background image"};

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.list, null);
                final ListView lv = (ListView) dialoglayout.findViewById(R.id.list);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder((MainActivity) getActivity());
                alertDialog.setView(dialoglayout);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getActivity(),android.R.layout.simple_list_item_1,names);
                lv.setAdapter(adapter);


                final AlertDialog dialog = alertDialog.show();


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) { // share code

                            String quote = sharedPreferences.getString("csqobtitle", "");
                            String author = sharedPreferences.getString("csqobauthor", "");
                            String cliptoBoard = quote + " - " + author;
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, cliptoBoard);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, "Share the quote"));
                        } else { // download image

                            Picasso.with(getContext()).load(sharedPreferences.getString("csqobimage", ""))
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            String fileName = sharedPreferences.getString("csdate", "");

                                            File file = new File(SplashActivity.IMAGES_PATH + "/" + fileName + ".jpg");
                                            try {
                                                file.createNewFile();
                                                FileOutputStream ostream = new FileOutputStream(file);
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                                ostream.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                Toast.makeText(getActivity(), "Image downloaded to"
                                                        + SplashActivity.CS_FOLDER_PATH
                                                        + SplashActivity.IMAGES_PATH, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    });
                        }
                        dialog.dismiss();
                    }
                });


                return false;
            }
        });



        GeneratePie();
        GenerateBar();


        gbSlideBar = (GBSlideBar) v.findViewById(R.id.gbslidebar);
        Resources resources = getResources();
        mAdapter = new SlideAdapter(resources, new int[]{
                R.drawable.btn_tag_selector,
                R.drawable.btn_more_selector,
                R.drawable.btn_reject_selector});

        mAdapter.setTextColor(new int[]{
                resources.getColor(R.color.colorAccent),
                resources.getColor(R.color.nice_green),
                resources.getColor(R.color.nice_orange)
        });

        gbSlideBar.setAdapter(mAdapter);
        gbSlideBar.setOnGbSlideBarListener(new GBSlideBarListener() {
            @Override
            public void onPositionSelected(int position) {
                if (position == 0) {
                    ll.setVisibility(View.GONE);
                    mPieChart.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    mBarChart.setVisibility(View.GONE);
                    mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                } else if (position == 1) {
                    mBarChart.setVisibility(View.GONE);
                    mPieChart.setVisibility(View.GONE);
                    header.setVisibility(View.GONE);
                    ll.animate().alpha(1.0f);
                    ll.setVisibility(View.VISIBLE);

                } else if (position == 2) {
                    mPieChart.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    mBarChart.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    mBarChart.animateY(1400);

                }
            }
        });



        gbSlideBar.setPosition(1);
        header.animate().alpha(0.0f);
        mPieChart.animate().alpha(0.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                header.setVisibility(View.GONE);
                mPieChart.setVisibility(View.GONE);
            }
        }, 100);

        return v;
    }



    private SpannableString generateCenterText() {

        double percentage = qCompleted * 1.0 / VarInit.getSharedCodeListInstance().size() * 100;
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
        entries1.add(new Entry(qHard, 0));
        entries1.add(new Entry(qMedium, 1));
        entries1.add(new Entry(qEasy, 2));
        entries1.add(new Entry(qBeginner, 3));
        entries1.add(new Entry(qCompleted, 4));
        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(ColorTemplate.COLORFUL_COLORS);
        ds1.setValueFormatter(new PercentFormatter());
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        PieData d = new PieData(xVals,ds1);
        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int yCount = 0, yTempHigh = 0;
        for (int i = 0; i < 12 ; i++){
            int j = i + 1; // stored value is actually one
            int month = sharedPreferences.getInt("csmonth" + j ,-1); // default value not 0!
            if(month >= 0){ // month i
                xVals.add(mMonths[i % 12]);
                yVals1.add(new BarEntry(month, yCount++));
                if(month > 10 && month > yTempHigh){
                    yTempHigh = month;
                    mBarChart.getAxisLeft().setAxisMaxValue(Math.round(month));
                    mBarChart.getAxisRight().setAxisMaxValue(Math.round(month));
                }
            }
        }


        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData d = new BarData(xVals, dataSets);
        return d;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
    }
}
