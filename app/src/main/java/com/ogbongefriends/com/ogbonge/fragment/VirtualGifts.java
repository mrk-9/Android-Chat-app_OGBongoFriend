package com.ogbongefriends.com.ogbonge.fragment;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.VirtualGift_adapter;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.api.sendGifttoUser;
import com.ogbongefriends.com.api.user_profile_api;

@SuppressLint("NewApi") public class VirtualGifts extends Fragment implements Runnable,MyLocationListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;

	private Uri selectedImage;

	private long str;
	private LocationHelper mLocationHelper;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;

	private GridView gifts_grid;

	private CheckBox chfollow;
	private CheckBox chsecretfollow;
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	// EventAdapter showfollowerlist;
	private ArrayList<HashMap<String, String>> data_map;
	
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
	private CheckBox ch1,ch2,ch3,ch4,ch5,ch6;
	private Button save;
	private Button checkInBtn;
	private Get_favourites_API favouritesApi;;
	private DB db;
	private int current_user_points=0;
	private CustomLoader p;
	private ImageLoader imageLoader;
	Notification nt;
	private EditText msg;
	private LinearLayout ll_user;
	private TextView nodata,userName;
	private getAllUserProfileAPI get_all_user_pfro_api;
	// getEventApi geteventapi;
	int count = 0;
	 private DisplayImageOptions options;
   private Context _ctx; 
   private sendGifttoUser sendGiftToUser;
   private user_profile_api user_profile_info;
  @SuppressLint("NewApi") public HashMap<String, String> urls;
  
	public VirtualGifts(Context ctx) {
		_ctx=ctx;
	}
	public VirtualGifts(){

	}
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
		p.show();
		rootView = inflater.inflate(R.layout.virtual_gifts, container, false);
		
		gifts_grid=(GridView)rootView.findViewById(R.id.gifts);
		userName=(TextView)rootView.findViewById(R.id.user_name);
		data_map=new ArrayList<HashMap<String,String>>();
		pref=new Preferences(_ctx);
		userName.append(" "+pref.get(Constants.OtherUserName));
		manageLocation();
		File cacheDir = StorageUtils.getCacheDirectory(_ctx);
		// options = CelUtils.getImageOption();
     
				
		 options = new DisplayImageOptions.Builder()
	     .resetViewBeforeLoading(false)  // default
	     .delayBeforeLoading(1000)
	     .displayer(new RoundedBitmapDisplayer(150))
	     .cacheInMemory(true) // default
	     .cacheOnDisk(true) // default
	     .considerExifParams(false) // default
	     .build();
		 
				ImageLoader.getInstance().init(CelUtils.getImageConfig(_ctx));
				
			 imageLoader = ImageLoader.getInstance();
			getuserInfo();	
			showuser();
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fragmentManager = getFragmentManager();
	
	return 	rootView;
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	
	
	private void getuserInfo(){
		db.open();
		Cursor c=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		c.moveToFirst();
		current_user_points=Integer.parseInt(c.getString(c.getColumnIndex(DB.Table.user_master.points.toString())));
		
	}
	
	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	
	
public void getAllUsersFroBanner(){
	
	get_all_user_pfro_api=new getAllUserProfileAPI(getActivity(), db, p){

		@Override
		protected void onError(Exception e) {
			// TODO Auto-generated method stub
			super.onError(e);
		}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			super.updateUI();
			
			showuser();
		}
		
	};
	
	
	 HashMap<String, String>map=new HashMap<String, String>();
     map.put("uuid", pref.get(Constants.KeyUUID));
     map.put("auth_token", pref.get(Constants.kAuthT));
     map.put("time_stamp", "");  
     map.put("type", "1"); 
     Log.d("arv", ""+mLocationHelper.getMyLocation());
     
     if (mLocationHelper.getMyLocation() == null) {
    	 map.put("latitude",""); 
	     map.put("longitude", ""); 
		}
     else{
    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
					.getLatitude())); 
	     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
					.getLongitude())); 
     }
    
     
     get_all_user_pfro_api.setPostData(map);
     callApi(get_all_user_pfro_api);
}




private void showuser(){
	// TODO Auto-generated method stub
	data_map.clear();
	data = db.findCursor(DB.Table.Name.gift_master, DB.Table.gift_master.status.toString()+" = 1",null, null);
	Log.d("data sizee", ""+data.getCount());
		data.moveToFirst();
		urls=new HashMap<String, String>();
		urls.put("type", "0");
		data_map.add(0, urls);
			
	for(int i=0; i<data.getCount();i++){
		if(data.getString(data.getColumnIndex(DB.Table.gift_master.gift_image.toString())).length()>3){
	
			urls=new HashMap<String, String>();
			
			String url=getActivity().getString(R.string.urlString)+"userdata/gift_gallery/"+data.getString(data.getColumnIndex(DB.Table.gift_master.gift_image.toString()));
			urls.put("type", "1");
			urls.put(DB.Table.gift_master.gift_image.toString(), url);
			urls.put(DB.Table.gift_master.id.toString(), data.getString(data.getColumnIndex(DB.Table.gift_master.id.toString())));
			urls.put(DB.Table.gift_master.gift_title.toString(), data.getString(data.getColumnIndex(DB.Table.gift_master.gift_title.toString())));
			urls.put(DB.Table.gift_master.gift_description.toString(), data.getString(data.getColumnIndex(DB.Table.gift_master.gift_description.toString())));
			urls.put(DB.Table.gift_master.cost.toString(), data.getString(data.getColumnIndex(DB.Table.gift_master.cost.toString())));
		data_map.add(i+1, urls);
	}
		data.moveToNext();
	}
		

		
		gifts_grid.setAdapter(new VirtualGift_adapter(_ctx, data_map) {
			
			@Override
			protected void onItemLongClick(View v, String string) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onItemClick(View v, final String string) {
				
				final Dialog dialog = new Dialog(_ctx);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
                dialog.setContentView(R.layout.send_msg_box);
                 msg=(EditText)dialog.findViewById(R.id.msg);
                 final TextView char_left=(TextView)dialog.findViewById(R.id.char_left);
				Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
				Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
				LinearLayout point_info=(LinearLayout)dialog.findViewById(R.id.points_info);
				EditText total_point=(EditText)dialog.findViewById(R.id.total_point);
				final EditText send_point=(EditText)dialog.findViewById(R.id.send_point);
				
				if(string.equalsIgnoreCase("-900")){
					getuserInfo();
					total_point.setText(string.valueOf(current_user_points));
					point_info.setVisibility(View.VISIBLE);
				}
				else{
					point_info.setVisibility(View.GONE);
				}
				
				msg.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						char_left.setText(msg.getText().length()+"/162");
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}
				});
				
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
						
						if(string.equalsIgnoreCase("-900")){
						
							if(send_point.getText().length()>0){
							if(current_user_points>Integer.parseInt(send_point.getText().toString())){
								dialog.dismiss();
								sendGifttoUser("-1",send_point.getText().toString());
							}
							else{
								Utils.same_id("Ogbonge", "Please Enter Points to be Send", getActivity());
							}
							}
							
							
						}
						else{
							if(msg.getText().length()>0){
								dialog.dismiss();
								sendGifttoUser(string,"");
							}
						}
						
					}
				});
                
			}
		});
		
			
			
		p.cancel();
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
			
			if(sendGiftToUser.resCode==1){
				
				
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("Arv ", "Gift send successfully");
						p.cancel();
						Utils.same_id("Message", "Gift sent successfully to "+pref.get(Constants.OtherUserName), _ctx);
						//getActivity().onBackPressed();
					}
				});
				
				//getActivity().onBackPressed();
			}
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
    map.put("other_user_uuid","");
    map.put("time_stamp", "");  
    user_profile_info.setPostData(map);
    callApi(user_profile_info);
}



public void sendGifttoUser(String giftId, String points){
	p.show();
	sendGiftToUser=new sendGifttoUser(_ctx, db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				if(sendGiftToUser.resCode==1){
					getUpdatedUserProfile();	
				}
				else{
getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							p.cancel();
							Utils.same_id("Message", sendGiftToUser.resMsg, _ctx);
							//getActivity().onBackPressed();
						}
					});
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
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_uuid",pref.get(Constants.OtherUser));  
	     map.put("gift_master_id",giftId.toString()); 
	     map.put("description",msg.getText().toString()); 
	     map.put("send_points",points.toString());
	     
	      
	     sendGiftToUser.setPostData(map);
	     callApi(sendGiftToUser);
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
	

	private void saveSetting(){
		
		if(ch1.isChecked()){
			pref.setBoolean(Constants.profSetting1, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting1, false);
			pref.commit();
		}
		
		if(ch2.isChecked()){
			pref.setBoolean(Constants.profSetting2, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting2, false);
			pref.commit();
		}
		
		if(ch3.isChecked()){
			pref.setBoolean(Constants.profSetting3, true);
			pref.commit();
		}
		else {
			pref.setBoolean(Constants.profSetting3, false);
			pref.commit();
		}
		if(ch4.isChecked()){
			pref.setBoolean(Constants.profSetting4, true);
			pref.commit();
		}
		
		else {
			pref.setBoolean(Constants.profSetting4, false);
			pref.commit();
		}
		
		if(ch5.isChecked()){
			pref.setBoolean(Constants.profSetting5, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting5, false);
			pref.commit();
		}
		
		if(ch6.isChecked()){
			pref.setBoolean(Constants.profSetting6, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting6, false);
			pref.commit();
		}
	}

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}
	
}
