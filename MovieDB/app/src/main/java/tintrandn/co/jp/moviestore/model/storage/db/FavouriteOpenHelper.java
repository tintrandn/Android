package tintrandn.co.jp.moviestore.model.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tintrandn.co.jp.moviestore.model.storage.dao.FavouriteDao;


public class FavouriteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourite_movie.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        FavouriteDao.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        FavouriteDao.onUpgrade(database, oldVersion, newVersion);
    }
}
