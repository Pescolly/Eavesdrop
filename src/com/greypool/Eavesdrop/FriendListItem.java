package com.greypool.Eavesdrop;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.ParseUser;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 11/29/13
 * Time: 3:48 AM
 */
public class FriendListItem extends ArrayAdapter<ExtendedParseUser> {

	private final Activity context;
	public ExtendedParseUser USERARRAY[];

	public FriendListItem(Activity context, int resource, ExtendedParseUser[] incomingArray){
		super(context, resource, incomingArray);
		View v;
		this.context = context;
		this.USERARRAY = incomingArray;							//assign local USERARRAY to incoming from main menu
		System.out.println("friendlist object built");
	}


	@Override
	public View getView(int position, View view, ViewGroup parent){
		try{
			System.out.println("Starting friendlistitem - getView");
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.friendlist_item, null, true);
			TextView text = (TextView) rowView.findViewById(R.id.txt);
			ImageView availableFlag = (ImageView) rowView.findViewById(R.id.loggedInFlag);
			availableFlag.setImageResource(R.drawable.redbox);
			text.setText(USERARRAY[position].USERNAME);
	//		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
	//		imageView.setImageResource(imageID[position]);

			return rowView;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}

}
