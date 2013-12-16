package com.greypool.Eavesdrop;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * NOT IN USE
 * NOT IN USE
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 12/9/13
 * Time: 12:16 AM
 * returns updated list item
 * called by LocalContactDBUpdate (periodically)
 * NOT IN USE
 * NOT IN USE
 */
public class FriendListUpdate{

	public SQLiteDatabase contactsDatabase;
	private LocalContactDBHelper contactDBhelper;
	private String[] CONTACT_DB_COLUMNS = {LocalContactDBHelper.COLUMN_ID, LocalContactDBHelper.USERNAME,
			LocalContactDBHelper.PHONENUMBER, LocalContactDBHelper.EMAIL, LocalContactDBHelper.AVAILABLE};
	ContentResolver CR;

	public FriendListUpdate(Context context){
		contactDBhelper = new LocalContactDBHelper(context);
		CR = context.getContentResolver();

	}

	public void open() throws SQLException{
		contactsDatabase = contactDBhelper.getReadableDatabase();
	}

	public void close(){
		contactDBhelper.close();
	}

	public void createAdapter(){
		try {
			System.out.println("Adapter has run");

		}catch (Exception e){
			System.out.println(e + "problem in friendlist createAdapter");
		}
	}

	public ExtendedParseUser[] createDatabase() {
		try {
			Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;                   				//create objects for queries
			String[] PROJECTION = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, ContactsContract.CommonDataKinds.Email
					.DATA};
			String contactId = "contactId";
			String SELECTION = ContactsContract.Data.CONTACT_ID +                //TODO: fix selection implementation
					" = " + contactId + " AND " +
					ContactsContract.Data.MIMETYPE + " = '" +
					ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
			String[] SELECTION_ARGS = null;
			String SORT_ORDER = null;

			ParseQuery<ParseUser> userQuery = ParseUser.getQuery();                //Parse objectfor query

			final Cursor cursor = this.CR.query(URI, PROJECTION, null, SELECTION_ARGS, SORT_ORDER);    //create local
			// query object

			int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);    //query results
			int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
			int phoneIDX = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
			int queryEMail = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA);
			int arrayCounter = 0;

			ExtendedParseUser[] result = new ExtendedParseUser[cursor.getCount()];                                //create
			while (cursor.moveToNext()) {                                                        //loop thru results
				final String localName = cursor.getString(nameIdx);
				final String id = cursor.getString(idIdx);
				final String emailAddress = cursor.getString(queryEMail);            ///TODO: fix to get only email
				final String localPhoneNumber = cursor.getString(phoneIDX);            //... if phone not available

				System.out.println("Looking for " + localName);
				if (localName == null || localPhoneNumber == null || id == null || emailAddress == null) {
					System.out.println("Skipping " + localName + localPhoneNumber + id + emailAddress);
					continue;
				}
				userQuery.whereEqualTo("phoneNumber", localPhoneNumber);
				List parseList = userQuery.find();

				if (parseList.size() != 0) {                                            // initialize result array elements
					ParseUser queriedUser = (ParseUser) parseList.get(0);
					String tempUserName = queriedUser.getUsername();
					System.out.println("Found " + tempUserName);
					if (tempUserName != null) {
						result[arrayCounter] = new ExtendedParseUser();
						result[arrayCounter].USERNAME = tempUserName;
						result[arrayCounter].PHONENUMBER = localPhoneNumber;
						result[arrayCounter].EMAIL = emailAddress;
						arrayCounter++;
					}
				}
			}
			cursor.close();
			ExtendedParseUser[] returnArray = new ExtendedParseUser[arrayCounter];    //create return array sized...
			for (int i = 0; i < arrayCounter; i++) {                                    //...according to number of matches
				returnArray[i] = result[i];                                            //copy and return
			}
			return returnArray;

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("THis is serial YO!");
			return null;
		}
	}


	public List<ExtendedParseUser> getContacts(ExtendedParseUser[] contacts){
		System.out.println("Creating contacts array");
		contactsDatabase = contactDBhelper.getWritableDatabase();									//open DB with helper
		ContentValues values = new ContentValues();
		System.out.println("Adding contacts to database");
		for(int index = 0; index < contacts.length; index++){			//populate local contacts DB with
		// contacs
		// array
			values.put(CONTACT_DB_COLUMNS[1], contacts[index].USERNAME);
			values.put(CONTACT_DB_COLUMNS[2], contacts[index].PHONENUMBER);
			values.put(CONTACT_DB_COLUMNS[3], contacts[index].EMAIL);
			values.put(CONTACT_DB_COLUMNS[4], contacts[index].AVAILABLE);
		}
		System.out.println("Database created.");
		List<ExtendedParseUser> contactList = new ArrayList<ExtendedParseUser>();
		Cursor cursor = contactsDatabase.query(LocalContactDBHelper.TABLENAME, CONTACT_DB_COLUMNS, null,null,null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			ExtendedParseUser username = new ExtendedParseUser();
			contactList.add(username);
			cursor.moveToNext();
		}
		System.out.println("Size of contacts list in FLU: "+contactList.size());
		cursor.close();
		return contactList;
	}

}
