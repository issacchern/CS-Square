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
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    private String [] snackBarChoices = {"Have you tried it yourself yet?", "I am sure you can do better!",
        "Just give it some time!" , "Don't give up, keep on trying!"};



    private Cursor employees;
    private DatabaseHelper db;

    private ListView lvUsers;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, snackBarChoices[(int)(Math.random()*100)%4 ], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        db = new DatabaseHelper(this);
        db.setForcedUpgrade();

        employees = db.getEmployees();

        List<String> listUser = new ArrayList<>();
        listUser.add(employees.getString(3));

        while(employees.moveToNext()){
            if(employees.getString(1) != null){

                Log.v("YESSSS",employees.getString(1));
                listUser.add(employees.getString(3));
            }

        }

        String highlighted = listUser.get(3);

        SyntaxHighlighter shl = new SyntaxHighlighter(highlighted);


        TextView text1 = (TextView) findViewById(R.id.text1);
     //   text1.setText(Html.fromHtml(highlighted));


        text1.setText(shl.formatToHtml());









        lvUsers = (ListView) findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                listUser);
        lvUsers.setAdapter(adapter);



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
}
