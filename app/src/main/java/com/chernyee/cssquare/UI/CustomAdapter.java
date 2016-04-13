package com.chernyee.cssquare.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chernyee.cssquare.Question1;
import com.chernyee.cssquare.R;
import com.chernyee.cssquare.Utility.DaoDBHelper;
import com.chernyee.cssquare.model.Question;
import com.chernyee.cssquare.model.QuestionDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Issac on 2/24/2016.
 */
public class CustomAdapter extends ArrayAdapter<Question1>{

    private List<Question1> items;
    private SharedPreferences sharedPreferences;
    private static Context context;


    private DaoDBHelper daoDBHelper;
    private QuestionDao questionDao;
    QueryBuilder qb;

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Question1 getItem(int position) {
        return items.get(position);
    }

    public CustomAdapter(Context context, int resource, List<Question1> items) {
        super(context, resource, items);
        this.items = items;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        daoDBHelper = DaoDBHelper.getInstance(context);
        questionDao = daoDBHelper.getQuestionDao();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        View v = convertView;
        if (v == null) {
            LayoutInflater vi= LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        Question1 q = items.get(position);
        Question question = questionDao.loadByRowId(Long.parseLong(q.getId()));

        boolean markStar = question.getBookmark();
        boolean markComplete = question.getRead();


        vh.holderStar = (ImageView) v.findViewById(R.id.item_star);
        vh.holderLayout = (LinearLayout) v.findViewById(R.id.item_list);

        if(markStar){
            vh.holderStar.setImageResource(R.drawable.star);
        } else{
            vh.holderStar.setImageResource(R.drawable.star_outline);
        }

        if(markComplete){
            vh.holderLayout.setBackgroundResource(R.color.background);
        } else{
            vh.holderLayout.setBackgroundResource(R.color.white);
        }



        if (q != null) {
            vh.holderTitle = (TextView) v.findViewById(R.id.item_title);
            vh.holderTag = (TextView) v.findViewById(R.id.item_tag);
            vh.holderDifficulty = (TextView) v.findViewById(R.id.item_difficulty);

            if ( vh.holderTitle != null) {

                vh.holderTitle.setText(q.getTitle());
            }

            if (vh.holderTag != null) {
                vh.holderTag.setText(q.getTag());
            }

            if (vh.holderDifficulty != null) {
                vh.holderDifficulty.setText(q.getDifficulty());

                if(q.getDifficulty().contains("Easy") || q.getDifficulty().contains("Beginner") ){
                    if(q.getAdditional().contains("hot")){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(vh.holderHot, null, null,null );
                    } else{
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                else if(q.getDifficulty().contains("Medium")){
                    int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                    int sizemedium = sharedPreferences.getInt("cslockmedium", 0);
                    int remaining = sizemedium - sizeComplete;
                    if(remaining > 0){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds( vh.holderImage, null, null, null);
                    }else{
                        if(q.getAdditional().contains("hot")){
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(vh.holderHot, null, null, null);
                        } else{
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }
                    }

                } else if(q.getDifficulty().contains("Hard")){
                    int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                    int sizeHard = sharedPreferences.getInt("cslockhard", 0);
                    int remaining = sizeHard - sizeComplete;
                    if(remaining > 0){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds( vh.holderImage, null, null, null);
                    }else{
                        if(q.getAdditional().contains("hot")){
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(vh.holderHot, null, null, null);
                        } else{
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }
                    }
                }
            }
        }
        v.setTag(vh);
        return v;
    }


    static class ViewHolder {
        TextView holderTitle;
        TextView holderTag;
        TextView holderDifficulty;
        ImageView holderStar;
        LinearLayout holderLayout;
        Drawable holderImage;
        Drawable holderHot;

        ViewHolder(){
            this.holderImage = context.getResources().getDrawable(R.drawable.lock);
            this.holderHot = context.getResources().getDrawable(R.drawable.fire);
        }
    }

}