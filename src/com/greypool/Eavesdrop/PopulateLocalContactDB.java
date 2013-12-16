package com.greypool.Eavesdrop;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 12/6/13
 * Time: 4:13 AM
 */
public class PopulateLocalContactDB extends IntentService {

	public PopulateLocalContactDB(){
		super("populateLocalContactDB");								//create service and name for debugging purposes
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Intent DBMessage = new Intent(Messages.POPULATE_DB).putExtra("POPULATE_DB", "aha is playing at thestatefair");
		this.sendBroadcast(DBMessage);
		this.stopSelf();
	}
}
