package android.example.com.bytetonightmusicalstructureapp.resolver;

/**
 * Created by ByteTonight on 14.05.2017.
 */

public class TagMenuActivity
{
    private static final int NO_IMAGE_PROVIDED = -1;
    private String tag;
    private String activityName;
    private String displayName;
    private int imageResourceId = NO_IMAGE_PROVIDED;
    private boolean showInMainMenu;


    public TagMenuActivity(String t, String a, boolean s, String d)
    {
        tag = t;
        activityName = a;
        showInMainMenu = s;
        displayName = d;
    }

    public  String getTag()
    {
        return tag;
    }

    public  String getActivityName()
    {
        return activityName;
    }

    public boolean showInMainMenu()
    {
        return showInMainMenu;
    }

    public  String getDisplayName()
    {
        return displayName;
    }

    public boolean hasImage()
    {
        return imageResourceId != NO_IMAGE_PROVIDED;
    }

    public int getImageResourceId()
    {
        return imageResourceId;
    }
}
