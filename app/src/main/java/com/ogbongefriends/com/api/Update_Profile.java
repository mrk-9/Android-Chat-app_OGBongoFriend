package com.ogbongefriends.com.api;

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
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class Update_Profile extends BasicApi implements Runnable {

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	private String postData;
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
//	private SharedPreferences pref;
	public static String auth_token;
	public static String resMsg;
	public static int resCode;
	CustomLoader p;
	Preferences pref;

	public Update_Profile(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.update_profile_api);
		// postData = getPostData();
		Log.d("URL ", ""+url);
		pref = new Preferences(ctx);
	}

	// =====
	public void setPostData(HashMap<String, String> map) {

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
			Log.d("Responce>>>>>>>>>>>>"+url ,res);
			JsonParser p = new JsonParser();
			JsonElement jele = p.parse(res);
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject(): null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();
				
				if(resCode==1 || resCode==900){
				
				pref.set(Constants.KeyUUID, objJson.get("uuid").getAsString());
				pref.set(Constants.kAuthT, objJson.get("auth_token").getAsString());
				pref.commit();
				
				}
				
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

		Log.v("registration/update", map12 + "");

		JSONObject json = new JSONObject();

		JSONObject json2 = new JSONObject();
		JSONObject json1 = new JSONObject();

		try {

//			json2.put("first_name", map12.get("username"));	
//			json2.put("last_name",map12.get("last_name"));
//			json2.put("gender", map12.get("gender"));
//			
//			json2.put("date_of_birth", map12.get("date_of_birth"));
//			
//			String []ar=map12.get("date_of_birth").split("-");
//			
//			
//			json2.put("year", ar[0].toString());
//			json2.put("month", ar[1].toString());
//			json2.put("day", ar[2].toString());
//			//json2.put("phone_no", map12.get("phone_no"));
//		//	json2.put("email",(map12.get("email"))); 
//			
//			
//			json2.put("address",(map12.get("address"))); 
//			json2.put("city", map12.get("city"));	
//			json2.put("state", map12.get("state"));
//			json2.put("country", map12.get("country"));
//			json2.put("pincode",map12.get("pincode"));
//			json2.put("about_me",map12.get("about_me"));
//			json2.put("interestedin",map12.get("interestedin"));
//			json2.put("relationship",map12.get("relationship"));
//			
//			
//			json2.put("sexuality", map12.get("sexuality"));
//			json2.put("height", map12.get("height"));
//			json2.put("weight",map12.get("weight"));
//			json2.put("body_type",map12.get("body_type"));
//			json2.put("hair_color",map12.get("hair_color"));
//			json2.put("eye_color",map12.get("eye_color"));
//			
//			
//			
//			json1.put("uuid", pref.get(Constants.KeyUUID).toString());
//			json1.put("auth_token", pref.get(Constants.kAuthT).toString());
			json1.put("user_data", json2);
			json.put(Constants.kappTag, json1);

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

}
