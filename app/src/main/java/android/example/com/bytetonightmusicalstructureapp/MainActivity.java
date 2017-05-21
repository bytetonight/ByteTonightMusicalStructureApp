package android.example.com.bytetonightmusicalstructureapp;

import android.content.Context;
import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        useTopMenu(false);
        setActiveMenuItem(textView_mnu_home);
        useMediaControls(true, mediaCtrlSize.SMALL);
        initMainMenu();


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (currentMedia == null)
            hideMediaControls();
        else
            showMediaControls();
    }

    @Override
    public void onBackPressed()
    {
        //There is no parent to this Activity, so just finish
        finish();
    }




}
