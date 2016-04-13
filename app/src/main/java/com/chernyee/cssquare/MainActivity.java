package com.chernyee.cssquare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;
import org.codechimp.apprater.AppRater;
import java.io.File;
import java.util.List;
import java.util.Observable;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.chernyee.cssquare.Utility.AwsUtil;
import com.chernyee.cssquare.Utility.DatabaseHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TransferListener {

    private SharedPreferences sharedPreferences;
    private AmazonS3Client s3;
    private List<S3ObjectSummary> s3ObjList;
    private TransferUtility transferUtility;
    private ProgressDialog progressDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private int onlineVersion = 0;
    private Toolbar toolbar;
    private int oldScrollY = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppRater.app_launched(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int databaseVersion = sharedPreferences.getInt("csdbversion", 7);


        if(VarInit.getSharedCodeListInstance().size() == 0)
            new VarInit(this,databaseVersion).initializeVariable();



        if(VarInit.getSharedCodeListInstance().size() == 0){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Download Database");
            builder1.setMessage("You have empty database. Do you wish to download database now?");
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
                                                onlineVersion = Integer.parseInt(summary.getKey());
                                            } else if(summary.getKey().equals("Questions.db")){

                                                File file = new File(SplashActivity.DATABASE_PATH + "/" + "Questions.db");
                                                TransferUtility transferUtility = AwsUtil.getTransferUtility(MainActivity.this);
                                                TransferObserver observer = transferUtility.download("sabi-data", "Questions.db", file);
                                                observer.setTransferListener(MainActivity.this);
                                                break; // no need to check for other cases

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
                        for (S3ObjectSummary summary : s3ObjList) {

                            if (summary.getKey().length() < 3) {
                                onlineVersion = Integer.parseInt(summary.getKey());
                            } else if(summary.getKey().equals("Questions.db")){
                                if(onlineVersion > currentVersion && isNetworkAvailable(getApplicationContext())){

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                            builder1.setTitle("New Update");
                                            builder1.setMessage("New data is available. Do you wish to download the data now?");
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

                                                            File file = new File(SplashActivity.DATABASE_PATH + "/" + "Questions.db");
                                                            TransferUtility transferUtility = AwsUtil.getTransferUtility(MainActivity.this);
                                                            TransferObserver observer = transferUtility.download("sabi-data", "Questions.db", file);
                                                            observer.setTransferListener(MainActivity.this);


                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }
                                    });
                                }
                                break ; // no need to see for other case
                            }
                        }
                    }
                }).start();
            }
        }

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
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
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
    protected void onDestroy() {
        super.onDestroy();
    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    // this following method is from AWS TransferListener

    @Override
    public void onStateChanged(int i, TransferState transferState) {
        if (transferState.toString().equals("IN_PROGRESS")) {
            progressDialog = ProgressDialog.show(MainActivity.this, "Downloading data",
                    "Please wait", true);
        } else if (transferState.toString().equals("COMPLETED")) {
            progressDialog.dismiss();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("csdbversion", onlineVersion);
            editor.commit();

            DatabaseHelper db = DatabaseHelper.getInstance(this, sharedPreferences.getInt("csdbversion", 7));
            db.resetDB();

            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onProgressChanged(int i, long l, long l1) {

    }

    @Override
    public void onError(int i, Exception e) {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

    }

}
