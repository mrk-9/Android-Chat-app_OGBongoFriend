package com.ogbongefriends.com.ogbonge.breakingnews;

import java.io.ByteArrayOutputStream;
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
import java.util.HashMap;

import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.WebViewActivity;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.Login.FacebookSignUp;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.FileCache;

public class NewsDetails extends Fragment{

	private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
		private LocationHelper mLocationHelper;
	
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_KEY_FACEBOOK_LOGIN = "is_facebook_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";
	FileCache fileCache;
	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;

	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private ProgressDialog pDialog;

	private static Twitter twitter;
	private static RequestToken requestToken;
	
	private static SharedPreferences mSharedPreferences;

	private static String APP_ID = "1509359755975991";


	//private static String APP_ID = "750560845021720";
		
	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	
	private DB db;
	private CustomLoader p;
	private ImageLoader imageLoader;
	Notification nt;
	int count = 0;
private Context _ctx;	
  public HashMap<String, String> urls;
  private JsonObject _jsondata;
  private int event_id;
  private VideoView myVideo;
  private ProgressBar pb;
  private static Preferences pref;
  private View rootView;
  
  private TextView date,title,description,postedby;
  private ImageView news_image,share_facebook,share_twitter;
  private DisplayImageOptions options;
  private ProgressDialog progDailog;
  private ProgressBar userImageProgress;
  private String url=getActivity().getString(R.string.urlString)+"userdata/news_media/",path="";
  private ArrayList<HashMap<String, String>> commentData;
	public NewsDetails(){

	}
	public NewsDetails(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
			pref=new Preferences(_ctx);
			initTwitterConfigs();
			fileCache=new FileCache(_ctx);
			rootView = inflater.inflate(R.layout.news_detail, container, false);
			_jsondata=new JsonObject();
			mSharedPreferences = _ctx.getSharedPreferences(PREF_NAME, 0);
			postedby=(TextView)rootView.findViewById(R.id.postedby);
			news_image=(ImageView)rootView.findViewById(R.id.userImage);
			date=(TextView)rootView.findViewById(R.id.date);
			userImageProgress=(ProgressBar)rootView.findViewById(R.id.userImageProgress);
			title=(TextView)rootView.findViewById(R.id.title);
			description=(TextView)rootView.findViewById(R.id.description);
			myVideo=(VideoView)rootView.findViewById(R.id.myVideo);
			share_facebook=(ImageView)rootView.findViewById(R.id.share_facebook);
			share_twitter=(ImageView)rootView.findViewById(R.id.share_twitter);
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//p.show();
	
		
	
		
		 File cacheDir = StorageUtils.getCacheDirectory(_ctx);
	     options = new DisplayImageOptions.Builder()
	        .resetViewBeforeLoading(false)  // default
	        .delayBeforeLoading(1000)
	        .cacheInMemory(true) // default
	        .cacheOnDisk(true) // default
	        .considerExifParams(false) // default
	        .build();
	  
	  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(_ctx)
				
				
				
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(26) // default
		        .diskCacheSize(1000 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .imageDownloader(new BaseImageDownloader(_ctx)) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(options) // default
		        .writeDebugLogs()
		        .build();
				
	  share_facebook.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAsyncRunner = new AsyncFacebookRunner(facebook);

			//loginToFacebook();
			
			
			Intent inte=new Intent(getActivity(), FacebookSignUp.class);
			Bundle b=new Bundle();
			b.putInt("from", 1);
			inte.putExtras(b);
			startActivity(inte);
		
		}
	});
			
	  
	
	  
	  
	  share_twitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				//File tmpFile = new File(p);
//				 final String photoUri = MediaStore.Images.Media.insertImage(getActivity().
//				         getContentResolver(), tmpFile.getAbsolutePath(), null, null);
//
//				 Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
//				         .setText("Hello from Google+!")
//				         .setType("image/jpeg")
//				         .setStream(Uri.parse(photoUri))
//				         .getIntent()
//				         .setPackage("com.google.android.apps.plus");
				
				
				// Invoke the ACTION_SEND intent to share to Google+ with attribution.
//				Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
//				                                .setText("Ogbonge Friendssss")
//				                                .setType("text/plain")
//				                                .setStream(Uri.parse(path))
//				                                .getIntent()
//				                               
//				                                .setPackage("com.google.android.apps.plus");
//				    startActivity(shareIntent);
				
				
				
				boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
				if (isLoggedIn) {
					new updateTwitterStatus().execute(title.getText().toString()+"  "+"http://www.ogbongefriends.com/dashboard/news");
					
				}
				else{
					loginToTwitter();	
		
				}
				
			}
		});
				
				
				ImageLoader.getInstance().init(config);
					
				 imageLoader = ImageLoader.getInstance();
				 event_id=pref.getInt("clicked_event_id");
	 
	getdata();
	return 	rootView;
		

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
	
	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void loginToFacebook() {

		String access_token = mSharedPreferences.getString("access_token", null);
		long expires = mSharedPreferences.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		
			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(getActivity(),
					new String[] { "user_photos","user_about_me","email" },
					new DialogListener() {

						@Override
						public void onCancel() {
						}

						@Override
						public void onComplete(Bundle values) {
							
							Editor e = mSharedPreferences.edit();

							// After getting access token, access token secret
							// store them in application preferences
							e.putString("access_token",
									facebook.getAccessToken());
							e.putLong("access_expires",
									facebook.getAccessExpires());
							// Store login status - true
							e.putBoolean(PREF_KEY_FACEBOOK_LOGIN, true);
							e.commit(); // save changes
							
//							SharedPreferences.Editor editor = mPrefs.edit();
//							editor.putString("access_token",
//									facebook.getAccessToken());
//							editor.putLong("access_expires",
//									facebook.getAccessExpires());
//							editor.commit();
//							Log.d("Arv", "Calling getProfileInformation");
							postImageonWall(mSharedPreferences.getString("access_token", null));
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
			postImageonWall(mSharedPreferences.getString("access_token", null));
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
	
	public void getdata(){

		db.open();
		Cursor data=db.findCursor(Table.Name.news_master, Table.news_master.id.toString()+" = "+event_id, null, null);
		Log.d("arv", ""+data.getCount());
		data.moveToFirst();
	
		
	//	date=(TextView)rootView.findViewById(R.id.date);
		title.setText(data.getString(data.getColumnIndex(Table.news_master.news_title.toString())));
		
		//description.setText(data.getString(data.getColumnIndex(Table.news_master.news_description.toString())));
		
		//description.setText(Html.fromHtml(data.getString(data.getColumnIndex(Table.news_master.news_description.toString()))));
		
		description.setText(Html.fromHtml(data.getString(data.getColumnIndex(Table.news_master.news_short_description.toString())), new URLImageParser(description, _ctx), null));
		
		date.setText("Posted: "+Utils.getDateFromSecond(Long.parseLong(data.getString(data.getColumnIndex(Table.news_master.add_date.toString()))), "MM-dd-yyyy,HH:mm"));
		postedby.setText("Posted By: "+data.getString(data.getColumnIndex(Table.news_master.posted_by.toString())));
		data=db.findCursor(Table.Name.news_media, Table.news_media.news_master_id.toString()+" = "+event_id, null, null);
		data.moveToFirst();
		if(data.getString(data.getColumnIndex(Table.news_media.media_type.toString())).equalsIgnoreCase("1")){
			news_image.setVisibility(View.VISIBLE);
			myVideo.setVisibility(View.GONE);
			path=url+data.getString(data.getColumnIndex(Table.news_media.news_image.toString()));
			imageLoader.displayImage(path, news_image, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					userImageProgress.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					userImageProgress.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					userImageProgress.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					userImageProgress.setVisibility(View.GONE);
				}
			});
			
		}
		else{
			userImageProgress.setVisibility(View.GONE);
			news_image.setVisibility(View.GONE);
			myVideo.setVisibility(View.VISIBLE);	
			String uu="http://download.itcuties.com/teaser/itcuties-teaser-480.mp4";
			//myVideo.setVideoURI(Uri.parse(url+data.getColumnIndex(Table.news_media.news_video.toString())));
			myVideo.setVideoURI(Uri.parse(uu));
			
			myVideo.requestFocus();
			myVideo.start();
			
			 progDailog = ProgressDialog.show(_ctx, "Please wait ...", "Retrieving data ...", true);

			 myVideo.setOnPreparedListener(new OnPreparedListener() {

		            public void onPrepared(MediaPlayer mp) {
		                // TODO Auto-generated method stub
		                progDailog.dismiss();
		            }
		        });
		}
		
		//imageLoader.displayImage(data.getString(data.getColumnIndex(Table.news_master.)), imageAware, listener)
	
	
	}

	private void loginToTwitter() {
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		
		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
			new	getAuth().execute("test");
				

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			//loginLayout.setVisibility(View.GONE);
			//shareLayout.setVisibility(View.VISIBLE);
		}
	}
	
	
	class getAuth extends AsyncTask<String, String, Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			final Intent intent = new Intent(_ctx, WebViewActivity.class);
			intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
			startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
			super.onPostExecute(result);
		}
		
		
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			
			try {
				
				Thread thread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                    	String verifier = data.getExtras().getString(oAuthVerifier);
	                    	AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

	        				long userID = accessToken.getUserId();
	        				final User user = twitter.showUser(userID);
	        				String username = user.getName();
	        				
	        				saveTwitterInfo(accessToken);
	                    	
	                    }
	                    catch(Exception e){
	                    	
	                    }
	                }
				});
				thread.start();

				//loginLayout.setVisibility(View.GONE);
				//shareLayout.setVisibility(View.VISIBLE);
			//	userName.setText(MainActivity.this.getResources().getString(R.string.hello) + username);

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
private void saveTwitterInfo(AccessToken accessToken) {
		
		long userID = accessToken.getUserId();
		
		User user;
		try {
			user = twitter.showUser(userID);
		
			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();
	
		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
	}




class updateTwitterStatus extends AsyncTask<String, String, Void> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		pDialog = new ProgressDialog(_ctx);
		pDialog.setMessage("Sharing to twitter...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	protected Void doInBackground(String... args) {

		String status = args[0];
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);
			
			// Access Token
			String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
			// Access Token Secret
			String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

			AccessToken accessToken = new AccessToken(access_token, access_token_secret);
			Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

			// Update status
			StatusUpdate statusUpdate = new StatusUpdate(status);
			
			//InputStream is = getResources().openRawResource(R.drawable.splash_new);
			InputStream is = (InputStream) new URL(path).getContent();
			statusUpdate.setMedia("test.jpg", is);
			
			twitter4j.Status response = twitter.updateStatus(statusUpdate);

			Log.d("Status", response.getText());
			
		} catch (TwitterException e) {
			Log.d("Failed to Share!", e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		/* Dismiss the progress dialog after sharing */
		pDialog.dismiss();
		
		Toast.makeText(_ctx, "Shared to Twitter!", Toast.LENGTH_SHORT).show();

		// Clearing EditText field
		//mShareEditText.setText("");
	}

}
	
	
 class SampleUploadListener implements AsyncFacebookRunner.RequestListener {

	@Override
	public void onComplete(String response, Object state) {
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
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		
	}

   

}

}




