package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import org.codechimp.apprater.AppRater;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.chernyee.cssquare.Utility.AwsUtil;
import com.chernyee.cssquare.Utility.DatabaseHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
            AboutFragment.OnFragmentInteractionListener,BookmarkFragment.OnFragmentInteractionListener,
            ToolFragment.OnFragmentInteractionListener, PreparationFragment.OnFragmentInteractionListener,
            InterviewFragment.OnFragmentInteractionListener, ShortQAFragment.OnFragmentInteractionListener,
            SettingsFragment.OnFragmentInteractionListener{

    public static String [] code_tag = {"All", "Array","String", "Hash table" , "Linked List", "Stack",
            "Tree" ,"Binary Search", "Backtracking", "DP" , "DFS", "BFS", "Greedy", "Design","Divide and Conquer","Sort", "Math", "Bit Manipulation"};

    private Cursor codingQuestionsCursor;
    private Cursor interviewQuestionsCursor;
    private DatabaseHelper db;

    private SharedPreferences sharedPreferences;

    private AmazonS3Client s3;
    private List<S3ObjectSummary> s3ObjList;
    private TransferUtility transferUtility;
    private ProgressDialog progressDialog;

    private boolean doubleBackToExitPressedOnce = false;

    public static HashMap<Integer, List<List<String>>> populateList = new HashMap<>();

    public static List<String> listId = new ArrayList<>();
    public static List<String> listTitle = new ArrayList<>();
    public static List<String> listDescription = new ArrayList<>();
    public static List<String> listCode = new ArrayList<>();
    public static List<String> listAnswer = new ArrayList<>();
    public static List<String> listHint = new ArrayList<>();
    public static List<String> listTag = new ArrayList<>();
    public static List<String> listCategory = new ArrayList<>();
    public static List<String> listDifficulty = new ArrayList<>();
    public static List<String> listAdditional = new ArrayList<>();

    public static List<List<String>> listBeginner = new ArrayList<List<String>>();
    public static List<List<String>> listEasy = new ArrayList<List<String>>();
    public static List<List<String>> listMedium = new ArrayList<List<String>>();
    public static List<List<String>> listHard = new ArrayList<List<String>>();
    public static List<List<String>> listCompleted = new ArrayList<List<String>>();

    public static List<List<String>> interviewHR = new ArrayList<List<String>>();
    public static List<List<String>> interviewKnowledge = new ArrayList<List<String>>();
    public static List<List<String>> interviewKnowledgeJava = new ArrayList<List<String>>();
    public static List<List<String>> interviewKnowledgeAndroid = new ArrayList<List<String>>();
    public static List<List<String>> interviewCoding = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            Log.v("EXIT", "EXIT");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppRater.app_launched(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);



        try{
            db = new DatabaseHelper(this);
            codingQuestionsCursor = db.getCodingQuestions();
            interviewQuestionsCursor = db.getInterviewQuestions();
            db.close();

        } catch(Exception e){
            File file = new File(SplashActivity.databasePath + "/" + "Questions.db");
            if(file.exists()) file.delete();
        }

        initializeVariables();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();


        if(listId.size() == 0){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Download Database");
            builder1.setMessage("You have empty database. Do you wish to download data now?");
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

                            if(isNetworkAvailable(getApplicationContext())){

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        s3 = AwsUtil.getS3Client(MainActivity.this);
                                        s3ObjList = s3.listObjects("sabi-data").getObjectSummaries();
                                        for (S3ObjectSummary summary : s3ObjList) {

                                            if (summary.getKey().length() < 3) {
                                                int onlineVersion = Integer.parseInt(summary.getKey());

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt("csdbversion", onlineVersion);
                                                editor.commit();
                                            } else {
                                                File file = new File(SplashActivity.databasePath + "/" + "Questions.db");
                                                transferUtility = AwsUtil.getTransferUtility(MainActivity.this);
                                                TransferObserver observer = transferUtility.download("sabi-data", "Questions.db", file);

                                                observer.setTransferListener(new TransferListener() {

                                                    public void onStateChanged(int id, String newState) {

                                                    }

                                                    @Override
                                                    public void onStateChanged(int i, TransferState transferState) {

                                                        if (transferState.toString().equals("IN_PROGRESS")) {

                                                            progressDialog = ProgressDialog.show(MainActivity.this, "Downloading data",
                                                                    "Please wait", true);

                                                        } else if (transferState.toString().equals("COMPLETED")) {

                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

                                                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                                                    }

                                                    public void onError(int id, Exception e) {// Do something in the callback.

                                                    }
                                                });

                                            }
                                        }
                                    }
                                }).start();
                            } else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setTitle("No Internet Access");
                                builder1.setMessage("You need to connect to Internet to download the data.");
                                builder1.setCancelable(false);
                                builder1.setNeutralButton(
                                        "Okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();


                            }



                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else{

            if(isNetworkAvailable(getApplicationContext())){

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        s3 = AwsUtil.getS3Client(MainActivity.this);
                        s3ObjList = s3.listObjects("sabi-data").getObjectSummaries();

                        int currentVersion = sharedPreferences.getInt("csdbversion", 7);

                        int onlineVersion = 0;

                        for (S3ObjectSummary summary : s3ObjList) {

                            if (summary.getKey().length() < 3) {
                                onlineVersion = Integer.parseInt(summary.getKey());

                            } else {

                                if(onlineVersion > currentVersion && isNetworkAvailable(getApplicationContext())){
                                    final int onlineVersion2 = onlineVersion;

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                            builder1.setTitle("New Update");
                                            builder1.setMessage("New data update is available. Do you wish to download the data now?");
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


                                                            File file = new File(SplashActivity.databasePath + "/" + "Questions.db");
                                                            transferUtility = AwsUtil.getTransferUtility(MainActivity.this);
                                                            TransferObserver observer = transferUtility.download("sabi-data", "Questions.db", file);

                                                            observer.setTransferListener(new TransferListener() {

                                                                public void onStateChanged(int id, String newState) {

                                                                }

                                                                @Override
                                                                public void onStateChanged(int i, TransferState transferState) {


                                                                    if (transferState.toString().equals("IN_PROGRESS")) {

                                                                        progressDialog = ProgressDialog.show(MainActivity.this, "Downloading data",
                                                                                "Please wait", true);

                                                                    } else if (transferState.toString().equals("COMPLETED")) {

                                                                        progressDialog.dismiss();
                                                                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }

                                                                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                                                                }

                                                                public void onError(int id, Exception e) {// Do something in the callback.

                                                                }
                                                            });

                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putInt("csdbversion", onlineVersion2);
                                                            editor.commit();


                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }


                            }
                        }
                    }
                }).start();
            }



        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBackToExitPressedOnce){
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce =true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.search){

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navi_home) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    HomeFragment fragment = new HomeFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();

                }
            }, 300);

        } else if(id == R.id.navi_prepare){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    PreparationFragment fragment = new PreparationFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }

            }, 300);

        } else if (id == R.id.navi_coding) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    SlidingTabFragment fragment = new SlidingTabFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();

                }
            }, 300);



        } else if(id == R.id.navi_bookmark){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BookmarkFragment fragment = new BookmarkFragment();
            transaction.replace(R.id.main_fragment, fragment);
            transaction.commit();


        } else if(id == R.id.navi_interview){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    InterviewFragment fragment = new InterviewFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }
            }, 300);


        } else if (id == R.id.navi_questions){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    ShortQAFragment fragment = new ShortQAFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }
            }, 300);




        } else if(id == R.id.navi_tool){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    ToolFragment fragment = new ToolFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }
            }, 300);




        } else if(id == R.id.navi_setting){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    SettingsFragment fragment = new SettingsFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }
            }, 300);

        } else if(id == R.id.navi_about){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    AboutFragment fragment = new AboutFragment();
                    transaction.replace(R.id.main_fragment, fragment);
                    transaction.commit();
                }
            }, 300);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initializeVariables(){

        listId.clear();
        listTitle.clear();
        listDescription.clear();
        listCode.clear();
        listAnswer.clear();
        listId.clear();
        listHint.clear();
        listTag.clear();
        listCategory.clear();
        listDifficulty.clear();
        listAdditional.clear();
        populateList.clear();
        listBeginner.clear();
        listEasy.clear();
        listMedium.clear();
        listHard.clear();
        listCompleted.clear();
        interviewHR.clear();
        interviewCoding.clear();
        interviewKnowledge.clear();
        interviewKnowledgeJava.clear();
        interviewKnowledgeAndroid.clear();

        if(codingQuestionsCursor != null && codingQuestionsCursor.moveToFirst()){
            do{
                listId.add(codingQuestionsCursor.getString(0));
                listTitle.add(codingQuestionsCursor.getString(1));
                listDescription.add(codingQuestionsCursor.getString(2));
                listCode.add(codingQuestionsCursor.getString(3));
                listAnswer.add(new String(codingQuestionsCursor.getBlob(4)));
                listHint.add(codingQuestionsCursor.getString(5));
                listTag.add(codingQuestionsCursor.getString(6));
                listCategory.add(codingQuestionsCursor.getString(7));
                listDifficulty.add(codingQuestionsCursor.getString(8));
                listAdditional.add(codingQuestionsCursor.getString(9));

            }while(codingQuestionsCursor.moveToNext());

            codingQuestionsCursor.close();
        }

        if(interviewQuestionsCursor != null && interviewQuestionsCursor.moveToFirst()){


            do{
                List<String> tempArray = new ArrayList<>();
                tempArray.add(interviewQuestionsCursor.getString(0));
                tempArray.add(interviewQuestionsCursor.getString(1));
                tempArray.add(interviewQuestionsCursor.getString(2));
                tempArray.add(interviewQuestionsCursor.getString(3));
                tempArray.add(interviewQuestionsCursor.getString(4));

                if(tempArray.get(3).equals("HR")){
                    interviewHR.add(tempArray);
                } else if(tempArray.get(3).equals("Knowledge")){
                    if(tempArray.get(4).equals("Java")){
                        interviewKnowledgeJava.add(tempArray);
                    } else if(tempArray.get(4).equals("Android")){
                        interviewKnowledgeAndroid.add(tempArray);
                    } else{
                        interviewKnowledge.add(tempArray);
                    }

                } else if(tempArray.get(3).equals("Coding")){
                    interviewCoding.add(tempArray);
                }

            }while(interviewQuestionsCursor.moveToNext());

            interviewQuestionsCursor.close();

        }




        for(int i = 0; i < code_tag.length; i++){

            List<List<String>> tempListList = new ArrayList<>();
            for(int j = 0; j < listId.size(); j++){

                if(i == 0){
                    List<String> tempList = new ArrayList<>();
                    tempList.add(listId.get(j));
                    tempList.add(listTitle.get(j));
                    tempList.add(listDescription.get(j));
                    tempList.add(listCode.get(j));
                    tempList.add(listAnswer.get(j));
                    tempList.add(listHint.get(j));
                    tempList.add(listTag.get(j));
                    tempList.add(listCategory.get(j));
                    tempList.add(listDifficulty.get(j));
                    tempList.add(listAdditional.get(j));
                    tempListList.add(tempList);
                }
                else if(listTag.get(j).contains(code_tag[i])){
                    List<String> tempList = new ArrayList<>();
                    tempList.add(listId.get(j));
                    tempList.add(listTitle.get(j));
                    tempList.add(listDescription.get(j));
                    tempList.add(listCode.get(j));
                    tempList.add(listAnswer.get(j));
                    tempList.add(listHint.get(j));
                    tempList.add(listTag.get(j));
                    tempList.add(listCategory.get(j));
                    tempList.add(listDifficulty.get(j));
                    tempList.add(listAdditional.get(j));
                    tempListList.add(tempList);
                }
            }

            Collections.sort(tempListList, new Comparator<List<String>>() {
                @Override
                public int compare(List<String> lhs, List<String> rhs) {
                    return lhs.get(1).compareTo(rhs.get(1));
                }
            });

            populateList.put(i,tempListList);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("csnoads", 1);
        editor.commit();
        editor.putInt("csmedium", 30);
        editor.commit();
        editor.putInt("cshard", 60);
        editor.commit();
        editor.putInt("cscomplete", listCompleted.size());
        editor.commit();

        Log.v("LISTCOMPLETED ACTIVITY", listCompleted.size() + "");
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
