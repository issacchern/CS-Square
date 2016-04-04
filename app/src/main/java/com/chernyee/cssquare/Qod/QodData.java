package com.chernyee.cssquare.Qod;

/**
 * Created by Issac on 4/3/2016.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QodData {

    private Success success;
    private Contents contents;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Success getSuccess() {
        return success;
    }

    public Contents getContents() {
        return contents;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public class Success {

        private Integer total;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getTotal() {
            return total;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }
    }


    public class Quote {
        private String quote;
        private String length;
        private String author;
        private List<String> tags = new ArrayList<String>();
        private String category;
        private String date;
        private String title;
        private String background;
        private String id;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        public String getQuote() {
            return quote;
        }

        public String getLength() {
            return length;
        }

        public String getAuthor() {
            return author;
        }

        public List<String> getTags() {
            return tags;
        }

        public String getCategory() {
            return category;
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public String getBackground() {
            return background;
        }

        public String getId() {
            return id;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }
    }


    public class Contents {

        private List<Quote> quotes = new ArrayList<Quote>();
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public List<Quote> getQuotes() {
            return quotes;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }
    }
}




