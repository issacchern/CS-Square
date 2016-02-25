package com.chernyee.cssquare;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Issac on 2/25/2016.
 */
public class SearchableActivity extends ListActivity {

    private CustomAdapter customAdapter;
    private TextView textView;
    private List<List<String>> customList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_item);
        textView =(TextView) findViewById(R.id.searchResult);
        textView.setVisibility(View.INVISIBLE);

        handleIntent(getIntent());



    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(this, QuestionActivity.class);
        i.putStringArrayListExtra("information", new ArrayList<>(customList.get(position)));
        startActivity(i);



        super.onListItemClick(l, v, position, id);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            customList = new ArrayList<List<String>>();

            for(int i = 0 ; i < MainActivity.populateList.get(0).size(); i++){

                if(MainActivity.populateList.get(0).get(i).get(1).toLowerCase().contains(query.toLowerCase())){
                    customList.add(MainActivity.populateList.get(0).get(i));
                }

            }


            customAdapter = new CustomAdapter(this, R.layout.list_item,
                    customList);
            setListAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();

            if(customList.size() < 1){
                textView.setVisibility(View.VISIBLE);
                textView.setText("Your search \'" + query + "\' did not match any query data.");

            }

        }
    }
}
