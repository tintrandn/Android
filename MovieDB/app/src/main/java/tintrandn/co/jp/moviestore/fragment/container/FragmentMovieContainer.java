package tintrandn.co.jp.moviestore.fragment.container;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.fragment.FragmentMovieDetail;
import tintrandn.co.jp.moviestore.fragment.FragmentMovieList;
import tintrandn.co.jp.moviestore.model.MovieBase;

public class FragmentMovieContainer extends Fragment implements FragmentMovieList.MovieListCallback, FragmentMovieList.UpdateFavBad, FragmentMovieDetail.UpdateFavBad {
    private String TAG = "MovieDetail";
    private FragmentManager mFragmentManager;
    private FragmentMovieList mFragmentMovieList;
    private FragmentMovieDetail mFragmentMovieDetail;
    private UpdateTabIcon mUpdateTabIcon;
    private MovieContainerCallback mMovieContainerCallback;
    private String mMovieName, mMovieReleaseDay, mMovieRate, mMovieImage, mMovieOverView, mMovieID;
    private boolean mMovieAdult;
    private Handler handler = new Handler();


    public FragmentMovieContainer() {

    }

    public FragmentMovieContainer(UpdateTabIcon mUpdateTabIcon, MovieContainerCallback mMovieContainerCallback) {
        this.mUpdateTabIcon = mUpdateTabIcon;
        this.mMovieContainerCallback = mMovieContainerCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_container,container,false);
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getFragments() == null) {
            addFragmentMovieList();
        }
        return v;
    }

    private void addFragmentMovieList() {
        mFragmentMovieList = new FragmentMovieList(this, this);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,mFragmentMovieList, "MovieList");
        fragmentTransaction.addToBackStack("addFragmentMovieList");
        fragmentTransaction.commit();

    }

    public void replaceFragmentMovieDetail() {
//        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentMovieDetail = new FragmentMovieDetail(getContext(),mMovieName,mMovieImage,mMovieReleaseDay,mMovieRate,mMovieOverView,mMovieAdult,mMovieID,FragmentMovieContainer.this);
        fragmentTransaction.replace(R.id.fragment_container, mFragmentMovieDetail, "MovieDetail");
        fragmentTransaction.addToBackStack("replaceFragmentMovieDetail");
        // Commit the transaction
        fragmentTransaction.commit();
    }
    //set movie title
    public void setTitle(){
        mFragmentMovieDetail.setTitle();
    }
    //update after change setting
    public void updateDisplay(){
        mFragmentMovieList.refreshData();
    }

    //Movie List call back
    @Override
    public void callBack(MovieBase movie) {
        mMovieName = movie.getTitle();
        mMovieReleaseDay = movie.getRelease_day();
        mMovieRate = movie.getRate();
        mMovieImage = movie.getImage();
        mMovieOverView = movie.getOverview();
        mMovieAdult = movie.getAdult();
        mMovieID = String.valueOf(movie.getMovie_Id());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getChildFragmentManager();
                Log.d(TAG,"Fragment size :"+ fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount()==2){
                    fm.popBackStack();
                }
                replaceFragmentMovieDetail();
            }
        });
        mMovieContainerCallback.updateShowStatus();
    }
    @Override
    public void updateBadge() {
        mUpdateTabIcon.updateTabIcon();
    }
    //check movie detail is alive
//    public boolean checkVisible() {
//        FragmentMovieDetail fragmentMovieDetail = (FragmentMovieDetail) mFragmentManager.findFragmentByTag("MovieDetail");
//        if(fragmentMovieDetail != null && fragmentMovieDetail.isVisible()){
//            Log.d(TAG,"Movie Detail is alive");
//            return true;
//        }
//        return false;
//    }

    public interface UpdateTabIcon{
        void updateTabIcon();
    }

    public interface MovieContainerCallback{
        void updateShowStatus();
    }
    //update after change favourite data
    public void loadData(){
//        mFragmentMovieList.loadData();
        mFragmentMovieList.refreshData();
        if (mFragmentMovieDetail != null) {
            mFragmentMovieDetail.updateStarIcon();
        }
    }
}

