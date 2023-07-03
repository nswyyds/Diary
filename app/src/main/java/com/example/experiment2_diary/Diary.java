package com.example.experiment2_diary;

import org.w3c.dom.Text;

import java.sql.Blob;

public class Diary {

    public Diary(String title, String author, String content, Blob picture, String time) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.picture = picture;
        this.time = time;
    }

    public String title;
    public String author;
    public String content;
    public Blob picture;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }




}
