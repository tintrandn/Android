package tintrandn.co.jp.moviestore.model.storage.entity;

public class Remind {

    private String _id;
    private String remind_movie_name;
    private String remind_movie_release_day;
    private String remind_movie_rate;
    private String remind_time;
    private String remind_movie_id;

    public Remind(String _id,/* String remind_movie_image,*/ String remind_movie_name, String remind_movie_release_day, String remind_movie_rate, /*String remind_movie_overview,*/ String remind_time,  /*String remind_adult,*/ String remind_movie_id) {
        this._id = _id;
        this.remind_movie_name = remind_movie_name;
        this.remind_movie_release_day = remind_movie_release_day;
        this.remind_movie_rate = remind_movie_rate;
        this.remind_time = remind_time;
        this.remind_movie_id = remind_movie_id;
    }

    public String get_id() {
        return _id;
    }

    public String getRemind_movie_name() {
        return remind_movie_name;
    }

    public String getRemind_movie_release_day() {
        return remind_movie_release_day;
    }

    public String getRemind_movie_rate() {
        return remind_movie_rate;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public String getRemind_movie_id() {
        return remind_movie_id;
    }

}
