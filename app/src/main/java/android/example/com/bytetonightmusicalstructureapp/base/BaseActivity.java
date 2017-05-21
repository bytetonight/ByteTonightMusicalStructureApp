package android.example.com.bytetonightmusicalstructureapp.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.example.com.bytetonightmusicalstructureapp.R;
import android.example.com.bytetonightmusicalstructureapp.models.MediaFile;
import android.example.com.bytetonightmusicalstructureapp.resolver.TagMenuActivity;
import android.example.com.bytetonightmusicalstructureapp.utils.Utils;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ByteTonight on 10.05.2017.
 * All Activities belonging to this App extend this Class to inherit functionality
 * such as a HorizontalScrollView top menu to simulate tabs, and MediaControls
 */

public class BaseActivity extends AppCompatActivity
{
    private static final String TAG = BaseActivity.class.getName();
    private static final String MY_CLASS_PACKAGE = "android.example.com.bytetonightmusicalstructureapp.";
    protected ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    protected ArrayList<MediaFile> playList = new ArrayList<>();
    protected static MediaFile currentMedia = null;
    protected LinearLayout mediaCtrlContainer;

    protected static boolean isPlaying = false;

    protected enum mediaCtrlSize
    {
        SMALL, LARGE
    }

    //protected HorizontalScrollView horizontalTopMenu;
    protected ViewStub mediaCtrl;
    protected ViewGroup mainContainer;
    protected TextView textView_mnu_home, textView_mnu_playing, textView_mnu_play_list,
             textView_mnu_albums, textView_mnu_songs,textView_mnu_artists, textView_mnu_settings,
            activeMenuItem, mediaPlayPause;

    private HashMap<String, String> taggedIntentMap  = new HashMap<>();

    protected AudioManager mAudioManager;
    private MediaPlayer mediaPlayer;


//region Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /**
         * Dear Reviewer,
         * before you say it : The Activity Class names are in HERE intentionally.
         * If I wanted them in strings.xml I would have added them to the tags directly.
         * Thank you for your understanding.
         */
        taggedIntentMap.put(getString(R.string.tag_home), "MainActivity");
        taggedIntentMap.put(getString(R.string.tag_playing), "NowPlayingActivity");
        taggedIntentMap.put(getString(R.string.tag_playlist), "PlayListActivity");
        taggedIntentMap.put(getString(R.string.tag_albums), "AlbumsActivity");
        taggedIntentMap.put(getString(R.string.tag_titles), "TitlesActivity");
        taggedIntentMap.put(getString(R.string.tag_artists), "ArtistsActivity");
        taggedIntentMap.put(getString(R.string.tag_settings), "SettingsActivity");

        mediaFiles.add(new MediaFile("1", "Virtual Title 1", "Virtual Artist", "virtual Path", 20000));
        mediaFiles.add(new MediaFile("2", "Virtual Title 2", "Virtual Artist", "virtual Path", 22000));
        mediaFiles.add(new MediaFile("3", "Virtual Title 3", "Virtual Artist", "virtual Path", 19000));


    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /**
         * Now this is a good time to use onResume
         * Each Child Activity that has NOT yet been created
         * inherits the correct stateful value of isPlaying from Base.
         * If an Activity is already started, and makes changes to the state of Base, these
         * changes will not reflect in other already started Activities.
         *
         * onResume makes sure the state of Base applies to Activities that have been started
         * already, and which you (re)enter by navigating back or similar.
         * I know .... life ain't easy
         */

        playPauseFlipFlopIcon();
        scrollToActiveMenuItem();


    }



    /**
     * All children use this event, except those which override this event,
     * for example those that don't have a parent Activity as per Manifest
     */
    @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }
//endregion

//region EventHandlers
    /**
     * This OnClickListener is only for the top menu
     */
    private View.OnClickListener mnuClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            if (v instanceof TextView || v instanceof LinearLayout)
            {
                String tag = v.getTag().toString();
                runActivity(tag);
            }
        }
    };

    /**
     * This OnClickListener is only for the Media Control(s)
     * and currently only toggles play / pause
     */
    private View.OnClickListener mediaCtrlClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            if (v instanceof TextView)
            {
                if (v == mediaPlayPause)
                {
                    isPlaying = !isPlaying;
                    playPauseFlipFlopIcon();
                }
            }
        }
    };


    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener()
            {
                @Override
                public void onAudioFocusChange(int focusChange)
                {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                    {

                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
                    {
                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                        mediaPlayer.start();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
                    {
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback and clean up resources
                        releaseMediaPlayer();
                    }
                }
            };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            Toast.makeText(BaseActivity.this, "mediaplayer completed", Toast.LENGTH_SHORT).show();
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };
//endregion

    /**
     * Call this to enable the top menu in an Activity extending this Class
     */
    protected void useTopMenu(boolean whether)
    {
        if (whether)
        {
            ViewStub topMenuStub = (ViewStub) findViewById(R.id.top_menu_stub);
            topMenuStub.setLayoutResource(R.layout.top_menu);
            topMenuStub.inflate();
            initTopMenu();
        }
    }



    protected void useMediaControls(boolean whether, mediaCtrlSize size)
    {
        if (whether)
        {
            mediaCtrl = (ViewStub) findViewById(R.id.media_control_stub);
            if (size == mediaCtrlSize.SMALL)
                mediaCtrl.setLayoutResource(R.layout.media_controls_small);
            else
                mediaCtrl.setLayoutResource(R.layout.media_controls_large);
            //mediaCtrl.setLayoutResource(R.layout.media_controls_large);

            mediaCtrl.inflate();
            initMediaControls();
        }
    }

    protected void showMediaControls()
    {
        if (null != mediaCtrlContainer)
        {
            mediaCtrlContainer.setVisibility(View.VISIBLE);
        }
    }

    protected void hideMediaControls()
    {
        if (null != mediaCtrlContainer)
        {
            mediaCtrlContainer.setVisibility(View.GONE);
        }
    }

    /**
     * What the name implies
     */
    private void initTopMenu()
    {
        try
        {
            //horizontalTopMenu = (HorizontalScrollView) findViewById(R.id.top_menu);
            textView_mnu_home = (TextView) findViewById(R.id.mnu_home);
            textView_mnu_playing = (TextView) findViewById(R.id.mnu_playing);
            textView_mnu_play_list = (TextView) findViewById(R.id.mnu_play_list);
            textView_mnu_albums = (TextView) findViewById(R.id.mnu_albums);
            textView_mnu_songs = (TextView) findViewById(R.id.mnu_songs);
            textView_mnu_artists = (TextView) findViewById(R.id.mnu_artists);
            textView_mnu_settings = (TextView) findViewById(R.id.mnu_settings);

            /**
             * Yes I want individual Listeners only for the top menu
             */
            textView_mnu_home.setOnClickListener(mnuClickListener);
            textView_mnu_playing.setOnClickListener(mnuClickListener);
            textView_mnu_play_list.setOnClickListener(mnuClickListener);
            textView_mnu_albums.setOnClickListener(mnuClickListener);
            textView_mnu_songs.setOnClickListener(mnuClickListener);
            textView_mnu_artists.setOnClickListener(mnuClickListener);
            textView_mnu_settings.setOnClickListener(mnuClickListener);
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "Forgot to include the top menu layout ?\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * What the name implies. These items don't have to be global
     */
    protected void initMainMenu()
    {
        try
        {
            TextView textView_main_mnu_playing = (TextView) findViewById(R.id.main_mnu_playing);
            TextView textView_main_mnu_play_list = (TextView) findViewById(R.id.main_mnu_play_list);
            TextView textView_main_mnu_albums = (TextView) findViewById(R.id.main_mnu_albums);
            TextView textView_main_mnu_songs = (TextView) findViewById(R.id.main_mnu_songs);
            TextView textView_main_mnu_artists = (TextView) findViewById(R.id.main_mnu_artists);
            TextView textView_main_mnu_settings = (TextView) findViewById(R.id.main_mnu_settings);


            textView_main_mnu_playing.setOnClickListener(mnuClickListener);
            textView_main_mnu_play_list.setOnClickListener(mnuClickListener);
            textView_main_mnu_albums.setOnClickListener(mnuClickListener);
            textView_main_mnu_songs.setOnClickListener(mnuClickListener);
            textView_main_mnu_artists.setOnClickListener(mnuClickListener);
            textView_main_mnu_settings.setOnClickListener(mnuClickListener);
            
            /*((LinearLayout)textView_main_mnu_playing.getParent()).setOnClickListener(mnuClickListener);
            ((LinearLayout)textView_main_mnu_play_list.getParent()).setOnClickListener(mnuClickListener);
            ((LinearLayout)textView_main_mnu_albums.getParent()).setOnClickListener(mnuClickListener);
            ((LinearLayout)textView_main_mnu_songs.getParent()).setOnClickListener(mnuClickListener);
            ((LinearLayout)textView_main_mnu_artists.getParent()).setOnClickListener(mnuClickListener);
            ((LinearLayout)textView_main_mnu_settings.getParent()).setOnClickListener(mnuClickListener);*/
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "Forgot to include the main menu ?\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void initMediaControls()
    {
        mediaCtrlContainer = (LinearLayout) findViewById(R.id.media_controls_container);
        mediaPlayPause = (TextView) findViewById(R.id.mediaPlayPause);
        /**
         * Yes I want an individual Listener only for the media control(s)
         */
        mediaPlayPause.setOnClickListener(mediaCtrlClickListener);
        playPauseFlipFlopIcon();

        if (currentMedia == null)
            hideMediaControls();
    }



    /**
     * Monitors BaseMenuActivity.isPlaying and sets the according Drawable on the MediaControl.
     */
    private void playPauseFlipFlopIcon()
    {
        Drawable playPauseIcon;
        if (isPlaying)
            playPauseIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_pause_circle_outline_white_48dp, null);
        else
            playPauseIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_play_circle_outline_white_48dp, null);

        Utils.setBackGroundDrawable(mediaPlayPause, playPauseIcon);
    }

    /**
     * Start an Activity from the Menu
     * @param tag
     */
    protected void runActivity(String tag)
    {

        String targetClass = taggedIntentMap.get( tag );
        if (targetClass == null)
            return;
        String fullyQualifiedClassName = MY_CLASS_PACKAGE + targetClass;
        //Toast.makeText(BaseMenuActivity.this, fullyQualifiedClassName, Toast.LENGTH_SHORT).show();
        Class<?> c = null;
        if (!fullyQualifiedClassName.isEmpty())
        {
            try
            {
                c = Class.forName(fullyQualifiedClassName);
            }
            catch (ClassNotFoundException e)
            {
                Log.v(TAG, "Class " + fullyQualifiedClassName + " not found");
            }

            try
            {
                Intent intent = new Intent(BaseActivity.this, c);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_FROM_BACKGROUND);*/
                startActivity(intent);
            }
            catch (ActivityNotFoundException e)
            {
                Log.v(TAG, "Activity not found " + e.getMessage());
            }
        }
    }

    /**
     * Highlight the top menu item for the current Activity
     * @param menuItem is the TextView menu item accessible by all direct ancestors of BaseMenuActivity
     */
    protected void setActiveMenuItem(TextView menuItem)
    {
        if (null == menuItem)
        {
            //At design time the Toast below can be useful
            //Toast.makeText(this, "Forgot to initialize the top menu ?", Toast.LENGTH_SHORT).show();
            return;
        }
        Drawable menuBG = ResourcesCompat.getDrawable(getResources(), R.drawable.top_menu_item_background, null);

        if (activeMenuItem != null)
            Utils.setBackGroundDrawable(activeMenuItem, null);
        activeMenuItem = menuItem;
        Utils.setBackGroundDrawable(activeMenuItem, menuBG);
        scrollToActiveMenuItem();
    }

    /**
     * scroll to the highlighted menu item (In the next Activity). Has to be called
     * in onResume as well to work with previously existing Activities
     */
    protected void scrollToActiveMenuItem()
    {
        if (activeMenuItem == null)
            return;
        activeMenuItem.getParent().requestChildFocus(activeMenuItem,activeMenuItem);
    }


    private void releaseMediaPlayer()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // Regardless of whether or not we were granted audio focus, abandon it. This also
        // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }



    /*protected void mediaCtrlSlideUpDown()
    {
        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        // Start animation
        linear_layout.startAnimation(slide_down);
    }*/


}
