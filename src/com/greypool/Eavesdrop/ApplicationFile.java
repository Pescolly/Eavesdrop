package com.greypool.Eavesdrop;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ApplicationFile extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		ParseUser.registerSubclass(ExtendedParseUser.class);
		Parse.initialize(this, "wqjcfIE4kWlFf1nDPCCf8cSCUoEQvaRMd3jnsciZ", "eUv9gdgFEwupTPeYLDje010ZIdc7eIqwfArz5b50");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
    }
}