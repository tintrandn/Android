package tintrandn.co.jp.moviestore.view.adapter;

import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
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


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private static final String TAG = "FavouriteApdater";
    private ArrayList<MovieBase> moviesList;
    private Context mContext;
    // Default maximum disk usage in bytes
    // private static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;
    // private static final String IMAGE_API = "http://image.tmdb.org/t/p/w640/";
    private static final String IMAGE_API = "http://image.tmdb.org/t/p/w200_and_h300_bestv2/";
    // Default cache folder name
//    private static final String DEFAULT_CACHE_DIR = "photos";
    private FavouriteAdapterCallback mFavouriteAdapterCallback;
    private ChangeFavourite mChangeFavourite;
    //    private static final String CHANGE_FAVOURITE = "tintrandn.co.jp.moviestore.CHANGE_FAVOURITE";
//    private static final String CHANGE_FAVOURITE_ML = "tintrandn.co.jp.moviestore.CHANGE_FAVOURITE_ML";
//    private static final String SHOW_MOVIE_DETAIL = "tintrandn.co.jp.moviestore.SHOW_MOVIE_DETAIL";

    public FavouriteAdapter(Context context, ArrayList<MovieBase> moviesList, FavouriteAdapterCallback mFavouriteAdapterCallback, ChangeFavourite mChangeFavourite) {
        this.mContext = context;
        this.moviesList = moviesList;
        this.mFavouriteAdapterCallback = mFavouriteAdapterCallback;
        this.mChangeFavourite = mChangeFavourite;
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
    // Custom RequestQueue
//    private static RequestQueue newRequestQueue(Context context) {
//        // define cache folder
//        File rootCache = context.getExternalCacheDir();
//        if (rootCache == null) {
//            rootCache = context.getCacheDir();
//        }
//
//        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
//        cacheDir.mkdirs();
//        HttpStack stack = new HurlStack();
//        Network network = new BasicNetwork(stack);
//        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
//        RequestQueue queue = new RequestQueue(diskBasedCache, network);
//        queue.start();
//        return queue;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie_row, parent, false);

        return new ViewHolder(itemView);
    }
    //display the movie information
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MovieBase movie = moviesList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent show_movie_detail = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("movie_name",moviesList.get(position).getTitle());
//                bundle.putString("movie_release_day",moviesList.get(position).getRelease_day());
//                bundle.putString("movie_rate",String.valueOf(moviesList.get(position).getRate()));
//                bundle.putString("movie_image",String.valueOf(moviesList.get(position).getImage()));
//                bundle.putString("movie_over_view",String.valueOf(moviesList.get(position).getOverview()));
//                bundle.putBoolean("movie_adult",moviesList.get(position).getAdult());
//                bundle.putString("movie_id",String.valueOf(moviesList.get(position).getMovie_Id()));
//                show_movie_detail.putExtras(bundle);
//                show_movie_detail.setAction(SHOW_MOVIE_DETAIL);
//                mContext.sendBroadcast(show_movie_detail);
//                Log.d(TAG,"Send broadcast "+SHOW_MOVIE_DETAIL);
                mFavouriteAdapterCallback.clickItem(moviesList.get(holder.getAdapterPosition()));
                Log.d (TAG,"Favourite Adapter call back");
            }
        });
        // Instantiate the RequestQueue.
        ImageLoader mImageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();
        mImageLoader.get(IMAGE_API + movie.getImage(), ImageLoader.getImageListener(holder.poster, R.drawable.loading, R.drawable.image_not_found));
        String rate = movie.getRate()+"/10";
        //Image URL - This can point to any image file supported by Android
        holder.poster.setImageUrl(IMAGE_API + movie.getImage(), mImageLoader);
        holder.title.setText(movie.getTitle());
        holder.release_day.setText(movie.getRelease_day());
//        holder.rating.setText(movie.getRate());
        holder.rating.setText(rate);
        holder.overview.setText(movie.getOverview());
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
                //delete to favourite db
                int favourite_id = moviesList.get(holder.getAdapterPosition()).getMovie_Id();
                Log.d(TAG,"Delete favourite id: " +favourite_id );
                favouriteDao.delete(favouriteDao.getFavouriteMovieID(String.valueOf(favourite_id)));
                //send broadcast to Favourite Fragment
//                Intent change_favourite = new Intent();
//                change_favourite.setAction(CHANGE_FAVOURITE);
//                mContext.sendBroadcast(change_favourite);
//                Log.d(TAG,"Send broadcast " +CHANGE_FAVOURITE);
//                Intent change_favourite_ml = new Intent();
//                change_favourite_ml.putExtra("MovieID",String.valueOf(favourite_id));
//                change_favourite_ml.setAction(CHANGE_FAVOURITE_ML);
//                mContext.sendBroadcast(change_favourite_ml);
//                Log.d(TAG,"Send broadcast " +CHANGE_FAVOURITE_ML);
                mChangeFavourite.changeFavourite(favourite_id);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setFilter(ArrayList<MovieBase> filterMovieList) {
        moviesList = new ArrayList<>();
        moviesList.addAll(filterMovieList);
        notifyDataSetChanged();
    }
    //================================================
    public interface FavouriteAdapterCallback{
        void clickItem(MovieBase movie);
    }
    //================================================
    public interface ChangeFavourite{
        void changeFavourite(int movie_id);
    }
}
