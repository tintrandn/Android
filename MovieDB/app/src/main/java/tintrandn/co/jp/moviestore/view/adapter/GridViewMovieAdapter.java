package tintrandn.co.jp.moviestore.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.util.CustomVolleyRequestQueue;


public class GridViewMovieAdapter extends RecyclerView.Adapter<GridViewMovieAdapter.ViewHolder> {

    private static final String TAG = "GridViewApdater";
    private Context mContext;
    private ArrayList<MovieBase> mMoviesList;
    private ICallback mICallback;

    public GridViewMovieAdapter(Context context,ArrayList<MovieBase> moviesList,ICallback iCallback) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.mICallback = iCallback;
    }

    public interface ICallback{
        void clickItem(MovieBase movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private NetworkImageView poster;


        ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            title = (TextView) view.findViewById(R.id.movie_title);
            poster = (NetworkImageView) view.findViewById(R.id.movies_poster);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"Create List View");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_movie_row, parent, false);

        return new ViewHolder(itemView);
    }
    //display the movie information
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieBase movie = mMoviesList.get(position);
        Log.d(TAG,"onBind "+movie.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICallback.clickItem(mMoviesList.get(holder.getAdapterPosition()));
            }
        });

        // Instantiate the RequestQueue.

        ImageLoader mImageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();
        //Image URL - This can point to any image file supported by Android
//        final String url = "http://image.tmdb.org/t/p/w533_and_h300_bestv2/";
        final String url = "http://image.tmdb.org/t/p/w200_and_h300_bestv2/";
//
        mImageLoader.get(url+movie.getImage(), ImageLoader.getImageListener(holder.poster, R.drawable.loading, R.drawable.image_not_found));
        holder.poster.setImageUrl(url+movie.getImage(),mImageLoader);
        holder.title.setText(movie.getTitle());

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

//    public void addNewMovie(ArrayList<MovieBase> moviesList){
//        Log.d(TAG,"Adapter movie size:" +mMoviesList.size());
//        mMoviesList.addAll(moviesList);
//        Log.d(TAG,"Adapter movie size:" +mMoviesList.size());
//        notifyDataSetChanged();
//    }

}
