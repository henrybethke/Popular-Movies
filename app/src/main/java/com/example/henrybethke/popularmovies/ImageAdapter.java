package com.example.henrybethke.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Henry on 11/22/2015.
 *
 */

//TODO: make this work with images - just using Strings right now
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private String[] mItems;

    public ImageAdapter(Context c, String[] items){
        mContext = c;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public String getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*TextView textView = new TextView(mContext);
        textView.setText(mItems[position]);
        textView.setBackgroundColor(mContext.getResources().getColor(R.color.material_blue_grey_800));
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        textView.setPadding(20, 20, 20, 20);*/
        //return textView;

        String baseUrl = "http://image.tmdb.org/t/p/w500";
        String imageUrl = baseUrl + mItems[position];

        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext).load(imageUrl).into(imageView);

        FrameLayout frameLayout = new FrameLayout(mContext);
        frameLayout.addView(imageView);
        Log.d(TAG, "height: " + frameLayout.getHeight());
        Log.d(TAG, "width: " + frameLayout.getWidth());

        return frameLayout;
    }


}
