package tintrandn.co.jp.moviestore.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Calendar;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.Cast;
import tintrandn.co.jp.moviestore.model.storage.dao.FavouriteDao;
import tintrandn.co.jp.moviestore.model.storage.dao.RemindDao;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;
import tintrandn.co.jp.moviestore.network.api.JSonParse;
import tintrandn.co.jp.moviestore.util.AlarmUtils;
import tintrandn.co.jp.moviestore.util.CustomVolleyRequestQueue;
import tintrandn.co.jp.moviestore.view.adapter.CastProfileAdapter;


public class FragmentMovieDetail extends Fragment {
    private Context mContext;
    private CardView mCastInfo;
    private CardView mCastNoInfo;
    private TextView remindTime;
    private ImageView mStartButton;
    private ImageView mSelectedButton;
    private Calendar mCalendar;
    private RecyclerView mRecyclerView;
    private static String mMovieName;
    private static String mMovieImage;
    private static String mMovieReleaseDay;
    private static String mMovieRate;
    private static String mMovieOverView;
    private static boolean mMovieAdult;
    private static String mMovieID;
    private static String mYear, mMonth, mDay, mHour, mMinute;
    private static boolean mSetTime;
    private UpdateFavBad mUpdateFavBad;
    private static final String TAG = "MovieDetail";

    public FragmentMovieDetail() {

    }

    public FragmentMovieDetail(Context mContext, String mMovieName, String mMovieImage, String mMovieReleaseDay,
                               String mMovieRate, String mMovieOverView, boolean mMovieAdult, String mMovieID, FragmentMovieDetail.UpdateFavBad mUpdateFavBad) {
        this.mContext = mContext;
        FragmentMovieDetail.mMovieName = mMovieName;
        FragmentMovieDetail.mMovieImage = mMovieImage;
        FragmentMovieDetail.mMovieReleaseDay = mMovieReleaseDay;
        FragmentMovieDetail.mMovieRate = mMovieRate;
        FragmentMovieDetail.mMovieOverView = mMovieOverView;
        FragmentMovieDetail.mMovieAdult = mMovieAdult;
        FragmentMovieDetail.mMovieID = mMovieID;
        this.mUpdateFavBad = mUpdateFavBad;
    }

    public void updateStarIcon(){
        final FavouriteDao favouriteDao = new FavouriteDao(mContext);
        if (mMovieID != null) {
            if (favouriteDao.checkFavouriteMovie(mMovieID)) {
                mStartButton.setVisibility(View.GONE);
                mSelectedButton.setVisibility(View.VISIBLE);
            } else {
                mStartButton.setVisibility(View.VISIBLE);
                mSelectedButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(true);
//            Log.d("MovieDetail","Set title " +mMovieName);
//            actionBar.setTitle(mMovieName);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MovieDetail","onPause");
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",mMovieName);
        editor.putString("image",mMovieImage);
        editor.putString("releaseday",mMovieReleaseDay);
        editor.putString("rate",mMovieRate);
        editor.putString("overview",mMovieOverView);
        editor.putBoolean("adult",mMovieAdult);
        editor.putString("movieID",mMovieID);
        editor.apply();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail,container,false);
        getActivity().invalidateOptionsMenu();
        mContext = v.getContext();
//        mCastScrool = (ScrollView) v.findViewById(R.id.cast_scrool_view);
        mCastInfo = (CardView) v.findViewById(R.id.cast_info_view);
        mCastNoInfo = (CardView) v.findViewById(R.id.no_cast_info);
        NetworkImageView mMovieDetailImage = (NetworkImageView) v.findViewById(R.id.movie_detail_image);
        //check sharepreference
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref !=null && mMovieID == null) {
            mMovieName = sharedPref.getString("name","TinTrandn");
            mMovieImage = sharedPref.getString("image"," ");
            mMovieReleaseDay = sharedPref.getString("releaseday","0");
            mMovieRate = sharedPref.getString("rate","0");
            mMovieOverView = sharedPref.getString("overview","");
            mMovieAdult = sharedPref.getBoolean("adult",false);
            mMovieID = sharedPref.getString("movieID","0");
        }
        //Set Movie Title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            Log.d("MovieDetail","Set title " +mMovieName);
            actionBar.setTitle(mMovieName);
        }
        // Instantiate the RequestQueue.
        ImageLoader mImageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();
        final String url = "http://image.tmdb.org/t/p/w200_and_h300_bestv2/";
//        final String url = "http://image.tmdb.org/t/p/w350_and_h196_bestv2/";
        mImageLoader.get(url+mMovieImage, ImageLoader.getImageListener(mMovieDetailImage, R.drawable.loading, R.drawable.image_not_found));
        mMovieDetailImage.setImageUrl(url+mMovieImage,mImageLoader);
        TextView mMovieDetailReleaseDay = (TextView) v.findViewById(R.id.movie_detail_release_day);
        TextView mMovieDetailRate = (TextView) v.findViewById(R.id.movie_detail_rating);
        TextView mMovieDetailOverview = (TextView) v.findViewById(R.id.movie_detail_overview);
        ImageView mAdultIcon = (ImageView) v.findViewById(R.id.movie_detail_adult);
        //display movie information
        mMovieDetailReleaseDay.setText(mMovieReleaseDay);
        String rate = mMovieRate+"/10";
        mMovieDetailRate.setText(rate);

//        String fakeData = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
//                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
//                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
//                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" ;
//        mMovieDetailOverview.setText(fakeData);

        mMovieDetailOverview.setText(mMovieOverView);
        if(!mMovieAdult) mAdultIcon.setVisibility(View.INVISIBLE);
        Log.d(TAG,"Movie Title: " +mMovieName+" Movie ID "+mMovieID);
        mStartButton = (ImageView) v.findViewById(R.id.movie_detail_image_like);
        mSelectedButton = (ImageView) v.findViewById(R.id.movie_detail_image_selected);

        final FavouriteDao favouriteDao = new FavouriteDao(mContext);
        if(favouriteDao.checkFavouriteMovie(mMovieID)){
            mStartButton.setVisibility(View.GONE);
            mSelectedButton.setVisibility(View.VISIBLE);
        }
        else{
            mStartButton.setVisibility(View.VISIBLE);
            mSelectedButton.setVisibility(View.GONE);
        }
//        Log.d(TAG,"Movie Title: " +mMovieName+" Movie ID "+mMovieID);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to favourite db
                Log.d(TAG,"Insert favourite id: " +mMovieID);
                ContentValues values = new ContentValues();
                values.put(FavouriteDao.COLUMN_MOVIE_ID,mMovieID);
                favouriteDao.insert(values);
                mStartButton.setVisibility(View.GONE);
                mSelectedButton.setVisibility(View.VISIBLE);
                //send broadcast to Favourite Fragment
//                Intent change_favourite = new Intent();
//                change_favourite.setAction(CHANGE_FAVOURITE);
//                mContext.sendBroadcast(change_favourite);
//                Log.d(TAG,"Send broadcast " +CHANGE_FAVOURITE);
                mUpdateFavBad.updateBadge();
            }
        });
        mSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete from favourite db
                Log.d(TAG,"Delete favourite id: " +mMovieID);
                favouriteDao.delete(favouriteDao.getFavouriteMovieID(mMovieID));
                mStartButton.setVisibility(View.VISIBLE);
                mSelectedButton.setVisibility(View.GONE);
                mUpdateFavBad.updateBadge();
            }
        });


        remindTime = (TextView) v.findViewById(R.id.movie_detail_remind_time) ;
        //get remind time
        RemindDao remindDao = new RemindDao(mContext);
        Remind remind = remindDao.queryWithMovieId(mMovieID);
        if (remind !=null){
            remindTime.setText(remind.getRemind_time());
            remindTime.setVisibility(View.VISIBLE);
        }

        Button remindButton = (Button) v.findViewById(R.id.movie_detail_btn_remind);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.movie_detail_cast_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        loadData();
        mCalendar = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = String.valueOf(hourOfDay);
                mMinute = String.valueOf(minute);
                if (minute <10){
                    mMinute = "0"+mMinute;
                }
                Log.d("Set remind time","Hour "+mHour+" Minute "+mMinute);
                if (mSetTime) {
                    showRemindTime();
                    mSetTime = false;
                }
            }
        };

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDay = String.valueOf(dayOfMonth);
                mMonth = "0"+String.valueOf(month+1);
                mYear = String.valueOf(year);
                Log.d("Set remind date","Year "+mYear+" Month "+mMonth+" Day "+mDay);
                if (!mSetTime) {
                    mSetTime = true;
                    updateTime();
                }
            }

            private void updateTime() {
                mCalendar = Calendar.getInstance();
                new TimePickerDialog(mContext, time, mCalendar
                        .get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false).show();
            }
        };

        remindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetTime = false;
                new DatePickerDialog(mContext, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return v;
    }

    private void loadData() {
        new MyTask().execute();
    }

    public void setTitle() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            Log.d("MovieDetail","Set title " +mMovieName);
            actionBar.setTitle(mMovieName);
        }
    }

    private class MyTask extends AsyncTask<Object, Object, ArrayList<Cast>> {
        @Override
        protected ArrayList<Cast> doInBackground(Object... params) {
            //display cast name and image
            JSonParse.Cast_Id mCast_id = JSonParse.startParse_Cast(mMovieID);
            Log.d(TAG,"Parse Cast and Crew: "+ mCast_id);
            if (mCast_id == null){
                return null;
            }
            return  mCast_id.getmCastArrayList();
//            return (mCast_id == null)? null:mCast_id.getmCastArrayList();
        }
        @Override
        protected void onPostExecute(ArrayList<Cast> result) {
            if(result != null) {
                CastProfileAdapter castProfileAdapter = new CastProfileAdapter(getActivity(),result);
                mRecyclerView.setAdapter(castProfileAdapter);
            }
            //display no cast infor
            else {
                mCastInfo.setVisibility(View.GONE);
                mCastNoInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showRemindTime() {
        String time = mYear+"/"+mMonth+"/"+mDay+" "+mHour+":"+mMinute;
        Log.d(TAG,"Time "+time);
        remindTime.setText(time);
        remindTime.setVisibility(View.VISIBLE);
        //save data to remind.db database
        ContentValues values = new ContentValues();
//        values.put(RemindDao.COLUMN_REMIMD_MOVIE_IMAGE, mMovieImage);
        values.put(RemindDao.COLUMN_REMIMD_MOVIE_NAME,  mMovieName);
        values.put(RemindDao.COLUMN_REMIMD_MOVIE_RELEASE_DAY, mMovieReleaseDay);
        values.put(RemindDao.COLUMN_REMIMD_MOVIE_RATE, mMovieRate);
//        values.put(RemindDao.COLUMN_REMIMD_MOVIE_OVER_VIEW, mMovieOverView);
//        values.put(RemindDao.COLUMN_REMIMD_MOVIE_ADULT, mMovieAdult);
        values.put(RemindDao.COLUMN_REMIND_TIME, time);
        values.put(RemindDao.COLUMN_REMIMD_MOVIE_ID, mMovieID);
        RemindDao remindDao = new RemindDao(mContext);
        if (remindDao.update(mMovieID,values) == 0)
        remindDao.insert(values);
        //Create Alarm
        AlarmUtils.create(mContext);
    }
//=================================================
    public interface UpdateFavBad{
        void updateBadge();
    }
}
