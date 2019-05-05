package tintrandn.co.jp.moviestore.model.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tintrandn.co.jp.moviestore.model.storage.dao.UserProfileDao;


public class UserProfileOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_profile.db";
    private static final int DATABASE_VERSION = 1;

    public UserProfileOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        UserProfileDao.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        UserProfileDao.onUpgrade(database, oldVersion, newVersion);
    }
}
