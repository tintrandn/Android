package tintrandn.co.jp.moviestore.model.storage.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

import tintrandn.co.jp.moviestore.model.storage.dao.UserProfileDao;
import tintrandn.co.jp.moviestore.model.storage.db.UserProfileOpenHelper;


public class UserProfileProvider extends ContentProvider {
    // database
    private UserProfileOpenHelper database;
    private static final String AUTHORITY = "tintrandn.co.jp.moviestore.model.storage.db.User";
    private static final String BASE_PATH = "userprofile";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, 1);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", 2);
    }
    @Override
    public boolean onCreate() {
        database = new UserProfileOpenHelper(getContext());
        return false;
    }
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(UserProfileDao.TABLE_NAME);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case 1:
                break;
            case 2:
                // adding the ID to the original query
                queryBuilder.appendWhere(UserProfileDao.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    private void checkColumns(String[] projection) {
        String[] available = { UserProfileDao.COLUMN_IMAGE_PATH,UserProfileDao.COLUMN_USER_NAME,UserProfileDao.COLUMN_USER_MAIL,
                UserProfileDao.COLUMN_USER_BIRTHDAY,UserProfileDao.COLUMN_USER_GENDER, UserProfileDao.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
    // Implements ContentProvider.query()


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id ;
        switch (uriType) {
            case 1:
                id = sqlDB.insert(UserProfileDao.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext()!=null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted ;
        switch (uriType) {
            case 1:
                Log.d("Delete", "Case 1");
                rowsDeleted = sqlDB.delete(UserProfileDao.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case 2:
                Log.d("Delete", "Case 2");
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            UserProfileDao.TABLE_NAME,
                            UserProfileDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            UserProfileDao.TABLE_NAME,
                            UserProfileDao.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext() !=null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated ;
        switch (uriType) {
            case 1:
                rowsUpdated = sqlDB.update(UserProfileDao.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case 2:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(UserProfileDao.TABLE_NAME,
                            values,
                            UserProfileDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(UserProfileDao.TABLE_NAME,
                            values,
                            UserProfileDao.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext()!=null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
