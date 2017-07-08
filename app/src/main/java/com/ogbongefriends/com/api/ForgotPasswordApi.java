package com.ogbongefriends.com.api;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
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

public class ForgotPasswordApi extends BasicApi implements Runnable {

	
	private DB db;
	private String url;
	
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
	
	public static String auth_token;
	public static int resCode;
	private String postData;
	public static String resMsg;
	Preferences pref;
	
	CustomLoader p;

	public ForgotPasswordApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		//setPostData(map);
		url = Utils.getCompleteApiUrl(ctx, R.string.forget_password_api);
		
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
				
				Log.v("Called url in get skill", url);
				
				//callUrl(url);
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
				
				
			

				if (objJson.has("Country")) {
					JsonArray CountryJson = objJson.get("Country").getAsJsonArray();
					HashMap<String, String> CountryList;
					
					
//					
//				for (int i = 0; i < CountryJson.size(); i++) {
//
//				CountryList = new HashMap<String, String>();
//					
//				CountryList.put(Table.sg_country.id.toString(), CountryJson.get(i).getAsJsonObject().get("id").getAsString());
//				CountryList.put(Table.sg_country.country_code.toString(), CountryJson.get(i).getAsJsonObject().get("country_code").getAsString());
//				CountryList.put(Table.sg_country.country_name.toString(), CountryJson.get(i).getAsJsonObject().get("country_name").getAsString());
//				CountryList.put(Table.sg_country.country_name_ar.toString(), CountryJson.get(i).getAsJsonObject().get("country_name_ar").getAsString());
//				
//				//********insert/update data in skill_categories table********
//				db.autoInsertUpdate(Table.Name.sg_country, CountryList, Table.sg_country.id+" = "+CountryJson.get(i).getAsJsonObject().get("id").getAsString(), null);
//					
//			}
//		}
//				
//				//======Update TimeStamp
//			 	Preferences pref = new Preferences(ctx);
//			 	pref.set(Constants.GetCountryTS, objJson.get(Constants.SyncTime).getAsString());
//			 	pref.commit();
//			

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

		Log.v("Login", map12 + "");

		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();

		try {

			json2.put("email", map12.get("email"));	
			json2.put("platform_type",map12.get("platform_type"));
			json2.put("device_id",map12.get("device_id"));
			json2.put("device_type", map12.get("device_type"));	
			
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


//	private String AsembleUrl(String email) {
//		// TODO Auto-generated method stub
////		Preferences pref = new Preferences(ctx);
////		String language = pref.get(Constants.KeyLanguageCode);
////		
////		String endUrl = "/" + language + "/" + email;
//		return "";
//	}

	
}
