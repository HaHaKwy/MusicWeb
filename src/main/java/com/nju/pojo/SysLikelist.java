package com.nju.pojo;

public class SysLikelist {
    private String listname;
    private String description;
    private int clickCount;//用户点击量
    private String imagePath;//图片路径

    public SysLikelist(String listname, String description, int clickCount, String imagePath) {
        this.listname = listname;
        this.description = description;
        this.clickCount = clickCount;
        this.imagePath = imagePath;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
