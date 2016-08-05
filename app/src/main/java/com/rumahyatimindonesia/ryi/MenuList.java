package com.rumahyatimindonesia.ryi;

/**
 * Created by wahyudhzt on 4/19/2016.
 */
public class MenuList {

    private String title;
    private int image;

    public MenuList(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
