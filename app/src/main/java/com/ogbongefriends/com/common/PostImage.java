package com.ogbongefriends.com.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.DB.DB;


public abstract class PostImage extends AsyncTask<String, Void, String> {

// ******* DECLARING VARIABLES *******
	
	public static String resMsg, imageName, orginal_file_name;
	
	String response, Url, FileName, UserId, Type, Upload_type ;
	
	Context context;
	
	Preferences pref;
	File f;
	
	//long FeedId;
	
	@SuppressWarnings("unused")
	private DB db;
	public static  int resCode;

	public abstract void receiveData();
	public abstract void receiveError();

	public PostImage(File fl, String url, String filename, String userId/*, long feedId*/, String type, String upload_type, Context c) {
pref=new Preferences(c);
		f        = fl;
		Url      = url;
		FileName = filename;
		UserId   = userId;
		//FeedId   = feedId;
		Type     = type;
		Upload_type = upload_type;
		context  = c;
		db = new DB(context);
	}

	@Override
	protected void onPreExecute() {

	}

	@SuppressWarnings("static-access")
	@Override
	protected String doInBackground(String... params) {

		FileInputStream fis;
		
		try {

			fis = new FileInputStream(f);

			//HttpFileUploader htfu = new HttpFileUploader(Url, Media.IMAGE_DIR, FileName, UserId/*, FeedId*/, Type, Upload_type);
			HttpFileUploader htfu = new HttpFileUploader(Url, pref.get(Constants.TempKeyUUID), FileName, UserId/*, FeedId*/, Type, pref.get(Constants.TempkAuthT),Upload_type);
			htfu.doStart(fis);
			
			response = htfu.updateCall();

		}
		catch (FileNotFoundException e) {
		
			e.printStackTrace();
			receiveError();
		}

		return response;
	}

	@Override
	protected void onPostExecute(String res) {

		if (res != null) {
	
			try {

				JsonParser p     = new JsonParser();
				JsonElement jele = p.parse(res);
				JsonObject obj   = jele.isJsonObject() ? jele.getAsJsonObject(): null;

				if (obj == null) {

				}
				else {

					JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
					
					resCode = objJson.get("res_code").getAsInt();
					resMsg 	= objJson.get("res_msg").getAsString();
					
					if(resCode == 1){
						Log.d("uploaded", "arv successfully");
					}
					/*	
					else{
						
					}				
					
					if (resMsg.equalsIgnoreCase("Size of an image can not be greater than 2MB.")) {						
						
						imageName = "Size of an image can not be greater than 2MB.";
					}
					else {
						
						imageName = objJson.get("image_name").getAsString();
					}*/

					receiveData();
				}

			} catch (Exception e) {
				e.printStackTrace();
			//	receiveError();

			}
		}
		else
		{
			receiveError();
		}

	}

	public interface CallbackReciever {
		public void receiveData();
		public void receiveError();

	}
}