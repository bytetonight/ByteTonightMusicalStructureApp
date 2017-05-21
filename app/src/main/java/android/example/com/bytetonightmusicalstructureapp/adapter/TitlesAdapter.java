package android.example.com.bytetonightmusicalstructureapp.adapter;

import android.content.Context;
import android.example.com.bytetonightmusicalstructureapp.R;
import android.example.com.bytetonightmusicalstructureapp.models.MediaFile;
import android.example.com.bytetonightmusicalstructureapp.resolver.TagMenuActivity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ByteTonight on 15.05.2017.
 */

public class TitlesAdapter extends ArrayAdapter
{
    protected List<MediaFile> items;
    protected int layoutResource;
    protected Context context;


    public TitlesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects)
    {
        super(context, resource, objects);
        items = objects;
        this.context = context;
        this.layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }



        MediaFile file = items.get(position);
        //Log.v(TAG, "word "+mw.toString());

        holder.textViewItemText.setText(file.getName());


        /*if (file.hasImage())
        {
            //Setting to visible because I'm re-using this view and it might be "GONE"
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(file.getImageResourceId());
        }
        else
        {
            if (holder.imageView != null)
                holder.imageView.setVisibility(View.GONE);
        }
        if (!file.showInMainMenu())
        {
            convertView.setVisibility(View.GONE);
            //Gotta fix this somehow ... skip item in listview
        }*/
        return convertView;
    }

    public static class ViewHolder
    {
        public TextView textViewItemText;
        public ImageView imageView;


        public ViewHolder(View v)
        {
            textViewItemText = (TextView) v.findViewById(R.id.textViewItemText);
            //imageView = (ImageView) v.findViewById(R.id.miwokWordImage);

        }
    }
}
