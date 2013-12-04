package com.greypool.Eavesdrop;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;

public class Login extends Activity {
	/** users log into app using email and password, via parse.com */
    ParseUser user = new ParseUser();
	String password;
	String userName;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        Parse.initialize(this, "wqjcfIE4kWlFf1nDPCCf8cSCUoEQvaRMd3jnsciZ", "eUv9gdgFEwupTPeYLDje010ZIdc7eIqwfArz5b50");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

//		ParseAnalytics.trackAppOpened(getIntent());
		Button loginInButton = (Button)findViewById(R.id.btnLogin);
		loginInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText userNameEntry = (EditText)findViewById(R.id.userNameEntry);
				userName = userNameEntry.getText().toString();
				EditText passwordEntry = (EditText)findViewById(R.id.passwordEntry);
				password = passwordEntry.getText().toString();

				ParseUser.logInInBackground(userName, password, new LogInCallback() {
					@Override
					public void done(ParseUser parseUser, ParseException e) {				//login with credentials
						if (user != null){
							Toast.makeText(getApplicationContext(), userName + " is logged in", Toast.LENGTH_LONG).show();
							System.out.println(userName + " is logged in");
							Intent gotoFriendList = new Intent(getApplicationContext(), FriendList.class);
							startActivity(gotoFriendList);									//start friend list activity
						} else {
							Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_LONG).show();
							System.out.println("login failed");
						}
					}
				});
			}
		});

	}

}
