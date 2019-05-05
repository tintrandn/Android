package tintrandn.co.jp.moviestore.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.fragment.FragmentAbout;
import tintrandn.co.jp.moviestore.fragment.FragmentFavourite;
import tintrandn.co.jp.moviestore.fragment.FragmentDrawer;
import tintrandn.co.jp.moviestore.fragment.container.FragmentSettingContainer;
import tintrandn.co.jp.moviestore.fragment.container.FragmentMovieContainer;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.dao.FavouriteDao;
import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;
import tintrandn.co.jp.moviestore.network.api.JSonParse;
import tintrandn.co.jp.moviestore.util.AlarmUtils;
import tintrandn.co.jp.moviestore.view.adapter.ViewPagerAdapter;

import static tintrandn.co.jp.moviestore.model.storage.dao.MovieDao.*;

public class MainActivity extends AppCompatActivity implements FragmentFavourite.FragmentFavouriteCallback, FragmentMovieContainer.UpdateTabIcon,
        FragmentFavourite.UpdateTabIcon_Fav, FragmentDrawer.FragmentDrawerListener, FragmentMovieContainer.MovieContainerCallback, FragmentSettingContainer.SettingContanerListener {

    private MovieDao mMovieDao;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private ArrayList<MovieBase> mMovieArrayList;
    private boolean mMovieFavourite;
    private String mMovieDetailStatus;
    private String mMovieSettingStatus;
    private FragmentMovieContainer mFragmentMovieContainer;
    private FragmentSettingContainer mFragmentSettingContainer;
    private FragmentFavourite mFragmentFavourite;
    private static Handler mHandler;
    private static final String TAG = "MainActivity";
    private static final String SHOW = "show";
    private static final String NOT_SHOW = "not_show";
    private static final String COMPLETE = "complete";
    private String [] tabTitle = {
            "Movies",
            "Favourite",
            "Setting",
            "About"
    };

    private int[] tabIcon = {
            R.mipmap.ic_home,
            R.mipmap.ic_favorite,
            R.mipmap.ic_settings,
            R.mipmap.ic_info
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"On resume");
        //add to receiver intent flag
        mHandler=new MyVeryOwnHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"On pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"On stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"Start notification");
        setIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            Log.d(TAG,"Notification intent "+ intent.getExtras().getString("movie_id"));
            String movie_id = intent.getExtras().getString("movie_id");
            MovieBase movie = mMovieDao.getAllFavouriteMovie(movie_id);
            clickFavourite(movie);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieDetailStatus = NOT_SHOW;
        mMovieSettingStatus = NOT_SHOW;
        mMovieFavourite = false;
        Log.d(TAG,"On create");
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tintht", "X@thu1mat".toCharArray());
            }
        });

        mFragmentFavourite = new FragmentFavourite(this, this);
        mFragmentMovieContainer = new FragmentMovieContainer(this,this);
        mFragmentSettingContainer = new FragmentSettingContainer();
        mFragmentSettingContainer.setSettingListener(MainActivity.this);
        mMovieDao = new MovieDao(getApplicationContext());
        setContentView(R.layout.activity_main);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.splash_screen);
        mViewPager = (ViewPager)findViewById(R.id.view_page_context);
//        createViewPager(mViewPager);
        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);
//        mTabLayout.setupWithViewPager(mViewPager);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        //if the device connect to wifi or mobile network
        if (activeNetwork != null) {
            Log.d(TAG,"Network type: "+ activeNetwork.getTypeName());
            new MyTask().execute();
        }
        else{
            Toast.makeText(this, "No network connection..", Toast.LENGTH_LONG).show();
            workingOffline();
//            mRelativeLayout.setVisibility(View.GONE);
        }
        //Create Alarm
        AlarmUtils.create(this);
//        ActionBar actionBar = getSupportActionBar();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FragmentDrawer mFragmentDrawer = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mFragmentDrawer.setDrawerListener(MainActivity.this);
        mFragmentDrawer.setUp((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // movie list tab
                if (position == 0){
                    if (mMovieDetailStatus.equals(NOT_SHOW)) {
                        getSupportActionBar().setTitle(tabTitle[position]);
                    }
                    else{
                        if (!mMovieFavourite)
                            mFragmentMovieContainer.setTitle();
                    }
                }
                //setting tab
                else if (position == 2) {
                    mMovieFavourite = false;
                    if (mMovieSettingStatus.equals(NOT_SHOW)) {
                        getSupportActionBar().setTitle(tabTitle[position]);
                    }
                    else{
                        getSupportActionBar().setTitle("All Remind");
                    }
                }
                // favourite and about
                else{
                    mMovieFavourite = false;
                    getSupportActionBar().setTitle(tabTitle[position]);
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        createTabIcon();
    }

    private void workingOffline() {
        Toast.makeText(this, "Working offline mode..", Toast.LENGTH_LONG).show();
        createViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        mRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void clickFavourite(MovieBase movie) {
        // call movie detail from favourite tab
        mMovieDetailStatus = SHOW;
        mMovieFavourite = true;
        mFragmentMovieContainer.callBack(movie);
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void updateTabIcon() {
        //update favourite badge
        mTabLayout.setupWithViewPager(mViewPager);
        createTabIcon();
        mFragmentFavourite.loadData();
    }

    @Override
    public void updateFavourite(int movie_id) {
        //update favourite badge
        mTabLayout.setupWithViewPager(mViewPager);
        createTabIcon();
        mFragmentMovieContainer.loadData();
        mFragmentFavourite.loadData();
    }
    //Drawer call back to change show all remind
    @Override
    public void callback() {
        mMovieSettingStatus = SHOW;
        mFragmentSettingContainer.changeFragment();
        mViewPager.setCurrentItem(2);
    }

    @Override
    public void updateShowStatus() {
        mMovieDetailStatus = SHOW;
    }

    @Override
    public void updateDisplay() {
        mFragmentMovieContainer.updateDisplay();
    }


    //Async task call API and save data to sqlite
    private class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            startParsePopMovie();
            startParseTopMovie();
            startParseUpMovie();
            startParseNowMovie();
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            //set display
            createViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);
            createTabIcon();
            mRelativeLayout.setVisibility(View.GONE);
            Message msg = mHandler.obtainMessage();
            msg.obj = COMPLETE;
            mHandler.sendMessage(msg);
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
    //start update pop movie to db
    private void startParsePopMovie() {
        //pasre API
        JSonParse.Page page;
        ContentValues values = new ContentValues();
        for (int pageNumber =1 ; pageNumber<3 ;pageNumber++) {
            page = JSonParse.startParse_Popular(String.valueOf(pageNumber));
            Log.d(TAG, "Start parse Popular: " + String.valueOf(pageNumber));
            if (page != null) {
                //init data
                mMovieArrayList = page.get_movieList();
                Log.d(TAG, "Create Value: " + mMovieArrayList.get(0).getImage() + mMovieArrayList.get(0).getTitle() + mMovieArrayList.get(0).getRelease_day());
                String[] release_year;
                for (MovieBase movieBase : mMovieArrayList) {
                    release_year = movieBase.getRelease_day().split("-");
                    values.put(COLUMN_MOVIE_IMAGE, movieBase.getImage());
                    values.put(COLUMN_MOVIE_TITLE, movieBase.getTitle());
                    values.put(COLUMN_MOVIE_RELEASE_DAY, movieBase.getRelease_day());
                    values.put(COLUMN_MOVIE_OVERVIEW, movieBase.getOverview());
                    values.put(COLUMN_MOVIE_RATE, movieBase.getRate());
                    values.put(COLUMN_MOVIE_ADULT, movieBase.getAdult());
                    values.put(COLUMN_MOVIE_ID, movieBase.getMovie_Id());
                    values.put(COLUMN_MOVIE_RELEASE_YEAR, release_year[0]);
                    // if the movie had on the db update, else insert the new movie
                    if (mMovieDao.update("pop", values, String.valueOf(movieBase.getMovie_Id())) == 0) {
                        mMovieDao.insert("pop", values);
                    }
                }
            }
        }
    }

    //start update top movie to db
    private void startParseTopMovie() {
        //pasre API
        JSonParse.Page page;
        ContentValues values = new ContentValues();
        for (int pageNumber =1 ; pageNumber<3 ;pageNumber++){
            page = JSonParse.startParse_Top_Rate(String.valueOf(pageNumber));
            Log.d(TAG,"Start parse Top rate: "+ String.valueOf(pageNumber));
            if (page != null) {
                //init data
                mMovieArrayList = page.get_movieList();
                Log.d(TAG, "Create Value: " + mMovieArrayList.get(0).getImage() + mMovieArrayList.get(0).getTitle() + mMovieArrayList.get(0).getRelease_day());
                String[] release_year;
                for (MovieBase movieBase : mMovieArrayList) {
                    release_year = movieBase.getRelease_day().split("-");
                    values.put(COLUMN_MOVIE_IMAGE, movieBase.getImage());
                    values.put(COLUMN_MOVIE_TITLE, movieBase.getTitle());
                    values.put(COLUMN_MOVIE_RELEASE_DAY, movieBase.getRelease_day());
                    values.put(COLUMN_MOVIE_OVERVIEW, movieBase.getOverview());
                    values.put(COLUMN_MOVIE_RATE, movieBase.getRate());
                    values.put(COLUMN_MOVIE_ADULT, movieBase.getAdult());
                    values.put(COLUMN_MOVIE_ID, movieBase.getMovie_Id());
                    values.put(COLUMN_MOVIE_RELEASE_YEAR, release_year[0]);
                    // if the movie had on the db update, else insert the new movie
                    if (mMovieDao.update("top", values, String.valueOf(movieBase.getMovie_Id())) == 0) {
                        mMovieDao.insert("top", values);
                    }
                }
            }
        }
    }

    //start update upcoming movie to db
    private void startParseUpMovie() {
        //pasre API
        JSonParse.Page page;
        ContentValues values = new ContentValues();
        for (int pageNumber =1 ; pageNumber<3 ;pageNumber++) {
            page = JSonParse.startParse_Upcoming(String.valueOf(pageNumber));
            Log.d(TAG, "Start parse Upcoming movie: " + String.valueOf(pageNumber));
            if (page != null) {
                //init data
                mMovieArrayList = page.get_movieList();
                Log.d(TAG, "Create Value: " + mMovieArrayList.get(0).getImage() + mMovieArrayList.get(0).getTitle() + mMovieArrayList.get(0).getRelease_day());
                String[] release_year;
                for (MovieBase movieBase : mMovieArrayList) {
                    release_year = movieBase.getRelease_day().split("-");
                    values.put(COLUMN_MOVIE_IMAGE, movieBase.getImage());
                    values.put(COLUMN_MOVIE_TITLE, movieBase.getTitle());
                    values.put(COLUMN_MOVIE_RELEASE_DAY, movieBase.getRelease_day());
                    values.put(COLUMN_MOVIE_OVERVIEW, movieBase.getOverview());
                    values.put(COLUMN_MOVIE_RATE, movieBase.getRate());
                    values.put(COLUMN_MOVIE_ADULT, movieBase.getAdult());
                    values.put(COLUMN_MOVIE_ID, movieBase.getMovie_Id());
                    values.put(COLUMN_MOVIE_RELEASE_YEAR, release_year[0]);
                    // if the movie had on the db update, else insert the new movie
                    if (mMovieDao.update("up", values, String.valueOf(movieBase.getMovie_Id())) == 0) {
                        mMovieDao.insert("up", values);
                    }
                }
            }
        }
    }

    //start update now playing movie to db
    private void startParseNowMovie() {
        //pasre API
        JSonParse.Page page;
        ContentValues values = new ContentValues();
        for (int pageNumber =1 ; pageNumber<3 ;pageNumber++){
            page = JSonParse.startParse_Now_Playing(String.valueOf(pageNumber));
            Log.d(TAG,"Start parse Now movie: "+ String.valueOf(pageNumber));
            if (page !=null) {
                //init data
                mMovieArrayList = page.get_movieList();
                Log.d(TAG, "Create Value: " + mMovieArrayList.get(0).getImage() + mMovieArrayList.get(0).getTitle() + mMovieArrayList.get(0).getRelease_day());
                String[] release_year;
                for (MovieBase movieBase : mMovieArrayList) {
                    release_year = movieBase.getRelease_day().split("-");
                    values.put(COLUMN_MOVIE_IMAGE, movieBase.getImage());
                    values.put(COLUMN_MOVIE_TITLE, movieBase.getTitle());
                    values.put(COLUMN_MOVIE_RELEASE_DAY, movieBase.getRelease_day());
                    values.put(COLUMN_MOVIE_OVERVIEW, movieBase.getOverview());
                    values.put(COLUMN_MOVIE_RATE, movieBase.getRate());
                    values.put(COLUMN_MOVIE_ADULT, movieBase.getAdult());
                    values.put(COLUMN_MOVIE_ID, movieBase.getMovie_Id());
                    values.put(COLUMN_MOVIE_RELEASE_YEAR, release_year[0]);
                    // if the movie had on the db update, else insert the new movie
                    if (mMovieDao.update("now", values, String.valueOf(movieBase.getMovie_Id())) == 0) {
                        mMovieDao.insert("now", values);
                    }
                }
            }
        }
    }

    //If not recreate Tab Icon, Tab Icon will not apprear
    private void createTabIcon() {
        if (mTabLayout !=null){
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tabItem = mTabLayout.getTabAt(i);
                if (tabItem != null) {
                    tabItem.setIcon(tabIcon[i]);
                }
            }
        }
        //custom favourite tab
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tablayout,null);
        TextView favourite_badge = (TextView)view.findViewById(R.id.favourite_badge);
        FavouriteDao favouriteDao = new FavouriteDao(getApplicationContext());
        int size = favouriteDao.getFavouriteSize();
        if (size > 0 && size < 10){
            favourite_badge.setVisibility(View.VISIBLE);
            favourite_badge.setText(String.valueOf(size));
        }
        else if (size > 9){
            favourite_badge.setVisibility(View.VISIBLE);
            favourite_badge.setText("9+");
        }
        TabLayout.Tab tabItem = mTabLayout.getTabAt(1);
        if (tabItem!=null) {
            tabItem.setCustomView(view);
        }
    }
    //===================================================
    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        mViewPagerAdapter.addFrag(new FragmentMovieContainer(),tabTitle[0]);
//        mViewPagerAdapter.addFrag(new FragmentFavourite(), tabTitle[1]);
        mViewPagerAdapter.addFrag(mFragmentMovieContainer, tabTitle[0]);
        mViewPagerAdapter.addFrag(mFragmentFavourite, tabTitle[1]);
        mViewPagerAdapter.addFrag(mFragmentSettingContainer, tabTitle[2]);
        mViewPagerAdapter.addFrag(new FragmentAbout(), tabTitle[3]);
        viewPager.setAdapter(mViewPagerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        int currentTab = mViewPager.getCurrentItem();
        MenuItem show = menu.findItem(R.id.action_show);
        MenuItem search = menu.findItem(R.id.action_search);
        switch (currentTab){
            case 0:
                if (mMovieDetailStatus.equals(NOT_SHOW)) {
                    show.setVisible(true);
                    search.setVisible(false);
                }
                else if (mMovieDetailStatus.equals(SHOW)){
                    Log.d(TAG, "Hide all icon");
                    search.setVisible(false);
                    show.setVisible(false);
                }
                break;
            case 1:
                search.setVisible(true);
                show.setVisible(false);
                break;
            case 2:
                search.setVisible(false);
                show.setVisible(false);
                break;
            case 3:
                search.setVisible(false);
                show.setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_movie) {
            Log.d(TAG,"click Movie");
            mViewPager.setCurrentItem(0);
            return true;
        }
        if (id == R.id.action_favourite) {
            Log.d(TAG,"click Favourite");
            mViewPager.setCurrentItem(1);
            return true;
        }
        if (id == R.id.action_settings) {
            Log.d(TAG,"click Setting");
            mViewPager.setCurrentItem(2);
            return true;
        }
        if (id == R.id.action_about) {
            Log.d(TAG,"click About");
            mViewPager.setCurrentItem(3);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean onBackPressed(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
                String ide = fragmentManager.getBackStackEntryAt(entry).getName();
                Log.d(TAG, "Found fragment: " + ide + " Entry: " +entry);
            }
            Log.d(TAG,"Found fragment Count size: " +fragmentManager.getBackStackEntryCount());
            if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack();
                return true;
            }
            List<android.support.v4.app.Fragment> fragmentList = fragmentManager.getFragments();

            if (fragmentList != null && fragmentList.size() > 0) {
                for (android.support.v4.app.Fragment fragment:fragmentList){
                    if (fragment == null){
                        continue;
                    }
                    if(fragment.isVisible()){
                        if(onBackPressed(fragment.getChildFragmentManager())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            FragmentManager fragmentManager = mFragmentMovieContainer.getChildFragmentManager();
            if(onBackPressed(fragmentManager)){
                mMovieDetailStatus = NOT_SHOW;
                return;
            }
        }
        else if (mViewPager.getCurrentItem() == 2){
            FragmentManager fragmentManager = mFragmentSettingContainer.getChildFragmentManager();
            if(onBackPressed(fragmentManager)){
                mMovieSettingStatus = NOT_SHOW;
                return;
            }
        }
        super.onBackPressed();
    }

    private class MyVeryOwnHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.toString().equals(COMPLETE)) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                if(extras != null)
                {
                    Log.d(TAG,"Notification intent "+ intent.getExtras().getString("movie_id"));
                    String movie_id = intent.getExtras().getString("movie_id");
                    if (movie_id != null) {
                        MovieBase movie = mMovieDao.getAllFavouriteMovie(movie_id);
                        clickFavourite(movie);
                    }
                }
            }
        }
    }
}
