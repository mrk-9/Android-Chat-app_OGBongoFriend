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

public class PostContactDetails extends BasicApi implements Runnable {

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	@SuppressWarnings("unused")
	private String postData;
	private Context ctx;
	public static String client_id;
	public static String userID;
	public static int resCode;
	HashMap<String, String> map1;
	public static String resMsg, resOuth_token;
	CustomLoader p;
	Preferences pref;


	public PostContactDetails(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		//setPostData(map);
		url = Utils.getCompleteApiUrl(ctx, R.string.postContact);
		
		pref=new  Preferences(ctx);
	}

	public void setPostData(JSONObject obj){
		
		
		postData = getPostData(obj);

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
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject()
					: null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				
				JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();
				
				
				if(resCode==1){
			
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

	
	private String getPostData(JSONObject map12) {

		// Preferences p = new Preferences(ctx);

		Log.v("Login", map12 + "");

		JSONObject json = new JSONObject();

		JSONObject json2 = new JSONObject();

		try {

			json2.put("contacts", map12);
		
			
			
			json2.put("uuid", pref.get(Constants.KeyUUID));
			json2.put("auth_token", pref.get(Constants.kAuthT));
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