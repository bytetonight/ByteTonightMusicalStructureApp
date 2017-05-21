package android.example.com.bytetonightmusicalstructureapp;

import android.content.Intent;
import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.example.com.bytetonightmusicalstructureapp.services.MusicService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class NowPlayingActivity extends BaseActivity
{

    private TextView empty;
    private SeekBar seekBar;
    private ImageView imageView_track_image;
    private TextView textView_track_title;
    private TextView textView_track_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_now_playing);
        useTopMenu(true);
        setActiveMenuItem(textView_mnu_playing);
        useMediaControls(true, mediaCtrlSize.LARGE);
        mainContainer = (LinearLayout) findViewById(R.id.mainContent);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initLayout();
        displayTitleData();
    }

    private void initLayout()
    {
        empty = (TextView) findViewById(R.id.now_playing_empty);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        imageView_track_image = (ImageView) findViewById(R.id.imageView_track_image);
        textView_track_title = (TextView) findViewById(R.id.textView_track_title);
        textView_track_duration = (TextView) findViewById(R.id.textView_track_duration);
    }

    private void displayTitleData()
    {
        if (currentMedia == null )
        {
            empty.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
        }
        else
        {
            empty.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
            textView_track_title.setText(currentMedia.getName());
            textView_track_duration.setText(currentMedia.getFriendlyDuration());
        }
    }




}
