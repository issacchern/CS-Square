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

import com.chernyee.cssquare.Question;
import com.chernyee.cssquare.R;

import java.util.List;

/**
 * Created by Issac on 2/24/2016.
 */
public class CustomAdapter extends ArrayAdapter<Question>{

    private List<Question> items;
    private SharedPreferences sharedPreferences;
    private static Context context;

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Question getItem(int position) {
        return items.get(position);
    }

    public CustomAdapter(Context context, int resource, List<Question> items) {
        super(context, resource, items);
        this.items = items;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        View v = convertView;
        if (v == null) {
            LayoutInflater vi= LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        Question q = items.get(position);

        int markStar = sharedPreferences.getInt("cs"+ q.getId(), 0);
        int markComplete = sharedPreferences.getInt("cse" + q.getId(), 0);

        vh.holderStar = (ImageView) v.findViewById(R.id.item_star);
        vh.holderLayout = (LinearLayout) v.findViewById(R.id.item_list);

        if(markStar == 1){
            vh.holderStar.setImageResource(R.drawable.star);
        } else{
            vh.holderStar.setImageResource(R.drawable.star_outline);
        }

        if(markComplete == 1){
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