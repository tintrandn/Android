package tintrandn.co.jp.moviestore.model.storage.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


import tintrandn.co.jp.moviestore.model.storage.entity.Remind;
import tintrandn.co.jp.moviestore.model.storage.provider.RemindProvider;


public class RemindDao extends Activity{

    private Context mContext;
    // Database table
    public static final String TABLE_NAME = "remind";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REMIMD_MOVIE_NAME = "remind_movie_name";
    public static final String COLUMN_REMIMD_MOVIE_RELEASE_DAY = "remind_movie_release_day";
    public static final String COLUMN_REMIMD_MOVIE_RATE = "remind_movie_rate";
    public static final String COLUMN_REMIMD_MOVIE_ID = "remind_movie_id";
    public static final String COLUMN_REMIND_TIME = "remind_time";

    //Create table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_REMIMD_MOVIE_NAME + " text not null, "
            + COLUMN_REMIMD_MOVIE_RELEASE_DAY + " text not null, "
            + COLUMN_REMIMD_MOVIE_RATE + " text not null, "
            + COLUMN_REMIMD_MOVIE_ID + " text not null, "
            + COLUMN_REMIND_TIME + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.d(RemindDao.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


    public RemindDao(Context context) {
        this.mContext = context;
    }
    //insert
    public void insert(ContentValues values){
        mContext.getContentResolver().insert(RemindProvider.CONTENT_URI, values);
        Log.d("Insert: ", String.valueOf(values));
    }

    //update remind table
    public int update(String movie_id, ContentValues values) {
        return mContext.getContentResolver().update(Uri.parse(RemindProvider.CONTENT_URI + "/" + movie_id), values, null, null);
    }


    //query remind with the movie id
    public Remind queryWithMovieId(String movie_id) {
        String[] projection = { COLUMN_ID,/*COLUMN_REMIMD_MOVIE_IMAGE,*/COLUMN_REMIMD_MOVIE_NAME, COLUMN_REMIMD_MOVIE_RELEASE_DAY,
                COLUMN_REMIMD_MOVIE_RATE,/*COLUMN_REMIMD_MOVIE_OVER_VIEW,COLUMN_REMIMD_MOVIE_ADULT,*/COLUMN_REMIMD_MOVIE_ID,COLUMN_REMIND_TIME};
        Cursor cursor = mContext.getContentResolver().query(RemindProvider.CONTENT_URI, projection, COLUMN_REMIMD_MOVIE_ID+ "=?", new String[]{movie_id},
                null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_NAME));
                String release_day = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RELEASE_DAY));
                String rate = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RATE));
                movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_ID));
                String time = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIND_TIME));
                Log.d("Query", "_id:" + id + /*"name: " + name + */" time: " + time + " movie id: " + movie_id);
                cursor.close();
                return new Remind(id, /*image,*/ name, release_day, rate,/* overview,*/ time, /*adult,*/ movie_id);
            }
        }
        return null;
    }
    //query n remind to display at drawer
    public ArrayList<Remind> getRemind(int count) {
        ArrayList<Remind> remindList = new ArrayList<>();
        Remind remind;
        int i = 0;
        String[] projection = { COLUMN_ID,/*COLUMN_REMIMD_MOVIE_IMAGE,*/COLUMN_REMIMD_MOVIE_NAME, COLUMN_REMIMD_MOVIE_RELEASE_DAY,
                COLUMN_REMIMD_MOVIE_RATE,/*COLUMN_REMIMD_MOVIE_OVER_VIEW,COLUMN_REMIMD_MOVIE_ADULT,*/COLUMN_REMIMD_MOVIE_ID,COLUMN_REMIND_TIME};
        Cursor cursor = mContext.getContentResolver().query(RemindProvider.CONTENT_URI, projection, null, null,
                null);
        if (cursor !=null) {
            while (cursor.moveToNext() && i != count) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_NAME));
                String release_day = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RELEASE_DAY));
                String rate = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RATE));
                String movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_ID));
                String time = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIND_TIME));
                remind = new Remind(id, /*image,*/ name, release_day, rate,/* overview,*/ time, /*adult,*/ movie_id);
                remindList.add(remind);
                i++;
            }
            cursor.close();
        }
        return remindList;
    }

    //query all
    public ArrayList<Remind> getAllRemind(){
        ArrayList<Remind> remindList = new ArrayList<>();
        Remind remind;
        String[] projection = { COLUMN_ID,/*COLUMN_REMIMD_MOVIE_IMAGE,*/COLUMN_REMIMD_MOVIE_NAME, COLUMN_REMIMD_MOVIE_RELEASE_DAY,
                COLUMN_REMIMD_MOVIE_RATE,/*COLUMN_REMIMD_MOVIE_OVER_VIEW,COLUMN_REMIMD_MOVIE_ADULT,*/COLUMN_REMIMD_MOVIE_ID,COLUMN_REMIND_TIME};
        Cursor cursor = mContext.getContentResolver().query(RemindProvider.CONTENT_URI, projection, null, null,
                null);
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_NAME));
                String release_day = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RELEASE_DAY));
                String rate = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_RATE));
                String movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIMD_MOVIE_ID));
                String time = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_REMIND_TIME));
                remind = new Remind(id, /*image,*/ name, release_day, rate,/* overview,*/ time, /*adult,*/ movie_id);
                remindList.add(remind);
            }
            cursor.close();
        }
        return remindList;
    }
    //delete
    public int delete(String remind_id){
        return mContext.getContentResolver().delete(Uri.parse(RemindProvider.CONTENT_URI+"/"+remind_id), null, null);
    }

    //check database exist
    public boolean checkDataBase() {
        File database = mContext.getDatabasePath("user_remind.db");
        if (!database.exists()) {
            // Database does not exist so copy it from assets here
            Log.i("Database", "Not Found");
            return false;
        } else {
            Log.i("Database", "Found");
            return true;
        }
    }
}
