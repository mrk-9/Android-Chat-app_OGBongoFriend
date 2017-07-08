package com.ogbongefriends.com.api;

import java.io.InputStream;
import java.util.ArrayList;
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


public class getEntityApi extends BasicApi implements Runnable {

	private DB db;
	private String url;
	private String postData;
	HashMap<String, String> map1;
	private Context ctx;
	public static int resCode;
	public static String resMsg;
	Preferences pref;
	CustomLoader p;
	
	public getEntityApi(Context ctx, DB db,CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		pref=new Preferences(this.ctx);
		url = Utils.getCompleteApiUrl(ctx, R.string.get_entity_api);
		p = new CustomLoader(ctx,
				android.R.style.Theme_Translucent_NoTitleBar);
	}

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
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject()
					: null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				JsonObject objJson = obj.get("ogbonge").getAsJsonObject();
				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();
			pref.set(Constants.getEntityTime, objJson.get("sync_time").getAsString()).commit();
			Log.d("arvi", "arvi "+pref.get(Constants.kTimeStamp));
				if (resCode == 0) {

				} else {
					this.db.open();
					if (objJson.get("countryData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("countryData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.country_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
					}
					
					if (objJson.get("playersData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("playersData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_master.toString(), iter, DB.Table.starsandclubs_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					if (objJson.get("starsnclubsCategoryData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("starsnclubsCategoryData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_category_master.toString(), iter, DB.Table.starsandclubs_category_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
					}	
					
					
					
					
					if (objJson.get("clubsData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("clubsData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_master.toString(), iter, DB.Table.starsandclubs_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					

					if (objJson.get("starsData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("starsData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_master.toString(), iter, DB.Table.starsandclubs_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					if (objJson.get("ninzaData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("ninzaData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_master.toString(), iter, DB.Table.starsandclubs_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					if (objJson.get("internationalData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("internationalData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.starsandclubs_master.toString(), iter, DB.Table.starsandclubs_master.id.toString()+" = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					
					
					
					if (objJson.get("stateData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("stateData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.nigiria_state_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					if (objJson.get("giftData") != null) {

						ArrayList<String>arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("giftData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.gift_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
					}	
				
					
					if (objJson.get("cityData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("cityData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.nigiria_city_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					
					if (objJson.get("jobData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("jobData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.job_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					
					
					if (objJson.get("educationData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("educationData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.education_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
						
					}	
					

					if (objJson.get("interestedinPurposeData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("interestedinPurposeData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.interestedin_purpose_master.toString(), iter, "id = "+singleCountry.get("id").getAsString(), null);
						}
						
					}	
			
					
					if (objJson.get("eventCategoriesData") != null) {

						ArrayList<String> arr = new ArrayList<String>();
						JsonArray userdata = objJson.get("eventCategoriesData")
								.getAsJsonArray();
						
						for(int i=0;i<userdata.size();i++){
						JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
						HashMap<String, String> iter = Utils
								.getMapFromJsonObject(singleCountry);
						
						db.autoInsertUpdate(DB.Table.Name.event_categories.toString(), iter, "id= "+singleCountry.get("id").getAsString(), null);
						}
					}
				}
				Log.v("Country responce", resMsg + "==" + resCode);

			}

		} catch (Exception e) {
			onError(e);

		}

	}
	
	
	private String getPostData(HashMap<String, String> map12) {
		JSONObject json = new JSONObject();
		
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

}

