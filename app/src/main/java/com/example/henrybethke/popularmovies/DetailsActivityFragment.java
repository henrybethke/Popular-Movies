package com.example.henrybethke.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    public static final String TAG = DetailsActivityFragment.class.getSimpleName();

    private Movie mMovie;
    private TextView mTitleText;
    private ImageView mPosterImage;
    private TextView mOverviewText;
    private TextView mReleaseDateText;
    private TextView mRatingText;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        mTitleText = (TextView)v.findViewById(R.id.titleTextview);
        mPosterImage = (ImageView)v.findViewById(R.id.posterImageview);
        mOverviewText = (TextView)v.findViewById(R.id.overviewTextview);
        mReleaseDateText = (TextView)v.findViewById(R.id.releaseDateTextview);
        mRatingText = (TextView)v.findViewById(R.id.ratingTextview);

        mMovie = getActivity().getIntent().getParcelableExtra("movie");
        getActivity().setTitle(mMovie.getTitle());
        mTitleText.setText(mMovie.getTitle());
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + mMovie.getPosterPath()).into(mPosterImage);
        mOverviewText.setText(mMovie.getOverview());
        mReleaseDateText.setText("Release date: " + mMovie.getReleaseDate());
        mRatingText.setText("User rating: " + mMovie.getVoteAverage() + "/10");

        return v;
    }
}
