package android.example.com.bytetonightmusicalstructureapp;

import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.os.Bundle;

public class ArtistsActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        useTopMenu(true);
        setActiveMenuItem(textView_mnu_artists);
        useMediaControls(true, mediaCtrlSize.SMALL);
    }
}
