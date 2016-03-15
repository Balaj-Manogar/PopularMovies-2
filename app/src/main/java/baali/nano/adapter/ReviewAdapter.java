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
        View view;
        ReviewHolder reviewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutId, parent, false);
            reviewHolder = new ReviewHolder(view);
            view.setTag(reviewHolder);
        }
        else
        {
            view = convertView;
            reviewHolder = (ReviewHolder) view.getTag();
        }


        MovieReview review = reviews.get(position);
        reviewHolder.reviewAuthorView.setText(review.getAuthor());
        reviewHolder.reviewContentView.setText(review.getContent());

        return view;
    }

    static class ReviewHolder
    {
        @Bind(R.id.review_author)
        TextView reviewAuthorView;

        @Bind(R.id.review_content)
        TextView reviewContentView;

        ReviewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
