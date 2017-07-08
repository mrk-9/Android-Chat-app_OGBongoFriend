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
import com.ogbongefriends.com.common.NotificationType;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;
import com.ogbongefriends.com.DB.DB.Table;

public class user_profile_api extends BasicApi implements Runnable {

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
	private String user_id="";
	CustomLoader p;
	Preferences pref;

	public user_profile_api(Context ctx, DB db, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		// url = "http://tmwtg.com/tmwtg/Cpanels/signup";
		url = Utils.getCompleteApiUrl(ctx, R.string.profile_info_api);
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
				if (objJson.get("Data") != null) {

					JsonObject userdata = objJson.get("Data").getAsJsonObject();

					try {
						
							HashMap<String, String> userMap = new HashMap<String, String>();
							userMap.put(Table.user_master.uuid.toString(), userdata.get("uuid").getAsString());
							userMap.put(Table.user_master.server_id.toString(), userdata.get("id").getAsString());
							userMap.put(Table.user_master.first_name.toString(), userdata.get("first_name").getAsString());
							userMap.put(Table.user_master.last_name.toString(), userdata.get("last_name").getAsString());
							//userMap.put(Table.user_master.user_name.toString(), userdata.get("user_name").getAsString());
							userMap.put(Table.user_master.email.toString(), userdata.get("email").getAsString());
							userMap.put(Table.user_master.date_of_birth.toString(), userdata.get("date_of_birth").getAsString());
							userMap.put(Table.user_master.gender.toString(), userdata.get("gender").getAsString());
							userMap.put(Table.user_master.phone_number.toString(), userdata.get("phone_number").getAsString());
							userMap.put(Table.user_master.profile_pic.toString(), userdata.get("profile_pic").getAsString());
							userMap.put(Table.user_master.handle_description.toString(), userdata.get("handle_description").getAsString());
							userMap.put(Table.user_master.address.toString(), userdata.get("address").getAsString());
							userMap.put(Table.user_master.state.toString(), userdata.get("state").getAsString());
							userMap.put(Table.user_master.city.toString(), userdata.get("city").getAsString());
							userMap.put(Table.user_master.country_master_id.toString(), userdata.get("country_master_id").getAsString());
							userMap.put(Table.user_master.pin_code.toString(), userdata.get("pin_code").getAsString());
							userMap.put(Table.user_master.latitude.toString(), userdata.get("latitude").getAsString());
							userMap.put(Table.user_master.longitude.toString(), userdata.get("longitude").getAsString());
							userMap.put(Table.user_master.hide_account.toString(), userdata.get("hide_account").getAsString());
							userMap.put(Table.user_master.rating.toString(), userdata.get("rating").getAsString());
							userMap.put(Table.user_master.points.toString(),  userdata.get("points").getAsString());
							userMap.put(Table.user_master.status.toString(), userdata.get("status").getAsString());
							userMap.put(Table.user_master.add_date.toString(), userdata.get("add_date").getAsString());
							userMap.put(Table.user_master.modify_date.toString(), userdata.get("add_date").getAsString());
							userMap.put(Table.user_master.about_myself.toString(), userdata.get(Table.user_master.about_myself.toString()).getAsString());
							userMap.put(Table.user_master.pass_7days_expiry_time.toString(), userdata.get(Table.user_master.pass_7days_expiry_time.toString()).getAsString());
							userMap.put(Table.user_master.meet_min_age.toString(), userdata.get(Table.user_master.meet_min_age.toString()).getAsString());
							userMap.put(Table.user_master.meet_max_age.toString(), userdata.get(Table.user_master.meet_max_age.toString()).getAsString());
							userMap.put(Table.user_master.education_master_id.toString(), userdata.get(Table.user_master.education_master_id.toString()).getAsString());
							userMap.put(Table.user_master.job_master_id.toString(), userdata.get(Table.user_master.job_master_id.toString()).getAsString());
							userMap.put(Table.user_master.interestedin_purpose_master_id.toString(), userdata.get(Table.user_master.interestedin_purpose_master_id.toString()).getAsString());
							userMap.put(Table.user_master.last_seen.toString(), userdata.get(Table.user_master.last_seen.toString()).getAsString());
							
							userMap.put(Table.user_master.login_status.toString(), userdata.get(Table.user_master.login_status.toString()).getAsString());
							
							userMap.put(Table.user_master.relationship_master_id.toString(), String.valueOf(0));
							
						if(map1.get("other_user_uuid").trim().length()<1){
							NotificationType.server_id=userdata.get("id").getAsString();
							NotificationType.UserPoints=userdata.get("points").getAsString();
							NotificationType.pass_7days_expiry_time=userdata.get(Table.user_master.pass_7days_expiry_time.toString()).getAsString();
							NotificationType.name=userdata.get("first_name").getAsString()+" "+userdata.get("last_name").getAsString();
							NotificationType.date_of_birth=userdata.get("date_of_birth").getAsString();
							NotificationType.profile_pic=userdata.get("profile_pic").getAsString();
						}


							if(objJson.has(Table.user_master.rate_count.toString())){
							userMap.put(Table.user_master.rate_count.toString(), objJson.get(Table.user_master.rate_count.toString()).getAsString());
							}
							
								userMap.put(Table.user_master.sexuality_master_id.toString(),String.valueOf(0));
								userMap.put(Table.user_master.hair_color_master_id.toString(),String.valueOf(0));
								userMap.put(Table.user_master.eye_color_master_id.toString(),String.valueOf(0));
							if(userdata.get("height_master_id").getAsString().length()<1){
								userMap.put(Table.user_master.height_master_id.toString(),"0");
							}
							else{
								userMap.put(Table.user_master.height_master_id.toString(), userdata.get("height_master_id").getAsString());	
							}
							
							
							if(userdata.get("verification_level").getAsString().length()<1){
								userMap.put(Table.user_master.verification_level.toString(),"0");
							}
							else{
								userMap.put(Table.user_master.verification_level.toString(), userdata.get("verification_level").getAsString());	
							}
							
							
							
							
							
							
							if(userdata.get("weight_master_id").getAsString().length()<1){
								userMap.put(Table.user_master.weight_master_id.toString(), "0");
								
							}
							else{
								userMap.put(Table.user_master.weight_master_id.toString(), userdata.get("weight_master_id").getAsString());
								
							}
							if(userdata.get("interestedin_master_id").getAsString().length()<1){
								userMap.put(Table.user_master.interestedin_master_id.toString(),"0");
									
							}
							else{
								userMap.put(Table.user_master.interestedin_master_id.toString(), userdata.get("interestedin_master_id").getAsString());
								
							}
							if(userdata.get("bodytype_master_id").getAsString().length()<1){
								userMap.put(Table.user_master.bodytype_master_id.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.bodytype_master_id.toString(), userdata.get("bodytype_master_id").getAsString());
							}
							if (objJson.get("friend_status") == null) {
								userMap.put(Table.user_master.friend_status.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.friend_status.toString(), objJson.get("friend_status").getAsString());
							}
													if (objJson.get("favourite_status") == null) {
								userMap.put(Table.user_master.favourite_status.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.favourite_status.toString(), objJson.get("favourite_status").getAsString());
							}

							if (objJson.get("backstage_album_cost") == null) {
								userMap.put(Table.user_master.backstage_album_cost.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.backstage_album_cost.toString(), objJson.get("backstage_album_cost").getAsString());
							}
							
							if (objJson.get("gift_count") == null) {
								userMap.put(Table.user_master.gift_count.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.gift_count.toString(), objJson.get("gift_count").getAsString());
							}
							
							if (objJson.get("photo_count") == null) {
								userMap.put(Table.user_master.photo_count.toString(), "0");
							}
							else{
								userMap.put(Table.user_master.photo_count.toString(), objJson.get("photo_count").getAsString());
							}
							
							
							
							
							if(userdata.has("advertise_me_expiry_time")){
								userMap.put(Table.user_master.advertise_me_expiry_time.toString(), userdata.get("advertise_me_expiry_time").getAsString());
							}
							
							
							user_id=userdata.get("uuid").getAsString();
							
							db.open();
							db.autoInsertUpdate(Table.Name.user_master,userMap,Table.user_master.uuid+ " = '"+ userdata.get("uuid").getAsString()+"'", null);
							//db.close();
						} catch (Exception e) {
						 e.printStackTrace();
				     	}
				}
				
				
				
				if (objJson.get("starandclubData") != null) {

					JsonArray SNC_data = objJson.get("starandclubData").getAsJsonArray();

					try {
						
						
						
							HashMap<String, String> snc_data = new HashMap<String, String>();
						db.delete(Table.Name.user_starsandclubs, null, null);
							for (int i = 0; i < SNC_data.size(); i++) {
								snc_data.clear();
								
								
								
								snc_data.put(Table.user_starsandclubs.starsandclubs_master_id.toString(), SNC_data.get(i).getAsJsonObject().get("starsandclubs_master_id").getAsString());
								snc_data.put(Table.user_starsandclubs.user_master_id.toString(), user_id);
								snc_data.put(Table.user_starsandclubs.type.toString(), SNC_data.get(i).getAsJsonObject().get("type").getAsString());
								snc_data.put(Table.user_starsandclubs.followers.toString(), SNC_data.get(i).getAsJsonObject().get("followers").getAsString());
								db.autoInsertUpdate(Table.Name.user_starsandclubs,snc_data,null, null);
							//db.close();
							}
							} catch (Exception e) {
						 e.printStackTrace();
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

		// Preferences p = new Preferences(ctx);

		Log.v("registration/update", map12 + "");

		JSONObject json = new JSONObject();

		JSONObject json2 = new JSONObject();

		try {

			json2.put("uuid", map12.get("uuid"));	
			json2.put("auth_token", map12.get("auth_token"));
			json2.put("other_user_uuid",map12.get("other_user_uuid"));
			json2.put("time_stamp", map12.get("time_stamp"));
			
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
