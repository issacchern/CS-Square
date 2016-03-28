package com.chernyee.cssquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chernyee.cssquare.UI.CustomAdapter;

import java.util.ArrayList;
import java.util.List;


public class BookmarkFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomAdapter customAdapter;
    private TextView textView;
    private List<List<String>> bookMarkList;
    private ListView lv;
    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        lv = (ListView) view.findViewById(R.id.bookmarkList);
        bookMarkList = new ArrayList<List<String>>();

        for(int i = 0; i < MainActivity.populateList.get(0).size(); i++){
            String markString = "cs" + MainActivity.populateList.get(0).get(i).get(0);
            int markScore = sharedPreferences.getInt(markString, 0);
            if(markScore == 1){
                bookMarkList.add(MainActivity.populateList.get(0).get(i));
            }

        }


        customAdapter = new CustomAdapter(getActivity(), R.layout.list_item,
                bookMarkList);
        lv.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), QuestionActivity.class);
                i.putStringArrayListExtra("information", new ArrayList<>(bookMarkList.get(position)));
                startActivity(i);
            }
        });


        if(bookMarkList.size() < 1){
            textView = (TextView) view.findViewById(R.id.bookmarkResult);
            textView.setText("No bookmark question is found.");

        }


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
