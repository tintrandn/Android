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

import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;
import tintrandn.co.jp.moviestore.model.storage.db.MovieOpenHelper;


public class MovieProvider extends ContentProvider {
    // database
    private MovieOpenHelper database;

    private static final String AUTHORITY = "tintrandn.co.jp.moviestore.model.storage.db.Movie";
    private static final String BASE_PATH_MOVIE_POP = "movie_pop";
    private static final String BASE_PATH_MOVIE_TOP = "movie_top";
    private static final String BASE_PATH_MOVIE_UP = "movie_up";
    private static final String BASE_PATH_MOVIE_NOW = "movie_now";

    public static final Uri CONTENT_URI_MOVIE_POP = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_MOVIE_POP);
    public static final Uri CONTENT_URI_MOVIE_TOP = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_MOVIE_TOP);
    public static final Uri CONTENT_URI_MOVIE_UP = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_MOVIE_UP);
    public static final Uri CONTENT_URI_MOVIE_NOW = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_MOVIE_NOW);

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_POP, 1);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_POP + "/#", 2);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_TOP, 3);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_TOP + "/#", 4);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_UP, 5);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_UP + "/#", 6);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_NOW, 7);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIE_NOW + "/#", 8);
    }
    @Override
    public boolean onCreate() {
        database = new MovieOpenHelper(getContext());
        return false;
    }
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        int uriType = sUriMatcher.match(uri);
        Log.d("Uri type: ", String.valueOf(uriType));
        switch (uriType) {
            case 1:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_POP);
                break;
            case 2:
                Log.d("Uri type: ", "movie_pop_table");
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_POP);
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDao.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case 3:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_TOP);
                break;
            case 4:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_TOP);
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDao.COLUMN_ID + "="
                        + uri.getLastPathSegment());
            case 5: // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_UP);
                break;
            case 6:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_UP);
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDao.COLUMN_ID + "="
                        + uri.getLastPathSegment());
            case 7:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_NOW);
                break;
            case 8:
                // Set the table
                queryBuilder.setTables(MovieDao.TABLE_NAME_MOVIE_NOW);
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDao.COLUMN_ID + "="
                        + uri.getLastPathSegment());

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        if (getContext() !=null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    private void checkColumns(String[] projection) {
        String[] available = { MovieDao.COLUMN_MOVIE_IMAGE,MovieDao.COLUMN_MOVIE_TITLE,
                MovieDao.COLUMN_MOVIE_RELEASE_DAY, MovieDao.COLUMN_MOVIE_OVERVIEW,
                MovieDao.COLUMN_MOVIE_RATE, MovieDao.COLUMN_MOVIE_ADULT,MovieDao.COLUMN_MOVIE_ID,
                MovieDao.COLUMN_MOVIE_RELEASE_YEAR,MovieDao.COLUMN_ID };
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
        long id;

            switch (uriType) {
                case 1:
                    id = sqlDB.insert(MovieDao.TABLE_NAME_MOVIE_POP, null, values);
                    if (getContext() !=null) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return Uri.parse(BASE_PATH_MOVIE_POP + "/" + id);
                case 3:
                    id = sqlDB.insert(MovieDao.TABLE_NAME_MOVIE_TOP, null, values);
                    if (getContext() !=null) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return Uri.parse(BASE_PATH_MOVIE_TOP + "/" + id);
                case 5:
                    id = sqlDB.insert(MovieDao.TABLE_NAME_MOVIE_UP, null, values);
                    if (getContext() !=null) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return Uri.parse(BASE_PATH_MOVIE_UP + "/" + id);
                case 7:
                    id = sqlDB.insert(MovieDao.TABLE_NAME_MOVIE_NOW, null, values);
                    if (getContext() !=null) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return Uri.parse(BASE_PATH_MOVIE_NOW + "/" + id);
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case 1:
                Log.d("Delete", "Case 1");
                rowsDeleted = sqlDB.delete(MovieDao.TABLE_NAME_MOVIE_POP, selection,
                        selectionArgs);
                break;
            case 2:
                Log.d("Delete", "Case 2");
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_POP,
                            MovieDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_POP,
                            MovieDao.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case 3:
                Log.d("Delete", "Case 1");
                rowsDeleted = sqlDB.delete(MovieDao.TABLE_NAME_MOVIE_TOP, selection,
                        selectionArgs);
                break;
            case 4:
                Log.d("Delete", "Case 2");
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_TOP,
                            MovieDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_TOP,
                            MovieDao.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case 5:
                Log.d("Delete", "Case 1");
                rowsDeleted = sqlDB.delete(MovieDao.TABLE_NAME_MOVIE_UP, selection,
                        selectionArgs);
                break;
            case 6:
                Log.d("Delete", "Case 2");
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_UP,
                            MovieDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_UP,
                            MovieDao.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case 7:
                Log.d("Delete", "Case 1");
                rowsDeleted = sqlDB.delete(MovieDao.TABLE_NAME_MOVIE_NOW, selection,
                        selectionArgs);
                break;
            case 8:
                Log.d("Delete", "Case 2");
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_NOW,
                            MovieDao.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieDao.TABLE_NAME_MOVIE_NOW,
                            MovieDao.COLUMN_ID + "=" + id
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
                rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_POP,
                        values,
                        selection,
                        selectionArgs);
                break;
            case 2:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_POP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_POP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case 3:
                rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_TOP,
                        values,
                        selection,
                        selectionArgs);
                break;
            case 4:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_TOP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_TOP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case 5:
                rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_UP,
                        values,
                        selection,
                        selectionArgs);
                break;
            case 6:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_UP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_UP,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case 7:
                rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_NOW,
                        values,
                        selection,
                        selectionArgs);
                break;
            case 8:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_NOW,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieDao.TABLE_NAME_MOVIE_NOW,
                            values,
                            MovieDao.COLUMN_MOVIE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
