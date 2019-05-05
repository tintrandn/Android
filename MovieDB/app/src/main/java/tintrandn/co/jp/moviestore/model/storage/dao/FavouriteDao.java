package tintrandn.co.jp.moviestore.model.storage.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import tintrandn.co.jp.moviestore.model.storage.entity.FavouriteMovie;
import tintrandn.co.jp.moviestore.model.storage.provider.FavouriteProvider;

public class FavouriteDao extends Activity{

    private Context context;
    // Database table
    public static final String TABLE_NAME = "favourite";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "remind_movie_id";

    //Create table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_ID + " text not null"
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


    public FavouriteDao(Context context) {
        this.context = context;
    }
    //insert
    public void insert(ContentValues values){
        context.getContentResolver().insert(FavouriteProvider.CONTENT_URI, values);
        Log.d("Insert: ", String.valueOf(values));
    }

    //update remind table
    public int update(String movie_id, ContentValues values) {
        return context.getContentResolver().update(Uri.parse(FavouriteProvider.CONTENT_URI + "/" + movie_id), values, null, null);
    }
    //check if movie_id have to favourite movie databast
    public boolean checkFavouriteMovie(String check_movie_id){
        String[] projection = { COLUMN_ID,COLUMN_MOVIE_ID};

        Cursor cursor = context.getContentResolver().query(FavouriteProvider.CONTENT_URI, projection, COLUMN_MOVIE_ID + " = ?", new String[]{check_movie_id}, null);
        if (cursor ==null || cursor.getCount() < 1){
            //cursor is empty
            return false;
        }
        else{
            cursor.close();
            return true;
        }
    }

    //return favourite_id
    public String getFavouriteMovieID(String check_movie_id){
        String[] projection = { COLUMN_ID,COLUMN_MOVIE_ID};
        String id = null;
        Cursor cursor = context.getContentResolver().query(FavouriteProvider.CONTENT_URI, projection, COLUMN_MOVIE_ID + " = ?", new String[]{check_movie_id}, null);
        if (cursor !=null) {
            while (cursor.moveToNext()) {
                id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_ID));
                Log.d("Query", "_id:" + id + "movie id: " + movie_id);
            }
            cursor.close();
        }
        return id;
    }

    //query all
    public ArrayList<FavouriteMovie> getAllFavouriteMovie(){
        ArrayList<FavouriteMovie> favouriteList = new ArrayList<>();
        FavouriteMovie favourite;
        String[] projection = { COLUMN_ID,COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(FavouriteProvider.CONTENT_URI, projection, null, null,
                null);
        if (cursor !=null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_ID));
                Log.d("Query", "_id:" + id + "movie id: " + movie_id);
                favourite = new FavouriteMovie(movie_id);
                favouriteList.add(favourite);
            }
            cursor.close();
        }
        return favouriteList;
    }
    //return database size
    public int getFavouriteSize(){
        ArrayList<FavouriteMovie> favouriteList = new ArrayList<>();
        FavouriteMovie favourite;
        String[] projection = { COLUMN_ID,COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(FavouriteProvider.CONTENT_URI, projection, null, null,
                null);
        if (cursor !=null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String movie_id = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_ID));
                Log.d("Query", "_id:" + id + "movie id: " + movie_id);
//                favourite = new FavouriteMovie(id, movie_id);
                favourite = new FavouriteMovie(movie_id);
                favouriteList.add(favourite);
            }
            cursor.close();
        }
        return favouriteList.size();
    }
    //delete
    public int delete(String favourite_id){
        Log.d("Delete: ", "movie id: "+favourite_id);
        return context.getContentResolver().delete(Uri.parse(FavouriteProvider.CONTENT_URI+"/"+favourite_id), null, null);
    }

    //check database exist
//    public boolean checkDataBase() {
//        File database = context.getDatabasePath("favourite_movie.db");
//        if (!database.exists()) {
//            // Database does not exist so copy it from assets here
//            Log.i("Database", "Not Found");
//            return false;
//        } else {
//            Log.i("Database", "Found");
//            return true;
//        }
//    }
}
