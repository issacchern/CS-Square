package com.chernyee.cssquare;

import android.content.Context;
import android.content.SharedPreferences;

import com.chernyee.cssquare.Utility.DaoDBHelper;
import com.chernyee.cssquare.model.NoteDao;
import com.chernyee.cssquare.model.Question;
import com.chernyee.cssquare.model.QuestionDao;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Issac on 3/28/2016.
 */

@Parcel
public class QuestionList {

    public static String [] code_tag = {"All", "Array","String", "Hash table" , "Linked List", "Stack",
            "Tree" ,"Binary Search", "Backtracking", "DP" , "DFS", "BFS", "Greedy", "Design","Divide and Conquer","Sort", "Math", "Bit Manipulation"};



    public static List<Question1> getViewPosition(int position, String difficulty, String sortBy){

        List<Question1> newQuestion1List = new ArrayList<>();
        Iterator<Question1> itr = VarInit.getSharedCodeListInstance().iterator();
        while(itr.hasNext()){
            Question1 q = itr.next();
            if(position == 0){
                if(difficulty.contains(q.difficulty)){
                    newQuestion1List.add(q);
                }
            } else if(q.tag.contains(code_tag[position])){
                if(difficulty.contains(q.difficulty)){
                    newQuestion1List.add(q);
                }
            }
        }
        if(sortBy.equals("titleAscending")){
            Collections.sort(newQuestion1List, new Comparator<Question1>() {
                @Override
                public int compare(Question1 lhs, Question1 rhs) {
                    return lhs.title.substring(lhs.title.indexOf("]") + 1).compareTo(rhs.title.substring(rhs.title.indexOf("]") + 1));
                }
            });
        } else if(sortBy.equals("titleDescending")){
            Collections.sort(newQuestion1List, new Comparator<Question1>() {
                @Override
                public int compare(Question1 lhs, Question1 rhs) {
                    return rhs.title.substring(rhs.title.indexOf("]") + 1).compareTo(lhs.title.substring(lhs.title.indexOf("]") + 1));
                }
            });
        } else{
            // default sort
            Collections.sort(newQuestion1List, new Comparator<Question1>() {
                @Override
                public int compare(Question1 lhs, Question1 rhs) {
                    return lhs.title.substring(lhs.title.indexOf("]") + 1).compareTo(rhs.title.substring(rhs.title.indexOf("]") + 1));
                }
            });
        }

        return newQuestion1List;

    }

    public static int getRemaining(String difficulty, Context context , boolean complete){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int count = 0;

        int remaining = sharedPreferences.getInt("cs" + difficulty.toLowerCase(), 0);
        if(remaining == 0){ // haven't run through the list yet

            DaoDBHelper daoDBHelper = DaoDBHelper.getInstance(context);
            QuestionDao questionDao = daoDBHelper.getQuestionDao();
            QueryBuilder qb = questionDao.queryBuilder();

            if(complete){
                count = (int) qb.where(QuestionDao.Properties.Read.eq(true)).buildCount().count();
            } else{
                count = (int) qb.where(qb.and(QuestionDao.Properties.Read.eq(false),
                        QuestionDao.Properties.Difficulty.eq(difficulty))).buildCount().count();
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cs" + difficulty.toLowerCase(), count);
            editor.commit();

        } else{
            count = remaining;
        }

        return count;
    }



    public static List<Question2> getInterviewList(String tag){

        List<Question2> newQuestion2List = new ArrayList<>();
        Iterator<Question2> itr = VarInit.getSharedQAListInstance().iterator();
        while(itr.hasNext()){
            Question2 q = itr.next();
            if(q.category.equals(tag)){
                newQuestion2List.add(q);
            }
        }
        return newQuestion2List;
    }
}
