package baali.nano.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 6425 on 14-03-2016.
 */
public class MovieReview implements Parcelable
{
    private  String id;
    private String author;
    private String content;
    public MovieReview(){

    }

    public MovieReview(String theId, String theAuthor, String theContent){
        id = theId;
        author = theAuthor;
        content = theContent;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
        dest.writeString(this.author);
        dest.writeString(this.content);
    }

    protected MovieReview(Parcel in)
    {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>()
    {
        public MovieReview createFromParcel(Parcel source)
        {
            return new MovieReview(source);
        }

        public MovieReview[] newArray(int size)
        {
            return new MovieReview[size];
        }
    };
}
