package android.example.com.bytetonightmusicalstructureapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.MediaController;

/**
 * Created by ByteTonight on 12.05.2017.
 */


public class MusicService extends Service
{
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //return super.onStartCommand(intent, flags, startId);
        /*mediaPlayer = MediaPlayer.create(this, "Your music file");
        mediaPlayer.start();*/
        return START_STICKY;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.stop();
    }


}
