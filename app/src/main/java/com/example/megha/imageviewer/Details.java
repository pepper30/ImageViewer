package com.example.megha.imageviewer;

import android.provider.ContactsContract;

public class Details {


    String image;
    String title;

    int likes;

    public Details() {
    }

    public Details(String image, String title, int likes) {
        this.image = image;
        this.title = title;
        this.likes = likes;
    }

    public String getImgUrl() {
        return this.image;
    }

    public void setImgUrl(String imgUrl) {
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return " " + this.getImgUrl() + " " + this.getTitle() + " " + this.getLikes();
    }
}
