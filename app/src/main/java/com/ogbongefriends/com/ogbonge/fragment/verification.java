package com.ogbongefriends.com.ogbonge.fragment;

import java.util.HashMap;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.api.verification_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Log;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

public class verification extends Fragment {

	int FACEBOOK_REQUEST_CODE = 1;
	int TWITTER_REQUEST_CODE = 2;
	int LINKEDIN_REQUEST_CODE = 3;
	int GOOGLE_REQUEST_CODE=4;
	private int type=0;
	
	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx,phone_num,verification_code;
	@SuppressWarnings("unused")
	private Button post;
	private TextView msg,textView1;
		Preferences pref;
		private boolean mSignInClicked;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	private DB db;
	private CustomLoader p;
	private View rootView;
	private Button fb,linkedIn,google_btn,send_verification,confirm_btn,resend_btn;
	 private Context _ctx; 
	 private LinearLayout v1,v2,mobile_info,resend_code;
	 private int verification_status=0;
	 private HashMap<String, String> hashMap;
		private GoogleApiClient mGoogleApiClient;
		private ConnectionResult mConnectionResult;
		private static final String PREF_NAME = "sample_twitter_pref";
		private  ProgressDialog progressDialog;
		private boolean mIntentInProgress;
		 private static final int RC_SIGN_IN = 0;
		 private TextView textView2;
		 private SocialAuthAdapter adapter;
		 private verification_api verification;
		 private user_profile_api user_profile,getUerprofile;;
		 
		
  @SuppressLint("NewApi") public HashMap<String, String> urls;
  
	public verification(Context ctx) {
		_ctx=ctx;
	}
	public verification(){

	}
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
		rootView = inflater.inflate(R.layout.verification, container, false);
		fb=(Button)rootView.findViewById(R.id.fb);
		msg=(TextView)rootView.findViewById(R.id.msg);
		msg.setText("To stay on Top & attract more friends, verify your account: Log in VIA Social network.");
		linkedIn=(Button)rootView.findViewById(R.id.linkedin);
	//	twitter=(Button)rootView.findViewById(R.id.twitter);
		google_btn=(Button)rootView.findViewById(R.id.google_btn);
		v1=(LinearLayout)rootView.findViewById(R.id.v1);
		v2=(LinearLayout)rootView.findViewById(R.id.v2);
		resend_code=(LinearLayout)rootView.findViewById(R.id.resend_code);
		mobile_info=(LinearLayout)rootView.findViewById(R.id.mobile_info);
		textView1=(TextView)rootView.findViewById(R.id.textView1);
		send_verification=(Button)rootView.findViewById(R.id.send_verification);
		phone_num=(EditText)rootView.findViewById(R.id.phone_num);
		textView2=(TextView)rootView.findViewById(R.id.textView2);
		confirm_btn=(Button)rootView.findViewById(R.id.confirm_btn);
		resend_btn=(Button)rootView.findViewById(R.id.resend_btn);
		verification_code=(EditText)rootView.findViewById(R.id.verification_code);
		
		pref=new Preferences(_ctx);
		
		 adapter = new SocialAuthAdapter(new ResponseListener());
	
		textView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(verification_status==1){
			mobile_info.setVisibility(View.VISIBLE);	
			textView1.setVisibility(View.GONE);
				}
			}
		});
		
		send_verification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			getVerificatinCode();
				
			}
		});
		 
		resend_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				getVerificatinCode();
			}
		});
		
		confirm_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				hashMap=new HashMap<String, String>();
				if(verification_code.getText().length()==6){
					
					type=3;
					hashMap.put("type", "3");
					hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
					hashMap.put("auth_token", pref.get(Constants.kAuthT));
					hashMap.put("social_network_type", "");
					hashMap.put("social_network_id", "");
					hashMap.put("verification_code", verification_code.getText().toString());
					hashMap.put("phone_no", phone_num.getText().toString());
					hashMap.put("email", "");
					verification.setPostData(hashMap);
					p.show();
					callApi(verification);
				}
				else{
					Utils.same_id("Ogbonge", "Please Enter a valid Varification Code", getActivity());
				}
				
			}
		});
		
		
		fb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.authorize(getActivity(), Provider.FACEBOOK);
			}
		});
		
		
		
		google_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.authorize(getActivity(), Provider.GOOGLEPLUS);
			}
		});
		
		linkedIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.authorize(getActivity(), Provider.LINKEDIN);
			}
		});

		
		
		user_profile=new user_profile_api(getActivity(), db, p){

			@Override
			protected void onDone() {
				p.cancel();
				// TODO Auto-generated method stub
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
if(verification.resCode==1){
					
					Utils.same_id("Ogbonge", verification.resMsg, getActivity());
					}
					else{
					Utils.same_id("Ogbonge", verification.resMsg, getActivity());
					}
				super.updateUI();
			}
			
			
		};
		
		
		getUerprofile=new user_profile_api(getActivity(), db, p){

			@Override
			protected void onDone() {
				p.cancel();
				// TODO Auto-generated method stub
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						getUserData();	
					}
				});
				
				super.updateUI();
			}
			
			
		};
		
		
		
		verification=new verification_api(getActivity(), db, p){
			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				p.cancel();
			if(verification_api.resCode==1){
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(type==3){
							mobile_info.setVisibility(View.GONE);
							textView1.setVisibility(View.VISIBLE);
							textView1.setText("Your Account has been DOUBLE VERIFIED, Admin will confirm your Account as soon as possible.");
						}
						
						if(type==1){
							verification_status=1;
							v1.setVisibility(View.GONE);
							v2.setVisibility(View.VISIBLE);
						}
						if(type==2){
							type=3;
							verification_status=2;
							phone_num.setEnabled(false);
							resend_code.setVisibility(View.VISIBLE);
							send_verification.setVisibility(View.GONE);
							
						}
						  HashMap<String, String>map=new HashMap<String, String>();
						     map.put("uuid", pref.get(Constants.KeyUUID));
						     map.put("auth_token", pref.get(Constants.kAuthT));
						     map.put("other_user_uuid","");
						     map.put("time_stamp", "");  
						     user_profile.setPostData(map);
						     callApi(user_profile);
					}
				});
			}
			else{
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.same_id("Ogbonge", verification_api.resMsg, getActivity());
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
		p.show();
		HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     getUerprofile.setPostData(map);
	     callApi(getUerprofile);
		
		
	return 	rootView;
	}

	
	
	
	private void getVerificatinCode(){
		
		
		hashMap=new HashMap<String, String>();
	//	if(phone_num.getText().length()==10){
			
			type=2;
			hashMap.put("type", "2");
			hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
			hashMap.put("auth_token", pref.get(Constants.kAuthT));
			hashMap.put("social_network_type", "");
			hashMap.put("social_network_id", "");
			hashMap.put("verification_code", "");
			hashMap.put("phone_no", phone_num.getText().toString());
			hashMap.put("email", "");
			verification.setPostData(hashMap);
			p.show();
			callApi(verification);
//		}
//		else{
//			Utils.same_id("Ogbonge", "Please Enter a valid Phone Number", getActivity());
//		}
		
	}
	
	
	private void getVerificatioStatus(){
		

		if(verification_status==0){
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
		}
		if(verification_status==1){
			v1.setVisibility(View.GONE);
			v2.setVisibility(View.VISIBLE);
			textView2.setText("A 6 digit string will be sent to your mobile number as a verification code");
		}
		
		if(verification_status==2){
			mobile_info.setVisibility(View.GONE);
			textView1.setVisibility(View.VISIBLE);
			textView1.setText("Your Account has been DOUBLE VERIFIED, Admin will confirm your Account as soon as possible.");

		}
	}
	
	
	private void getUserData(){
		db.open();
		Cursor c=db.findCursor(Table.Name.user_master, Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		c.moveToFirst();
		verification_status=Integer.parseInt(c.getString(c.getColumnIndex(Table.user_master.verification_level.toString())));
		phone_num.setText(c.getString(c.getColumnIndex(Table.user_master.phone_number.toString())));
		getVerificatioStatus();
	}
	
	
	private void hitAPI(HashMap<String, String> data){
		p.show();
		verification.setPostData(data);
		callApi(verification);
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
	
	
	private final class ResponseListener implements DialogListener 
	{
	   public void onComplete(Bundle values) {
	    
	      adapter.getUserProfileAsync(new ProfileDataListener());                   
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
//			if(arg0.equalsIgnoreCase("twitter")){
//				   
////				 //type 'facebook','twitter','linkedin','google','yahoo'
//				   
//					
////				  "type": "1",
////				    "uuid": "ob_54981e82bc54e",
////				    "auth_token": "zGhyRqCQMNDIkL7ScpeYWmB1JX9d0fWix5SKHjwmVUOoUfYx2K",
////				    "social_network_type": "",
////				    "social_network_id": "",
////				    "verification_code": "",
////				    "phone_no": "",
////				     "email": "arvind.yadav@alcanzarsoft.com"
//
//				hashMap.put("type", "1");
//				hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
//				hashMap.put("auth_token", pref.get(Constants.kAuthT));
//				hashMap.put("social_network_type", "twitter");
//				hashMap.put("social_network_id", profileMap.getValidatedId());
//				hashMap.put("verification_code", "");
//				hashMap.put("phone_no", "");
//				hashMap.put("email", "");
//				
//				
//					
//					if(profileMap.getEmail()!=null){
//						hashMap.put("email", ""+profileMap.getEmail());
//					
//					}
//					else{
//						hashMap.put("email", "");
//					
//					}
//					
//					
//					
//					hitAPI(map);
//			}
			  
			  
			  
			  
			  
			  if(arg0.equalsIgnoreCase("googleplus")){
				  
				  
				  type=1;		
					hashMap.put("type", "1");
					hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
					hashMap.put("auth_token", pref.get(Constants.kAuthT));
					hashMap.put("social_network_type", "google");
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
			
			
			if(arg0.equalsIgnoreCase("linkedin")){
			
	   
				 
					
//					map.put("email", ""+profileMap.getEmail());
//					map.put("password", "");
//					map.put("platform_type","1");
//					map.put("device_type","1");
//					map.put("device_id",Utils.getDeviceID(LoginActivity.this));
//					map.put("latitude","26.839389");
//					map.put("longitude","80.916710");
//					map.put("social_network_type","linkedin");
//					map.put("social_network_id",profileMap.getValidatedId());
					
					
				type=1;
					hashMap.put("type", "1");
					hashMap.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
					hashMap.put("auth_token", pref.get(Constants.kAuthT));
					hashMap.put("social_network_type", "linkedin");
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
			
			
			if(arg0.equalsIgnoreCase("facebook")){
				
							
//				map.put("email", ""+profileMap.getEmail());
//				map.put("password", "");
//				map.put("platform_type","1");
//				map.put("device_type","1");
//				map.put("device_id",Utils.getDeviceID(LoginActivity.this));
//				map.put("latitude","26.839389");
//				map.put("longitude","80.916710");
//				map.put("social_network_type","facebook");
//				map.put("social_network_id",profileMap.getValidatedId());
//				
//				
//				
				
				
				
				type=1;
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
	
	
	
	
	
	}

