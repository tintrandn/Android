package tintrandn.co.jp.moviestore.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.dao.FavouriteDao;
import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;
import tintrandn.co.jp.moviestore.model.storage.entity.FavouriteMovie;
import tintrandn.co.jp.moviestore.view.adapter.FavouriteAdapter;

public class FragmentFavourite extends Fragment implements FavouriteAdapter.FavouriteAdapterCallback, FavouriteAdapter.ChangeFavourite {

    private RecyclerView mRecyclerListView;
    private FavouriteDao mFavouriteDao;
    private ArrayList<MovieBase> mMovieList;
    private FavouriteAdapter mFavouriteAdapter;
    private MovieDao mMovieDao;
    private FragmentFavouriteCallback mFragmentFavouriteCallback;
    private UpdateTabIcon_Fav mUpdateTabIcon_fav;
    private static final String TAG = "FragmentFavourite";
//    private static final String CHANGE_FAVOURITE = "tintrandn.co.jp.moviestore.CHANGE_FAVOURITE";


    private ArrayList<MovieBase> filter(String query) {
        query = query.toLowerCase();
        final ArrayList<MovieBase> filteredMovieList = new ArrayList<>();
        for (MovieBase movie : mMovieList) {
            String title = movie.getTitle().toLowerCase();
            if (title.contains(query)) {
                filteredMovieList.add(movie);
            }
        }
        return filteredMovieList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "Item select");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d(TAG, "Search");
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "on Query:" + newText);
                    ArrayList<MovieBase> filterMovieList = filter(newText);
                    mFavouriteAdapter.setFilter(filterMovieList);
                    return true;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"On Create View");
        View v = inflater.inflate(R.layout.fragment_favourite,container,false);
        setHasOptionsMenu(true);
        Context mContext = v.getContext();
        mRecyclerListView = (RecyclerView)v.findViewById(R.id.favourite_recycle_list);
        mMovieList = new ArrayList<>();
        LinearLayoutManager mLayoutManagerList = new LinearLayoutManager(getActivity());
        mRecyclerListView.setLayoutManager(mLayoutManagerList);
        mFavouriteDao = new FavouriteDao(mContext);
        mMovieDao = new MovieDao(mContext);
        loadData();
        return v;
    }

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG,"Receiver broadcast" + CHANGE_FAVOURITE);
//            loadData();
//        }
//    };
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if (receiver != null)
//            getActivity().unregisterReceiver(receiver);
//        receiver = null;
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CHANGE_FAVOURITE);
//        getActivity().registerReceiver(receiver, filter);
//    }

    public FragmentFavourite(FragmentFavouriteCallback mFragmentFavouriteCallback, UpdateTabIcon_Fav mUpdateTabIcon_fav) {
        this.mFragmentFavouriteCallback = mFragmentFavouriteCallback;
        this.mUpdateTabIcon_fav = mUpdateTabIcon_fav;
    }


    public void loadData() {
        new MyAsyncTask().execute();
    }

    @Override
    public void clickItem(MovieBase movie) {
        Log.d(TAG,"Fragment Favourite call back");
        mFragmentFavouriteCallback.clickFavourite(movie);
    }

    @Override
    public void changeFavourite(int movie_id) {
        mUpdateTabIcon_fav.updateFavourite(movie_id);
    }

    private class MyAsyncTask extends AsyncTask<Object, Object, ArrayList<MovieBase>> {

        MyAsyncTask() {

        }

        @Override
        protected ArrayList<MovieBase> doInBackground(Object... params) {
            Log.d(TAG, "Loading data..");
            //query favourite list from farourite_movie.db
            ArrayList<FavouriteMovie> favouriteMovieList = mFavouriteDao.getAllFavouriteMovie();
            //query all movie from movies.db
            mMovieList = new ArrayList<>();
            MovieBase movieBase;
            for (FavouriteMovie favouriteMovie:favouriteMovieList ){
                movieBase = mMovieDao.getAllFavouriteMovie(favouriteMovie.getMovie_id());
                mMovieList.add(movieBase);
            }
            return mMovieList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieBase> result) {
            super.onPostExecute(result);
            //init data
            mFavouriteAdapter = new FavouriteAdapter(getActivity(),result, FragmentFavourite.this, FragmentFavourite.this);
            mRecyclerListView.setAdapter(mFavouriteAdapter);
        }
    }
    //=================================================
    public interface FragmentFavouriteCallback{
        void clickFavourite(MovieBase movie);
    }
    //=================================================
    public interface UpdateTabIcon_Fav{
        void updateFavourite(int movie_id);
    }

}
