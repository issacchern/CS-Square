package com.chernyee.cssquare;

/**
 * Created by Issac on 2/22/2016.
 */

import com.chernyee.cssquare.UI.SlidingTabLayout;
import com.chernyee.cssquare.CustomAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SlidingTabFragment extends Fragment{



    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private CustomAdapter customAdapter;
    private SharedPreferences sharedPreferences;
    public static HashMap<Integer, List<List<String>>> populateListCopy;
    private List<List<String>> listOfListInFragment;
    private ListView lv;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter, menu);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.filter){

            String levelstr = sharedPreferences.getString("cslevel", "All");
            final ArrayList<Integer> selectedItems=new ArrayList<>();


            CharSequence[] items = { "Easy","Medium","Hard"};
            boolean[] checkedItems = {false, false, false};

            if(!levelstr.equals("All")){
                if(levelstr.contains("Easy")){
                    checkedItems[0] = true;
                    selectedItems.add(0);

                }
                if(levelstr.contains("Medium")){
                    checkedItems[1] = true;
                    selectedItems.add(1);
                }
                if(levelstr.contains("Hard")){
                    checkedItems[2] = true;
                    selectedItems.add(2);
                }
            } else{
                checkedItems[0] = true;
                checkedItems[1] = true;
                checkedItems[2] = true;
                selectedItems.add(0);
                selectedItems.add(1);
                selectedItems.add(2);
            }

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Select difficulty level")
                    .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {

                            if (isChecked) {
                                selectedItems.add(indexSelected);
                            } else if (selectedItems.contains(indexSelected)) {
                                // Else, if the item is already in the array, remove it
                                selectedItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                        }
                    }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String level = "";
                            if(selectedItems.size() > 0){
                                for(int i = 0 ; i < selectedItems.size(); i++){
                                    if(selectedItems.get(i) == 0){
                                        level += "Easy";
                                    } else if(selectedItems.get(i) == 1){
                                        level += "Medium";
                                    } else if(selectedItems.get(i) == 2){
                                        level += "Hard";
                                    }

                                }

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("cslevel", level);
                                editor.commit();

                                new FragmentAsyncTask().execute();

                                Toast.makeText(getContext(), "List has been refreshed!", Toast.LENGTH_LONG).show();

                            } else{
                                Toast.makeText(getContext(), "You must select either one of difficulty level!", Toast.LENGTH_LONG).show();
                            }




                        }
                    }).create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        int pageNumber = sharedPreferences.getInt("csviewpager",0);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(pageNumber);



    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("csviewpager", mViewPager.getCurrentItem());
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cslistview", 0);
        editor.commit();
        new FragmentAsyncTask().execute();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("ONCREATEVIEW", "Do i get called?");
        View view = inflater.inflate(R.layout.slidingtab_fragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
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
            return MainActivity.code_tag.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           // return "Item " + (position + 1);
            return MainActivity.code_tag[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            container.addView(view);

            lv = (ListView) view.findViewById(R.id.questionlist);


            customAdapter = new CustomAdapter(getActivity(), R.layout.list_item,
                    populateListCopy.get(position));
            lv.setAdapter(customAdapter);

            final int itemNumber = sharedPreferences.getInt("cslistview",0);
            lv.setSelection(itemNumber);
            final int final_position = position;

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("cslistview", position);
                    editor.commit();

                    if(populateListCopy.get(final_position).get(position).get(8).contains("Medium")){

                        int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                        int sizemedium = sharedPreferences.getInt("csmedium", 0);

                        int remaining = sizemedium - sizeComplete;

                        if(remaining > 0){
                            Toast.makeText(getContext(), "You need to at least complete " + remaining + " questions to unlock Medium level." , Toast.LENGTH_SHORT).show();
                        } else{
                            Intent i = new Intent(getActivity(), QuestionActivity.class);
                            i.putStringArrayListExtra("information", new ArrayList<>(populateListCopy.get(final_position).get(position)));
                            startActivity(i);
                        }


                    } else if(populateListCopy.get(final_position).get(position).get(8).contains("Hard")) {

                        int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                        int sizehard = sharedPreferences.getInt("cshard", 0);

                        int remaining = sizehard - sizeComplete;

                        if(remaining > 0){
                            Toast.makeText(getContext(), "You need to at least complete " + remaining + " questions to unlock Hard level." , Toast.LENGTH_SHORT).show();
                        } else{
                            Intent i = new Intent(getActivity(), QuestionActivity.class);
                            i.putStringArrayListExtra("information", new ArrayList<>(populateListCopy.get(final_position).get(position)));
                            startActivity(i);
                        }

                    } else{
                        Intent i = new Intent(getActivity(), QuestionActivity.class);
                        i.putStringArrayListExtra("information", new ArrayList<>(populateListCopy.get(final_position).get(position)));
                        startActivity(i);
                    }
                }
            });

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        //    Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
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
            if(mViewPager != null){
                mViewPager.setAdapter(new SamplePagerAdapter());
                mSlidingTabLayout.setViewPager(mViewPager);
            }


        }

        @Override
        protected Void doInBackground(Void... params) {

            String levelstr = sharedPreferences.getString("cslevel", "All");



            if(levelstr.equals("All") || levelstr.equals("EasyMediumHard") || levelstr.equals("EasyHardMedium") || levelstr.equals("MediumEasyHard")
                    || levelstr.equals("MediumHardEasy") || levelstr.equals("HardEasyMedium") || levelstr.equals("HardMediumEasy")){

                populateListCopy = new HashMap<>(MainActivity.populateList);


            } else {

                populateListCopy = new HashMap<>();

                for (int i = 0; i < MainActivity.code_tag.length; i++) {

                    List<List<String>> tempListList = new ArrayList<>();
                    for (int j = 0; j < MainActivity.listId.size(); j++) {

                        if (!levelstr.contains(MainActivity.listDifficulty.get(j))) continue;

                        if (i == 0) {
                            List<String> tempList = new ArrayList<>();
                            tempList.add(MainActivity.listId.get(j));
                            tempList.add(MainActivity.listTitle.get(j));
                            tempList.add(MainActivity.listDescription.get(j));
                            tempList.add(MainActivity.listCode.get(j));
                            tempList.add(MainActivity.listAnswer.get(j));
                            tempList.add(MainActivity.listHint.get(j));
                            tempList.add(MainActivity.listTag.get(j));
                            tempList.add(MainActivity.listCategory.get(j));
                            tempList.add(MainActivity.listDifficulty.get(j));
                            tempList.add(MainActivity.listAdditional.get(j));
                            tempListList.add(tempList);
                        } else if (MainActivity.listTag.get(j).contains(MainActivity.code_tag[i])) {
                            List<String> tempList = new ArrayList<>();
                            tempList.add(MainActivity.listId.get(j));
                            tempList.add(MainActivity.listTitle.get(j));
                            tempList.add(MainActivity.listDescription.get(j));
                            tempList.add(MainActivity.listCode.get(j));
                            tempList.add(MainActivity.listAnswer.get(j));
                            tempList.add(MainActivity.listHint.get(j));
                            tempList.add(MainActivity.listTag.get(j));
                            tempList.add(MainActivity.listCategory.get(j));
                            tempList.add(MainActivity.listDifficulty.get(j));
                            tempList.add(MainActivity.listAdditional.get(j));
                            tempListList.add(tempList);
                        }
                    }

                    Collections.sort(tempListList, new Comparator<List<String>>() {
                        @Override
                        public int compare(List<String> lhs, List<String> rhs) {
                            return lhs.get(1).compareTo(rhs.get(1));
                        }
                    });

                    populateListCopy.put(i, tempListList);
                }
            }
            return null;
        }
    }


}