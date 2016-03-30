package com.chernyee.cssquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chernyee.cssquare.UI.CustomAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BookmarkFragment extends Fragment {

    private CustomAdapter customAdapter;
    private TextView textView;
    private List<Question> bookMarkList;
    private ListView lv;
    private SharedPreferences sharedPreferences;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        lv = (ListView) view.findViewById(R.id.bookmarkList);
        bookMarkList = new ArrayList<>();

        // not recommended but I got no choice :(

        for(int i = 0; i < SplashActivity.sharedCodeList.size(); i++){
            int markScore = sharedPreferences.getInt("cs" + SplashActivity.sharedCodeList.get(i).getId(), 0);
            if(markScore == 1){
                bookMarkList.add(SplashActivity.sharedCodeList.get(i));
            }
        }
        customAdapter = new CustomAdapter(getActivity(), R.layout.list_item, bookMarkList);
        lv.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), QuestionActivity.class);
                Bundle bundle = new Bundle();
                Parcelable wrapped = Parcels.wrap(bookMarkList.get(position));
                bundle.putParcelable("data", wrapped);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        if(bookMarkList.size() < 1){
            textView = (TextView) view.findViewById(R.id.bookmarkResult);
            textView.setText("No bookmark question is found.");
        }
        return view;
    }
}
