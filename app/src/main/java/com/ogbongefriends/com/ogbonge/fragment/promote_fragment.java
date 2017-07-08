package com.ogbongefriends.com.ogbonge.fragment;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.ogbonge.profile.updateProfileApi;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.confirm_pointsApi;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.api.getTransactionApi;
import com.ogbongefriends.com.api.sendFriendRequestApi;
import com.ogbongefriends.com.api.settingAccountApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.ImageAdapter;
import com.ogbongefriends.com.common.PostImage_AfterLogin;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

public class promote_fragment extends Fragment implements Runnable,OnClickListener,MyLocationListener {

	// private ListView listView;
	private ArrayList<HashMap<String, String>> feedData;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	Cursor data;
	private String imageName="";
	private long destination;
	private int userPoints=0;
	private user_profile_api user_profile_info;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	public final int SELECT_FILE=100;
	Cursor secfollowingdatacorsor;
	public Integer[] Imgid = {R.drawable.app_icon, R.drawable.add_me_here,
	        R.drawable.blue_favourite, R.drawable.blue_like};
	ArrayList<HashMap<String, String>> followerdata;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	Cursor placedatacursor;
	View rootView;
	Notification nt;
	int count = 0;
	private settingAccountApi setting_account_api;
	public static CustomLoader p;
	
	private ImageLoader imageLoader;
	private Preferences pref;
	private Location location=null;
	private getAllUserProfileAPI get_all_user_pfro_api;
	private sendFriendRequestApi sendFriendRequest;
	private Context _ctx;
	private getAlbumApi get_album_api;
	private updateProfileApi update_profile;
	private RelativeLayout grab_attention_layout;
	private JsonArray photos_of_you;
	private ImageAdapter imageAdapter;
	Animation anim;
	private static String user_id;
    Handler handler;
    private DB db;
    ImageView iv,prof,addProf;
    int countUrl;
    public TextView dynamic_data;
    private long days=0,hours=0,mins=0,secs=0,mseconds=0;
    private String advertise_long,points;
    private TextView advertise_time;
    private Calendar c;
    private LinearLayout add_image,advertise_time_parent;
    private Button addme_button;
    private getTransactionApi get_transaction_api;
    confirm_pointsApi confirm_points_api;
	public JsonObject _jsondata;
    Runnable galleryAnimRunnable;
	private ArrayList<String>GiftUrls=new ArrayList<String>();
	public promote_fragment(Context ctx) {
_ctx=ctx;
	}
	
	public promote_fragment() {
			}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.promote_fragment, container, false);
		addme_button=(Button)rootView.findViewById(R.id.button1);
		prof=(ImageView)rootView.findViewById(R.id.prof);
		advertise_time=(TextView)rootView.findViewById(R.id.advertise_time);
		add_image=(LinearLayout)rootView.findViewById(R.id.add_image);
		advertise_time_parent=(LinearLayout)rootView.findViewById(R.id.advertise_time_parent);
		dynamic_data=(TextView)rootView.findViewById(R.id.trm_con);
		addProf=(ImageView)rootView.findViewById(R.id.addProf);
		
ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
		
		
        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
        .diskCacheExtraOptions(480, 800, null)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCacheSize(2 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .imageDownloader(new BaseImageDownloader(getActivity())) // default
        .imageDecoder(new BaseImageDecoder(false)) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
        .writeDebugLogs()
        .build();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(false) // default
        .cacheOnDisk(false) // default
        .considerExifParams(false) // default
        .build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
		
		 c = Calendar.getInstance(); 
		pref=new Preferences(_ctx);
		 p= DrawerActivity.p;
		 db=new DB(_ctx);
		 
				 user_id=pref.get(Constants.KeyUUID);

				 
	
				 
				 get_transaction_api=new getTransactionApi(_ctx, db, p){
					@Override
					protected void onError(Exception e) {
						// TODO Auto-generated method stub
						super.onError(e);
					}

					@Override
					protected void onDone() {
						// TODO Auto-generated method stub
						super.onDone();
						if(get_transaction_api.resCode==1){
							getUpdatedUserProfile();
						
						}
						else{
						}
					}

					@Override
					protected void onResponseReceived(InputStream is) {
						// TODO Auto-generated method stub
						super.onResponseReceived(is);
					}

					@Override
					protected void updateUI() {
						// TODO Auto-generated method stub
						super.updateUI();
					
					}
					
				};
		 
		 addme_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPointInfo();
			}
		});
		 
		addProf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				startActivityForResult(
						Intent.createChooser(intent, "Select File"),
						SELECT_FILE);
			}
		});
		
		 update_profile=new updateProfileApi(getActivity(), db, p){

				@Override
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
				}

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					p.cancel();
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
					p.cancel();
					Toast.makeText(_ctx, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
					//Utils.same_id("Message", "Profile Updated Successfully", getActivity());
					Utils.same_id("Message", "Profile Picture Updated Successfully", getActivity());
					
					
					
				//getActivity().onBackPressed();
				}
			 };
			 
			 setting_account_api=new settingAccountApi(_ctx, db, p){

					@Override
					protected void onResponseReceived(InputStream is) {
						// TODO Auto-generated method stub
						super.onResponseReceived(is);
					}

					@Override
					protected void onDone() {
						// TODO Auto-generated method stub
						super.onDone();
						db.open();
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								data=db.findCursor(DB.Table.Name.setting_master, DB.Table.setting_master.id.toString()+" = 1", null, null);
								 if(data.getCount()>0){
									data.moveToFirst();
								points=	data.getString(data.getColumnIndex(DB.Table.setting_master.advertise_me_value.toString()));
									
								 }
							}
						});
						
						 getUserProfileFromApi();
					}

					@Override
					protected void updateUI() {
						// TODO Auto-generated method stub
						super.updateUI();
					}
					 
				 };
				 p.show();
			callApi(setting_account_api);
			
		return rootView;
	}

	
	
	private void init(){
		
		 db.open();
			data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+user_id+"'", null, null);
			Log.d("arv", "arv "+data.getCount());
			data.moveToFirst();
			
			
			 mseconds = c.getTimeInMillis()/1000;
			 userPoints=Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_master.points.toString())));
			 if(data.getString(data
						.getColumnIndex(DB.Table.user_master.advertise_me_expiry_time.toString())).length()>3){
			 destination=Long.parseLong(data.getString(data
						.getColumnIndex(DB.Table.user_master.advertise_me_expiry_time.toString())));
			 
				advertise_long=String.valueOf(Long.parseLong(data.getString(data
						.getColumnIndex(DB.Table.user_master.advertise_me_expiry_time.toString())))-mseconds);
				
				
			if(Long.parseLong(advertise_long)>0){
				CounterClass timer = new CounterClass(Long.parseLong(advertise_long)*1000,1000);
				timer.start();
				advertise_time_parent.setVisibility(View.VISIBLE);
				dynamic_data.setVisibility(View.GONE);
				add_image.setVisibility(View.GONE);
			//	advertise_time.setText(""+((((Long.parseLong(advertise_long) / 60) / 60) / 24)  % 7)+"day,"+" "+(((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) / 60) % 24)+"hours, "+((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) % 60)+"mins ");
				
			}
			else{
				advertise_time_parent.setVisibility(View.GONE);
				dynamic_data.setVisibility(View.VISIBLE);
				add_image.setVisibility(View.VISIBLE);
				
			}
			 }
			 else{
				 advertise_time_parent.setVisibility(View.GONE);
					dynamic_data.setVisibility(View.VISIBLE);
					add_image.setVisibility(View.VISIBLE);
			 }
			db.close();
	}
	
	private void getUserProfileFromApi(){
		 p.show();
	     user_profile_info=new user_profile_api(_ctx, db, p){
			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
//			getActivity().runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					p.cancel();
//					init();
//					
//				}
//			});
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				init();
				showUserData();
			}
	     };
	     
	     
	     HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     user_profile_info.setPostData(map);
	     callApi(user_profile_info);
	}
	
	
	private void getUpdatedUserProfile(){
		 p.show();
	     user_profile_info=new user_profile_api(_ctx, db, p){
			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				
//			getActivity().runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					p.cancel();
//					init();
//					
//				}
//			});
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				
				updatDrawerData();
				
			}
	     };
	     
	     
	     HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     user_profile_info.setPostData(map);
	     callApi(user_profile_info);
	}
	
	
	private void updatDrawerData(){
		db.open();
		data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+user_id+"'", null, null);
		Log.d("arv", "arv "+data.
				getCount());
		if(data.getCount()>0){
		data.moveToFirst();
	String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
	imageLoader.displayImage(url, DrawerActivity.userPhoto);

	points=	data.getString(data.getColumnIndex(DB.Table.user_master.points.toString()));
		DrawerActivity.dashboard_points.setText(points);
		
		db.close();
		p.cancel();
		}
		((DrawerActivity) getActivity()).displayView(5);
	}
	
	private void showUserData(){
		db.open();
		data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+user_id+"'", null, null);
		Log.d("arv", "arv "+data.getCount());
		data.moveToFirst();
	String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
	data=db.findCursor(DB.Table.Name.setting_master, DB.Table.setting_master.id.toString()+" = 1",null,null);
	data.moveToFirst();
points=	data.getString(data.getColumnIndex(DB.Table.setting_master.advertise_me_value.toString()));
		imageLoader.displayImage(url, prof);
		
		dynamic_data.setText(data.getString(data.getColumnIndex(DB.Table.setting_master.advertise_me_content.toString())));
		db.close();
		p.cancel();
	}
	
	
private void setData(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		data.put("profile_pic", PostImage_AfterLogin.imageName);
		data.put("type", "1");
		hitAPI(data);
	}

private void callPropmtForPass(){
	
	
	
	
	final Dialog dialog = new Dialog(_ctx);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    dialog.setContentView(R.layout.confirmation_dialog);
   TextView title=(TextView)dialog.findViewById(R.id.title);
   TextView msg=(TextView)dialog.findViewById(R.id.message);
	Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
	ok_btn.setText("Continue");
	Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
	cancel_btn.setText("Skip");
	title.setText(getString(R.string.app_name));
	String str="To Advertise yourself "+points+" points will be deducted from your account.";
	msg.setText(str);
    dialog.show();
    cancel_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			getActivity().onBackPressed();
		}
	});
    
    ok_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 dialog.dismiss();
			 HashMap<String, String>map=new HashMap<String, String>();
		     map.put("uuid", pref.get(Constants.KeyUUID));
		     map.put("auth_token", pref.get(Constants.kAuthT));
		     map.put("other_uuid","");  
		     map.put("time_stamp",""); 
		     map.put("type","2"); 
		    
		     p.show();
		     get_transaction_api.setPostData(map);
		     callApi(get_transaction_api);
			}
			
		
	});
	
		// show it
    dialog.show();

	
	
		
}



private void getPointInfo() {
	// TODO Auto-generated method stub
	
	
	
	confirm_points_api=new confirm_pointsApi(_ctx, db, p){

		@Override
		protected void onError(Exception e) {
			// TODO Auto-generated method stub
			super.onError(e);
		}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
			
			getActivity().runOnUiThread(new Runnable() {
				  public void run() {
					  showPrompt(_jsondata);
				  }

				private void showPrompt(JsonObject jsn) {
					// TODO Auto-generated method stub
					

					Log.d("arv", "arv"+jsn.get("points").getAsString()+"  "+jsn.get("item_points").getAsString());
					userPoints=Integer.parseInt(jsn.get("points").getAsString());
					
					if(userPoints>Integer.parseInt(points)){ 
						callPropmtForPass();
				     }
					
					else{
						
						
						
						final Dialog dialog = new Dialog(_ctx);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			            dialog.setContentView(R.layout.confirmation_dialog);
			           TextView title=(TextView)dialog.findViewById(R.id.title);
			           TextView msg=(TextView)dialog.findViewById(R.id.message);
						Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
						Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
						title.setText("OgbongeFriends");
						String str="You do not have sufficient points in your account. Would you like to purchase more points ?.";
						
						msg.setText(str);
			            dialog.show();
			            cancel_btn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
			            
			            ok_btn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								((DrawerActivity) getActivity()).displayView(11);
							    
							}
						});
						
							// show it
			            dialog.show();
						
							}
					

				
					
				}
				});
			
			
		}

		@Override
		protected void onResponseReceived(InputStream is) {
			// TODO Auto-generated method stub
			super.onResponseReceived(is);
			
			_jsondata=confirm_pointsApi.jsondata;
			
			
			
			
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			super.updateUI();
		
		}
		
	};
	
	
	 HashMap<String, String>map=new HashMap<String, String>();
     map.put("uuid", pref.get(Constants.KeyUUID));
     map.put("auth_token", pref.get(Constants.kAuthT));
     map.put("other_uuid","");  
     map.put("time_stamp",""); 
     map.put("type","2"); 
    
   
     confirm_points_api.setPostData(map);
     callApi(confirm_points_api);
	

	
	
}

private void callApi(Runnable r) {

	if (!Utils.isNetworkConnectedMainThred(getActivity())) {
		Log.v("Internet Not Conneted", "");
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Thread.currentThread().setPriority(1);
				p.cancel();
				Utils.same_id("Error", getString(R.string.no_internet),
						getActivity());
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



@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	 
	
}

public void hitAPI(final HashMap<String, String> map) {

	p.show();
	update_profile.setPostData(map);
	callApi(update_profile);

}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
				
			 if (requestCode == SELECT_FILE) {
				try{
				Uri selectedImageUri = data.getData();
			//	p.show();
				String tempPath = CelUtils.getpath(selectedImageUri, getActivity());
				Log.d("Image Path Select", ""+tempPath);
				String[] Img_char=tempPath.split("/");
				//imageName=Img_char[Img_char.length-1];
				 String[] Img_size = { MediaStore.Images.Media.SIZE};
				 p.show();
				 PostCurrentImage(tempPath);
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				prof.setImageBitmap(bm);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	protected void PostCurrentImage(String imagePost) {

		String upload_type = String.valueOf(1);
		String type = "image/jpg";
		File file 	= new File(imagePost);
		String url 	=getActivity().getString(R.string.urlString)+"api/uploadPhoto/index";
		
PostImage_AfterLogin callbackservice = new PostImage_AfterLogin(file, url, imagePost, pref.get(Constants.KeyUUID)/*, Feedid*/, type, upload_type, getActivity()) {
			
			@Override
			public void receiveData() {
				
				if(PostImage_AfterLogin.resCode == 1){					
					Log.d("uploadedddddd*********", "successfully");
					imageName=PostImage_AfterLogin.imageName;
					  setData();
					//p.cancel();
				
					Log.e("", "postImage " + imageName);
				}
				else{
					p.cancel();
					Utils.alert(getActivity(), PostImage_AfterLogin.resMsg);					
				}						
			}						
							
			@Override
			public void receiveError() {
				p.cancel();
				getActivity().runOnUiThread(new Runnable() {
									
					@Override
					public void run() {
							
						Utils.alert(getActivity(), "Error in uploading Image");
					}
				});
			}
		};
		
		callbackservice.execute(url, null, null);
	}
	
	public class CounterClass extends CountDownTimer {

		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			advertise_time_parent.setVisibility(View.GONE);
			dynamic_data.setVisibility(View.VISIBLE);
			add_image.setVisibility(View.VISIBLE);
			
		}

		@Override
		public void onTick(long millisUntilFinished) {

			long millis = millisUntilFinished;
			String hms = String.format("%02d: %02d: %02d: %02d", TimeUnit.MILLISECONDS.toDays(millis) ,TimeUnit.MILLISECONDS.toHours(millis)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
					TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
			advertise_time.setText(hms);
		}
	}
	
}