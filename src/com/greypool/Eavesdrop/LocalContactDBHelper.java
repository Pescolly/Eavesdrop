package com.greypool.Eavesdrop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 12/7/13
 * Time: 4:09 AM
 */
public class LocalContactDBHelper extends SQLiteOpenHelper{

	public static final String TABLENAME = "CachedContacts";						//table and query strings
	public static final String COLUMN_ID = "id INTEGER PRIMARY KEY AUTOINCREMENT";
	public static final String USERNAME = "username TEXT";
	public static final String PHONENUMBER = "phonenumber INTEGER";
	public static final String EMAIL = "email TEXT";
	public static final String AVAILABLE = "available INTEGER";

	private static final String DATABASE_NAME = "CachedContactDatabase";						//DB Strings
	private static final int DATABASE_VERSION = 1;


	private String DATABASE_CREATE = "CREATE TABLE " + TABLENAME + "(" + COLUMN_ID + USERNAME + PHONENUMBER + EMAIL +
			AVAILABLE + ")";

	public LocalContactDBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);													//create table
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
}
