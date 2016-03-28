package com.chernyee.cssquare;

/**
 * Created by Issac on 3/17/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.chernyee.cssquare.UI.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;


public class ShortQATabFragment extends Fragment {
    private static final String ARG_TEXT = "ARG_TEXT";

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<List<String>> expandableListData;

    public ShortQATabFragment() {
    }

    public static ShortQATabFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        ShortQATabFragment sampleFragment = new ShortQATabFragment();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_short_tab_qa, container, false);

        boolean isCode = false;

        if(getArguments().getString(ARG_TEXT).equals("Code")){
            expandableListData = new ArrayList<List<String>>(MainActivity.interviewCoding);
            isCode = true;

        } else if(getArguments().getString(ARG_TEXT).equals("Algorithm")){
            expandableListData = new ArrayList<List<String>>(MainActivity.interviewKnowledge);
        } else if(getArguments().getString(ARG_TEXT).equals("Java")){
            expandableListData = new ArrayList<List<String>>(MainActivity.interviewKnowledgeJava);
        } else if(getArguments().getString(ARG_TEXT).equals("Android")) {
            expandableListData = new ArrayList<List<String>>(MainActivity.interviewKnowledgeAndroid);
        } else{
            expandableListData = new ArrayList<List<String>>(MainActivity.interviewHR);
        }

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListData, isCode);
        expandableListView.setAdapter(expandableListAdapter);

        return v;
    }
}