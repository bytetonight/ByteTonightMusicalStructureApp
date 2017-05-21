package android.example.com.bytetonightmusicalstructureapp.models;


import java.util.concurrent.TimeUnit;

/**
 * Created by ByteTonight on 18.05.2017.
 */

public class MediaFile
{
    private String id;
    private String name;
    private String artist;
    private String path;
    private long duration;

    public MediaFile(String id, String name, String artist, String path, long duration)
    {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getPath()
    {
        return path;
    }

    public long getDuration()
    {
        return duration;
    }

    public String getFriendlyDuration()
    {
        Long millisUntilFinished = duration% (1000 * 60 * 60);
        Long minutes = TimeUnit.MINUTES.convert(millisUntilFinished, TimeUnit.MILLISECONDS);
        millisUntilFinished = millisUntilFinished% (1000 * 60);
        Long seconds = TimeUnit.SECONDS.convert(millisUntilFinished, TimeUnit.MILLISECONDS);

        return String.format("%02d:%02d", minutes , seconds );
    }
}
