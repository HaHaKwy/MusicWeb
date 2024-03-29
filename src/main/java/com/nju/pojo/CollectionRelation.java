package com.nju.pojo;

//数据库中保存的收藏关系
public class CollectionRelation {
    //用户主键
    private String username;
    //歌曲主键
    private String songname;
    private String singername;
    //歌单名
    private String listname;
    //歌单名 + 用户名 = 歌单主键


    public CollectionRelation() {
        return;
    }

    public CollectionRelation(String username, String songname, String singername, String listname) {
        this.username = username;
        this.songname = songname;
        this.singername = singername;
        this.listname = listname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }
}
