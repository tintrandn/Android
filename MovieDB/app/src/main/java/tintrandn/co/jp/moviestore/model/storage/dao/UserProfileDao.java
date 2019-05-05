package tintrandn.co.jp.moviestore.model.storage.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;


import tintrandn.co.jp.moviestore.model.UserProfile;
import tintrandn.co.jp.moviestore.model.storage.provider.UserProfileProvider;


public class UserProfileDao extends Activity {

    private Context context;
    // Database table

    public static final String TABLE_NAME = "user_profile";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMAGE_PATH = "user_image";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_MAIL = "user_mail";
    public static final String COLUMN_USER_BIRTHDAY = "user_birthday";
    public static final String COLUMN_USER_GENDER = "user_gender";

    //Create table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IMAGE_PATH + " text, "
            + COLUMN_USER_NAME + " text not null, "
            + COLUMN_USER_MAIL + " text not null, "
            + COLUMN_USER_BIRTHDAY + " text not null, "
            + COLUMN_USER_GENDER + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.d(UserProfileDao.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


    public UserProfileDao(Context context) {
        this.context = context;
    }

    //insert
    public void insert(ContentValues values) {
        context.getContentResolver().insert(UserProfileProvider.CONTENT_URI, values);
        Log.d("Insert: ", String.valueOf(values));
    }

    //query 1 user
    public UserProfile getUser() {
        UserProfile user;
        String[] projection = {COLUMN_ID, COLUMN_IMAGE_PATH, COLUMN_USER_NAME,
                COLUMN_USER_MAIL, COLUMN_USER_BIRTHDAY, COLUMN_USER_GENDER};
        Cursor cursor = context.getContentResolver().query(UserProfileProvider.CONTENT_URI, projection, null, null,
                null);
        if (cursor !=null) {
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(COLUMN_ID));
                String image = cursor.getString(cursor
                        .getColumnIndex(COLUMN_IMAGE_PATH));
                String name = cursor.getString(cursor
                        .getColumnIndex(COLUMN_USER_NAME));
                String mail = cursor.getString(cursor
                        .getColumnIndex(COLUMN_USER_MAIL));
                String birthday = cursor.getString(cursor
                        .getColumnIndex(COLUMN_USER_BIRTHDAY));
                String gender = cursor.getString(cursor
                        .getColumnIndex(COLUMN_USER_GENDER));
                user = new UserProfile(Integer.valueOf(id), image, name, mail, birthday, gender);
                cursor.close();
                return user;
            }
        }
        return null;
    }

    //query all
//    public ArrayList<UserProfile> getAllUser() {
//        ArrayList<UserProfile> userList = new ArrayList<UserProfile>();
//        UserProfile user;
//        String[] projection = {COLUMN_ID, COLUMN_IMAGE_PATH, COLUMN_USER_NAME,
//                COLUMN_USER_MAIL, COLUMN_USER_BIRTHDAY, COLUMN_USER_GENDER};
//        Cursor cursor = context.getContentResolver().query(RemindProvider.CONTENT_URI, projection, null, null,
//                null);
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_ID));
//            String image = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_IMAGE_PATH));
//            String name = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_USER_NAME));
//            String mail = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_USER_MAIL));
//            String birthday = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_USER_BIRTHDAY));
//            String gender = cursor.getString(cursor
//                    .getColumnIndex(COLUMN_USER_GENDER));
//
//            user = new UserProfile(Integer.getInteger(id), image, name, mail, birthday, gender);
//            userList.add(user);
//        }
//        cursor.close();
//        return userList;
//    }

    //delete user
    public int delete(String user_id) {
        return context.getContentResolver().delete(Uri.parse(UserProfileProvider.CONTENT_URI + "/" + user_id), null, null);
    }

    //update user
    public int update(ContentValues values, String user_id) {
        return context.getContentResolver().update(Uri.parse(UserProfileProvider.CONTENT_URI + "/" + user_id), values, null, null);
    }

    //check database exist
    public boolean checkDataBase() {
        File database = context.getDatabasePath("user_profile.db");
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
