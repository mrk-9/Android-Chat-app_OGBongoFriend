package com.ogbongefriends.com.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//===
public class Preferences {

	private Context _context;
	private SharedPreferences _preferences;
	private Editor _editor;	
	private String prefName	=	"cel";
	
	//=====
	public Preferences(Context context){
		
		_context = context;
		_preferences = this._context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		_editor = this._preferences.edit();
	}
	
	//=====
	public Preferences commit(){
		_editor.commit();
		return this;
	}
	
	//=====	
	public Preferences set(String key, String value){
	
		_editor.putString(key, value);
		return this;
	}
	
	//=====
	public String get(String key){		
		return _preferences.getString(key, "");
	}
	
	//=====	
	public Preferences set(String key, int value){
		
		_editor.putInt(key, value);
		
		return this;
	}
	
public Preferences set(String key, long value){
		
		_editor.putLong(key, value);
		return this;
	}
	
	//=====
	public int getInt(String key){		
		return _preferences.getInt(key, 0);
	}
	
	public long getlong(String key){		
		return _preferences.getInt(key, -1);
	}
	
	//=====	
	public Preferences setBoolean(String key, boolean value){
		
		_editor.putBoolean(key, value);
		return this;
	}
	
	//=====
	public void removeKey(String key){
		_editor.remove(key);
	}
	
	//=====
	public boolean getBoolean(String key){		
		return _preferences.getBoolean(key, false);
	}
		
}