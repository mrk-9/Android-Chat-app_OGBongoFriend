package com.ogbongefriends.com.Login;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.CustomLoader;

public class facebookVerification extends Activity {


private static String APP_ID = "1509359755975991";
private SensorManager senSensorManager;

	//private static String APP_ID = "750560845021720";

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	private GoogleApiClient mGoogleApiClient;
	public static CustomLoader p;
	private ConnectionResult mConnectionResult;
	private Sensor senAccelerometer;
	private static SharedPreferences mSharedPreferences;
	private static final String PREF_NAME = "sample_twitter_pref";
	private  ProgressDialog progressDialog;
	private boolean mIntentInProgress;
	 private static final int RC_SIGN_IN = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin_signup);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
					 
Bundle b=getIntent().getExtras();
				loginToFacebook();
		
	}

	/**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
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
					new String[] { "user_photos","user_about_me","email","publish_actions","user_birthday"},
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
							
							
							
							getProfileInformation();
						
							//postImageonWall(mPrefs.getString("access_token", null));
							
//							if(Integer.parseInt(getIntent().getExtras().getString("from"))==1){
							//postOnWall("testing arvind");
//							}
//							else{
//								getProfileInformation();
//							}
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
			//postOnWall("testing arvind");
//			if(Integer.parseInt(getIntent().getExtras().getString("from"))==1){
//				postOnWall("testing arvind");
//			}
//			else{
				getProfileInformation();
//			}
			
			//postImageonWall(mPrefs.getString("access_token", null));
			//
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		

		
		
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	
	
	public void postOnWall(String msg) {
        Log.d("Tests", "Testing graph API wall post");
         try {
                String response ="";
                Bundle parameters = new Bundle();
                parameters.putString("message", msg);
                parameters.putString("description", "test test test");
                 mAsyncRunner.request("me/feed", parameters, 
                        new RequestListener() {
							
							@Override
							public void onMalformedURLException(MalformedURLException e, Object state) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onIOException(IOException e, Object state) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFileNotFoundException(FileNotFoundException e, Object state) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFacebookError(FacebookError e, Object state) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete(String response, Object state) {
								// TODO Auto-generated method stub
								String json = response;
								Log.d("arv", "arv"+json);
								
							}
						});
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
                }
         } catch(Exception e) {
             e.printStackTrace();
         }
    }
	
	
	
	

	private void postImageonWall(String token) {
	    byte[] data = null;

	    Bitmap bi = BitmapFactory.decodeResource(getResources(),
	            R.drawable.ic_launcher);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	    data = baos.toByteArray();
	    Bundle params = new Bundle();
	    params.putString(Facebook.TOKEN, token);
	    params.putString("method", "photos.upload");
	    params.putByteArray("picture", data); // image to post
	    params.putString("caption", "My text on wall with Image "); // text to post
	    AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	    mAsyncRunner.request(null, params, "POST", new SampleUploadListener(),
	            null);
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

					hashMap.put("first_name", profile.getString("first_name"));

					hashMap.put("last_name", profile.getString("last_name"));

					hashMap.put("id", profile.getString("id"));

					hashMap.put("gender", profile.getString("gender"));

					try {

					hashMap.put("email", profile.getString("email"));
				
					} catch (Exception e) {
					}
					try {

					hashMap.put("birthday", profile.getString("birthday"));

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
	
	
	
	public void GetAlbumdata() {
		mAsyncRunner.request("me/albums", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					Log.d("Arvind Profile Data", ""+profile.toString());
									
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
	
	
	
	
	public void showAccessTokens() {
		String access_token = facebook.getAccessToken();

		Toast.makeText(getApplicationContext(),
				"Access Token: " + access_token, Toast.LENGTH_LONG).show();
	}
	
	
	class SampleUploadListener implements AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
			Log.d("arvi", "arvi1111");
		}

		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			Log.d("arvi", "arvi2222");
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub
			Log.d("arvi", "arvi3333");
		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub
			Log.d("arvi", "arvi4444");
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			Log.d("arvi", "arvi444");
		}
	}
}
