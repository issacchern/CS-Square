package com.chernyee.cssquare.ApiData;

/**
 * Created by Issac on 4/6/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JokesData {

    private List<Image> images = new ArrayList<Image>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public List<Image> getImages() {
        return images;
    }


    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }



    public class Image {

        private String title;
        private String background;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

}