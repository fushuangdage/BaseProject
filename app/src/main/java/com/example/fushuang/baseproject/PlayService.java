package com.example.fushuang.baseproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {


    private MediaPlayer mPlayer;
    private PlayBinder mBinder;

    public PlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mBinder = new PlayBinder(mPlayer);
        mPlayer.setOnCompletionListener(this);
    }

    class PlayBinder extends Binder{
        private MediaPlayer player;

        public PlayBinder(MediaPlayer player) {
            this.player = player;
        }

        public MediaPlayer getMediaPlayer(){
            return player;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

       return mBinder;
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
