package com.greypool.Eavesdrop;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 10/17/13
 * Time: 3:11 AM
 */
public class FriendList extends Activity {

	public ListView list;
	private TextView resultTextView;
	ExtendedParseUser[] friends;
	protected static final int TIMER_RUNTIME = 10000;
	protected boolean mbActive;
	protected ProgressBar mProgressBar;
	private Handler mHandler = new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.progressbar);
			mProgressBar = (ProgressBar)findViewById(R.id.friendListProgressBar);


			friends = getNames();                                                    //load friends and populate list
			setContentView(R.layout.friendlist);

			if (friends == null) {
				System.out.println("It's not working");
			}
			FriendListItem adapter = new FriendListItem(FriendList.this, friends);
			list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Toast.makeText(FriendList.this, "you clicked" + friends[position], Toast.LENGTH_SHORT);
				}
			});

			//TODO: separate thread for background processes.
			// 1. friend list update
			// 2. wait for signal to start recording

		} catch (Exception e) {
			System.out.println(e + " Problem in onCreate");
		}
	}




	private void FriendListUpdate() {
		int friendsArrayLenth = friends.length;
		for (int i = 0; i < friendsArrayLenth; i++) {
			friends[i].AVAILABLE = (Boolean) friends[i].get("loggedin");        //check availability for each friend
		}
	}

	private ExtendedParseUser[] getNames() {
		try {
			Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;                        //create objects for queries
			ContentResolver CR = getContentResolver();
			String[] PROJECTION = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, ContactsContract.CommonDataKinds.Email
					.DATA};
			String contactId = getIntent().getStringExtra("contactId");
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