package com.example.weatherwithfriends.me;

import java.util.Arrays;
import java.util.HashSet;

import com.example.weatherwithfriends.FindFriendWeather;
import com.example.weatherwithfriends.friends.database.FriendTable;
import com.example.weatherwithfriends.friends.database.FriendsDatabaseHelper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;


public class MeContentProvider extends ContentProvider {

	// database
	private FriendsDatabaseHelper database;

	// used for the UriMacher
	private static final int FRIENDS = 10;
	private static final int FRIEND_ID = 20;

	private static final String AUTHORITY = "com.example.weatherwithfriends.friends.contentprovider";

	private static final String BASE_PATH = "friends";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/friends";
	//plural?
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/friend";
	//singular?	

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, FRIENDS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FRIEND_ID);
	}

	@Override
	public boolean onCreate() {
		database = new FriendsDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(FriendTable.TABLE_FRIENDS);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case FRIENDS:
				break;
			case FRIEND_ID:
				// adding the ID to the original query
				queryBuilder.appendWhere(FriendTable.COLUMN_ID + "="
						+ uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
			}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		
		long id = 0;
		switch (uriType) {
			case FRIENDS:
				id = sqlDB.insert(FriendTable.TABLE_FRIENDS, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//Log.v("ERHIGDS:FLKJD", uri.toString());
		//this is correct Uri, but... :(((
		
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
			case FRIENDS:
				rowsDeleted = sqlDB.delete(FriendTable.TABLE_FRIENDS, selection,
						selectionArgs);
				break;
			case FRIEND_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = sqlDB.delete(FriendTable.TABLE_FRIENDS,
							FriendTable.COLUMN_ID + "=" + id, 
							null);
				} else {
					rowsDeleted = sqlDB.delete(FriendTable.TABLE_FRIENDS,
							FriendTable.COLUMN_ID + "=" + id 
							+ " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case FRIENDS:
			rowsUpdated = sqlDB.update(FriendTable.TABLE_FRIENDS, 
					values, 
					selection,
					selectionArgs);
			break;
		case FRIEND_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(FriendTable.TABLE_FRIENDS, 
						values,
						FriendTable.COLUMN_ID + "=" + id, 
						null);
			} else {
				rowsUpdated = sqlDB.update(FriendTable.TABLE_FRIENDS, 
						values,
						FriendTable.COLUMN_ID + "=" + id 
						+ " and " 
						+ selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { 
				FriendTable.COLUMN_FRIEND,
				FriendTable.COLUMN_CITY,
				FriendTable.COLUMN_STATE,
				FriendTable.COLUMN_COUNTRY,
				FriendTable.COLUMN_TEMP,
				FriendTable.COLUMN_TXT,
				FriendTable.COLUMN_ICON,
				FriendTable.COLUMN_TIME};
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
	

} 