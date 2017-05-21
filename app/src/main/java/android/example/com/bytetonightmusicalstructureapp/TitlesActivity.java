package android.example.com.bytetonightmusicalstructureapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.example.com.bytetonightmusicalstructureapp.adapter.TitlesAdapter;
import android.example.com.bytetonightmusicalstructureapp.base.BaseActivity;
import android.example.com.bytetonightmusicalstructureapp.models.MediaFile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TitlesActivity extends BaseActivity
{
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 19213712;
    private ListView mListView;
    private TitlesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        useTopMenu(true);
        setActiveMenuItem(textView_mnu_songs);
        useMediaControls(true, mediaCtrlSize.SMALL);
        loadTitlesFromSdCard();
        initListView();

    }

    private void initListView()
    {
        mListView = (ListView) findViewById(R.id.titlesList);
        mAdapter = new TitlesAdapter(this, R.layout.title_list_item, mediaFiles);
        mListView.setOnItemClickListener(mItemClickListener);
        mListView.setOnItemLongClickListener(mItemLongClickListener);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
    }


    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            currentMedia = (MediaFile) parent.getItemAtPosition(position);
            showMediaControls();
            //Toast.makeText(TitlesActivity.this, currentTitle.getFriendlyDuration(), Toast.LENGTH_SHORT).show();
        }
    };

    private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            currentMedia = (MediaFile) parent.getItemAtPosition(position);
            showMediaControls();
            return false;
        }
    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.title_options_menu, menu);
        ListView lv = (ListView)v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MediaFile obj = (MediaFile) lv.getItemAtPosition(acmi.position);
        //Toast.makeText(TitlesActivity.this,   obj.getName(),Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        MediaFile obj = (MediaFile) mListView.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.play:
                //editNote(info.id);
                //Call play method
                if (!isPlaying)
                    mediaPlayPause.performClick();
                return true;
            case R.id.add:
                //deleteNote(info.id);
                playList.add(obj);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Loads the list of music files from the external storage into a list of mediaFiles
     */
    private void loadTitlesFromSdCard()
    {
        /*
            Will fix this later
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                + Environment.getExternalStorageDirectory())));
                */

        //Manifest permissions alone will not do the trick on Android N
        //so we need to request user permissions like in LSL scripts
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }
        //mediaFiles.clear();
        ContentResolver cr = this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor resultSet = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if (resultSet != null)
        {
            count = resultSet.getCount();

            if (count > 0)
            {
                while (resultSet.moveToNext())
                {
                    int filetitle = resultSet.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int file_artist = resultSet.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int file_id = resultSet.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int filePath = resultSet.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int duration = resultSet.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    MediaFile info = new MediaFile(
                            resultSet.getString(file_id),
                            resultSet.getString(filetitle),
                            resultSet.getString(file_artist),
                            resultSet.getString(filePath),
                            resultSet.getLong(duration)
                    );
                    mediaFiles.add(info);
                }
            }
        }

        resultSet.close();
    }



}
