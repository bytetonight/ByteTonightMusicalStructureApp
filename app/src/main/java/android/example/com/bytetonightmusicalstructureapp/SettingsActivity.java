package android.example.com.bytetonightmusicalstructureapp;

import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.example.com.bytetonightmusicalstructureapp.utils.Utils;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        useTopMenu(true);
        setActiveMenuItem(textView_mnu_settings);
        useMediaControls(true, mediaCtrlSize.SMALL);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Utils.fromHtml(getString(R.string.mock_settings)));
    }
}
