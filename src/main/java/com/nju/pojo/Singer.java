package com.nju.pojo;

public class Singer {
    private String singername;//歌手名
    private int albumnum;//专辑数
    private int songnum;//单曲数
    private String imagePath;//图片路径
    private String description;//歌手描述信息

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public int getAlbumnum() {
        return albumnum;
    }

    public void setAlbumnum(int albumnum) {
        this.albumnum = albumnum;
    }

    public int getSongnum() {
        return songnum;
    }

    public void setSongnum(int songnum) {
        this.songnum = songnum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Singer{" +
                "singername='" + singername + '\'' +
                ", albumnum=" + albumnum +
                ", songnum=" + songnum +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
