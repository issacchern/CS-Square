package com.chernyee.cssquare;


import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by Issac on 3/28/2016.
 */

@Parcel
public class Question{


    String id;
    String title;
    String description;
    String code;
    String answer;
    String hint;
    String tag;
    String category;
    String difficulty;
    String additional;

    public Question(){

    }

    public Question(String id, String title, String description, String code,
                        String answer, String hint, String tag, String category,
                        String difficulty, String additional){
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
        this.answer = answer;
        this.hint = hint;
        this.tag = tag;
        this.category = category;
        this.difficulty = difficulty;
        this.additional = additional;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
