package com.greypool.Eavesdrop;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 10/17/13
 * Time: 3:11 AM
 */
public class FriendList extends ListActivity {

	public ListView list;
	private FriendListUpdate friendListUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.friendlist);
			Intent startDBSync = new Intent(this, PopulateLocalContactDB.class);		//start Population of contactDB
			startService(startDBSync);				//Send intent to start proc
			//wait for startDBSync to return
			BroadcastReceiver mMessReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String message = intent.getStringExtra("message");
				}
			};
			//LocalBroadcastManager.getInstance(this).registerReceiver(mMessReceiver, new IntentFilter("name"));

		} catch (Exception e) {
			System.out.println(e + " Problem in friendlist onCreate");
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();

	}

	public void createAdapter(){
		try {
			friendListUpdate = new FriendListUpdate(this);							//create list with DB
			friendListUpdate.open();
			List<ExtendedParseUser> contacts = friendListUpdate.getContacts();		//return list from DB

			ArrayAdapter<ExtendedParseUser> adapter = new ArrayAdapter<ExtendedParseUser>(this, R.layout.friendlist,
					contacts);
			setListAdapter(adapter);
			System.out.println("Adapter has run");

		}catch (Exception e){
			System.out.println(e + "problem in friendlist createAdapter");
		}
	}

}