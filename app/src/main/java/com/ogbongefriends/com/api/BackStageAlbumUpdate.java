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
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class BackStageAlbumUpdate extends BasicApi implements Runnable {

	
	private DB db;
	private String url;
	
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
	
	public static String auth_token;
	public static int resCode;
	private String postData;
	public static String resMsg;
	public static  JsonObject jsondata;
	Preferences pref;
	
	CustomLoader p;

	public BackStageAlbumUpdate(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		//setPostData(map);
		url = Utils.getCompleteApiUrl(ctx, R.string.albumInfo);
		
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
				
				if (objJson.has("Data")) {
					
					JsonObject CountryJson = objJson.get("Data").getAsJsonObject();
				
					HashMap<String, String>hdada=new HashMap<String, String>();
				
					hdada.put(Table.photo_album_master.user_master_id.toString(), pref.get(Constants.KeyUUID));
					hdada.put(Table.photo_album_master.album_category_id.toString(), CountryJson.get(Table.photo_album_master.album_category_id.toString()).getAsString());
					hdada.put(Table.photo_album_master.album_title.toString(), CountryJson.get(Table.photo_album_master.album_title.toString()).getAsString());
					hdada.put(Table.photo_album_master.album_description.toString(), CountryJson.get(Table.photo_album_master.album_description.toString()).getAsString());
					hdada.put(Table.photo_album_master.privacy_settings.toString(), CountryJson.get(Table.photo_album_master.privacy_settings.toString()).getAsString());
					hdada.put(Table.photo_album_master.status.toString(), CountryJson.get(Table.photo_album_master.status.toString()).getAsString());
					hdada.put(Table.photo_album_master.add_date.toString(), CountryJson.get(Table.photo_album_master.add_date.toString()).getAsString());
					
					hdada.put(Table.photo_album_master.modify_date.toString(), CountryJson.get(Table.photo_album_master.modify_date.toString()).getAsString());
					hdada.put(Table.photo_album_master.point_value.toString(), CountryJson.get(Table.photo_album_master.point_value.toString()).getAsString());

					db.open();
					db.autoInsertUpdate(Table.Name.photo_album_master, hdada, Table.photo_album_master.user_master_id+" = "+"'"+ pref.get(Constants.KeyUUID) +"'", null);
					db.close();

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
		JSONObject json2 ;

		try {

			json2=new JSONObject(map12);
			
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
