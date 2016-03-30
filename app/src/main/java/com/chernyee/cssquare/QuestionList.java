package com.chernyee.cssquare;

import android.content.Context;
import android.content.SharedPreferences;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Issac on 3/28/2016.
 */

@Parcel
public class QuestionList {

    public static String [] code_tag = {"All", "Array","String", "Hash table" , "Linked List", "Stack",
            "Tree" ,"Binary Search", "Backtracking", "DP" , "DFS", "BFS", "Greedy", "Design","Divide and Conquer","Sort", "Math", "Bit Manipulation"};


    public static List<Question> getViewPosition(int position, String difficulty, String sortBy){

        List<Question> newQuestionList = new ArrayList<>();
        Iterator<Question> itr = SplashActivity.sharedCodeList.iterator();
        while(itr.hasNext()){
            Question q = itr.next();
            if(position == 0){
                if(difficulty.contains(q.difficulty)){
                    newQuestionList.add(q);
                }
            } else if(q.tag.contains(code_tag[position])){
                if(difficulty.contains(q.difficulty)){
                    newQuestionList.add(q);
                }
            }
        }
        if(sortBy.equals("titleAscending")){
            Collections.sort(newQuestionList, new Comparator<Question>() {
                @Override
                public int compare(Question lhs, Question rhs) {
                    return lhs.title.compareTo(rhs.title);
                }
            });
        } else if(sortBy.equals("titleDescending")){
            Collections.sort(newQuestionList, new Comparator<Question>() {
                @Override
                public int compare(Question lhs, Question rhs) {
                    return rhs.title.compareTo(lhs.title);
                }
            });
        } else{
            // default sort
            Collections.sort(newQuestionList, new Comparator<Question>() {
                @Override
                public int compare(Question lhs, Question rhs) {
                    return lhs.title.compareTo(rhs.title);
                }
            });
        }

        return newQuestionList;

    }



    public static List<Question> getDifficulty(String difficulty, Context context , boolean complete){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        List<Question> newQuestionList = new ArrayList<>();
        Iterator<Question> itr = SplashActivity.sharedCodeList.iterator();
        while(itr.hasNext()){
            Question q = itr.next();
            if(complete){
                String markString = "cse"+ q.id;
                int markScore = sharedPreferences.getInt(markString, 0);
                if(markScore == 1){
                    newQuestionList.add(q);
                }
            } else{
                if(q.difficulty.contains(difficulty)){
                    newQuestionList.add(q);
                }
            }
        }
        Collections.sort(newQuestionList, new Comparator<Question>() {
            @Override
            public int compare(Question lhs, Question rhs) {
                return lhs.title.compareTo(rhs.title);
            }
        });


        return newQuestionList;
    }



    public static List<Question2> getInterviewList(String tag){

        List<Question2> newQuestion2List = new ArrayList<>();
        Iterator<Question2> itr = SplashActivity.sharedQAList.iterator();
        while(itr.hasNext()){
            Question2 q = itr.next();
            if(q.category.equals(tag)){
                newQuestion2List.add(q);
            }
        }
        return newQuestion2List;
    }
}
