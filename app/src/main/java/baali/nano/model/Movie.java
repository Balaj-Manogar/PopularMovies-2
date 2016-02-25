package baali.nano.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Balaji on 07/01/16.
 */
public class Movie implements Parcelable
{

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        public Movie createFromParcel(Parcel source)
        {
            return new Movie(source);
        }

        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };
    private long id;
    private String title;
    private String originalTitle;
    private boolean adult;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private double popularity;
    private int voteCount;
    private boolean video;
    private String voteAverage;

    public Movie()
    {
    }

    protected Movie(Parcel in)
    {
        this.id = in.readLong();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.adult = in.readByte() != 0;
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readString();
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public void setVoteCount(int voteCount)
    {
        this.voteCount = voteCount;
    }

    public boolean isAdult()
    {
        return adult;
    }

    public void setAdult(boolean adult)
    {
        this.adult = adult;
    }

    public String getBackdropPath()
    {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath)
    {
        this.backdropPath = backdropPath;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public double getPopularity()
    {
        return popularity;
    }

    public void setPopularity(double popularity)
    {
        this.popularity = popularity;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isVideo()
    {
        return video;
    }

    public void setVideo(boolean video)
    {
        this.video = video;
    }

    public String getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString()
    {
        return "Movie{" +
                "adult=" + adult +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeLong(this.id);
        out.writeString(this.title);
        out.writeString(this.originalTitle);
        out.writeByte(adult ? (byte) 1 : (byte) 0);
        out.writeString(this.posterPath);
        out.writeString(this.backdropPath);
        out.writeString(this.overview);
        out.writeString(this.releaseDate);
        out.writeDouble(this.popularity);
        out.writeInt(this.voteCount);
        out.writeByte(video ? (byte) 1 : (byte) 0);
        out.writeString(this.voteAverage);
    }
}
