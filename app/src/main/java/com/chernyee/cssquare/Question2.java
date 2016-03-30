package com.chernyee.cssquare;

import org.parceler.Parcel;

/**
 * Created by Issac on 3/28/2016.
 */
@Parcel
public class Question2 {
    String id;
    String question;
    String answer;
    String category;
    String tag;

    public Question2(){

    }

    public Question2(String question){
        this.question = question;
    }

    public Question2(String id, String question, String answer, String category, String tag){

        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.tag = tag;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
