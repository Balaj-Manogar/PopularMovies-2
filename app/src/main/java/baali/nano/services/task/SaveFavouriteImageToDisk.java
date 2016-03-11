package baali.nano.services.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import baali.nano.config.AppFetchStatus;

/**
 * Created by Balaji on 07/03/16.
 */
public class SaveFavouriteImageToDisk implements Target
{
    private static final String TAG = SaveFavouriteImageToDisk.class.getSimpleName();
    private Context context;
    private String fileName;

    public SaveFavouriteImageToDisk(Context context, String fileName)
    {
        this.context = context;
        this.fileName = fileName;
    }

    private Context getContext()
    {
        return context;
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String localPath = AppFetchStatus.getLocalStoragePath(context);
                final File dir = new File(localPath);

//                                File f = new File(Environment.getDataDirectory().getPath()
//                                        + "/data/" + getContext().getApplicationContext().getPackageName());
//                                boolean isThere = f.isDirectory();
//                                Boolean writable = f.canWrite();
                Log.d(TAG, "onBitmapLoaded: " + dir.getAbsolutePath());
                boolean dirExists = dir.exists();
                if (!dirExists)
                {
                    boolean createDirs = dir.mkdirs();
                    if (!createDirs)
                    {
                        return;
                    }
                }

                final File image = new File(dir + fileName);
                try
                {
                    image.createNewFile();
                    FileOutputStream fos = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                    Log.d(TAG, "onBitmapLoaded Image: " + image.getAbsolutePath());
                    fos.flush();
                    fos.close();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable)
    {
        Toast.makeText(getContext(), "Image downloading failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable)
    {

    }
}
