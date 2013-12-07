package com.greypool.Eavesdrop;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 12/6/13
 * Time: 4:13 AM
 */
public class PopulateLocalUserDB extends IntentService {

	public PopulateLocalUserDB(String name){
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ExtendedParseUser[] users;
		users = getNames();
	}

	private ExtendedParseUser[] getNames() {
		try {
			Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;                        //create objects for queries
			ContentResolver CR = getContentResolver();
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

			final Cursor cursor = CR.query(URI, PROJECTION, null, SELECTION_ARGS, SORT_ORDER);    //create local query object

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
}
