package android.example.com.bytetonightmusicalstructureapp;

import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.example.com.bytetonightmusicalstructureapp.utils.Utils;
import android.os.Bundle;
import android.widget.TextView;

public class PlayListActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        useTopMenu(true);
        setActiveMenuItem(textView_mnu_play_list);
        useMediaControls(true, mediaCtrlSize.SMALL);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Utils.fromHtml(getString(R.string.mock_playlist)));
    }


}
