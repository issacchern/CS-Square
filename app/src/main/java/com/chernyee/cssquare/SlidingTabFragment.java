package com.chernyee.cssquare;

/**
 * Created by Issac on 2/22/2016.
 */

import com.chernyee.cssquare.UI.SlidingTabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlidingTabFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private String [] code_tag = {"All", "Array", "String", "Linked List", "Tree" , "DP" , "DFS", "BFS", "Greedy"};




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.slidingtab_fragment, container, false);
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
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(String.valueOf(position + 1));

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