package com.nju.pojo;

public class Lyric {
    private float time;
    private String lrc;

    public Lyric() {
    }

    public Lyric(float time, String lrc) {
        this.time = time;
        this.lrc = lrc;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "time=" + time +
                ", lrc='" + lrc + '\'' +
                '}';
    }
}
