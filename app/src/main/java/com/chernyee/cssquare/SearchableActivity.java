package com.chernyee.cssquare;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.UI.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Issac on 2/25/2016.
 */
public class SearchableActivity extends ListActivity {

    private CustomAdapter customAdapter;
    private TextView textView;
    private List<List<String>> customList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onResume() {
        super.onResume();
        if(customAdapter != null) customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_item);
        textView =(TextView) findViewById(R.id.searchResult);
        textView.setVisibility(View.INVISIBLE);

        if(customAdapter != null)customAdapter.notifyDataSetChanged();
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        if(customList.get(position).get(8).contains("Medium")){

            int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
            int sizemedium = sharedPreferences.getInt("csmedium", 0);

            int remaining = sizemedium - sizeComplete;

            if(remaining > 0){
                Toast.makeText(this, "You need to at least complete " + remaining + " questions to unlock Medium level.", Toast.LENGTH_SHORT).show();
            } else{
                Intent i = new Intent(this, QuestionActivity.class);
                i.putStringArrayListExtra("information", new ArrayList<>(customList.get(position)));
                startActivity(i);
            }



        } else if(customList.get(position).get(8).contains("Hard")){

            int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
            int sizemedium = sharedPreferences.getInt("cshard", 0);
            int remaining = sizemedium - sizeComplete;

            if(remaining > 0){
                Toast.makeText(this, "You need to at least complete " + remaining + " questions to unlock Hard level.", Toast.LENGTH_SHORT).show();
            } else{
                Intent i = new Intent(this, QuestionActivity.class);
                i.putStringArrayListExtra("information", new ArrayList<>(customList.get(position)));
                startActivity(i);
            }

        } else{
            Intent i = new Intent(this, QuestionActivity.class);
            i.putStringArrayListExtra("information", new ArrayList<>(customList.get(position)));
            startActivity(i);
        }
        super.onListItemClick(l, v, position, id);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);

            if(query.toLowerCase().equals("csunlock")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("csmedium", 0);
                editor.commit();
                editor.putInt("cshard", 0);
                editor.commit();
                Toast.makeText(this, "You have gained access to all questions now!", Toast.LENGTH_SHORT).show();
                finish();
            } else if(query.toLowerCase().equals("csnoads")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("csnoads", 1);
                editor.commit();
                Toast.makeText(this, "You have removed all ads!", Toast.LENGTH_SHORT).show();
                finish();
            }

            customList = new ArrayList<List<String>>();
            for(int i = 0 ; i < MainActivity.populateList.get(0).size(); i++){
                if(MainActivity.populateList.get(0).get(i).get(1).toLowerCase().contains(query.toLowerCase())){
                    customList.add(MainActivity.populateList.get(0).get(i));
                }
            }

            customAdapter = new CustomAdapter(this, R.layout.list_item, customList);
            setListAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();

            if(customList.size() < 1){
                textView.setVisibility(View.VISIBLE);
                textView.setText("Your search \'" + query + "\' did not match any query data.");
            }

        } else{
            customList = new ArrayList<List<String>>();
            String query = intent.getStringExtra("extraInfo");
            if(query.equals("Completed")){

                for(int i = 0 ; i < MainActivity.populateList.get(0).size(); i++) {
                    String markString = "cse" + MainActivity.populateList.get(0).get(i).get(0);
                    if (sharedPreferences.getInt(markString, 0) == 1) {
                        customList.add(MainActivity.populateList.get(0).get(i));
                    }
                }

            } else{

                for(int i = 0 ; i < MainActivity.populateList.get(0).size(); i++){
                    if(MainActivity.populateList.get(0).get(i).get(8).contains(query)){
                        String markString = "cse"+MainActivity.populateList.get(0).get(i).get(0);
                        if (sharedPreferences.getInt(markString, 0) == 0) { //because we only want the undone one
                            customList.add(MainActivity.populateList.get(0).get(i));
                        }
                    }
                }
            }
            customAdapter = new CustomAdapter(this, R.layout.list_item,
                    customList);
            setListAdapter(customAdapter);
        }
    }
}
