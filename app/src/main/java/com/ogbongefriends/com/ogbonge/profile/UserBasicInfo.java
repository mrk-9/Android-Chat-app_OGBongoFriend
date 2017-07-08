package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class UserBasicInfo extends Fragment{
	private View rootView;
	private Context _ctx;
	private CustomLoader p;
	private Preferences pref;
	private DB db;
	private Button basic,contact,about,interested,personal;
	private user_profile_api user_profile_info;
	private profileInfoAdapter profileInfoAdapter;
	String[] dataToShow={"Basic Information","Contact Details","About Me","Interested In","Personal Information"};
	public UserBasicInfo(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		rootView = inflater.inflate(R.layout.update_profile_list, container, false);
		basic=(Button)rootView.findViewById(R.id.basic);
		contact=(Button)rootView.findViewById(R.id.contact);
		about=(Button)rootView.findViewById(R.id.about);
		interested=(Button)rootView.findViewById(R.id.interested);
		personal=(Button)rootView.findViewById(R.id.personal);
		
		basic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(50);
			}
		});
		
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(51);	
			}
		});

		interested.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		((DrawerActivity) getActivity()).displayView(53);	
	}
});

		about.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		((DrawerActivity) getActivity()).displayView(52);	
	}
});

		personal.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		((DrawerActivity) getActivity()).displayView(54);	
	}
});
		
		pref=new Preferences(_ctx);
		db=new DB(_ctx);
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
		
		 getUserProfile();
		 
		 
		 
		 
		 
		 profileInfoAdapter=new profileInfoAdapter(getActivity(),dataToShow) {
			
			@Override
			protected void onItemClick(View v, Integer string) {
				// TODO Auto-generated method stub
				Log.d("arv Position ", "arv"+string);
				if(string==0){
					
				}
				if(string==1){
					((DrawerActivity) getActivity()).displayView(51);	
				}
				if(string==2){
					((DrawerActivity) getActivity()).displayView(52);	
				}
				if(string==3){
					((DrawerActivity) getActivity()).displayView(53);	
				}
				if(string==4){
					((DrawerActivity) getActivity()).displayView(54);	
				}
			}
		};
		//list.setAdapter(profileInfoAdapter);
		

		
	return 	rootView;
		

	}

	private void getUserProfile(){
		
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
				p.cancel();
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				
				super.updateUI();
			p.cancel();
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
	
	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(_ctx)) {
			Log.v("Internet Not Conneted", "");
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Thread.currentThread().setPriority(1);
					p.cancel();
					Utils.same_id("Error", getString(R.string.no_internet),
							_ctx);
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

	}
