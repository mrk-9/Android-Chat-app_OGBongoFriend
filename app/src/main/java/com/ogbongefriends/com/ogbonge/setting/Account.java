package com.ogbongefriends.com.ogbonge.setting;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.SharedPreferences;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.Login.LoginActivity;
import com.ogbongefriends.com.Splash;
import com.ogbongefriends.com.api.UpdateNotificationApi;
import com.ogbongefriends.com.api.getNotificationApi;
import com.ogbongefriends.com.api.logOut;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class Account extends Fragment implements Runnable {

	// private ListView listView;
	private ArrayList<HashMap<String, String>> feedData;
	private ArrayList<HashMap<String, String>> placeData;
	// private EventAdapter eventAdapter;
	private ArrayList<HashMap<String, String>> eventsData;

	@SuppressWarnings("unused")
	private EditText posttetx,emailText;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;

	private Uri imageUri;

	private Uri selectedImage;
//	private SharedPreferences pref;

	private long str;
	
	boolean bool=true;

	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;

	private ImageView attchment_img;
	private Button remave_img_btn;
	private ImageView profileImage;
	private ImageView dialogProfileImage;

	private Button showPlaceBtn;
	private Button showEventBtn;
//	private Editor edit;
	private long other_user;
	public SharedPreferences mPrefs;
	private long loginId;

	private CheckBox chfollow;
	private CheckBox chsecretfollow;
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	// EventAdapter showfollowerlist;
	ArrayList<HashMap<String, String>> followerdata;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	Cursor placedatacursor;
	private String followStatus;
//	private String followintType;
	View rootView;
	private String imgname;
	private TextView notificationText;
	private TextView chatText;
	private TextView checkInText;
	private Button notificationBtn;
	private Button save;
	private Button checkInBtn;
	private Preferences pref;
	Notification nt;
	private Button logout;
	// getEventApi geteventapi;
	int count = 0;
	private logOut log_out_api;
	private Context _ctx;
	public static DB db;
	public static CustomLoader p;
	private Button change_password;
	private Button hide_account;
private UpdateNotificationApi updateNotificationApi;
private com.ogbongefriends.com.api.getNotificationApi getNotificationApi;
private SocialAuthAdapter adapter;

	public Account(){

	}
	public Account(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		pref= new Preferences(getActivity());
		rootView = inflater.inflate(R.layout.email_notification_setting, container, false);
		logout=(Button)rootView.findViewById(R.id.sign_out);
		change_password=(Button)rootView.findViewById(R.id.change_pass);
		emailText=(EditText)rootView.findViewById(R.id.emailText);
		hide_account=(Button)rootView.findViewById(R.id.hideAccount);
		mPrefs=getActivity().getSharedPreferences(Constants.SOCIAL_MEDIA, getActivity().MODE_PRIVATE);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		db=new DB(_ctx);
		db.open();
		adapter = new SocialAuthAdapter(new ResponseListener());
		data = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'",null, null);
		Log.d("data sizee", ""+data.getCount());
			data.moveToFirst();
			emailText.setText(data.getString(data.getColumnIndex(DB.Table.user_master.email.toString())));
		p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
		// ============= manage image height================
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogoutAlert();
			}
		});
		fragmentManager = getFragmentManager();
		
		change_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(43);
			}
		});
	
		hide_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					UpdateNotification();
			//		hide_account.setText("Make Account Visible");
				
			}
		});
		
			log_out_api=new logOut(_ctx, db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}
			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				pref.set(Constants.KeyUUID, "");
				pref.set(Constants.kAuthT, "");
				pref.set(Constants.TempKeyUUID, "");
				pref.set(Constants.TempkAuthT, "");

				storeRegistrationId(getActivity(),"");


				pref.set("client_id", "");

				pref.set("wio_interestedin_purpose_master_id", 0);
				pref.set("wio_interestedin_master_id",0 );
				pref.set("wio_meet_min_age", 0);
				pref.set("wio_meet_max_age", 0);
				pref.commit();

				Utils.endSession(_ctx);
				pref.commit();
				p.cancel();
				//adapter.signOut(_ctx, Provider.FACEBOOK);
			//	adapter.signOut(_ctx, String.valueOf(Provider.FACEBOOK));
			//	adapter.signOut(_ctx, String.valueOf(Provider.YAHOO));
			//	adapter.signOut(_ctx, String.valueOf(Provider.TWITTER));
			//	adapter.signOut(_ctx, String.valueOf(Provider.LINKEDIN));
				
				mPrefs.edit().clear().commit();
				CelUtils.turnGPSDisable(_ctx);
				startActivity(new Intent(_ctx, LoginActivity.class));
				getActivity().finish();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
		};
		 
		followerdata = new ArrayList<HashMap<String, String>>();
		
		getNotification();
	return 	rootView;
		
	
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		com.ogbongefriends.com.common.Log.d("test", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Splash.PROPERTY_REG_ID, regId);
		editor.putInt(Splash.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.

		/*return getSharedPreferences(LoginActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);*/
	return 	getActivity().getSharedPreferences(getActivity().getClass().getSimpleName(),Context.MODE_PRIVATE);
	}
	public void getNotification(){
		p.show();
		getNotificationApi=new getNotificationApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				p.cancel();
			}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
				p.cancel();
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fetchData();
					}
				});
				
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				p.cancel();
			}
			
		};
		
		HashMap<String, String>key=new HashMap<String, String>();
		key.put("uuid", pref.get(Constants.KeyUUID));
		key.put("auth_token", pref.get(Constants.kAuthT));
		key.put("time_stamp",""); 
		    
		getNotificationApi.setPostData(key);
		     callApi(getNotificationApi);
	}
	
	
	private void fetchData(){
		db.open();
		data = db.findCursor(DB.Table.Name.user_notification_master, DB.Table.user_notification_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'",null, null);
		Log.d("data sizee", ""+data.getCount());
			data.moveToFirst();
		int i=Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_notification_master.hide_account.toString())));
			
		if(i==1){
			//hide_account.setEnabled(false);
			hide_account.setText("Make Account Visible");
			bool=true;
		}
		else{
			hide_account.setEnabled(true);
			hide_account.setText("Hide Account");
			bool=false;
			
		}
		db.close();
	}
	
	public void UpdateNotification(){
		p.show();
		updateNotificationApi=new UpdateNotificationApi(_ctx, db, p){

											
			     @Override
			    protected void updateUI() {
							
				Log.v("UpdateNotificationApi", "updateUI");	
				
				
//				pref = new Preferences(LoginActivity.this);
//				
//				pref.set("", "");
//				pref.commit();
				
				
				if (UpdateNotificationApi.resCode == 1) {
					 
					
					  p.cancel();
					  if(bool==true){
						  bool=false;
						  hide_account.setText("Hide Account");
					  }
					  else{
						  bool=true;
						  hide_account.setText("Make Account Visible");
					  }
					 
					 
					}
					//event_adapter.notifyDataSetChanged();
				
				
				
		
			}		
			
			@Override
			protected void onError(Exception e) {
						
				Log.v("UpdateNotificationApi", "onError");
				p.cancel();
			}
															
			@Override
			protected void onDone() {
												
				Log.v("UpdateNotificationApi", "onDone");
				p.cancel();				
				
			}
			
			
		         };

			
		
		
		
		HashMap<String, String>data_key=new HashMap<String, String>();
			if(data.getCount()>0){	
				
				
				data_key.put(DB.Table.user_notification_master.push_commentonpost.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_commentonpost.toString())));
				data_key.put(DB.Table.user_notification_master.email_commentonpost.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_commentonpost.toString())));
				data_key.put(DB.Table.user_notification_master.push_newmatchfriend.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_newmatchfriend.toString())));
				data_key.put(DB.Table.user_notification_master.email_newmatchfriend.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_newmatchfriend.toString())));
				data_key.put(DB.Table.user_notification_master.push_newchatmessage.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_newchatmessage.toString())));
				data_key.put(DB.Table.user_notification_master.email_newchatmessage.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_newchatmessage.toString())));
				data_key.put(DB.Table.user_notification_master.push_rateonphoto.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_rateonphoto.toString())));
				data_key.put(DB.Table.user_notification_master.email_rateonphoto.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_rateonphoto.toString())));
				
				data_key.put(DB.Table.user_notification_master.push_someonelikeyou.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_someonelikeyou.toString())));
				data_key.put(DB.Table.user_notification_master.email_someonelikeyou.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_someonelikeyou.toString())));
				data_key.put(DB.Table.user_notification_master.push_someonefavyou.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_someonefavyou.toString())));
				data_key.put(DB.Table.user_notification_master.email_someonefavyou.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_someonefavyou.toString())));
				data_key.put(DB.Table.user_notification_master.push_someonesendgift.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.push_someonesendgift.toString())));
				data_key.put(DB.Table.user_notification_master.email_someonesendgift.toString(), data.getString(data.getColumnIndex(DB.Table.user_notification_master.email_someonesendgift.toString())));
			
			if(bool==true){
				data_key.put(DB.Table.user_notification_master.hide_account.toString(), "0");
			}
			else{
				data_key.put(DB.Table.user_notification_master.hide_account.toString(), "1");
			}
				String status=data.getString(data.getColumnIndex(DB.Table.user_notification_master.hide_account.toString()));
				 //bool= Integer.parseInt(status);
			}
		db.close();
		updateNotificationApi.setPostData(data_key);
	     callApi(updateNotificationApi);
		
	}
	
	


	
	
	private void LogoutAlert(){

		
		final Dialog dialog = new Dialog(_ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        dialog.setContentView(R.layout.confirmation_dialog);
       TextView title=(TextView)dialog.findViewById(R.id.title);
       TextView msg=(TextView)dialog.findViewById(R.id.message);
		Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
		Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
		title.setText(getString(R.string.app_name));
		msg.setText(getString(R.string.logout_alert));
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

				HashMap<String, String>map=new HashMap<String, String>();
				
				map.put("uuid", pref.get(Constants.KeyUUID));
				map.put("auth_token", pref.get(Constants.kAuthT));
				map.put("client_id", pref.get(Constants.kClintId));
				
				
				p.show();
				log_out_api.setPostData(map);
				callApi(log_out_api);
				
				
				
				
				dialog.dismiss();
				
			    
			}
		});
		
			// show it
        dialog.show();
		
	
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
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	private final class ResponseListener implements DialogListener 
	{
	   public void onComplete(Bundle values) {
	    
	     // adapter.getUserProfileAsync(new ProfileDataListener());                   
	   }

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(SocialAuthError arg0) {
		// TODO Auto-generated method stub
		
	}
	}
}
