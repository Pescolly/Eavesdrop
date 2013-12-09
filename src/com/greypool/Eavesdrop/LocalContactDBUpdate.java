package com.greypool.Eavesdrop;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 11/30/13
 * Time: 7:30 PM
 * onStartup -- populates friendlist
 * service -- Checks PARSE database for updates to friends account info, i.e. username and availability.
 * Check for PUSH?
 */
public class LocalContactDBUpdate extends IntentService{

	public LocalContactDBUpdate(){
		super("LocalContactDBUpdate");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
