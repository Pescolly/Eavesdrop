package com.greypool.Eavesdrop;

import android.widget.ImageView;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 11/29/13
 * Time: 6:26 PM
 */
@ParseClassName("ExtendedParseUser")

public class ExtendedParseUser extends ParseUser{
	public ImageView USERIMAGE;
	public String USERNAME;
	public String EMAIL;
	public String PHONENUMBER;
	public Boolean AVAILABLE;

	public ExtendedParseUser(){
		super();
		System.out.println("constructor");

	}

}
