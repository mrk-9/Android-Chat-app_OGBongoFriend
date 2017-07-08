package com.ogbongefriends.com.Login;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ogbongefriends.com.common.Constants;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;

	private boolean bool=false;
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String Token, String Expires,long user_id){
		// Storing login value as TRUE
		editor.putBoolean(Constants.IS_LOGIN, true);
		
		editor.putString(Constants.AccessToken, Token);
				
		editor.putString(Constants.AccessExpires, Expires);
			
		editor.putLong(Constants.UserID, user_id);
		
		// commit changes
		editor.commit();
	}
	
	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public Boolean checkLogin(){
		// Check login status
		if(this.isLoggedIn()){
			
			
//			// user is not logged in redirect him to Login Activity
//			Intent i = new Intent(_context, MainActivity.class);
//			// Closing all the Activities
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			// Add new Flag to start new Activity
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			
//			// Staring Login Activity
//			_context.startActivity(i);
			bool= true;
		}else{
			bool=false;
		}
		return bool;
		
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		
		user.put(Constants.AccessToken, pref.getString(Constants.AccessToken, null));
		
		user.put(Constants.AccessExpires, pref.getString(Constants.AccessExpires, "0"));
		
		user.put(Constants.UserID, String.valueOf(pref.getLong(Constants.UserID, -1l)));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
//		// After logout redirect user to Loing Activity
//		Intent i = new Intent(_context, LoginActivity.class);
//		// Closing all the Activities
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		
//		// Add new Flag to start new Activity
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		// Staring Login Activity
//		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(Constants.IS_LOGIN, false);
	}
}
