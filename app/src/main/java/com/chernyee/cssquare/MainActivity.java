package com.chernyee.cssquare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import org.codechimp.apprater.AppRater;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import us.feras.mdv.MarkdownView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
            AboutFragment.OnFragmentInteractionListener,BookmarkFragment.OnFragmentInteractionListener,
            ToolFragment.OnFragmentInteractionListener, PreparationFragment.OnFragmentInteractionListener,
            InterviewFragment.OnFragmentInteractionListener, ShortQAFragment.OnFragmentInteractionListener{



    public static String [] code_tag = {"All", "Array","String", "Hash table" , "Linked List", "Stack",
            "Tree" ,"Binary Search", "Backtracking", "DP" , "DFS", "BFS", "Greedy", "Design","Divide and Conquer","Sort", "Math", "Bit Manipulation"};

    private Cursor codingQuestionsCursor;
    private Cursor interviewQuestionsCursor;
    private DatabaseHelper db;

    private SharedPreferences sharedPreferences;

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

        db = new DatabaseHelper(this);
        db.setForcedUpgrade();

        codingQuestionsCursor = db.getCodingQuestions();
        interviewQuestionsCursor = db.getInterviewQuestions();

        db.close();

        initializeVariables();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

            Intent intent = new Intent(this, SettingsActivity.class);
           startActivity(intent);



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
        listEasy.clear();
        listMedium.clear();
        listHard.clear();
        listCompleted.clear();
        interviewHR.clear();
        interviewCoding.clear();
        interviewKnowledge.clear();
        interviewKnowledgeJava.clear();
        interviewKnowledgeAndroid.clear();

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
        editor.putInt("csnoads", 0);
        editor.commit();
        editor.putInt("csmedium", 30);
        editor.commit();
        editor.putInt("cshard", 60);
        editor.commit();
        editor.putInt("cscomplete", listCompleted.size());
        editor.commit();
    }
}
