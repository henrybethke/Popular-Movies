package com.example.henrybethke.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Henry on 11/25/2015.
 */
public class Movie implements Parcelable{
    public static final String TAG = Movie.class.getSimpleName();

    private String title;
    private String overview;
    private String posterPath;
    private int voteAverage;
    private String releaseDate;

    public Movie(){}

    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        voteAverage = in.readInt();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {

        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeInt(voteAverage);
        dest.writeString(releaseDate);
    }
}
