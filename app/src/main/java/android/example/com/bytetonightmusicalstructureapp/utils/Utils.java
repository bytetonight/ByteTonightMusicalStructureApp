package android.example.com.bytetonightmusicalstructureapp.utils;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ByteTonight on 12.05.2017.
 */

public class Utils
{

    /**
     * Deprecation work around
     * @param v is the View you want to apply the Drawable to
     * @param d is the Drawable you want to apply
     */
    @SuppressWarnings("deprecation")
    public static void setBackGroundDrawable(View v, Drawable d)
    {
        if (v == null)
            return;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            v.setBackgroundDrawable(d);

        }
        else
        {
            v.setBackground(d);
        }
    }

    /**
     * Yet another deprecation in Android-N to work around
     * @param html is the string that might contain unwanted HTML entities
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html)
    {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }
        else
        {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
