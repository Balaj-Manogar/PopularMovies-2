package baali.nano.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public static boolean isOnline(Context context)
    {
        boolean status=false;
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connectivity = cm.getActiveNetworkInfo();
        status = (connectivity == null) ? false : true;
        return status;
    }

}
