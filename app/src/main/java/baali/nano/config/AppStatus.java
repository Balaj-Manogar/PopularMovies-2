package baali.nano.config;

import android.content.Context;
import android.os.Environment;

import baali.nano.R;

/**
 * Created by Balaji on 10/03/16.
 */
public class AppStatus
{
    private static MovieFetchOptions state;

    public static MovieFetchOptions getState()
    {
        return state;
    }

    public static void setState(MovieFetchOptions state)
    {
        AppStatus.state = state;
    }

    public static String getLocalStoragePath(Context context)
    {
        return Environment.getDataDirectory().getPath()
            + "/data/" + context.getApplicationContext().getPackageName()
            + "/" + context.getString(R.string.offline_directory) ;
    }
}
