package com.chernyee.cssquare;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    public static String [] snackBarChoices = {"Have you tried it yourself yet?", "I am sure you can do better!",
        "Just give it some time!" , "Don't give up, keep on trying!"};

    public static String [] code_tag = {"All", "Array", "String", "Linked List", "Tree" , "DP" , "DFS", "BFS", "Greedy"};




    private Cursor codingQuestionsCursor;
    private DatabaseHelper db;

    private ListView lvUsers;
    private ListAdapter adapter;

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

    public static void setPopulateList(){

        for(int i = 0; i < code_tag.length; i++){

            List<List<String>> tempListList = new ArrayList<>();
            for(int j = 0; j < listId.size(); j++){

                if(i == 0){
                    List<String> tempList = new ArrayList<>();
                    tempList.add(listTitle.get(j));
                    tempList.add(listTag.get(j));
                    tempList.add(listDifficulty.get(j));
                    tempListList.add(tempList);
                }
                else if(listTag.get(j).contains(code_tag[i])){
                    List<String> tempList = new ArrayList<>();
                    tempList.add(listTitle.get(j));
                    tempList.add(listTag.get(j));
                    tempList.add(listDifficulty.get(j));
                    tempListList.add(tempList);
                }


            }
            populateList.put(i,tempListList);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Snackbar.make(view, snackBarChoices[(int) (Math.random() * 100) % 4], Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        db = new DatabaseHelper(this);
        db.setForcedUpgrade();

        codingQuestionsCursor = db.getEmployees();


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



        setPopulateList();




        String highlighted = listAnswer.get(20);

        SyntaxHighlighter shl = new SyntaxHighlighter(highlighted);
        TextView text1 = (TextView) findViewById(R.id.code);
        text1.setText(shl.formatToHtml());

        int lineNumber = countLines(highlighted);
        String printLineNumber= "";
        for(int i = 1; i <= lineNumber; i++){
            if(i < lineNumber)
                printLineNumber += (i < 10) ? " " + i + "\n" : "" + i + "\n" ;
            else printLineNumber += (i < 10) ? " " + i : "" + i;
        }

        TextView text2 = (TextView) findViewById(R.id.codeNumber);
        text2.setText(printLineNumber);






//        DatabaseAccess mDbHelper = new DatabaseAccess(this);
//        mDbHelper.createDatabase();
//        mDbHelper.open();
//
//        Cursor testdata = mDbHelper.getTestData();
//
//        mDbHelper.close();








    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HomeFragment fragment = new HomeFragment();
            transaction.replace(R.id.main_fragment, fragment);
            transaction.commit();



        } else if (id == R.id.coding) {
//            Intent intent = new Intent(getApplicationContext(),CodeActivity.class);
//            startActivity(intent);


                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SlidingTabFragment fragment = new SlidingTabFragment();
                transaction.replace(R.id.main_fragment, fragment);
                transaction.commit();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public List<String> getListId() {
        return listId;
    }

    public List<String> getListTitle() {
        return listTitle;
    }

    public List<String> getListDescription(){
        return listDescription;
    }

    public List<String> getListCode(){
        return listCode;
    }

    public List<String> getListHint(){
        return listHint;
    }

    public List<String> getListAnswer(){
        return listAnswer;
    }

    public List<String> getListTag(){
        return listTag;
    }

    public List<String> getListCategory(){
        return listCategory;
    }


    public List<String> getListDifficulty(){
        return listDifficulty;
    }

    public List<String> getListAdditional(){
        return listAdditional;
    }

    public static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
