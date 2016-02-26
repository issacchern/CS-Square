package com.chernyee.cssquare;

/**
 * Created by Issac on 2/22/2016.
 */

import com.chernyee.cssquare.UI.SlidingTabLayout;
import com.chernyee.cssquare.CustomAdapter;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlidingTabFragment extends Fragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private CustomAdapter customAdapter;

    private ListView lv;

    @Override
    public void onResume() {
        super.onResume();
        if(customAdapter != null)
             customAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
                    MainActivity.populateList.get(position));
            lv.setAdapter(customAdapter);

            final int final_position = position;

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getActivity(), QuestionActivity.class);
                    i.putStringArrayListExtra("information", new ArrayList<>(MainActivity.populateList.
                            get(final_position).get(position)));
                    startActivity(i);
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
}