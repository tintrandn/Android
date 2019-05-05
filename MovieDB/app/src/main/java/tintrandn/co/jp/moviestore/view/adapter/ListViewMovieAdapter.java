package tintrandn.co.jp.moviestore.view.adapter;

import android.content.ContentValues;
import android.content.Context;
//import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.dao.FavouriteDao;
import tintrandn.co.jp.moviestore.util.CustomVolleyRequestQueue;


public class ListViewMovieAdapter extends RecyclerView.Adapter<ListViewMovieAdapter.ViewHolder> {

    private static final String TAG = "ListViewApdater";
    private ArrayList<MovieBase> mMoviesList;
    private Context mContext;
    private ChangeFavouriteMovie mChangeFavouriteMovie;
    private ICallback mICallback;
//    private static final String CHANGE_FAVOURITE = "tintrandn.co.jp.moviestore.CHANGE_FAVOURITE";

    public ListViewMovieAdapter(Context context,ArrayList<MovieBase> moviesList,ICallback iCallback, ChangeFavouriteMovie mChangeFavouriteMovie) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.mICallback = iCallback;
        this.mChangeFavouriteMovie = mChangeFavouriteMovie;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, release_day, rating, overview;
        private NetworkImageView poster;
        private ImageView adult;
        private ImageView start;

        ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movie_title);
            release_day = (TextView) view.findViewById(R.id.movie_release_day);
            rating = (TextView) view.findViewById(R.id.movie_rating);
            overview = (TextView) view.findViewById(R.id.movie_description);
            poster = (NetworkImageView) view.findViewById(R.id.movies_poster);
            adult = (ImageView) view.findViewById(R.id.movie_adult);
            start = (ImageView) view.findViewById(R.id.movie_like) ;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"Create List View");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie_row, parent, false);
//        boolean mIsLike = false;
        return new ViewHolder(itemView);
    }
    //display the movie information
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MovieBase movie = mMoviesList.get(position);
        String rate = movie.getRate() + "/10";
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICallback.clickItem(mMoviesList.get(holder.getAdapterPosition()));
            }
        });
        Log.d(TAG, movie.getImage());
        // Instantiate the RequestQueue.
        ImageLoader mImageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();
        final String url = "http://image.tmdb.org/t/p/w200_and_h300_bestv2/";
        mImageLoader.get(url+ movie.getImage(), ImageLoader.getImageListener(holder.poster, R.drawable.loading, R.drawable.image_not_found));
        holder.poster.setImageUrl(url+ movie.getImage(),mImageLoader);
        holder.title.setText(movie.getTitle());
        holder.release_day.setText(movie.getRelease_day());
//        holder.rating.setText(movie.getRate());
        holder.rating.setText(rate);
        holder.overview.setText(movie.getOverview());
        Log.d(TAG,"List view get Adult "+movie.getTitle()+" "+movie.getAdult());
        if(!movie.getAdult()) holder.adult.setVisibility(View.INVISIBLE);
        //handler add to favourite list
        final FavouriteDao favouriteDao = new FavouriteDao(mContext);

        if(favouriteDao.checkFavouriteMovie(String.valueOf(movie.getMovie_Id()))){
            holder.start.setImageResource(R.mipmap.ic_start_selected);
        }
        else{
            holder.start.setImageResource(R.mipmap.ic_star_border_black);
        }
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to favourite db
                int favourite_id = mMoviesList.get(holder.getAdapterPosition()).getMovie_Id();
                //if movie have had in db, remove it
                if (favouriteDao.checkFavouriteMovie(String.valueOf(favourite_id))){
                    Log.d(TAG,"Delete favourite id: " +favourite_id);
                    favouriteDao.delete(favouriteDao.getFavouriteMovieID(String.valueOf(favourite_id)));
                    holder.start.setImageResource(R.mipmap.ic_star_border_black);
                }
                else{
                    Log.d(TAG,"Insert favourite id: " +favourite_id);
                    ContentValues values = new ContentValues();
                    values.put(FavouriteDao.COLUMN_MOVIE_ID,String.valueOf(favourite_id));
                    favouriteDao.insert(values);
                    holder.start.setImageResource(R.mipmap.ic_start_selected);
                }
                //send broadcast to Favourite Fragment
//                Intent change_favourite = new Intent();
//                change_favourite.setAction(CHANGE_FAVOURITE);
//                mContext.sendBroadcast(change_favourite);
//                Log.d(TAG,"Send broadcast " +CHANGE_FAVOURITE);
                mChangeFavouriteMovie.changeFavourite();
                //notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public interface ICallback{
        void clickItem(MovieBase movie);
    }

    public interface ChangeFavouriteMovie{
        void changeFavourite();
    }

    public void addNewMovie(ArrayList<MovieBase> moviesList){
        Log.d(TAG,"Adapter movie size:" +mMoviesList.size());
        mMoviesList.addAll(moviesList);
        Log.d(TAG,"Adapter movie size:" +mMoviesList.size());
        notifyDataSetChanged();
    }
}
