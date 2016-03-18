package com.chernyee.cssquare;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Issac on 3/17/2016.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<List<String>> expandableData;
    private boolean isCode;

    public CustomExpandableListAdapter(Context context, List<List<String>> expandableData, boolean isCode) {
        this.context = context;
        this.expandableData = expandableData;
        this.isCode = isCode;
    }


    @Override
    public int getGroupCount() {
        return this.expandableData.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableData.get(listPosition).get(1);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableData.get(listPosition).get(2);
    }


    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }


    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }


    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_content, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        TextView expandedListTextView2 = (TextView) convertView.findViewById(R.id.expandedListItem2);

        TagGroup mTagGroup = (TagGroup) convertView.findViewById(R.id.tag_group);

        String tag = expandableData.get(listPosition).get(4);
        String [] tags = tag.split(",");
        for(int i = 0; i < tags.length ; i++){
            tags[i] = tags[i].trim();
        }
        mTagGroup.setTags(tags);

        HorizontalScrollView expandedScrollView = (HorizontalScrollView) convertView.findViewById(R.id.expandedScrollView);

        if(isCode){
            SyntaxHighlighter sh1 = new SyntaxHighlighter(expandedListText);
            expandedListTextView2.setText(sh1.formatToHtml());
            expandedListTextView.setVisibility(View.GONE);

        } else{
            expandedListTextView.setText(expandedListText);
            expandedScrollView.setVisibility(View.GONE);
        }


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}