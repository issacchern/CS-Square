package com.chernyee.cssquare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import us.feras.mdv.MarkdownView;


public class PreparationFragment extends Fragment{

    private MarkdownView markdownView;
    private View v;
    private AdView adView;
    private AdRequest adRequest;
    private SharedPreferences sharedPreferences;

    public PreparationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_preparation, container, false);
        markdownView = (MarkdownView) v.findViewById(R.id.markdownView);
        int setAds = sharedPreferences.getInt("csnoads", 0);
        if(setAds == 0){
            adView = (AdView) v.findViewById(R.id.adView);
            adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                    .addTestDevice("4F9BBB4D58E9E204C5EC4D011342A9DD")  // An example device ID
                    .build();
            adView.loadAd(adRequest);
        }
        else{
            adView = (AdView) v.findViewById(R.id.adView);
            adView.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new MyTask().execute();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(),"Please wait while the view is loading ...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            markdownView.loadMarkdownFile("file:///android_asset/README.md", "file:///android_asset/alt.css");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
