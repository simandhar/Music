package com.example.simandhar.myapplication;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class Myplayer {
    public int song_no;
    public int prev_song;
    public static final MediaPlayer mp =new MediaPlayer();
    public final int PLAY = 0;
    public final int PAUSE = 1;
    public final int STOP = 3;
    public int duration;
    public int state;
    public  static MyAdapter mA;
    public static List<AudioModel> mDataset;

    public Myplayer(int song,List <AudioModel> mDataset)
    {
        song_no =song;
        prev_song=-1;
        state=-1;
        this.mDataset=mDataset;
       // mp= new MediaPlayer();
    }
    public void setAd(MyAdapter myAdapter)
    {
        mA=myAdapter;
    }


}
