package tintrandn.co.jp.moviestore.model.storage.entity;

public class FavouriteMovie {
    private String movie_id;

    public FavouriteMovie(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}
