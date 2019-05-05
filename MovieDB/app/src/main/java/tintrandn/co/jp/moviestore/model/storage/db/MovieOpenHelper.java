package tintrandn.co.jp.moviestore.model.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;


public class MovieOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static String mDataName = "movies.db";

    public MovieOpenHelper(Context context) {
        super(context, mDataName, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        MovieDao.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        MovieDao.onUpgrade(database, oldVersion, newVersion);
    }
}
