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

public class GetUserGiftApi extends BasicApi implements Runnable {

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	private String postData;
	private Context ctx;
	HashMap<String, String> map1;
	private int type;
	@SuppressWarnings("unused")
//	private SharedPreferences pref;
	public static String auth_token;
	public static int resCode;
	public static String resMsg;
 public	JsonArray userdata=null;
	CustomLoader p;
	Preferences pref;

	public GetUserGiftApi(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.getGift);
		// postData = getPostData();
		Log.d("URL ", ""+url);
		pref = new Preferences(ctx);
	}
	
	public GetUserGiftApi(Context ctx, DB db,String page , CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.getGift);
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
				
				
				
				if (objJson.get("data") != null) {

					 userdata = objJson.get("data").getAsJsonArray();
					
					 
					 
					 db.open();
						HashMap<String, String> userMap = new HashMap<String, String>();
						HashMap<String, String> userDataMap = new HashMap<String, String>();
						
						for (int i = 0; i < userdata.size(); i++) {
						
							if(type==1){
								userMap.put(DB.Table.gift_sent_to_user.receiver_master_id.toString(), pref.get(Constants.KeyUUID));
								userMap.put(DB.Table.gift_sent_to_user.sender_master_id.toString(), userdata.get(i).getAsJsonObject().get("uuid").getAsString());
							}
							else{
								userMap.put(DB.Table.gift_sent_to_user.receiver_master_id.toString(), map1.get("other_uuid"));
								userMap.put(DB.Table.gift_sent_to_user.sender_master_id.toString(), userdata.get(i).getAsJsonObject().get("uuid").getAsString());
							}
						
							
					
							userMap.put(DB.Table.gift_sent_to_user.gitf_id.toString(), userdata.get(i).getAsJsonObject().get("gift_sent_to_user_id").getAsString());
							userMap.put(DB.Table.gift_sent_to_user.gift_cost.toString(), userdata.get(i).getAsJsonObject().get("gift_cost").getAsString());
							userMap.put(DB.Table.gift_sent_to_user.description.toString(), userdata.get(i).getAsJsonObject().get("description").getAsString());
							userMap.put(DB.Table.gift_sent_to_user.gift_master_id.toString(), userdata.get(i).getAsJsonObject().get("gift_master_id").getAsString());
							userMap.put(DB.Table.gift_sent_to_user.add_date.toString(), userdata.get(i).getAsJsonObject().get("add_date").getAsString());
							userMap.put(DB.Table.gift_sent_to_user.status.toString(), "1");
							//userMap.put(Table.gift_sent_to_user.modify_date.toString(), userdata.get(i).getAsJsonObject().get("modify_date").getAsString());
							
							db.autoInsertUpdate(DB.Table.Name.gift_sent_to_user,userMap, DB.Table.gift_sent_to_user.gitf_id.toString()+" = "+userdata.get(i).getAsJsonObject().get("gift_sent_to_user_id").getAsString(),null);
							
							userDataMap.put(DB.Table.user_master.first_name.toString(), userdata.get(i).getAsJsonObject().get("first_name").getAsString());
							userDataMap.put(DB.Table.user_master.last_name.toString(), userdata.get(i).getAsJsonObject().get("last_name").getAsString());
							userDataMap.put(DB.Table.user_master.gender.toString(), userdata.get(i).getAsJsonObject().get("gender").getAsString());
							userDataMap.put(DB.Table.user_master.profile_pic.toString(), userdata.get(i).getAsJsonObject().get("profile_pic").getAsString());
							userDataMap.put(DB.Table.user_master.server_id.toString(), userdata.get(i).getAsJsonObject().get("id").getAsString());
							userDataMap.put(DB.Table.user_master.city.toString(), "1");
							userDataMap.put(DB.Table.user_master.uuid.toString(), userdata.get(i).getAsJsonObject().get("uuid").getAsString());
							db.autoInsertUpdate(DB.Table.Name.user_master,userDataMap, DB.Table.user_master.uuid.toString()+ " = '"+ userdata.get(i).getAsJsonObject().get("uuid").getAsString()+"'", null);
//						
						}	
						db.close();
					}
				else{
					userdata=null;
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

		try {

			json2.put("uuid", map12.get("uuid"));	
			json2.put("auth_token", map12.get("auth_token"));
			json2.put("time_stamp", map12.get("time_stamp"));
			json2.put("type", map12.get("type"));
			json2.put("other_uuid", map12.get("other_uuid"));
			type=Integer.parseInt(map12.get("type"));
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
