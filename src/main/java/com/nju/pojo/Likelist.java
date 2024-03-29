package com.nju.pojo;

//歌单类
public class Likelist {
    private String listname;//歌单名
    private String username;//创建用户
    private int numOfSong;//歌曲数量
    private String creationDate; //创建日期
    private String description;//歌单描述
    private String imagePath;//图片路径

    public Likelist() {
    }

    public Likelist(String listname, String username, int numOfSong, String creationDate, String description, String imagePath) {
        this.listname = listname;
        this.username = username;
        this.numOfSong = numOfSong;
        this.creationDate = creationDate;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumOfSong() {
        return numOfSong;
    }

    public void setNumOfSong(int numOfSong) {
        this.numOfSong = numOfSong;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
