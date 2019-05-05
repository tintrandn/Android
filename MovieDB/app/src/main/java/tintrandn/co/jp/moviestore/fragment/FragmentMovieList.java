package tintrandn.co.jp.moviestore.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;
import tintrandn.co.jp.moviestore.view.adapter.GridViewMovieAdapter;
import tintrandn.co.jp.moviestore.view.adapter.ListViewMovieAdapter;


public class FragmentMovieList extends Fragment implements ListViewMovieAdapter.ICallback, GridViewMovieAdapter.ICallback, ListViewMovieAdapter.ChangeFavouriteMovie {

    private String TAG = "MovieList";
    private Context mContext;
    private boolean show_Grid = false;
    private boolean load_More = false;
    private RecyclerView mrecyclerListView;
    private RecyclerView mrecyclerGridView;
    private SwipeRefreshLayout listSwipe;
    private SwipeRefreshLayout gridSwipe;
    private LinearLayoutManager mLayoutManagerList;
    private GridLayoutManager mLayoutManagerGrid;
    private ListViewMovieAdapter mListViewAdapter;
    private ArrayList<MovieBase> mMovieArrayList;
    private static final int SPAN_COUNT = 2;
    private static int pageNumber = 1;
    private String mCategory, mRate, mReleaseYear, mSortBy;
    private String[] keys = {"pref_key_movie_category","pref_key_movie_rate_from","pref_key_movie_release_from","pref_key_movie_sort_by"};
    private MovieListCallback mMovieListCallback;
    private UpdateFavBad mUpdateFavBad;

    public FragmentMovieList() {

    }
    public FragmentMovieList(MovieListCallback mMovieListCallback, UpdateFavBad mUpdateFavBad) {
        this.mMovieListCallback = mMovieListCallback;
        this.mUpdateFavBad = mUpdateFavBad;
    }

    //===============================================================
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getContext().getApplicationContext();
    }
    //===============================================================
    public void getFilter() {
        SharedPreferences filter = PreferenceManager.getDefaultSharedPreferences(/*getActivity()*/mContext);
        if (filter != null) {
            mCategory = filter.getString(keys[0], "pop");
            mRate = filter.getString(keys[1], "0");
            mReleaseYear = filter.getString(keys[2], "2015");
            mSortBy = filter.getString(keys[3], "Release Date");
        }
        else{
            mCategory =  "pop";
            mRate = "0";
            mReleaseYear = "2015";
            mSortBy =  "release";
        }
        switch (mCategory){
            case "Popular Movies":
                mCategory = "pop";
                break;
            case "Top Rated Movies":
                mCategory = "top";
                break;
            case "Upcoming Movies":
                mCategory = "up";
                break;
            case "Now Playing Movies":
                mCategory = "now";
                break;
            default:
                mCategory = "pop";
                break;
        }
        if (mSortBy.equals("Release Date")){
            mSortBy = "release";
        }
        else{
            mSortBy = "rated";
        }
        Log.d(TAG,"Get filter condition");
    }
    //===============================================================
    public void loadData() {
        MovieDao movieDao = new MovieDao(mContext);
        List<MovieBase> movieList;
        Log.d(TAG, "Load data Category: " + mCategory + " Rate: " + mRate + " Year: " + mReleaseYear + " sort: " + mSortBy);
        movieList = movieDao.getMovieFullCondition(mCategory,mRate,mReleaseYear,mSortBy);
        Log.d(TAG, "Movie list size:" + movieList.size());
        if (movieList.size()>19) {
            movieList = movieList.subList(0, 20);
        }
        mMovieArrayList.addAll(movieList);
        // specify an adapter
        mListViewAdapter = new ListViewMovieAdapter(getContext(),mMovieArrayList, FragmentMovieList.this, FragmentMovieList.this);
        GridViewMovieAdapter mGridViewAdapter = new GridViewMovieAdapter(getContext(), mMovieArrayList, FragmentMovieList.this);
        mrecyclerListView.setAdapter(mListViewAdapter);
        mrecyclerGridView.setAdapter(mGridViewAdapter);
        listSwipe.setRefreshing(false);
        gridSwipe.setRefreshing(false);
        Log.d(TAG, "Completed");
    }
    //===============================================================
    private void loadMore() {
        MovieDao movieDao = new MovieDao(getContext().getApplicationContext());
        if (pageNumber <6) {
            pageNumber++;
        }
            List<MovieBase> movieList;
            Log.d(TAG, "Category: " + mCategory + " Rate: " + mRate + " Year: " + mReleaseYear + " sort: " + mSortBy);
            movieList = movieDao.getMovieFullCondition(mCategory,mRate,mReleaseYear,mSortBy);
            Log.d(TAG, "Movie list size:" + movieList.size());
            switch (pageNumber) {
                case 2:
                    Log.d(TAG,"Case 2");
                    if (movieList.size() > 39) {
                        movieList = movieList.subList(20, 40);
                    }
                    else{
                        movieList = movieList.subList(20,movieList.size());
                    }
                    break;
                case 3:
                    Log.d(TAG,"Case 3");
                    if (movieList.size() > 59) {
                        movieList = movieList.subList(40, 60);
                    }
                    else{
                        movieList = movieList.subList(40,movieList.size());
                    }
                    break;
                case 4:
                    Log.d(TAG,"Case 4");
                    if (movieList.size() > 79) {
                        movieList = movieList.subList(60, 80);
                    }
                    else{
                        movieList = movieList.subList(60,movieList.size());
                    }
                    break;
                case 5:
                    Log.d(TAG,"Case 5");
                    if (movieList.size() > 99) {
                        movieList = movieList.subList(80, 100);
                    }
                    else{
                        movieList = movieList.subList(80,movieList.size());
                    }
                    break;
                default:
                    break;
            }
            ArrayList<MovieBase> newMovieList = new ArrayList<>();
            newMovieList.addAll(movieList);
            for (MovieBase base:newMovieList)
                Log.d(TAG,"new movie title: "+ base.getTitle());
            mListViewAdapter.addNewMovie(newMovieList);
//            mGridViewAdapter.addNewMovie(newMovieList);
        load_More = false;
        Log.d(TAG,"Loadmore pagenumber:" +String.valueOf(pageNumber));
    }

    //===============================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);
        Log.d(TAG,"OnCreateView");
        getActivity().invalidateOptionsMenu();
        mrecyclerListView = (RecyclerView)v.findViewById(R.id.recycle_list);
        mrecyclerGridView = (RecyclerView)v.findViewById(R.id.recycle_grid);
        listSwipe = (SwipeRefreshLayout) v.findViewById(R.id.list_swipe_refresh);
        gridSwipe = (SwipeRefreshLayout) v.findViewById(R.id.grid_swipe_refresh);
        mrecyclerListView.setHasFixedSize(true);
        mrecyclerGridView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerList = new LinearLayoutManager(/*getActivity()*/mContext);
        mrecyclerListView.setLayoutManager(mLayoutManagerList);
        //use a grid layout manager
        mLayoutManagerGrid = new GridLayoutManager(/*getActivity()*/mContext,SPAN_COUNT);
        mrecyclerGridView.setLayoutManager(mLayoutManagerGrid);
        //implements loadmore function
        mrecyclerListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pos;
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                pos = mLayoutManagerList.findLastCompletelyVisibleItemPosition();
                if(pos == mMovieArrayList.size()-1 && mMovieArrayList.size() >=20*pageNumber && !load_More) {
                    Log.d("...", "Last Item Now !");
                    //Do pagination.. i.e. fetch new data
                    load_More = true;
                    getFilter();
                    loadMore();
                    Toast.makeText(getContext(),"Loading..",Toast.LENGTH_LONG).show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mrecyclerGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pos;
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                pos = mLayoutManagerGrid.findLastCompletelyVisibleItemPosition();
                if(pos == mMovieArrayList.size()-1 && mMovieArrayList.size() >=20*pageNumber && !load_More) {
                    Log.d("...", "Last Item Now !");
                    //Do pagination.. i.e. fetch new data
                    load_More = true;
                    getFilter();
                    loadMore();
                    Toast.makeText(getContext(),"Loading..",Toast.LENGTH_LONG).show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //run async task to load data and set list view or grid view
        mMovieArrayList = new ArrayList<>();
        getFilter();
        loadData();
        //===========================================================================
        //refresh listener
        listSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

        });

        // Configure the refreshing colors
        listSwipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        gridSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Configure the refreshing colors
        gridSwipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"MovieList on Resume" );
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            Log.d("MovieList","Set title Movie List" );
            actionBar.setTitle("Movie List");
        }
        //check the show state
        SharedPreferences settings = /*getActivity()*/mContext.getSharedPreferences("SHOW_STATE", Context.MODE_PRIVATE);
        if (settings!=null) {
            show_Grid = settings.getBoolean("ShowGrid", false);
            if (show_Grid) {
                gridSwipe.setVisibility(View.VISIBLE);
                listSwipe.setVisibility(View.GONE);
                Log.d(TAG, "Grid show");
            } else {
                gridSwipe.setVisibility(View.GONE);
                listSwipe.setVisibility(View.VISIBLE);
                Log.d(TAG, "List show");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"MovieList on Pause" );
        SharedPreferences settings = /*getActivity()*/mContext.getSharedPreferences("SHOW_STATE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // Necessary to clear first if we save preferences onPause.
        editor.clear();
        editor.putBoolean("ShowGrid", show_Grid);
        editor.apply();
    }

    //refresh data
    public void refreshData() {
        Log.d(TAG,"refresh");
        mMovieArrayList.clear();
        pageNumber = 1;
        getFilter();
        loadData();
    }
    //===============================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_show = menu.findItem(R.id.action_show);
        if (show_Grid){
            action_show.setIcon(R.mipmap.ic_list_white);
        }
        else{
            action_show.setIcon(R.mipmap.ic_grid_on_white);
        }
    }

    //===============================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG,"Item select " +String.valueOf(id));
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show) {
            if (!show_Grid) {
                // Show grid view
                show_Grid = true;
                item.setIcon(R.mipmap.ic_list_white);
                gridSwipe.setVisibility(View.VISIBLE);
                listSwipe.setVisibility(View.GONE);
                Log.d(TAG,"List show");
            } else {
                // Show list view
                show_Grid = false;
                gridSwipe.setVisibility(View.GONE);
                listSwipe.setVisibility(View.VISIBLE);
                item.setIcon(R.mipmap.ic_grid_on_white);
                Log.d(TAG,"Grid show");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickItem(MovieBase movie) {
        mMovieListCallback.callBack(movie);
        Log.d(TAG,"Movie List Call back");
    }

    @Override
    public void changeFavourite() {
        mUpdateFavBad.updateBadge();
    }

    public interface MovieListCallback{
        void callBack(MovieBase movie);
    }

    public interface UpdateFavBad{
        void updateBadge();
    }
}
