package com.example.henrybethke.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.example.henrybethke.popularmovies.R.string.key_sort_order;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = MainActivityFragment.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mAdapter;
    private String mSortPreference;
    private int mNumItemsPreference;
    String mJson;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferences();
    }


    @Override
    public void onResume() {
        super.onResume();
        setPreferences();

        setGridView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)v.findViewById(R.id.movie_gridview);


        setGridView();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                String text = mAdapter.getItem(position);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
            }
        });

        final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String API_KEY = "api_key";
        String apiKey = "231626b579fe5a025e8ad3f3c1f3332a";
        final String SORT_PARAM = "sort_by";

        //TODO clean this up, use string resources
        String sortBy = "";
        if(mSortPreference.equals("Highest Rated")){
            sortBy = "vote_average";
        }else if(mSortPreference.equals("Popular")){
            sortBy = "popularity";
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(SORT_PARAM, sortBy)
                .build();

        String builtUri = uri.toString() + ".desc";
        Log.d(TAG, "builtUri: " + builtUri);

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(builtUri)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "failure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("response not successful: " + response);
                }
                Log.d(TAG, "response: " + response.body().string());
                mJson = response.body().string();
            }
        });

        return v;
    }

    private void setGridView() {
        mAdapter = new ImageAdapter(getActivity(), new String[] {
                "test1",
                "test2",
                "test3",
                "test4",
                "test5",
                "test6",
                "test7",
                mSortPreference
        });
        mGridView.setAdapter(mAdapter);
    }

    private void setPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortPreference = prefs.getString(getResources().getString(R.string.key_sort_order),
                getResources().getString(R.string.pref_sort_default));
        mNumItemsPreference = Integer.parseInt(prefs.getString(getResources().getString(R.string.key_num_items_pref),
                getResources().getString(R.string.num_items_default_value)));
    }
}
