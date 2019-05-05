package tintrandn.co.jp.moviestore.model;

import com.google.gson.annotations.SerializedName;


public class MovieBase {
    private String _id;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("title")
    private String title;
    @SerializedName("release_date")
    private String release_day;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private float rate;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("id")
    private int movie_Id;

    private String release_year;

    public MovieBase(String image, String title, String release_day, String overview, float rate, boolean adult, int movie_Id, String release_year) {
        this.image = image;
        this.title = title;
        this.release_day = release_day;
        this.overview = overview;
        this.rate = rate;
        this.adult = adult;
        this.movie_Id = movie_Id;
        this.release_year = release_year;
    }

    public MovieBase( String _id, String image, String title, String release_day, String overview, float rate, boolean adult, int movie_Id, String release_year) {
        this._id = _id;
        this.image = image;
        this.title = title;
        this.release_day = release_day;
        this.overview = overview;
        this.rate = rate;
        this.adult = adult;
        this.movie_Id = movie_Id;
        this.release_year = release_year;
    }

    public MovieBase(String _id, String image, String title, String release_day, String overview, float rate, boolean adult, int movie_id) {
        this._id = _id;
        this.image = image;
        this.title = title;
        this.release_day = release_day;
        this.overview = overview;
        this.rate = rate;
        this.adult = adult;
        this.movie_Id = movie_id;
    }

    //===========================================================
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    //===========================================================
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    //===========================================================
    public String getRelease_day() {
        return release_day;
    }
    //===========================================================
    public String getOverview() {
        return overview;
    }
    //===========================================================
    public String getRate() {
        return Float.toString(rate);
    }
    public void setRate(float rate) {
        this.rate = rate;
    }
    //===========================================================
    public int getMovie_Id() {
        return movie_Id;
    }
    //===========================================================
    public boolean getAdult() {
        return adult;
    }
    //===========================================================
    public String getRelease_year() {
        return release_year;
    }
}
