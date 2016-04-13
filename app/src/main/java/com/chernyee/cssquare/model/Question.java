package com.chernyee.cssquare.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "QUESTION".
 */
public class Question {

    private Long id;
    private Boolean bookmark;
    private Boolean read;
    private String comment;
    private java.util.Date date;
    private String difficulty;

    public Question() {
    }

    public Question(Long id) {
        this.id = id;
    }

    public Question(Long id, Boolean bookmark, Boolean read, String comment, java.util.Date date, String difficulty) {
        this.id = id;
        this.bookmark = bookmark;
        this.read = read;
        this.comment = comment;
        this.date = date;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}