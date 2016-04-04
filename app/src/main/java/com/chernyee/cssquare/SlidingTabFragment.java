package com.chernyee.cssquare;

/**
 * Created by Issac on 2/22/2016.
 */

import com.chernyee.cssquare.UI.CustomAdapter;
import com.chernyee.cssquare.UI.SlidingTabLayout;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SlidingTabFragment extends Fragment{

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private CustomAdapter customAdapter;
    private SharedPreferences sharedPreferences;
    private ListView lv;
    private String difficulty;
    private String sortBy;
    private HashMap<Integer,List<Question>> hashMap;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter, menu);
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.filter){

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.filter, null);
            final CheckBox inputBeginner = (CheckBox) dialoglayout.findViewById(R.id.beginner);
            final CheckBox inputEasy = (CheckBox) dialoglayout.findViewById(R.id.easy);
            final CheckBox inputMedium = (CheckBox) dialoglayout.findViewById(R.id.medium);
            final CheckBox inputHard = (CheckBox) dialoglayout.findViewById(R.id.hard);
            final RadioButton titleAscending = (RadioButton) dialoglayout.findViewById(R.id.titleAscending);
            final RadioButton titleDescending = (RadioButton) dialoglayout.findViewById(R.id.titleDescending);

            if(difficulty.contains("Beginner")) inputBeginner.setChecked(true);
            if(difficulty.contains("Easy")) inputEasy.setChecked(true);
            if(difficulty.contains("Medium")) inputMedium.setChecked(true);
            if(difficulty.contains("Hard")) inputHard.setChecked(true);
            if(sortBy.equals("titleAscending")) titleAscending.setChecked(true);
            else if(sortBy.equals("titleDescending")) titleDescending.setChecked(true);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setView(dialoglayout);

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });

            alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do something with it()

                    if(!inputBeginner.isChecked() && !inputEasy.isChecked() && !inputMedium.isChecked()
                            && !inputHard.isChecked()) {
                        Toast.makeText(getContext(), "Please select at least one difficulty." , Toast.LENGTH_LONG).show();
                    } else{
                        String difficult = "";
                        String sort = "";
                        if(inputBeginner.isChecked()) difficult += "Beginner";
                        if(inputEasy.isChecked()) difficult += "Easy";
                        if(inputMedium.isChecked()) difficult += "Medium";
                        if(inputHard.isChecked()) difficult += "Hard";
                        if(titleAscending.isChecked()) sort = "titleAscending";
                        else if(titleDescending.isChecked()) sort = "titleDescending";

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("csfilterdifficulty", difficult);
                        editor.putString("csfiltersortby", sort);
                        editor.commit();
                        difficulty = sharedPreferences.getString("csfilterdifficulty", "BeginnerEasyMediumHard"); // default value
                        sortBy = sharedPreferences.getString("csfiltersortby", "titleAscending"); // default value
                        if(mViewPager != null){
                            mViewPager.setAdapter(new SamplePagerAdapter());
                            mSlidingTabLayout.setViewPager(mViewPager);
                        }
                        Toast.makeText(getContext(), "List has been refreshed!", Toast.LENGTH_LONG).show();
                    }
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(customAdapter != null)
             customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        difficulty = sharedPreferences.getString("csfilterdifficulty", "BeginnerEasyMediumHard"); // default value
        sortBy = sharedPreferences.getString("csfiltersortby", "titleAscending"); // default value
        hashMap = new HashMap<>();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidingtab_fragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout.setViewPager(mViewPager);

        return view;
    }


    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            customAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return QuestionList.code_tag.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           // return "Item " + (position + 1);
            return QuestionList.code_tag[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
            container.addView(view);
            List<Question> qList = QuestionList.getViewPosition(position, difficulty, sortBy);
            hashMap.put(position, qList);
            lv = (ListView) view.findViewById(R.id.questionlist);
            customAdapter = new CustomAdapter(getActivity(), R.layout.list_item,qList);
            lv.setAdapter(customAdapter);

            final int final_position = position;

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(hashMap.get(final_position).get(position).getDifficulty().contains("Medium")){
                        int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                        int sizemedium = sharedPreferences.getInt("cslockmedium", 0);
                        int remaining = sizemedium - sizeComplete;
                        if(remaining > 0){
                            Toast.makeText(getContext(), "You need to at least complete " + remaining +
                                    " questions to unlock Medium level." , Toast.LENGTH_SHORT).show();
                        } else{
                            Intent i = new Intent(getActivity(), QuestionActivity.class);
                            Bundle bundle = new Bundle();
                            Parcelable wrapped = Parcels.wrap(hashMap.get(final_position).get(position));
                            bundle.putParcelable("data", wrapped);
                            i.putExtras(bundle);
                            startActivity(i);
                        }


                    } else if(hashMap.get(final_position).get(position).getDifficulty().contains("Hard")) {
                        int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                        int sizehard = sharedPreferences.getInt("cslockhard", 0);
                        int remaining = sizehard - sizeComplete;
                        if(remaining > 0){
                            Toast.makeText(getContext(), "You need to at least complete " + remaining +
                                    " questions to unlock Hard level." , Toast.LENGTH_SHORT).show();
                        } else{
                            Intent i = new Intent(getActivity(), QuestionActivity.class);
                            Bundle bundle = new Bundle();
                            Parcelable wrapped = Parcels.wrap(hashMap.get(final_position).get(position));
                            bundle.putParcelable("data", wrapped);
                            i.putExtras(bundle);
                            startActivity(i);
                        }

                    } else{
                        Intent i = new Intent(getActivity(), QuestionActivity.class);
                        Bundle bundle = new Bundle();
                        Parcelable wrapped = Parcels.wrap(hashMap.get(final_position).get(position));
                        bundle.putParcelable("data", wrapped);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            });

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



    private class FragmentAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... params) {



            return null;
        }
    }

}