package com.example.todolistapp.Model;

public class Task {

    private String title;
    private String detail;
    private String dateAddedOn;
    private int id;

    public Task() {
    }

    public Task(String title, String detail, String dateAddedOn, int id) {
        this.title = title;
        this.detail = detail;
        this.dateAddedOn = dateAddedOn;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDateAddedOn() {
        return dateAddedOn;
    }

    public void setDateAddedOn(String dateAddedOn) {
        this.dateAddedOn = dateAddedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
