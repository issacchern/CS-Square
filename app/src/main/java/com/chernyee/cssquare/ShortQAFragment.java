package com.chernyee.cssquare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

import java.util.ArrayList;
import java.util.List;


public class ShortQAFragment extends Fragment {

    private BottomBar mBottomBar;

    public ShortQAFragment() {
        // Required empty public constructor
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_short_qa, container, false);

        mBottomBar = BottomBar.attach(v.findViewById(R.id.fragmentContainer), savedInstanceState);
        mBottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(ShortQATabFragment.newInstance("Coding"), R.drawable.code_tags, "Code"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Knowledge"), R.drawable.sitemap, "Concept"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Java"), R.drawable.coffee, "Java"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Android"), R.drawable.android, "Android")
        );

        return v;
    }

}
