package com.chernyee.cssquare;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chernyee.cssquare.UI.CustomAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Issac on 2/25/2016.
 */
public class SearchableActivity extends ListActivity {

    private CustomAdapter customAdapter;
    private TextView textView;
    private List<Question> customList;
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
        customList = new ArrayList<>();
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

        if(customList.get(position).getDifficulty().contains("Medium")){

            int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
            int sizemedium = sharedPreferences.getInt("csmedium", 0);
            int remaining = sizemedium - sizeComplete;
            if(remaining > 0){
                Toast.makeText(this, "You need to at least complete " + remaining + " questions to unlock Medium level.", Toast.LENGTH_SHORT).show();
            } else{
                Intent i = new Intent(this, QuestionActivity.class);
                Bundle bundle = new Bundle();
                Parcelable wrapped = Parcels.wrap(customList.get(position));
                bundle.putParcelable("data", wrapped);
                i.putExtras(bundle);
                startActivity(i);
            }

        } else if(customList.get(position).getDifficulty().contains("Hard")){

            int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
            int sizemedium = sharedPreferences.getInt("cshard", 0);
            int remaining = sizemedium - sizeComplete;

            if(remaining > 0){
                Toast.makeText(this, "You need to at least complete " + remaining + " questions to unlock Hard level.", Toast.LENGTH_SHORT).show();
            } else{
                Intent i = new Intent(this, QuestionActivity.class);
                Bundle bundle = new Bundle();
                Parcelable wrapped = Parcels.wrap(customList.get(position));
                bundle.putParcelable("data", wrapped);
                i.putExtras(bundle);
                startActivity(i);
            }

        } else{
            Intent i = new Intent(this, QuestionActivity.class);
            Bundle bundle = new Bundle();
            Parcelable wrapped = Parcels.wrap(customList.get(position));
            bundle.putParcelable("data", wrapped);
            i.putExtras(bundle);
            startActivity(i);
        }
        super.onListItemClick(l, v, position, id);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query.toLowerCase().equals("csunlock")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("cslockmedium", 0);
                editor.commit();
                editor.putInt("cslockhard", 0);
                editor.commit();
                Toast.makeText(this, "You have gained access to all questions now!", Toast.LENGTH_SHORT).show();
                finish();
            } else if (query.toLowerCase().equals("csunlock29")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("cscomplete", 29);
                Toast.makeText(this, "Complete 29 questions!", Toast.LENGTH_SHORT).show();
                editor.commit();
                finish();
            } else if(query.toLowerCase().equals("csunlock59")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("cscomplete", 59);
                Toast.makeText(this, "Complete 59 questions!", Toast.LENGTH_SHORT).show();
                editor.commit();
                finish();
            } else if (query.toLowerCase().equals("csnoads")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("csnoads", 1);
                editor.commit();
                Toast.makeText(this, "You have removed all ads!", Toast.LENGTH_SHORT).show();
                finish();
            } else{
                Iterator<Question> itr = VarInit.getSharedCodeListInstance().iterator(); // not recommended but I got no choice :(
                while(itr.hasNext()){
                    Question q = itr.next();
                    if(q.getTitle().toLowerCase().contains(query.toLowerCase())){
                        customList.add(q);
                    }
                }
            }

            customAdapter = new CustomAdapter(this, R.layout.list_item, customList);
            setListAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();

            if (customList.size() < 1) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("Your search \'" + query + "\' did not match any query data.");
            }

        } else {

            String difficulty = getIntent().getExtras().getString("data");

            Iterator<Question> itr = VarInit.getSharedCodeListInstance().iterator(); // not recommended

            if(difficulty.equals("Bookmarked")){

                for(int i = 0; i < VarInit.getSharedCodeListInstance().size(); i++){
                    String num = VarInit.getSharedCodeListInstance().get(i).getId();
                    int markRead = sharedPreferences.getInt("cs" + num , 0);
                    if(markRead == 1) customList.add(VarInit.getSharedCodeListInstance().get(i));
                }

            } else if(difficulty.equals("Completed")){
                for(int i = 0; i < VarInit.getSharedCodeListInstance().size(); i++){
                    String num = VarInit.getSharedCodeListInstance().get(i).getId();
                    int markRead = sharedPreferences.getInt("cse" + num , 0);
                    if(markRead == 1) customList.add(VarInit.getSharedCodeListInstance().get(i));
                }

            } else{
                while(itr.hasNext()){
                    Question q = itr.next();
                    if(q.getDifficulty().toLowerCase().contains(difficulty.toLowerCase())){
                        customList.add(q);
                    }
                }
            }

            customAdapter = new CustomAdapter(this, R.layout.list_item,
                    customList);
            setListAdapter(customAdapter);

            if (customList.size() < 1) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("No bookmark is found.");
            }
        }
    }
}
