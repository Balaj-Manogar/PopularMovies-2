package baali.nano.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 6425 on 14-03-2016.
 *
 */
public class MovieVideo implements Parcelable
{
    private String id;
    private String key;
    private String name;
    private int size;

    private String site;

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeInt(this.size);
        dest.writeString(this.site);
    }

    public MovieVideo()
    {
    }

    protected MovieVideo(Parcel in)
    {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.size = in.readInt();
        this.site = in.readString();
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>()
    {
        public MovieVideo createFromParcel(Parcel source)
        {
            return new MovieVideo(source);
        }

        public MovieVideo[] newArray(int size)
        {
            return new MovieVideo[size];
        }
    };
}
