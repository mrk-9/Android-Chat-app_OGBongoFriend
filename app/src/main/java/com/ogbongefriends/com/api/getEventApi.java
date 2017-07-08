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
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class getEventApi extends BasicApi implements Runnable {

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
	private String server_id; 
	private String type="";
	
	public static int currentPage1=0;
	public static int currentPage2=0;
	public static int currentPage3=0;
	
	CustomLoader p;
	Preferences pref;

	public getEventApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.getevent_api);
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
				if(type.equalsIgnoreCase("1")){
					currentPage1=objJson.get("page_index").getAsInt();
				}
				
				if(type.equalsIgnoreCase("2")){
					currentPage2=objJson.get("page_index").getAsInt();
				}
				
				if(type.equalsIgnoreCase("3")){
					currentPage3=objJson.get("page_index").getAsInt();
				}
				
				if(resCode==1 || resCode==900){
				
				if (objJson.get("Data") != null) {

					db.open();
					JsonArray userdata = objJson.get("Data").getAsJsonArray();

					try {
				
						for(int i=0;i<userdata.size();i++){
							
							JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
							
							
							if (singleCountry.get("eventUser") != null){
								
								JsonObject data=singleCountry.get("eventUser").getAsJsonObject();	
								HashMap<String, String>data_to_insert= CelUtils.getMapFromJsonObject(data);
								server_id=data.get("id").getAsString();
								data_to_insert.remove("id");
								data_to_insert.put("server_id", server_id);
								
								db.autoInsertUpdate(DB.Table.Name.user_master, data_to_insert, DB.Table.user_master.uuid+" = "+"'"+ data.get("uuid").getAsString() +"'", null);
								
							}
							
							singleCountry.remove("eventUser");
							HashMap<String, String>data_to_event=CelUtils.getMapFromJsonObject(singleCountry);
							data_to_event.put("type", type);
							db.autoInsertUpdate(DB.Table.Name.event_master, data_to_event, DB.Table.event_master.id+" = "+"'"+ singleCountry.get("id").getAsString() +"'", null);
						}
				db.close();
				
				
					}
				catch(Exception e)
				{
					e.printStackTrace();
				}
					
				}
				
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
		JSONObject json = new JSONObject();
		
		type=map12.get("type");
		// Preferences p = new Preferences(ctx);
		try {

			JSONObject json1=new JSONObject(map12);		
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
