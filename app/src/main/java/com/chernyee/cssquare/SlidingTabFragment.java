package com.chernyee.cssquare;

/**
 * Created by Issac on 2/22/2016.
 */

import com.chernyee.cssquare.UI.SlidingTabLayout;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SlidingTabFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private String [] code_tag = {"All", "Array", "String", "Linked List", "Tree" , "DP" , "DFS", "BFS", "Greedy"};

    private SearchView searchView;
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        handleIntent(getActivity().getIntent());
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView =(SearchView) MenuItemCompat.getActionView(searchItem);




//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        ComponentName componentName = new ComponentName(getContext(),MainActivity.class);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

//
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // text changed, apply filter?
                Toast.makeText(getContext(),query, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {




                // perform final search
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidingtab_fragment, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);



    }


    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return code_tag.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           // return "Item " + (position + 1);
            return code_tag[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            container.addView(view);

            ListView lv = (ListView) view.findViewById(R.id.questionlist);

//
//            adapter = new ArrayAdapter<String>(getApplicationContext(),
//                    android.R.layout.simple_list_item_1, android.R.id.text1,
//                    listUser);
//            lvUsers.setAdapter(adapter);






     //       TextView title = (TextView) view.findViewById(R.id.item_title);
     //       title.setText(String.valueOf(position + 1));




            // Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        //    Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }

    }
}