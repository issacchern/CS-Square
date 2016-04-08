package com.chernyee.cssquare;

/**
 * Created by Issac on 4/7/2016.
 */
public class FlashCardObject {

    private String frontText;
    private String backText;
    private String colorCode;

    public FlashCardObject(String frontText, String backText, String colorCode){
        this.frontText = frontText;
        this.backText = backText;
        this.colorCode = colorCode;
    }


    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public String getColorCode() {
        return colorCode;
    }
}
