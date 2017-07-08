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
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

public class GetPhotoRating extends BasicApi implements Runnable {

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
	CustomLoader p;
	Preferences pref;

	public GetPhotoRating(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_photo_rating);
		// postData = getPostData();
		Log.d("URL ", ""+url);
		pref = new Preferences(ctx);
	}
	
	public GetPhotoRating(Context ctx, DB db,String page , CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.get_photo_rating);
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
db.open();
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
				
				
				
				if (objJson.get("Data") != null) {

					 userdata = objJson.get("Data").getAsJsonArray();
					 for(int i=0;i<userdata.size();i++){
					 HashMap<String, String>checkData=new HashMap<String, String>();
					 checkData.put(Table.photo_rating.rating.toString(), userdata.get(i).getAsJsonObject().get("rating").getAsString());
					 checkData.put(Table.photo_rating.photo_gallery_id.toString(), userdata.get(i).getAsJsonObject().get("photo_gallery_id").getAsString());
					 checkData.put(Table.photo_rating.user_master_id.toString(), userdata.get(i).getAsJsonObject().get("user_master_id").getAsString());
					 checkData.put(Table.photo_rating.add_date.toString(), userdata.get(i).getAsJsonObject().get("add_date").getAsString());
					 checkData.put(Table.photo_rating.modify_date.toString(), userdata.get(i).getAsJsonObject().get("modify_date").getAsString());
					 
					 db.autoInsertUpdate(Table.Name.photo_rating,checkData , Table.photo_rating.photo_gallery_id.toString()+" = "+userdata.get(0).getAsJsonObject().get("photo_gallery_id").getAsString()+" AND "+Table.photo_rating.user_master_id.toString()+" = '"+userdata.get(0).getAsJsonObject().get("user_master_id").getAsString()+"'", null);
			
					 if(userdata.get(i).getAsJsonObject().get("ratingUser")!=null){
						 
						 HashMap<String, String>userData=new HashMap<String, String>();
						 userData.put(Table.user_master.server_id.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("id").getAsString());
						 userData.put(Table.user_master.uuid.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("uuid").getAsString());
						 userData.put(Table.user_master.first_name.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("first_name").getAsString());
						 userData.put(Table.user_master.last_name.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("last_name").getAsString());
						 userData.put(Table.user_master.email.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("email").getAsString());
						 userData.put(Table.user_master.gender.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("gender").getAsString());
						 userData.put(Table.user_master.city.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("city").getAsString());
						 userData.put(Table.user_master.profile_pic.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("profile_pic").getAsString());
						 userData.put(Table.user_master.points.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("points").getAsString());
						 userData.put(Table.user_master.last_seen.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("last_seen").getAsString());
						 userData.put(Table.user_master.status.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("status").getAsString());
						 userData.put(Table.user_master.date_of_birth.toString(), userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("date_of_birth").getAsString());
						
						 db.autoInsertUpdate(Table.Name.user_master,userData , Table.user_master.uuid.toString()+" = '"+userdata.get(i).getAsJsonObject().get("ratingUser").getAsJsonObject().get("uuid").getAsString()+"'", null);
 
					 }
					 
					 }
				}
			}
		} catch (Exception e) {
			//db.close();
			onError(e);

		}
		//db.close();
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
