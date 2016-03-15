package baali.nano.model;

/**
 * Created by 6425 on 14-03-2016.
 */
public class MovieReview
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
}
