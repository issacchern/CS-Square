package com.chernyee.cssquare;

/**
 * Created by Issac on 3/17/2016.
 */

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.chernyee.cssquare.UI.CustomExpandableListAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ShortQATabFragment extends Fragment {
    private static final String ARG_TEXT = "ARG_TEXT";
    private static List<Question2> questionList;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    public ShortQATabFragment() {
    }

    public static ShortQATabFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        ShortQATabFragment sampleFragment = new ShortQATabFragment();
        sampleFragment.setArguments(args);
        return sampleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_short_tab_qa, container, false);
        String category = getArguments().getString(ARG_TEXT);

        questionList = new ArrayList<>(QuestionList.getInterviewList(category));
        boolean isCode = false;

        if(category.equals("Coding")){
            isCode = true;
        }
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), questionList, isCode);
        expandableListView.setAdapter(expandableListAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }
}