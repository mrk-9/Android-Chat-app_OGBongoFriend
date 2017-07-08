package com.ogbongefriends.com.ogbonge.fragment;

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
import com.ogbongefriends.com.api.ApiException;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

// type=5 who fav'd me 
// type=6 Who liked me
// type =7 my ogbonge friend


public class searchApi extends BasicApi implements Runnable {

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	private String postData;
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
	public static String auth_token;
	public static int resCode,page_index;
	public static JsonArray userdata;
	public static String resMsg;
	CustomLoader p;
	Preferences pref;

	public searchApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_search_api);
		userdata=new JsonArray();
		Log.d("URL ", ""+url);
		pref = new Preferences(ctx);
	}
	
	public searchApi(Context ctx, DB db,String page , CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_all_users);
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

			JsonParser p = new JsonParser();
			JsonElement jele = p.parse(res);
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject(): null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();
				page_index=objJson.get("page_index").getAsInt();
				
				
				if (objJson.get("data") != null) {

					 userdata = objJson.get("data").getAsJsonArray();
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
		JSONObject json3 = new JSONObject();
		

		try {
			
			json2.put("uuid", map12.get("uuid"));	
			json2.put("auth_token", map12.get("auth_token"));
			json2.put("time_stamp", map12.get("time_stamp"));
			json2.put("type",map12.get("type"));
			json2.put("category", map12.get("category"));
			json2.put("keyword", map12.get("keyword"));
			json2.put("page_index", map12.get("page_index"));
			
			// for custom search
			if(map12.get("type").toString().equalsIgnoreCase("1")){
			
			json3.put("interestedin_purpose_master_id",map12.get("interestedin_purpose_master_id")); 
			json3.put("interestedin_master_id",map12.get("interestedin_master_id")); 
			json3.put("age_range",map12.get("age_range")); 
			json3.put("near_by",map12.get("near_by")); 
			json3.put("distance",map12.get("distance"));
			
			json2.put("stardard_search_data",json3); 
			}
			
			if(map12.get("type").toString().equalsIgnoreCase("3")||(map12.get("type").toString().equalsIgnoreCase("4"))||(map12.get("type").toString().equalsIgnoreCase("5"))||(map12.get("type").toString().equalsIgnoreCase("6"))||(map12.get("type").toString().equalsIgnoreCase("7"))){
				
				
				json3.put("interestedin_purpose_master_id",map12.get("interestedin_purpose_master_id")); 
				json3.put("interestedin_master_id",map12.get("interestedin_master_id")); 
				json3.put("age_range",map12.get("age_range")); 
				json3.put("near_by",map12.get("near_by")); 
				json3.put("distance",map12.get("distance"));
				
				json2.put("stardard_search_data",json3); 
				}
			//for who is online
			if(map12.get("type").toString().equalsIgnoreCase("2")){
				
				
				if(pref.getInt("wio_interestedin_purpose_master_id")>0){
					json3.put("interestedin_purpose_master_id",String.valueOf(pref.getInt("wio_interestedin_purpose_master_id"))); 
				}
				else{
					json3.put("interestedin_purpose_master_id","");
				}
				if(pref.getInt("wio_interestedin_master_id")==0){
					json3.put("interestedin_master_id","3");
				}
				else{
					json3.put("interestedin_master_id",String.valueOf(pref.getInt("wio_interestedin_master_id")));
				}
				if(pref.getInt("wio_meet_max_age")==0 || pref.getInt("wio_meet_min_age")==0){
					json3.put("age_range","18,80");
				}
				else{
					json3.put("age_range",String.valueOf(pref.getInt("wio_meet_min_age"))+","+String.valueOf(pref.getInt("wio_meet_max_age")));

				}

				json3.put("distance",String.valueOf(pref.getInt("wio_location")));
				
				json2.put("stardard_search_data",json3); 
				
			}
			
			
			
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

}
