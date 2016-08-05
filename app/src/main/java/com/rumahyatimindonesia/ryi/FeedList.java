package com.rumahyatimindonesia.ryi;

import java.util.Date;

/**
 * Created by wahyudhzt on 26/04/2016.
 */
public class FeedList {
    private String title, date;
    private String image;

    public FeedList(String date, String title, String image ){
        this.title = title;
        this.image = image;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

