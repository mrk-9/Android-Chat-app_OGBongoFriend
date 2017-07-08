package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.ApiException;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class updateProfileApi extends BasicApi implements Runnable{

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	@SuppressWarnings("unused")
	private String postData;
	private Context ctx;
	public static int resCode;
	HashMap<String, String> map1;
	public static String resMsg, resOuth_token;
	CustomLoader p;
	private int type=0;
	Preferences pref;


	public updateProfileApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		//setPostData(map);
		url = Utils.getCompleteApiUrl(ctx, R.string.update_profile_api);
		
		pref=new  Preferences(ctx);
	}

	public void setPostData(HashMap<String, String> map){
		
		
		map1 = map;
		postData = getPostData(map1);

	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (url == null) {
			Utils.log(Constants.kApiTag, "Url Not Found in Registration Api");
		} else {
			if (Utils.CNet()) {
				postData(url, postData);
			} else {
				p.cancel();
				Utils.NoInternet(ctx);
			}

		}
	}

	@Override
	protected void onResponseReceived(InputStream is) {
		// TODO Auto-generated method stub
		try {

			String res = getString(is);

			JsonParser p = new JsonParser();
			JsonElement jele = p.parse(res);
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject()
					: null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {
				db.open();
				
				if(type==3){
					
					HashMap<String, String>user_master=new HashMap<String, String>();
					HashMap<String, String>user_star_club=new HashMap<String, String>();
					user_master.put(DB.Table.user_master.interestedin_master_id.toString(), map1.get(DB.Table.user_master.interestedin_master_id.toString()));
					user_master.put(DB.Table.user_master.interestedin_purpose_master_id.toString(), map1.get(DB.Table.user_master.interestedin_purpose_master_id.toString()));
					
					String[] test1=map1.get("age_range").split(",");
					user_master.put(DB.Table.user_master.meet_min_age.toString(), String.valueOf(test1[0]));
					user_master.put(DB.Table.user_master.meet_max_age.toString(), String.valueOf(test1[1]));
					
					user_star_club.put(DB.Table.user_starsandclubs.type.toString(), map1.get("starsAndclubsType1"));
					user_star_club.put(DB.Table.user_starsandclubs.starsandclubs_master_id.toString(), map1.get("starsAndclubsid1"));
					db.autoInsertUpdate(DB.Table.Name.user_starsandclubs.toString(), user_star_club, DB.Table.user_starsandclubs.user_master_id+" = '"+pref.get(Constants.KeyUUID)+"' AND "+ DB.Table.user_starsandclubs.starsandclubs_master_id+" = "+map1.get("starsAndclubsid1"), null);
					user_star_club.put(DB.Table.user_starsandclubs.type.toString(), map1.get("starsAndclubsType2"));
					user_star_club.put(DB.Table.user_starsandclubs.starsandclubs_master_id.toString(), map1.get("starsAndclubsid2"));
					db.autoInsertUpdate(DB.Table.Name.user_starsandclubs.toString(), user_star_club, DB.Table.user_starsandclubs.user_master_id+" = '"+pref.get(Constants.KeyUUID)+"' AND "+ DB.Table.user_starsandclubs.starsandclubs_master_id+" = "+map1.get("starsAndclubsid2"), null);
					db.autoInsertUpdate(DB.Table.Name.user_master.toString(), user_master, DB.Table.user_master.uuid+" = '"+pref.get(Constants.KeyUUID)+"'", null);
				}
				else{
					map1.remove("type");
				if(map1.containsKey("age_range")){
					String test=map1.get("age_range");
					String[] test1=test.split(",");
					map1.remove("age_range");
					map1.put("meet_min_age", test1[0]);
					map1.put("meet_max_age", test1[1]);
					
					db.autoInsertUpdate(DB.Table.Name.user_master.toString(), map1, DB.Table.user_master.uuid+" = '"+pref.get(Constants.KeyUUID)+"'", null);
				}
				else{
					db.autoInsertUpdate(DB.Table.Name.user_master.toString(), map1, DB.Table.user_master.uuid+" = '"+pref.get(Constants.KeyUUID)+"'", null);
				}
				}
			db.close();
			}

		} catch (Exception e) {
			onError(e);

		}

	}

	@Override
	protected void onError(final Exception e) {
		// TODO Auto-generated method stub
		if (ctx instanceof Activity) {
			((Activity) ctx).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Utils.onError(ctx, e);
				}
			});
		}
	}

	@Override
	protected void onDone() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateUI() {
		// TODO Auto-generated method stub

	}

	
	private String getPostData(HashMap<String, String> map12) {

		// Preferences p = new Preferences(ctx);

		Log.v("Login", map12 + "");

		JSONObject json = new JSONObject();

		JSONObject json2 = new JSONObject();

		try {

			
			JSONObject object = new JSONObject(map12);
			
			json2.put("uuid", pref.get(Constants.KeyUUID));
			json2.put("auth_token", pref.get(Constants.kAuthT));
			json2.put("user_data", object);
			type=object.getInt("type");
			json.put(Constants.kappTag, json2);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			json.toString(3);
			Log.v("signmap", json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}
	
//	private String makrCompleteURL(HashMap<String, String> map12) {
//		// TODO Auto-generated method stub
//
//		
//		String fb_id=(map12.get("fb_id") != null) ? map12.get("fb_id") : "";
//		String endurl = "?email=" + map12.get("email") + "&password="+ map12.get("password") + "&fb_id=" + fb_id+ "&device_id=" + Utils.getDeviceID(ctx)+ "&platform_type=android-iphone";
//	
//		return endurl;
//	}


}
