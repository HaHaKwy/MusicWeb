package com.nju.pojo;

public class Comment {
    private String username;
    private String listname;
    private String creator;
    private String text;
    private String timestamp;

    public Comment(){

    }

    public Comment(String username, String listname, String creator, String text, String timestamp) {
        this.username = username;
        this.listname = listname;
        this.creator = creator;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
