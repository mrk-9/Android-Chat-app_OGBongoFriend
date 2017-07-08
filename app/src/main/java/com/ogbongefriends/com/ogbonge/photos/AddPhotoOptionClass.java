package com.ogbongefriends.com.ogbonge.photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.api.verification_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.PostImage_AfterLogin;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.FileCache;

public class AddPhotoOptionClass extends Activity {


private static String APP_ID = "1509359755975991";


	//private static String APP_ID = "750560845021720";
		
	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private ArrayList<HashMap<String, String>>data;
 private	Calendar cal;
	private SharedPreferences mPrefs;
	 Dialog dialog;
	 ArrayList<String>url;
	 private DB db;
	 private CustomLoader p;
	 downloadImages dimage;
	 private int verification_status=-1;
	 private boolean fromFb=false;
	 FileCache fileCache;
	private GridView album_grid;
private fb_album_adapter fbalbumAdapter;
private Preferences pref;
public static int uploadType=0;
private FbImage_Adapter fb_image_adapter;
private user_profile_api getUerprofile;
private verification_api verification;
private HashMap<String, String>hashMap;
private SocialAuthAdapter adapter;	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin_signup);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		data=new ArrayList<HashMap<String,String>>();
		fileCache=new FileCache(AddPhotoOptionClass.this);
		pref=new Preferences(AddPhotoOptionClass.this);
		uploadType=pref.getInt("upload_type");
		url=new ArrayList<String>();
		dimage=new downloadImages();
		adapter = new SocialAuthAdapter(new ResponseListener());
		cal=Calendar.getInstance();
		db=new DB(AddPhotoOptionClass.this);
		 p = new CustomLoader(AddPhotoOptionClass.this, android.R.style.Theme_Translucent_NoTitleBar);
		 
		String path = Environment.getExternalStorageDirectory()+"/OgbongeDir/facebook_pics/";
	
		File f = new File(path);   
		purgeDirectory(f);
		fb_image_adapter=new FbImage_Adapter(AddPhotoOptionClass.this,data) {
			
			@Override
			protected void onItemClick(View v, String string) {
				// TODO Auto-generated method stub
				url.add(string);
				dimage.execute(url);
				
			}
		};
		
		
		getUerprofile=new user_profile_api(AddPhotoOptionClass.this, db, p){

			@Override
			protected void onDone() {
				p.cancel();
				// TODO Auto-generated method stub
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						getUserData();	
					}
				});
				
				super.updateUI();
			}
			
			
		};
		
		
		
		verification=new verification_api(AddPhotoOptionClass.this, db, p){
			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				p.cancel();
			if(verification_api.resCode==1){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						  HashMap<String, String>map=new HashMap<String, String>();
						     map.put("uuid", pref.get(Constants.KeyUUID));
						     map.put("auth_token", pref.get(Constants.kAuthT));
						     map.put("other_user_uuid","");
						     map.put("time_stamp", "");  
						     getUerprofile.setPostData(map);
						     callApi(getUerprofile);
					}
				});
			}
			else{
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//Utils.same_id("Ogbonge", verification_api.resMsg, AddPhotoOptionClass.this);
						
						same_idBack("Ogbonge", verification_api.resMsg);
						
					}
				});
				
			}
				
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
		};
		
		
		fbalbumAdapter=new fb_album_adapter(AddPhotoOptionClass.this,data) {
			
			@Override
			protected void onItemClick(View v, String string) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Log.d("arv", "arv  "+string);
				GetAlbumPhotoes(string);
			}
		};
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     getUerprofile.setPostData(map);
	     callApi(getUerprofile);
		
				loginToFacebook();
		
	}

	/**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {

		mPrefs = getSharedPreferences(Constants.SOCIAL_MEDIA, MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		
			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "user_photos","user_about_me","email" },
					new DialogListener() {

						@Override
						public void onCancel() {
						}

						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							Log.d("Arv", "Calling getProfileInformation");
							GetAlbumdata();
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
		else{
			GetAlbumdata();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					Log.d("Arvind Profile Data", ""+profile.toString());
					final String name = profile.getString("name");
					final String email = profile.getString("email");
					HashMap<String, String> hashMap = new HashMap<String, String>();

					hashMap.put("f_name", profile.getString("first_name"));

					hashMap.put("l_name", profile.getString("last_name"));

					hashMap.put("id", profile.getString("id"));

					hashMap.put("gender", profile.getString("first_name"));

					try {

					hashMap.put("email", profile.getString("first_name"));

					} catch (Exception e) {
					}
					try {

					hashMap.put("birthday", profile.getString("first_name"));

					} catch (Exception e) {
					}
					
					
				//	GetAlbumdata();
					
					
					Intent returnIntent = new Intent();
					returnIntent.putExtra("map", hashMap);
					setResult(RESULT_OK,returnIntent);
					finish();
										
									
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	

	
	private final class ResponseListener implements  org.brickred.socialauth.android.DialogListener 
	{
	   public void onComplete(Bundle values) {
	    
	      adapter.getUserProfileAsync(new ProfileDataListener());                   
	   }

	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onError(SocialAuthError arg0) {
		// TODO Auto-generated method stub
		
	}

	

	
	}

	private final class ProfileDataListener implements SocialAuthListener<Profile>{

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExecute(String arg0, Profile arg1) {
			// TODO Auto-generated method stub
			 Profile profileMap = arg1;
			  hashMap=new HashMap<String, String>();

			if(arg0.equalsIgnoreCase("facebook")){
				hashMap.put("type", "1");
				hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
				hashMap.put("auth_token", pref.get(Constants.kAuthT));
				hashMap.put("social_network_type", "facebook");
				hashMap.put("social_network_id", profileMap.getValidatedId());
				hashMap.put("verification_code", "");
				hashMap.put("phone_no", "");
				
				if(profileMap.getEmail()!=null){
					hashMap.put("email", ""+profileMap.getEmail());
				}
				else{
					hashMap.put("email", "");
				}
				
				
				hitAPI(hashMap);
				
				
			}
			
			
			}
		
	}
	private void hitAPI(HashMap<String, String> data){
		p.show();
		verification.setPostData(data);
		callApi(verification);
	}
	
	
	public void GetAlbumdata() {
		mAsyncRunner.request("me/albums", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					
					JsonParser p = new JsonParser();
					JsonElement jele = p.parse(json);
					
					JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject(): null;
					data.clear();
					JsonArray userdata = obj.get("data").getAsJsonArray();
					
			        for (int i = 0; i < userdata.size(); i++) {
			        	JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
			        	
			        	if(singleCountry.has("cover_photo")){
			        	
			        	HashMap<String, String> info=new HashMap<String, String>();
			           String url="https://graph.facebook.com/";
			          
			           info.put("id", singleCountry.get("id").getAsString());
			           info.put("cover_photo", singleCountry.get("cover_photo").getAsString()); 
			          
			           info.put("outh", mPrefs.getString("access_token", null)); 
			           if(singleCountry.has("name")){
			           info.put("name", singleCountry.get("name").getAsString());  
			           }
			           else{
			        	   info.put("name", "");  
			           }
			           
			           if(singleCountry.has("count")){
			           info.put("count", singleCountry.get("count").getAsString());  
			           }
			           else{
			        	   info.put("count", "");
			           }
			           info.put("url", url);    
			           data.add(info);
			        	}
			        }
			        
			        if(data.size()>0){
					AddPhotoOptionClass.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							 dialog = new Dialog(AddPhotoOptionClass.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.fb_album);
							album_grid=(GridView)dialog.findViewById(R.id.gridView1);
							album_grid.setAdapter(fbalbumAdapter);
							fbalbumAdapter.notifyDataSetChanged();
							
							dialog.show();
							}
							
						
					});
			        }
					else{
						AddPhotoOptionClass.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								mPrefs.edit().clear().commit();
								same_idBACK("Ogbonge", "No public Album found and facebook is not allowing to access private albums.", AddPhotoOptionClass.this);
								//Toast.makeText(AddPhotoOptionClass.this	, "No Photo Found", Toast.LENGTH_LONG);
							}
						});
						
					}
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void getUserData(){
		db.open();
		Cursor c=db.findCursor(Table.Name.user_master, Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		c.moveToFirst();
		verification_status=Integer.parseInt(c.getString(c.getColumnIndex(Table.user_master.verification_level.toString())));
		
	}
	
	

	

	public void GetAlbumPhotoes(String album_id) {
		mAsyncRunner.request(album_id+"/photos", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					
					JsonParser p = new JsonParser();
					JsonElement jele = p.parse(json);
					
					JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject(): null;
					
					JsonArray userdata = obj.get("data").getAsJsonArray();
					data.clear();
				    for (int i = 0; i < userdata.size(); i++) {
			        	JsonObject singleCountry=userdata.get(i).getAsJsonObject();	
			        	
			        	HashMap<String, String> info=new HashMap<String, String>();
			        	
			        	info.put("source", singleCountry.get("picture").getAsString());
			        	data.add(info);
			        	
			        	
			        	//JsonArray Imagedata = singleCountry.get("images").getAsJsonArray();
			        	
//			        	 for (int j = 0; i < Imagedata.size(); i++) {
//					        	JsonObject Image=userdata.get(j).getAsJsonObject();	
//					        	
//			        	 }
			        	
			        	
			        }
			        
					
					AddPhotoOptionClass.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog = new Dialog(AddPhotoOptionClass.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.fb_album);
							album_grid=(GridView)dialog.findViewById(R.id.gridView1);
							album_grid.setAdapter(fb_image_adapter);
							fb_image_adapter.notifyDataSetChanged();
							
							dialog.show();
						}
					});
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	protected void PostCurrentImage(String imagePost) {

		String upload_type = String.valueOf(uploadType);
		String type = "image/jpg";
		File file 	= new File(imagePost);
		String url 	=AddPhotoOptionClass.this.getString(R.string.urlString)+"api/uploadPhoto/index";				
		PostImage_AfterLogin callbackservice = new PostImage_AfterLogin(file, url, imagePost, pref.get(Constants.KeyUUID)/*, Feedid*/, type, upload_type, AddPhotoOptionClass.this) {
			
			@Override
			public void receiveData() {
				
				if(PostImage_AfterLogin.resCode == 1){					
					Log.d("uploadedddddd*********", "successfully");
					
					//p.cancel();
					//Utils.same_id("Message", PostImage_AfterLogin.resMsg, AddPhotoOptionClass.this);
					Utils.showToastOnMainThread(AddPhotoOptionClass.this, PostImage_AfterLogin.resMsg, Toast.LENGTH_LONG);
					if(fromFb==true && verification_status==0){
						
						adapter.authorize(AddPhotoOptionClass.this, Provider.FACEBOOK);
					}
					else{
						onBackPressed();
					}
					
					Log.e("", "postImage " + imageName);
				}
				else{
					//p.cancel();
					Utils.alert(AddPhotoOptionClass.this, PostImage_AfterLogin.resMsg);					
				}						
			}						
							
			@Override
			public void receiveError() {
				//p.cancel();
				AddPhotoOptionClass.this.runOnUiThread(new Runnable() {
									
					@Override
					public void run() {
							
						Utils.alert(AddPhotoOptionClass.this, "Error in uploading Image");
					}
				});
			}
		};
		callbackservice.execute(url, null, null);
	}	
		
	
	public class downloadImages extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			File mydir2=new File(Environment.getExternalStorageDirectory()+"/OgbongeDir/facebook_pics/");
			
			if(!mydir2.exists())
				mydir2.mkdirs();
			else
			    Log.d("error", "MyOgbongePhotos. already exists");
			
			for(int i=0;i<url.size();i++){
				
				Bitmap bt=getBitmap(url.get(i));
				
				saveToSdCard(bt,Environment.getExternalStorageDirectory()+"/OgbongeDir/facebook_pics/",String.valueOf(cal.getTimeInMillis()));
				
			}
			
			
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			String path = Environment.getExternalStorageDirectory()+"/OgbongeDir/facebook_pics/";
			File f = new File(path);        
			File file[] = f.listFiles();
			
			
			for(int i=0;i<file.length;i++){
				
			
				String tt=file[i].getPath();
				Log.d("arv", "arv  "+tt);
				PostCurrentImage(tt);
				
			}
			dialog.dismiss();
			fromFb=true;
			
	//arvind		
			//FileUtils.cleanDirectory(path); 
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			
			
			
			super.onProgressUpdate(values);
			
			
		}
		
		
	}
	
	public void same_id(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AddPhotoOptionClass.this);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.cancel();
						// UpdateProfile.this.finish();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	public void same_idBack(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AddPhotoOptionClass.this);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.cancel();
						onBackPressed();
						// UpdateProfile.this.finish();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	
	
	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(this)) {
			Log.v("Internet Not Conneted", "");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					p.cancel();
					same_id("Error", getString(R.string.no_internet));
				}
			});
			return;
		} else {
			Log.v("Internet Conneted", "");
		}

		Thread t = new Thread(r);
		t.setName(r.getClass().getName());
		t.start();

	}
	
	
	void purgeDirectory(File dir) {
		if(dir.length()>0){
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
		}
	}
	
	public static String saveToSdCard(Bitmap bitmap, String path,String name) {

	    String stored = null;

//	    File sdcard = Environment.getExternalStorageDirectory() ; 

	//    File folder = new File(sdcard.getAbsoluteFile(), ".your_specific_directory");//the dot makes this directory hidden to the user
	 //   folder.mkdir(); 
	    File file = new File(path, name + ".jpg") ;
	    if (file.exists())
	        return stored ;

	    try {
	        FileOutputStream out = new FileOutputStream(file);
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	        out.flush();
	        out.close();
	        stored = "success";
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	     return stored;
	   }
	
	
	private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
  
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
  
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }
	
	private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
  
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
  
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
	
	
	public void showAccessTokens() {
		String access_token = facebook.getAccessToken();

		Toast.makeText(getApplicationContext(),
				"Access Token: " + access_token, Toast.LENGTH_LONG).show();
	}
	
public void same_idBACK(String title, String message, Context ctx) {
		
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        dialog.setContentView(R.layout.alert_dialog);
       TextView title_msg=(TextView)dialog.findViewById(R.id.title);
       TextView msg=(TextView)dialog.findViewById(R.id.message);
		Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
		
		
		ok_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				AddPhotoOptionClass.this.onBackPressed();	
			}
		});
		
		title_msg.setText(title);
		msg.setText(message);
		//title_msg.setText("Confirmation..");
		//String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
		//msg.setText(str);
        dialog.show();

	}
}
