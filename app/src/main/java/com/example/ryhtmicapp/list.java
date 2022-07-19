package com.example.ryhtmicapp;

public class list {
  /*  var songname: String? = "",
    var songurl: String?="",

    var imguri: String?="",
    var userId: String?="",
    var votesCount: Int = 0,
    var id: Int =0,*/
    private String songname="";
    private String songurl="";
    private String imguri="";

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongurl() {
        return songurl;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public list(String songname, String songurl, String imguri) {
        this.songname = songname;
        this.songurl = songurl;
        this.imguri = imguri;
    }
}
