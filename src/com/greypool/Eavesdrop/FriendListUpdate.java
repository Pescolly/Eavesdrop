package com.greypool.Eavesdrop;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 12/9/13
 * Time: 12:16 AM
 * returns updated list item
 * called by LocalContactDBUpdate (periodically) and FriendList (onCreate/onResume)
 */
public class FriendListUpdate {

	private SQLiteDatabase database;
	private LocalContactDBHelper dbHelper;
	private String[] allColumns = {"id", "username","phonenumber", "email",	"available"};	//column names

	public FriendListUpdate(Context context){
		dbHelper = new LocalContactDBHelper(context);
	}

	public void open() throws SQLException{
		database = dbHelper.getReadableDatabase();
	}

	public void close(){
		dbHelper.close();
	}

	public List<ExtendedParseUser> getContacts(){
		List<ExtendedParseUser> contacts = new ArrayList<ExtendedParseUser>();
		Cursor cursor = database.query(LocalContactDBHelper.TABLENAME, allColumns, null,null,null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			ExtendedParseUser contact = new ExtendedParseUser();
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contacts;
	}

}
