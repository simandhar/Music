package com.example.simandhar.myapplication;

import android.graphics.Bitmap;

public class AudioModel {
    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    Bitmap bitimage;
    Bitmap blurimage;
    String aArt;

    public String getaPath() {
        return aPath;
    }
    public void setaPath(String aPath) {
        this.aPath = aPath;
    }
    public String getaName() {
        return aName;
    }
    public void setaName(String aName) {
        this.aName = aName;
    }
    public void setaArt(String aArt) {
        this.aArt = aArt;
    }
    public String getaAlbum() {
        return aAlbum;
    }
    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }
    public String getaArtist() {
        return aArtist;
    }
    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }
    public String getaArt() {
        return aArt;
    }
    public void setabitimage            (Bitmap abitimage) {
        this.bitimage = abitimage;
    }
    public Bitmap getabitimage() {
        return bitimage;
    }
    public void setablurbitimage            (Bitmap abitimage) {
        this.blurimage = abitimage;
    }
    public Bitmap getablurbitimage() {
        return blurimage;
    }
}