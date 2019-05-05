package tintrandn.co.jp.moviestore.model.storage.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;


import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.provider.MovieProvider;

public class MovieDao extends Activity{

    private Context context;
    // Database table
    public static final String TABLE_NAME_MOVIE_POP = "movie_pop";
    public static final String TABLE_NAME_MOVIE_TOP = "movie_top";
    public static final String TABLE_NAME_MOVIE_UP = "movie_up";
    public static final String TABLE_NAME_MOVIE_NOW = "movie_now";
    public static final String COLUMN_ID = "_id";
    // Database column
    public static final String COLUMN_MOVIE_IMAGE = "image";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_RELEASE_DAY = "release_day";
    public static final String COLUMN_MOVIE_RELEASE_YEAR = "release_year";
    public static final String COLUMN_MOVIE_OVERVIEW = "over_view";
    public static final String COLUMN_MOVIE_RATE = "rate";
    public static final String COLUMN_MOVIE_ADULT = "adult";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    //Create table movie popular
    private static final String MOVIE_POP_DATABASE_CREATE = "create table "
            + TABLE_NAME_MOVIE_POP
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_IMAGE + " text not null, "
            + COLUMN_MOVIE_TITLE + " text not null, "
            + COLUMN_MOVIE_RELEASE_DAY + " text not null, "
            + COLUMN_MOVIE_OVERVIEW + " text not null, "
            + COLUMN_MOVIE_RATE + " real, "
            + COLUMN_MOVIE_ADULT + " integer, "
            + COLUMN_MOVIE_ID + " integer, "
            + COLUMN_MOVIE_RELEASE_YEAR + " text not null"
            + ");";
    //Create table movie top
    private static final String MOVIE_TOP_DATABASE_CREATE = "create table "
            + TABLE_NAME_MOVIE_TOP
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_IMAGE + " text not null, "
            + COLUMN_MOVIE_TITLE + " text not null, "
            + COLUMN_MOVIE_RELEASE_DAY + " text not null, "
            + COLUMN_MOVIE_OVERVIEW + " text not null, "
            + COLUMN_MOVIE_RATE + " real, "
            + COLUMN_MOVIE_ADULT + " integer, "
            + COLUMN_MOVIE_ID + " integer, "
            + COLUMN_MOVIE_RELEASE_YEAR + " text not null"
            + ");";
    //Create table movie up
    private static final String MOVIE_UP_DATABASE_CREATE = "create table "
            + TABLE_NAME_MOVIE_UP
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_IMAGE + " text, "
            + COLUMN_MOVIE_TITLE + " text not null, "
            + COLUMN_MOVIE_RELEASE_DAY + " text not null, "
            + COLUMN_MOVIE_OVERVIEW + " text not null, "
            + COLUMN_MOVIE_RATE + " real, "
            + COLUMN_MOVIE_ADULT + " integer, "
            + COLUMN_MOVIE_ID + " integer, "
            + COLUMN_MOVIE_RELEASE_YEAR + " text not null"
            + ");";
    //Create table movie now
    private static final String MOVIE_NOW_DATABASE_CREATE = "create table "
            + TABLE_NAME_MOVIE_NOW
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_IMAGE + " text not null, "
            + COLUMN_MOVIE_TITLE + " text not null, "
            + COLUMN_MOVIE_RELEASE_DAY + " text not null, "
            + COLUMN_MOVIE_OVERVIEW + " text not null, "
            + COLUMN_MOVIE_RATE + " real, "
            + COLUMN_MOVIE_ADULT + " integer, "
            + COLUMN_MOVIE_ID + " integer, "
            + COLUMN_MOVIE_RELEASE_YEAR + " text not null"
            + ");";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(MOVIE_POP_DATABASE_CREATE);
        database.execSQL(MOVIE_TOP_DATABASE_CREATE);
        database.execSQL(MOVIE_UP_DATABASE_CREATE);
        database.execSQL(MOVIE_NOW_DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.d(MovieDao.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_POP);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_TOP);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_UP);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_NOW);
        onCreate(database);
    }


    public MovieDao(Context context) {
        this.context = context;
    }
    //insert
    public void insert(String table, ContentValues values){
        switch (table){
            case "pop":
                context.getContentResolver().insert(MovieProvider.CONTENT_URI_MOVIE_POP, values);
                break;
            case "top":
                context.getContentResolver().insert(MovieProvider.CONTENT_URI_MOVIE_TOP, values);
                break;
            case "up":
                context.getContentResolver().insert(MovieProvider.CONTENT_URI_MOVIE_UP, values);
                break;
            case "now":
                context.getContentResolver().insert(MovieProvider.CONTENT_URI_MOVIE_NOW, values);
                break;
            default:
                context.getContentResolver().insert(MovieProvider.CONTENT_URI_MOVIE_POP, values);
                break;
        }
        Log.d("Insert: ", String.valueOf(values));
    }

    //query from n to nth movie
//    public ArrayList<MovieBase> getMovie(String table, int start, int end) {
//        ArrayList<MovieBase> movieList = new ArrayList<>();
//        MovieBase movie;
//        int i = 0;
//        String[] projection = { COLUMN_ID,COLUMN_MOVIE_IMAGE,COLUMN_MOVIE_TITLE, COLUMN_MOVIE_RELEASE_DAY,
//                COLUMN_MOVIE_OVERVIEW,COLUMN_MOVIE_RATE,COLUMN_MOVIE_ADULT,COLUMN_MOVIE_ID,COLUMN_MOVIE_RELEASE_YEAR};
//
//        Cursor cursor;
//        switch (table) {
//            case "pop":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection, null, null, null);
//                break;
//            case "top":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_TOP, projection, null, null, null);
//                break;
//            case "up":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_UP, projection, null, null, null);
//                break;
//            case "now":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_NOW, projection, null, null, null);
//                break;
//            default:
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection, null, null, null);
//                break;
//        }
//        cursor.moveToPosition(start - 1);
//        while (cursor.moveToNext()  && i !=(end - start)) {
//            String id = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_ID));
//            String image = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_IMAGE));
//            String title = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_TITLE));
//            String release_day = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DAY));
//            String overview = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_OVERVIEW));
//            float rate = Float.parseFloat(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RATE)));
//            boolean adult = Boolean.valueOf(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_ADULT)));
//            int movie_id = Integer.valueOf(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
//            String release_year = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_YEAR));
//            Log.d("Query movie","_id:"+ id + "image: "+ image + " title: " + title + " release_day: " + release_day
//                              + "overview: " + overview + "adult " + String.valueOf(adult) + " movie_id: " + movie_id+ " release_year: " + release_year);
//            movie = new MovieBase(id,image,title,release_day,overview,rate,adult,movie_id,release_year);
//            movieList.add(movie);
//            i++;
//        }
//        cursor.close();
//        return movieList;
//    }

    //query with full condition
    public ArrayList<MovieBase> getMovieFullCondition(String table, String rating, String year, String sort) {
        ArrayList<MovieBase> movieList = new ArrayList<>();
        MovieBase movie;
        String[] projection = { COLUMN_ID,COLUMN_MOVIE_IMAGE,COLUMN_MOVIE_TITLE, COLUMN_MOVIE_RELEASE_DAY,
                COLUMN_MOVIE_OVERVIEW,COLUMN_MOVIE_RATE,COLUMN_MOVIE_ADULT,COLUMN_MOVIE_ID,COLUMN_MOVIE_RELEASE_YEAR};
        Cursor cursor;
        if (sort.equals("release")){
            sort = MovieDao.COLUMN_MOVIE_RELEASE_DAY;
        }
        else{
            sort = MovieDao.COLUMN_MOVIE_RATE;
        }

        switch (table) {
            case "pop":
                Log.d("Query","Table name:" +table);
                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection,
                        COLUMN_MOVIE_RATE + " >=? AND " + COLUMN_MOVIE_RELEASE_YEAR + " >=? ", new String[]{rating,year}, sort+" DESC");
                break;
            case "top":
                Log.d("Query","Table name:" +table);
                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection,
                        COLUMN_MOVIE_RATE + " >=? AND " + COLUMN_MOVIE_RELEASE_YEAR + " >=? ", new String[]{rating,year}, sort+" DESC");
                break;
            case "up":
                Log.d("Query","Table name:" +table);
                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection,
                        COLUMN_MOVIE_RATE + " >=? AND " + COLUMN_MOVIE_RELEASE_YEAR + " >=? ", new String[]{rating,year}, sort+" DESC");
                break;
            case "now":
                Log.d("Query","Table name:" +table);
                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection,
                        COLUMN_MOVIE_RATE + " >=? AND " + COLUMN_MOVIE_RELEASE_YEAR + " >=? ", new String[]{rating,year}, sort+" DESC");
                break;
            default:
                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection,
                        COLUMN_MOVIE_RATE + " >=? AND " + COLUMN_MOVIE_RELEASE_YEAR + " >=? ", new String[]{rating,year}, sort+" DESC");
                break;
        }
        if (cursor !=null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String image = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_IMAGE));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_TITLE));
                String release_day = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DAY));
                String overview = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_OVERVIEW));
                float rate = Float.parseFloat(cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_RATE)));
                String adult_str = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_ADULT));
                boolean adult;
                adult = !adult_str.equals("0");
                int movie_id = Integer.valueOf(cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
                String release_year = cursor.getString(cursor
                        .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_YEAR));
                Log.d("Query movie", "_id:" + id + "image: " + image + " title: " + title + " release_day: " + release_day
                        + "overview: " + overview + "adult " + String.valueOf(adult) + " movie_id: " + movie_id);
                movie = new MovieBase(id, image, title, release_day, overview, rate, adult, movie_id, release_year);
                movieList.add(movie);
            }
            cursor.close();
        }
        return movieList;
    }

    //query all
//    public ArrayList<MovieBase> getAllMovie(String table) {
//        ArrayList<MovieBase> movieList = new ArrayList<>();
//        MovieBase movie;
//        String[] projection = {COLUMN_ID, COLUMN_MOVIE_IMAGE, COLUMN_MOVIE_TITLE, COLUMN_MOVIE_RELEASE_DAY,
//                COLUMN_MOVIE_OVERVIEW, COLUMN_MOVIE_RATE, COLUMN_MOVIE_ADULT, COLUMN_MOVIE_ID, COLUMN_MOVIE_RELEASE_YEAR};
//        Cursor cursor;
//        switch (table) {
//            case "pop":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection, null, null, null);
//                break;
//            case "top":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_TOP, projection, null, null, null);
//                break;
//            case "up":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_UP, projection, null, null, null);
//                break;
//            case "now":
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_NOW, projection, null, null, null);
//                break;
//            default:
//                cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI_MOVIE_POP, projection, null, null, null);
//                break;
//        }
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_ID));
//            String image = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_IMAGE));
//            String title = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_TITLE));
//            String release_day = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DAY));
//            String overview = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_OVERVIEW));
//            float rate = Float.parseFloat(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RATE)));
//            boolean adult = Boolean.valueOf(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_ADULT)));
//            int movie_id = Integer.valueOf(cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
//            String release_year = cursor.getString(cursor
//                    .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_YEAR));
//            Log.d("Query movie", "_id:" + id + "image: " + image + " title: " + title + " release_day: " + release_day
//                    + "overview: " + overview + "adult " + String.valueOf(adult) + " movie_id: " + movie_id);
//            movie = new MovieBase(id, image, title, release_day, overview, rate, adult, movie_id, release_year);
//            movieList.add(movie);
//        }
//        cursor.close();
//        return movieList;
//    }

    //query all table with favourite id
    public MovieBase getAllFavouriteMovie( String favourite_id) {
//        ArrayList<MovieBase> movieList = new ArrayList<>();
        Uri [] tables = {
                MovieProvider.CONTENT_URI_MOVIE_POP,
                MovieProvider.CONTENT_URI_MOVIE_TOP,
                MovieProvider.CONTENT_URI_MOVIE_UP,
                MovieProvider.CONTENT_URI_MOVIE_NOW
        };
        MovieBase movie = null;
        String[] projection = {COLUMN_ID, COLUMN_MOVIE_IMAGE, COLUMN_MOVIE_TITLE, COLUMN_MOVIE_RELEASE_DAY,
                COLUMN_MOVIE_OVERVIEW, COLUMN_MOVIE_RATE, COLUMN_MOVIE_ADULT, COLUMN_MOVIE_ID, COLUMN_MOVIE_RELEASE_YEAR};
        Cursor cursor;
        for (Uri table:tables){
            cursor = context.getContentResolver().query(table, projection, COLUMN_MOVIE_ID + " = ?", new String[] {favourite_id}, null);
            if (cursor !=null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor
                            .getColumnIndex(COLUMN_ID));
                    String image = cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_IMAGE));
                    String title = cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_TITLE));
                    String release_day = cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DAY));
                    String overview = cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_OVERVIEW));
                    float rate = Float.parseFloat(cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_RATE)));
                    boolean adult = Boolean.valueOf(cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_ADULT)));
                    int movie_id = Integer.valueOf(cursor.getString(cursor
                            .getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
                    String release_year = cursor.getString(cursor
                            .getColumnIndex(COLUMN_MOVIE_RELEASE_YEAR));

                    Log.d("Query movie", "_id:" + id + "image: " + image + " title: " + title + " release_day: " + release_day
                            + "overview: " + overview + "adult " + String.valueOf(adult) + " movie_id: " + movie_id + "release year: " + release_year);
                    movie = new MovieBase(id, image, title, release_day, overview, rate, adult, movie_id, release_year);
//                movieList.add(movie);
                }
                cursor.close();
            }
        }
        return movie;
    }
//    //delete
//    public int delete(String remind_id){
//        return context.getContentResolver().delete(Uri.parse(MovieProvider.CONTENT_URI+"/"+remind_id), null, null);
//    }

    //update movie pop table
    public int update(String table, ContentValues values, String movie_id) {
        switch (table) {
            case "pop":
                return context.getContentResolver().update(Uri.parse(MovieProvider.CONTENT_URI_MOVIE_POP + "/" + movie_id), values, null, null);

            case "top":
                return context.getContentResolver().update(Uri.parse(MovieProvider.CONTENT_URI_MOVIE_TOP + "/" + movie_id), values, null, null);

            case "up":
                return context.getContentResolver().update(Uri.parse(MovieProvider.CONTENT_URI_MOVIE_UP + "/" + movie_id), values, null, null);

            case "now":
                return context.getContentResolver().update(Uri.parse(MovieProvider.CONTENT_URI_MOVIE_NOW + "/" + movie_id), values, null, null);

            default:
                return context.getContentResolver().update(Uri.parse(MovieProvider.CONTENT_URI_MOVIE_POP + "/" + movie_id), values, null, null);
        }
    }

    //check database exist
//    public boolean checkDataBase() {
//        File database = context.getDatabasePath("movies.db");
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
