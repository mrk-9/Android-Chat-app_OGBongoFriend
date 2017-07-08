package com.ogbongefriends.com.ogbonge.setting;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.getNotificationApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class AccountSettings extends Fragment implements Runnable {

	// private ListView listView;
	private ArrayList<HashMap<String, String>> feedData;
	private ArrayList<HashMap<String, String>> placeData;
	// private EventAdapter eventAdapter;
	private ArrayList<HashMap<String, String>> eventsData;

	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;

	private Uri imageUri;

	private Uri selectedImage;
//	private SharedPreferences pref;

	private long str;

	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;

	private ImageView attchment_img;
	private Button remave_img_btn;
	private ImageView profileImage;
	private ImageView dialogProfileImage;

	private Button showPlaceBtn;
	private Button showEventBtn;
//	private Editor edit;
	private long other_user;

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
 private	View rootView;
	private ListView list;
	Notification nt;
	// getEventApi geteventapi;
	int count = 0; 
	String email="yad@@@@@@@.com";
	private settingAdapter settingAdapter;
	private getNotificationApi getNotificationApi;
	private Context _ctx;
	private Button account,notification,verification,feedback,payment,terms,privacy,disclaimer,aboutus,how_it_works;
	private DB db;
	CustomLoader p;
	String[] dataToShow={"Account","Notifications","Verification","Feedback","Payment Settings","Terms & Conditions","Privacy","Disclaimer","About Us"};
	public AccountSettings(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
if(rootView==null){
		rootView = inflater.inflate(R.layout.account_setting, container, false);
		list=(ListView)rootView.findViewById(R.id.listView1);
		db=new DB(_ctx);
		pref=new Preferences(_ctx);
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
		
		 account=(Button)rootView.findViewById(R.id.account);
		 notification=(Button)rootView.findViewById(R.id.notification);
		 verification=(Button)rootView.findViewById(R.id.verification);
		 feedback=(Button)rootView.findViewById(R.id.feedback);
		 payment=(Button)rootView.findViewById(R.id.payment);
		 terms=(Button)rootView.findViewById(R.id.terms);
		 privacy=(Button)rootView.findViewById(R.id.privacy);
		 disclaimer=(Button)rootView.findViewById(R.id.disclaimer);
		 aboutus=(Button)rootView.findViewById(R.id.aboutus);
		 how_it_works=(Button)rootView.findViewById(R.id.how_it_works);
		 
		 

		 account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(42);
				
			}
		});
		 
		 notification.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DrawerActivity) getActivity()).displayView(41);
				}
			});
		 
		 verification.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DrawerActivity) getActivity()).displayView(58);
				}
			});
		 
		 feedback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DrawerActivity) getActivity()).displayView(59);
				}
			});
		 
		 payment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
		 payment.setVisibility(View.GONE);
		 
		 terms.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pref.set(Constants.SettingType, "4").commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
			});
		 
		 privacy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pref.set(Constants.SettingType, "2").commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
			});
		 
		 disclaimer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pref.set(Constants.SettingType, "3").commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
			});
		 
		 aboutus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pref.set(Constants.SettingType, "1").commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
			});
		 
		 how_it_works.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pref.set(Constants.SettingType, "5").commit();
				((DrawerActivity) getActivity()).displayView(61);
			}
		});
		 
		 
		settingAdapter=new settingAdapter(getActivity(),dataToShow,email) {

			@Override
			protected void onItemClick(View v, Integer string) {
				// TODO Auto-generated method stub
				Log.d("arv Position ", "arv"+string);
				if(string==0){
					((DrawerActivity) getActivity()).displayView(42);
				}
				if(string==1){
					((DrawerActivity) getActivity()).displayView(41);
				}
				if(string==2){
					((DrawerActivity) getActivity()).displayView(58);
				}
				if(string==3){
					((DrawerActivity) getActivity()).displayView(59);
				}
				if(string==5){
					pref.set("category", 4).commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
				
				if(string==6){
					pref.set("category", 2).commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
				
				if(string==7){
					pref.set("category", 3).commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
				
				if(string==8){
					pref.set("category", 1).commit();
					((DrawerActivity) getActivity()).displayView(61);
				}
			}
		};
		list.setAdapter(settingAdapter);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		fragmentManager = getFragmentManager();
		 
		followerdata = new ArrayList<HashMap<String, String>>();
		getNotification();
}		
	return 	rootView;


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
				p.cancel();
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

}
