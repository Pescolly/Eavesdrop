package com.greypool.Eavesdrop;

import android.app.Activity;
import android.app.IntentService;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
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
public class FriendList extends ListActivity {

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
		//	mProgressBar = (ProgressBar)findViewById(R.id.friendListProgressBar);
			setContentView(R.layout.friendlist);
			IntentService DBsync = new IntentService() {
				@Override
				protected void onHandleIntent(Intent intent) {
					//To change body of implemented methods use File | Settings | File Templates.
				}
			}

/*			list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Toast.makeText(FriendList.this, "you clicked" + friends[position].USERNAME, Toast.LENGTH_SHORT);
				}
			});
*/
			//TODO: separate thread for background processes.
			// 1. friend list update
			// 2. wait for signal to start recording

		} catch (Exception e) {
			System.out.println(e + " Problem in onCreate");
		}
	}

	private void FriendListUpdate() {
		int friendsArrayLength = friends.length;
		for (int i = 0; i < friendsArrayLength; i++) {
			friends[i].AVAILABLE = (Boolean) friends[i].get("loggedin");        //check availability for each friend
		}
	}
}