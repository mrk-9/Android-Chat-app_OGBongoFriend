package com.ogbongefriends.com.ogbonge.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.ogbonge.search.Gridview_adapter;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class findOgbongeFriend extends Fragment implements Runnable,MyLocationListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;

	private Uri selectedImage;
//	private SharedPreferences pref;

	private long str;
	private LocationHelper mLocationHelper;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;
	private ImageView promote_yourself;
	private GridView user_of_city;

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
	private DB db;
	private CustomLoader p;
	private TextView nodata;
	private ImageLoader imageLoader;
	private LinearLayout ll_user;
	Notification nt;
	private getAllUserProfileAPI get_all_user_pfro_api;
	DisplayImageOptions options;
	// getEventApi geteventapi;
	int count = 0;
   private Context _ctx; 
  public HashMap<String, String> urls;
  private PullToRefreshGridView mPullRefreshGridView;
	public findOgbongeFriend(){

	}
	public findOgbongeFriend(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
		p.show();
		rootView = inflater.inflate(R.layout.who_is_online, container, false);
		searchedata=new JsonArray();
		mPullRefreshGridView = (PullToRefreshGridView) rootView.findViewById(R.id.user_of_city);
		user_of_city=mPullRefreshGridView.getRefreshableView();
		ll_user=(LinearLayout)rootView.findViewById(R.id.parent_user_images);
		nodata=(TextView)rootView.findViewById(R.id.nodata_text);
		promote_yourself=(ImageView)rootView.findViewById(R.id.promote_yourself);
		data_map=new ArrayList<HashMap<String,String>>();
		pref=new Preferences(_ctx);
		manageLocation();
		promote_yourself.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(93);
			}
		});
		 mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

				

				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<GridView> refreshView) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<GridView> refreshView) {
					// TODO Auto-generated method stub
					
				}

			});
	
				
				 options = CelUtils.getImageOption();
				
				ImageLoader.getInstance().init(CelUtils.getImageConfig(_ctx));
				
			 imageLoader = ImageLoader.getInstance();
				
			
		
		
		
		getAllUsers();
		
		
		
		
		
//		View header = inflater.inflate(R.layout.profile_hadder, null);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// ============= manage image height================

		fragmentManager = getFragmentManager();
	
		// ==================================================

		/*pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		edit = pref.edit();*/
		

		
	return 	rootView;
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	
	
	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	
	
	
public void getAllUsers(){
		

	
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
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				searchedata=get_all_user_pfro_api.getUserdata();
				Log.d("JSON Array",String.valueOf(get_all_user_pfro_api.getUserdata()));
				Log.d("JSON Array",String.valueOf(searchedata));
				getAllUsersFroBanner();
				
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
	     map.put("time_stamp", ""); 
	     map.put("type", "2");
	     map.put("page_index", "1");
	     
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
			showuserForBanner();
			if(!(searchedata==null)){
			showuser(searchedata);
			}
			else{
				nodata.setText("No Friend Found");
				nodata.setVisibility(View.VISIBLE);
				Log.d("Null JSON", "Null JSON");
			}
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

private void showuserForBanner(){
	// TODO Auto-generated method stub
db.open();
	data = db.findCursor(DB.Table.Name.user_master,null,null, null);
Log.d("data sizee", ""+data.getCount());
	data.moveToFirst();
	
		for(int i=0;i<data.getCount();i++){
		
		if(data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString())).length()>3){
			
			RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			RelativeLayout parent = new RelativeLayout(getActivity());
			parent.setLayoutParams(parent_param);
				 
			 if(i==0){
				 parent.setBackgroundColor(Color.DKGRAY);
			 }
			 
		parent.setTag(data.getString(data.getColumnIndex(DB.Table.user_master.uuid.toString())));
		parent.setTag(R.id.account_setting_text, i);
//		parent.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				loadUser(v,(Integer) v.getTag(R.id.en1));
//				current_user=(Integer) v.getTag(R.id.en1);
//				//v.setBackgroundColor(Color.DKGRAY);
//				
//				
//			}
//		});
			String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
			ImageView imagView = new ImageView(getActivity());
			imagView.setScaleType(ScaleType.FIT_XY);
			ll_user.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
			  lp.setMargins(5, 0, 0, 0);

			    imagView.setLayoutParams(lp);

			    imagView.setTag(url);
			    imagView.setImageResource(R.drawable.profile);
			    imageLoader.displayImage(url, imagView);

			RelativeLayout.LayoutParams online_param = new RelativeLayout.LayoutParams(15, 15);

			online_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			    ImageView online_image = new ImageView(getActivity());

			    online_image.setLayoutParams(online_param);

			    online_image.setImageResource(R.drawable.online_dot);

			    TextView tv = new TextView(getActivity());

			    tv.setText(data.getString(data.getColumnIndex(DB.Table.user_master.first_name.toString())));

			    tv.setTextColor(Color.WHITE);

			    parent.addView(imagView);

			    parent.addView(tv);

			    parent.addView(online_image);

			   

			    ll_user.addView(parent);
		
		}
		
		data.moveToNext();
		}
		
		p.cancel();
	}

	
private void showuser(JsonArray usersData){
	// TODO Auto-generated method stub
	
			
			
	for(int i=0; i<usersData.size();i++){
		
		JsonObject form = usersData.get(i).getAsJsonObject();
		urls=new HashMap<String, String>();
		if(!(form.get(DB.Table.user_master.profile_pic.toString()).getAsString().equals(""))){
			String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+form.get(DB.Table.user_master.id.toString()).getAsString()+"/photos_of_you/"+form.get(DB.Table.user_master.profile_pic.toString()).getAsString();
				
			urls.put("Image_url", url);
			urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
			urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
	}
		else{
			urls.put("Image_url","");
			urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
			urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
		}
		data_map.add(i, urls);
	}
	
		
		user_of_city.setAdapter(new Gridview_adapter(_ctx, data_map)
		{

			@Override
			protected void onItemClick(View v, String string) {
				// TODO Auto-generated method stub
				pref.set(Constants.selected_user_id,string );
				pref.commit();
				((DrawerActivity) getActivity()).displayView(99);	
			}
			
		});
		
		p.cancel();
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

