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

public class GetAllComment_of_Photo extends BasicApi implements Runnable {

	@SuppressWarnings("unused")
	private DB db;
	private String url;
	private String postData;
	private Context ctx;
	HashMap<String, String> map1;
	@SuppressWarnings("unused")
//	private SharedPreferences pref;
	public static String auth_token;
	public static int resCode;
	public static String resMsg;
 public	JsonArray userdata=null;
 public static int type=0;
 public static int commentId=0;
	CustomLoader p;
	Preferences pref;

	public GetAllComment_of_Photo(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_photo_comment);
		// postData = getPostData();
		Log.d("URL ", ""+url);
		pref = new Preferences(ctx);
	}
	
	public GetAllComment_of_Photo(Context ctx, DB db,String page , CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_photo_comment);
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
				
				JsonObject commentedUser=new JsonObject();
				
				if(type==2){
				if (objJson.get("Data")!= null) {

					 userdata = objJson.get("Data").getAsJsonArray();
					 
					 try {
							db.open();
							
								HashMap<String, String> userMap = new HashMap<String, String>();
								HashMap<String, String> UserDataMap = new HashMap<String, String>();
								for (int i = 0; i < userdata.size(); i++) {
									userMap.clear();
								userMap.put(DB.Table.photo_comment.id.toString(), userdata.get(i).getAsJsonObject().get("id").getAsString());
								userMap.put(DB.Table.photo_comment.user_master_id.toString(), userdata.get(i).getAsJsonObject().get("user_master_id").getAsString());
								userMap.put(DB.Table.photo_comment.photo_gallery_id.toString(), userdata.get(i).getAsJsonObject().get("photo_gallery_id").getAsString());
								userMap.put(DB.Table.photo_comment.comment.toString(), userdata.get(i).getAsJsonObject().get("comment").getAsString());
								userMap.put(DB.Table.photo_comment.add_date.toString(), userdata.get(i).getAsJsonObject().get("add_date").getAsString());
								userMap.put(DB.Table.photo_comment.modify_date.toString(), userdata.get(i).getAsJsonObject().get("modify_date").getAsString());
								
								if(userdata.get(i).getAsJsonObject().get("commentUser")!=null){
								
								commentedUser = userdata.get(i).getAsJsonObject().get("commentUser").getAsJsonObject();
								
								UserDataMap.clear();
								
								UserDataMap.put(DB.Table.user_master.uuid.toString(), commentedUser.get("uuid").getAsString());
								UserDataMap.put(DB.Table.user_master.server_id.toString(), commentedUser.get("id").getAsString());
								UserDataMap.put(DB.Table.user_master.first_name.toString(), commentedUser.get("first_name").getAsString());
								UserDataMap.put(DB.Table.user_master.last_name.toString(), commentedUser.get("last_name").getAsString());
								UserDataMap.put(DB.Table.user_master.email.toString(), commentedUser.get("email").getAsString());
								UserDataMap.put(DB.Table.user_master.date_of_birth.toString(), commentedUser.get("date_of_birth").getAsString());
								UserDataMap.put(DB.Table.user_master.gender.toString(), commentedUser.get("gender").getAsString());
								
								UserDataMap.put(DB.Table.user_master.profile_pic.toString(), commentedUser.get("profile_pic").getAsString());
								UserDataMap.put(DB.Table.user_master.city.toString(), commentedUser.get("city").getAsString());
								UserDataMap.put(DB.Table.user_master.latitude.toString(), commentedUser.get("latitude").getAsString());
								UserDataMap.put(DB.Table.user_master.longitude.toString(), commentedUser.get("longitude").getAsString());
								UserDataMap.put(DB.Table.user_master.points.toString(),  commentedUser.get("points").getAsString());
								UserDataMap.put(DB.Table.user_master.status.toString(), commentedUser.get("status").getAsString());
								UserDataMap.put(DB.Table.user_master.last_seen.toString(), commentedUser.get("last_seen").getAsString());
								
								db.autoInsertUpdate(DB.Table.Name.user_master,UserDataMap, DB.Table.user_master.uuid+ " = '"+ userdata.get(i).getAsJsonObject().get("user_master_id").getAsString()+"'", null);
								}
								db.autoInsertUpdate(DB.Table.Name.photo_comment,userMap, DB.Table.photo_comment.id+ " = '"+ userdata.get(i).getAsJsonObject().get("id").getAsString()+"'", null);
								
								}	
								//db.close();
				
							} catch (Exception e) {
							 e.printStackTrace();
					     	}
					 
				}
				}
				else{
					commentId=objJson.get("comment_id").getAsInt();
							
							
							
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
		try{
		
			type=Integer.parseInt(map12.get("type"));
		JSONObject json2 = new JSONObject(map12);

		
			
			
			
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
