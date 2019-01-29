package com.example.simandhar.myapplication;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AudioModel> myDataset;
    private boolean flag=true;

   // private View cons_lay=findViewById(R.id.cons_lay);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        final Myplayer Myp=new Myplayer(-1,myDataset);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_list1);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        myDataset= getAllAudioFromDevice(this);
        mAdapter = new MyAdapter(myDataset,Myp,this);
        mRecyclerView.setAdapter(mAdapter);
        Myp.setAd((MyAdapter) mAdapter);
        final View barview=findViewById(R.id.barview);
        final TextView bartext =findViewById(R.id.bartext);
        final ImageButton barbutton=findViewById(R.id.barbutton);
        final ImageView barimage=findViewById(R.id.barimage);
        final View player=findViewById(R.id.player);
        final ImageView album_big=findViewById(R.id.albumart_big);
        final SeekBar seekbar=findViewById(R.id.seekBar);
        final TextView seekval =findViewById(R.id.seekval);
        final TextView totaltime=findViewById(R.id.totaltime);
        final ImageButton Next=findViewById(R.id.next);
        final ImageButton Prev=findViewById(R.id.previous);
        final ImageButton Play_pause=findViewById(R.id.play_pause);
        final View buttons =findViewById(R.id.buttons);
        barbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Myp.state==1)
                {
                    barbutton.setImageResource(R.drawable.pause);
                    Play_pause.setImageResource(R.drawable.pause);
                    Myp.state=0;
                    Myp.mp.start();

                }
                else
                {
                    barbutton.setImageResource(R.drawable.play);
                    Play_pause.setImageResource(R.drawable.play);
                    Myp.state=1;
                    Myp.mp.pause();

                }
                mAdapter.notifyItemChanged(Myp.song_no);

            }
        });


        bartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setVisibility(View.GONE);
                buttons.setVisibility(View.VISIBLE);
                barview.setVisibility(View.GONE);
                player.setVisibility(View.VISIBLE);
                seekbar.setMax(Myp.duration);
                totaltime.setText((Myp.duration/1000)/60+":"+String.format("%02d",(int)(Myp.duration/1000)%60).toString());


            }
        });
        album_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setVisibility(View.GONE);
                buttons.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                barview.setVisibility(View.VISIBLE);


            }
        });
        new Thread(new Runnable(){

            public void run() {
                while (true) {
                    if (Myp.song_no != -1 && Myp.mp.isPlaying() && flag==true) {

                        seekbar.setProgress(Myp.mp.getCurrentPosition());


                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }
        }).start();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekval.setText((progress/1000)/60+":"+String.format("%02d",(int)(progress/1000)%60));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                flag=false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Myp.mp.seekTo(seekBar.getProgress());
                flag=true;

            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myp.prev_song=Myp.song_no;
                Myp.song_no=(Myp.song_no+1) % myDataset.size();
                try {
                    Myp.mp.reset();
                    Myp.mp.setDataSource(myDataset.get(Myp.song_no).getaPath());
                    Myp.mp.prepare();
                    Myp.mp.start();
                    Myp.duration=Myp.mp.getDuration();
                    seekbar.setMax(Myp.mp.getDuration());
                }
                catch(Exception e){

                }
                totaltime.setText((Myp.duration/1000)/60+":"+String.format("%02d",(int)(Myp.duration/1000)%60).toString());
                Myp.state=0;
                Play_pause.setImageResource(R.drawable.pause);
                barbutton.setImageResource(R.drawable.pause);
                mAdapter.notifyItemChanged(Myp.prev_song);
                mAdapter.notifyItemChanged(Myp.song_no);
                bartext.setText(myDataset.get(Myp.song_no).getaName());
                barimage.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
                album_big.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
               // findViewById(R.id.cons_lay).setBackgroundDrawable(new BitmapDrawable(blur.fastblur(myDataset.get(Myp.song_no).getabitimage(),(float)0.3,50)));


            }
        });
        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myp.prev_song=Myp.song_no;
                if(Myp.mp.getCurrentPosition()>5000)
                {
                    //keep the same song no
                }
                else {
                    if (((Myp.song_no - 1) % myDataset.size()) < 0) {
                        Myp.song_no = myDataset.size() + ((Myp.song_no - 1) % myDataset.size());
                    } else {
                        Myp.song_no = (Myp.song_no - 1) % myDataset.size();
                    }
                }
                Log.e("Myactivity","Myp.song_no "+Myp.song_no);
                try {
                    Myp.mp.reset();
                    Myp.mp.setDataSource(myDataset.get(Myp.song_no).getaPath());
                    Myp.mp.prepare();
                    Myp.mp.start();
                    Myp.duration=Myp.mp.getDuration();
                    seekbar.setMax(Myp.mp.getDuration());
                }
                catch(Exception e){

                }
                Myp.state=0;
                Play_pause.setImageResource(R.drawable.pause);
                barbutton.setImageResource(R.drawable.pause);
                totaltime.setText((Myp.duration/1000)/60+":"+String.format("%02d",(int)(Myp.duration/1000)%60).toString());
                mAdapter.notifyItemChanged(Myp.prev_song);
                mAdapter.notifyItemChanged(Myp.song_no);
                bartext.setText(myDataset.get(Myp.song_no).getaName());
                barimage.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
                album_big.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
                //findViewById(R.id.cons_lay).setBackgroundDrawable(new BitmapDrawable(blur.fastblur(myDataset.get(Myp.song_no).getabitimage(),(float)0.3,50)));


            }
        });
        Play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Myp.state==1)
                {
               Myp.mp.start();
               Play_pause.setImageResource(R.drawable.pause);
                    barbutton.setImageResource(R.drawable.pause);
                Myp.state=0;
                }
                else
                {
                    Myp.mp.pause();
                    Play_pause.setImageResource(R.drawable.play);
                    barbutton.setImageResource(R.drawable.play);
                    Myp.state=1;
                }
                mAdapter.notifyItemChanged(Myp.song_no);

            }
        });
        Myp.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Myp.state=1;
                Myp.prev_song=Myp.song_no;
                Myp.song_no=(Myp.song_no+1) % myDataset.size();
                try {
                    Myp.mp.reset();
                    Myp.mp.setDataSource(myDataset.get(Myp.song_no).getaPath());
                    Myp.mp.prepare();
                    Myp.mp.start();
                    Myp.duration=Myp.mp.getDuration();
                    seekbar.setMax(Myp.mp.getDuration());
                }
                catch(Exception e){

                }
                totaltime.setText((Myp.duration/1000)/60+":"+String.format("%02d",(int)(Myp.duration/1000)%60).toString());
                bartext.setText(myDataset.get(Myp.song_no).getaName());
                mAdapter.notifyItemChanged(Myp.prev_song);
                mAdapter.notifyItemChanged(Myp.song_no);
                barimage.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
                    album_big.setImageBitmap(myDataset.get(Myp.song_no).getabitimage());
                //findViewById(R.id.cons_lay).setBackgroundDrawable(new BitmapDrawable(blur.fastblur(myDataset.get(Myp.song_no).getabitimage(),(float)0.3,50)));

            }
        });
        
    }


    public List<AudioModel> getAllAudioFromDevice(final Context context) {
        final List<AudioModel> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        //Bitmap bitmap;
        //Bitmap bluri;
        if (c != null) {
            while (c.moveToNext()) {
                AudioModel audioModel = new AudioModel();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);
                //String art = c.getString(4);
                if(path != null && path.endsWith(".mp3")) {
                    audioModel.setaName(name);
                    audioModel.setaAlbum(album);
                    audioModel.setaArtist(artist);
                    audioModel.setaPath(path);
                    mmr.setDataSource(path);
                    byte[] data = mmr.getEmbeddedPicture();
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        //Bitmap bluri= blur.fastblur(bitmap,(float)0.2,15);
                        //audioModel.setablurbitimage(bluri);
                        audioModel.setabitimage(bitmap);

                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music);
                        //Bitmap bluri= blur.fastblur(bitmap,(float)0.2,20);
                        //audioModel.setablurbitimage(bluri);
                        audioModel.setabitimage(bitmap);
                       // audioModel.setabitimage(null);
                    }
                    tempAudioList.add(audioModel);
                }
                //audioModel.setaArt(art);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);


            }
            c.close();
        }

        return tempAudioList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
