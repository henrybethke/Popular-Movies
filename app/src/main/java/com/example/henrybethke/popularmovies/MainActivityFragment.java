package com.example.henrybethke.popularmovies;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static com.example.henrybethke.popularmovies.R.string.key_sort_order;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = MainActivityFragment.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mAdapter;
    private String mSortPreference;
    private Movie[] mMovies;

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

        downloadImages();
        if(mMovies != null) setGridView();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)v.findViewById(R.id.movie_gridview);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                String text = mAdapter.getItem(position);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra("movie", mMovies[position]);
                startActivity(intent);
            }
        });

        downloadImages();

        return v;
    }

    private void downloadImages() {
        final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String API_KEY = "api_key";
        String apiKey = "231626b579fe5a025e8ad3f3c1f3332a";
        final String SORT_PARAM = "sort_by";
        final String RELEASE_DATE_PARAM = "primary_release_date.gte";

        //TODO: let user pick this parameter using date picker dialog
        Calendar releaseDate = Calendar.getInstance();
        releaseDate.add(Calendar.YEAR, -1);
        int month = releaseDate.get(Calendar.MONTH) + 1;  //index starts at 0
        String releaseDateString = releaseDate.get(Calendar.YEAR) + "-"
                + month + "-"
                + releaseDate.get(Calendar.DAY_OF_MONTH );

        //TODO: clean this up, use string resources - this is kinda crappy
        //TODO: vote_average results vary a lot because lots of movies have perfect ratings
        String sortBy = "";
        if(mSortPreference.equals("Popular")){
            sortBy = "popularity";
        }else{
            sortBy = "vote_average";
        }

        //TODO add parameter to get recent movies
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(SORT_PARAM, sortBy + ".desc")
                .appendQueryParameter(RELEASE_DATE_PARAM, releaseDateString)
                .build();

        //String builtUri = uri.toString();

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(uri.toString())
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
                String json = "";
                json = response.body().string();
                try {
                    mMovies = getMoviesFromJson(json);
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setGridView();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Movie[] getMoviesFromJson(String json) throws JSONException{
        JSONObject obj = new JSONObject(json);
        JSONArray results = obj.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];
        Log.d(TAG, "results lenght: " + results.length());
        for(int i = 0; i < results.length(); i++){
            Movie movie = new Movie();
            JSONObject jsonMovie = results.getJSONObject(i);
            movie.setTitle(jsonMovie.getString("original_title"));
            movie.setOverview(jsonMovie.getString("overview"));
            movie.setPosterPath(jsonMovie.getString("poster_path"));
            movie.setReleaseDate(jsonMovie.getString("release_date"));
            movie.setVoteAverage(jsonMovie.getInt("vote_average"));
            movies[i] = movie;
        }

        return movies;
    }

    private void setGridView() {
 rm       String[] imagePaths = new String[mMovies.length];
        for(int i = 0; i < mMovies.length; i++){
            imagePaths[i] = mMovies[i].getPosterPath();
        }
        mAdapter = new ImageAdapter(getActivity(), imagePaths);
        mGridView.setAdapter(mAdapter);
    }

    private void setPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortPreference = prefs.getString(getResources().getString(R.string.key_sort_order),
                getResources().getString(R.string.pref_sort_default));
    }
}
