package com.example.henrybethke.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    public static final String TAG = DetailsActivityFragment.class.getSimpleName();


    private TextView mTextView;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        mTextView = (TextView)v.findViewById(R.id.test_detail_textview);

        mTextView.setText("placeholder detail text: " + getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));

        return v;
    }
}
