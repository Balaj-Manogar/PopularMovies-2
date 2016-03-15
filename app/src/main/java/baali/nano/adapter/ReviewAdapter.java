package baali.nano.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import baali.nano.R;
import baali.nano.model.MovieReview;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Balaji on 15/03/16.
 */
public class ReviewAdapter extends ArrayAdapter<MovieReview>
{

    private List<MovieReview> reviews;
    private Context context;
    private final int layoutId;

    @Bind(R.id.review_author)
    TextView reviewAuthorView;

    @Bind(R.id.review_content)
    TextView reviewContentView;

    public ReviewAdapter(Context context, int resource, List<MovieReview> reviews)
    {
        super(context, resource, reviews);
        this.context = context;
        this.layoutId = resource;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater.inflate(layoutId, parent, false);
        }
        ButterKnife.bind(this, view);
        MovieReview review = reviews.get(position);
        reviewAuthorView.setText(review.getAuthor());
        reviewContentView.setText(review.getContent());
        return view;
    }
}
