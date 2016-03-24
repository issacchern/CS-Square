package com.chernyee.cssquare;

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
import java.util.List;

/**
 * Created by Issac on 2/24/2016.
 */
public class CustomAdapter extends ArrayAdapter<List<String>>{

    private List<List<String>> items;
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
    public List<String> getItem(int position) {
        return items.get(position);
    }

    public CustomAdapter(Context context, int resource, List<List<String>> items) {
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


        List<String> p = items.get(position);


        String markString = "cs"+p.get(0);
        String markString2 = "cse" + p.get(0);
        int markScore = sharedPreferences.getInt(markString, 0);
        int markScore2 = sharedPreferences.getInt(markString2, 0);

        vh.holderStar = (ImageView) v.findViewById(R.id.item_star);
        vh.holderLayout = (LinearLayout) v.findViewById(R.id.item_list);

        if(markScore == 1){
            vh.holderStar.setImageResource(R.drawable.star);
        } else{
            vh.holderStar.setImageResource(R.drawable.star_outline);
        }

        if(markScore2 == 1){
            vh.holderLayout.setBackgroundResource(R.color.background);
        } else{
            vh.holderLayout.setBackgroundResource(R.color.white);
        }



        if (p != null && p.size() > 0) {
            vh.holderTitle = (TextView) v.findViewById(R.id.item_title);
            vh.holderTag = (TextView) v.findViewById(R.id.item_tag);
            vh.holderDifficulty = (TextView) v.findViewById(R.id.item_difficulty);

            if ( vh.holderTitle != null) {

                vh.holderTitle.setText(p.get(1));
            }

            if (vh.holderTag != null) {
                vh.holderTag.setText(p.get(6));
            }

            if (vh.holderDifficulty != null) {
                vh.holderDifficulty.setText(p.get(8));

                if(p.get(8).contains("Easy")){
                    if(p.get(9).contains("hot")){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(vh.holderHot, null, null,null );
                    } else{
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                else if(p.get(8).contains("Medium")){
                    int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                    int sizemedium = sharedPreferences.getInt("csmedium", 0);

                    int remaining = sizemedium - sizeComplete;
                    if(remaining > 0){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds( vh.holderImage, null, null, null);

                    }else{
                        if(p.get(9).contains("hot")){
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(vh.holderHot, null, null, null);
                        } else{
                            vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }
                    }


                } else if(p.get(8).contains("Hard")){

                    int sizeComplete = sharedPreferences.getInt("cscomplete", 0);
                    int sizeHard = sharedPreferences.getInt("cshard", 0);

                    int remaining = sizeHard - sizeComplete;
                    if(remaining > 0){
                        vh.holderTitle.setCompoundDrawablesWithIntrinsicBounds( vh.holderImage, null, null, null);
                    }else{
                        if(p.get(9).contains("hot")){
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