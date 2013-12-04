package com.greypool.Eavesdrop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 10/17/13
 * Time: 4:42 AM
 * activity for users to sign up account with parse.com
 */
public class SignUp extends Activity{

    ParseUser user = new ParseUser();
    String phoneNumber;
    TelephonyManager tManager;
	String nameText;
	String emailText;
	String passwordText;
	static boolean returnValue;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);										//create signup form
        setContentView(R.layout.signup);
        Context context = getApplicationContext();
        tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
       	Button registerButton = (Button) findViewById(R.id.btnRegister);
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {										//input information from edittexts
				returnValue = signUpUser();										// to blank variables
				if(returnValue == true){										// launch login activity
					Intent gotoLogin = new Intent(getApplicationContext(), Login.class);	 //launch login activity
					startActivity(gotoLogin);
					Toast.makeText(getApplicationContext(), "Starting new activity", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "Didn't return correctly", Toast.LENGTH_LONG).show();
				}
			}
		});

    }
    private boolean signUpUser(){
        phoneNumber = tManager.getLine1Number();									//get device phone number

		if (phoneNumber == null){
            phoneNumber = "666";
		}

		final EditText mEmail = (EditText)findViewById(R.id.reg_email);				//get email from text field
		emailText = mEmail.getText().toString();
		final EditText mPassword = (EditText)findViewById(R.id.reg_password);		//get password form text field
		passwordText = mPassword.getText().toString();
		final EditText mUsername = (EditText)findViewById(R.id.reg_userName);		//get username from text field
		nameText = mUsername.getText().toString();
        user.setUsername(nameText);													//set username, password, email
        user.setPassword(passwordText);
        user.setEmail(emailText);
        user.put("phoneNumber", phoneNumber);
        user.signUpInBackground(new SignUpCallback() {							//sign up user in background process
			@Override
			public void done(ParseException e) {
				if (e == null) {
					System.out.println("Wokring");								//return confirmation as toast
					System.out.println(phoneNumber);
					Toast.makeText(getApplicationContext(), "User "+ nameText + " has been signed up",
							Toast.LENGTH_LONG).show();						//TODO: deal with non working returns
				} else {
					System.out.println("not working");
					Toast.makeText(getApplicationContext(), "Error with signup.", Toast.LENGTH_LONG).show();
				}
			}
		});
		mEmail.setText("");
		mPassword.setText("");
		mUsername.setText("");
		return true;														//return true TODO: return proper value
    }

}
