package edu.dsa.dennis.restclientandroid_v2;

import com.google.gson.annotations.SerializedName;

public class Track {
    String id;
    String title;
    String singer;

    public Track(String title, String singer) {
        this.title = title;
        this.singer = singer;
    }
    public Track(String id,String title, String singer ) {
        this.id=id;
        this.title = title;
        this.singer = singer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}

