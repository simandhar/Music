package com.example.simandhar.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<AudioModel>  mDataset;
    private Myplayer myp;
    public MainActivity main_a;
    final ImageButton barbutton ;//=main_a.findViewById(R.id.barbutton);
    final TextView bartext;// =main_a.findViewById(R.id.bartext);
    final ImageView barimage;// =main_a.findViewById(R.id.barimage);
    final ImageView album_big;
    final TextView seekval;
    final SeekBar seekbar;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ImageButton mImageButton;
        public ImageView albumart;
        public ImageView albumart_for;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.song);
            albumart = v.findViewById(R.id.albumart);
            albumart_for = v.findViewById(R.id.albumart_for);
            mImageButton=v.findViewById(R.id.imageButton);

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<AudioModel> myDataset, Myplayer myp,MainActivity main_a) {
        mDataset = myDataset;
        this.myp=myp;
        this.main_a=main_a;
        barbutton =main_a.findViewById(R.id.barbutton);
        bartext =main_a.findViewById(R.id.bartext);
        barimage =main_a.findViewById(R.id.barimage);
        album_big=main_a.findViewById(R.id.albumart_big);
        seekbar =main_a.findViewById(R.id.seekBar);
        seekval=main_a.findViewById(R.id.seekval);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_list, parent, false);
        final MyViewHolder vh = new MyViewHolder(v);
        final MyAdapter mA=this;

        vh.mTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                final int curr_position = vh.getAdapterPosition();
                if (myp.song_no == curr_position) {

                        myp.state = 0;
                        barbutton.setImageResource(R.drawable.pause);
                        ImageView iv=main_a.findViewById(R.id.play_pause);
                        iv.setImageResource(R.drawable.pause);
                        myp.prev_song = myp.song_no;

                } else {
                    myp.state = 0;
                    myp.prev_song = myp.song_no;
                    myp.song_no = curr_position;
                    barbutton.setImageResource(R.drawable.pause);
                    bartext.setText(mDataset.get(myp.song_no).getaName());

                        album_big.setImageBitmap(mDataset.get(myp.song_no).getabitimage());
                        barimage.setImageBitmap(mDataset.get(myp.song_no).getabitimage());

                    //main_a.findViewById(R.id.cons_lay).setBackgroundDrawable(new BitmapDrawable(blur.fastblur(mDataset.get(myp.song_no).getabitimage(),(float)0.2,20)));
                    ImageView iv=main_a.findViewById(R.id.play_pause);
                    iv.setImageResource(R.drawable.pause);


                }
                mA.notifyItemChanged(myp.prev_song);
                mA.notifyItemChanged(curr_position);
                main_a.findViewById(R.id.barview).setVisibility(View.VISIBLE);
                main_a.findViewById(R.id.barview).setBackgroundResource(R.drawable.border);
                //Bitmap bluri= blur.fastblur(mDataset.get(myp.song_no).getabitimage(),(float)0.1,25);
               // bluri=blur.applyDarkFilter(bluri);
                //main_a.findViewById(R.id.coord_lay).setBackgroundColor(blur.getaveragecolor(mDataset.get(myp.song_no).getabitimage())); //setBackgroundDrawable(new BitmapDrawable(main_a.getResources(),bluri));
                //main_a.findViewById(R.id.coord_lay).setBackg;
                try {
                    myp.mp.reset();
                    myp.mp.setDataSource(mDataset.get(myp.song_no).getaPath());
                    myp.mp.prepare();
                    myp.duration=myp.mp.getDuration();

                    myp.mp.start();
                }
                catch (IOException ie){}

            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.mTextView.setText(mDataset.get(position).getaName());
            if(mDataset.get(position).getabitimage()!=null) {
                holder.albumart.setImageBitmap(mDataset.get(position).getabitimage()); //associated cover art in bitmap
                holder.albumart.setAdjustViewBounds(true);
            }
            else
            {
                holder.albumart.setImageResource(R.drawable.music);
            }
            if(position==myp.song_no && myp.state==0)
            {

               // holder.mImageButton.setImageResource(R.drawable.pause);
                holder.mTextView.setTextColor(Color.RED);
            }
            else
            {
                //holder.mImageButton.setImageResource(R.drawable.play);
                holder.mTextView.setTextColor(Color.BLACK);
            }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}