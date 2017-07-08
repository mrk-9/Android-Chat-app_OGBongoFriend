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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.GetUserGiftApi;
import com.ogbongefriends.com.api.Redeem_Gift_Api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.NotificationType;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class UserGiftList extends Fragment implements Runnable,MyLocationListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;
private String gift_id="";
	private Uri selectedImage;

	private long str;
	private LocationHelper mLocationHelper;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;
	private Dialog dialog;
	private float totalPointsgain=0;
	private int totalPoints=0;
	private ListView gifts_list;

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
	private Button checkInBtn,redeem_btn;
	private DB db;
	private String other_user_uuid;
	private CustomLoader p;
	private ImageLoader imageLoader;
	private int gift_type=0,redeem_type=1;
	Notification nt;
	private LinearLayout ll_user;
	private TextView nodata;
	private GetUserGiftApi getUserGift;
	public ArrayList<String> gift_to_redeem;
	private Redeem_Gift_Api redeem_gift_api;
	// getEventApi geteventapi;
	int count = 0;
	 private DisplayImageOptions options;
   private Context _ctx; 
  @SuppressLint("NewApi") public HashMap<String, String> urls;
  
	public UserGiftList(Context ctx) {
		_ctx=ctx;
	}
	public UserGiftList(){

	}
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
		p.show();
		rootView = inflater.inflate(R.layout.user_gift_list, container, false);
	//	init();
	
	return 	rootView;
		

	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	
	
	
	private void init(){
		gift_to_redeem=new ArrayList<String>();
		gifts_list=(ListView)rootView.findViewById(R.id.users_gift_list);
		nodata=(TextView)rootView.findViewById(R.id.nodata_text);
		redeem_btn=(Button)rootView.findViewById(R.id.redeem_btn);
		data_map=new ArrayList<HashMap<String,String>>();
		pref=new Preferences(_ctx);
		Log.d("arv ", "arv name"+pref.get(Constants.OtherUserName));
		other_user_uuid=pref.get(Constants.OtherUser);
		if(other_user_uuid.length()>1){
			gift_type=2;
			redeem_btn.setVisibility(View.GONE);
		}
		else{
			gift_type=1;
			NotificationType.someone_send_gift_notification=0;
		}
		
		redeem_gift_api=new Redeem_Gift_Api(_ctx, db, p){

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				p.cancel();
				if(redeem_gift_api.resCode==1){
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(redeem_type==1){
							redeem_type++;
							showRedeeminfo(redeem_gift_api.objJson);
						}
						else{
							
						dialog.dismiss();
//						pref.set("push", 1).commit();
//						((DrawerActivity) getActivity()).displayView(48);

							NotificationType.UserPoints=String.valueOf(Integer.parseInt(NotificationType.UserPoints)+(int) Math.round(totalPointsgain));
						Utils.same_id("Congratulations...", "You have got " + totalPointsgain + " Ogbonge Points", _ctx);
						init();
						}
					}
				});
				
				}
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
			
			
		};
		
		manageLocation();
		File cacheDir = StorageUtils.getCacheDirectory(_ctx);
		 options = CelUtils.getImageOption();
      
     
		 redeem_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				if(gift_to_redeem.size()>0){
					redeem_type=1;
					HashMap<String, String> data_to_send=new HashMap<String, String>();
					Log.d("arvin", ""+gift_to_redeem.toString());
					gift_id="";
					for(int i=0;i<gift_to_redeem.size();i++){
						if(i==0){
							gift_id=gift_id+gift_to_redeem.get(i);	
						}
						else{
							gift_id=gift_id+","+gift_to_redeem.get(i);
						}
					}
				
					data_to_send.put("gift_array", gift_id);
					data_to_send.put("uuid", pref.get(Constants.KeyUUID));
					data_to_send.put("auth_token", pref.get(Constants.kAuthT));
					data_to_send.put("type", String.valueOf(redeem_type));
					redeem_gift_api.setPostData(data_to_send);
					p.show();
					callApi(redeem_gift_api);
				}
				else{
					Utils.alert(_ctx, getString(R.string.select_gift));
				}
				
			}
		});
			
				
		 ImageLoader.getInstance().init(CelUtils.getImageConfig(_ctx));
				
			 imageLoader = ImageLoader.getInstance();
				
			 //showuser();
		getuserGifts();
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fragmentManager = getFragmentManager();
		
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
		init();
		super.onStart();
		
		
	}

	private void showRedeeminfo(JsonObject info){
		JsonObject data=info.get("data").getAsJsonObject();
		 dialog = new Dialog(_ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        dialog.setContentView(R.layout.redeem_gift_info);
        
        TextView total_points=(TextView)dialog.findViewById(R.id.total_points);
        TextView reduction=(TextView)dialog.findViewById(R.id.reduction);
        TextView reduction_percent=(TextView)dialog.findViewById(R.id.reduction_percent);
        TextView redeem_points=(TextView)dialog.findViewById(R.id.redeem_points);
       Button back=(Button)dialog.findViewById(R.id.back_btn);
       Button contin=(Button)dialog.findViewById(R.id.cont_btn);
        
       total_points.setText(data.get("total_points").getAsString());
		totalPoints=Integer.parseInt(data.get("total_points").getAsString());
       totalPointsgain=Float.parseFloat(data.get("gain_points").getAsString());
       reduction.setText(data.get("reduce_points").getAsString());
       redeem_points.setText(data.get("gain_points").getAsString());
       reduction_percent.setText("Reduce points("+ (data.get("total_points").getAsInt()/data.get("reduce_points").getAsFloat()) + "%)");
       
        dialog.show();
	
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				redeem_type=1;
			}
		});
        
        
        contin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HashMap<String, String> data_to_send=new HashMap<String, String>();
				data_to_send.put("gift_array", gift_id);
				data_to_send.put("uuid", pref.get(Constants.KeyUUID));
				data_to_send.put("auth_token", pref.get(Constants.kAuthT));
				data_to_send.put("type", String.valueOf(redeem_type));
				redeem_gift_api.setPostData(data_to_send);
				p.show();
				callApi(redeem_gift_api);
				
			}
		});
        
	}
	
	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	
public void getuserGifts(){
		
	getUserGift=new GetUserGiftApi(_ctx, db, p){

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
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(searchedata.size()>0){
							showuser();
							}
							else{
								nodata.setText("No Gift Found");
								nodata.setVisibility(View.VISIBLE);
								Log.d("Null JSON", "Null JSON");
							}
					p.cancel();
					}
				});
				
				
			}

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				searchedata=getUserGift.userdata;
				
				Log.d("JSON Array",String.valueOf(getUserGift.userdata));
				Log.d("JSON Array",String.valueOf(searchedata));
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				//getAllUsersFroBanner();
				
			}
			
		};
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_uuid",other_user_uuid);  
	     map.put("time_stamp",""); 
	     map.put("type",String.valueOf(gift_type)); 
	    
	     
	     getUserGift.setPostData(map);
	     callApi(getUserGift);
	}
	

private void showuser(){
	// TODO Auto-generated method stub

	String query="";
	
	if(gift_type==1){
		query="select * from "+ DB.Table.Name.gift_sent_to_user+" where "+ DB.Table.gift_sent_to_user.receiver_master_id.toString()+" = '"+pref.get(Constants.KeyUUID)+"' AND status=1";
	}
	else{
	
		query="select * from "+ DB.Table.Name.gift_sent_to_user+" where "+ DB.Table.gift_sent_to_user.receiver_master_id.toString()+" = '"+other_user_uuid+"' AND status=1";
	}
	
	db.open();
	Cursor giftData=db.findCursor(query, null);
	
	
		if(giftData.getCount()>0){	
			giftData.moveToFirst();
	for(int i=0; i<giftData.getCount();i++){
		
		urls=new HashMap<String, String>();
		urls.put("gift_type",String.valueOf(gift_type));
		urls.put(DB.Table.gift_sent_to_user.description.toString(), giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.description.toString())));
		urls.put(DB.Table.gift_sent_to_user.gift_cost.toString(), giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.gift_cost.toString())));
		urls.put(DB.Table.gift_sent_to_user.gitf_id.toString(), giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.gitf_id.toString())));
		urls.put(DB.Table.gift_sent_to_user.gift_master_id.toString(), giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.gift_master_id.toString())));
		
		db.open();
		if(giftData.getInt(giftData.getColumnIndex(DB.Table.gift_sent_to_user.gift_master_id.toString()))>0){
			
			data = db.findCursor(DB.Table.Name.gift_master, DB.Table.gift_master.id.toString()+" = "+giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.gift_master_id.toString())),null, null);
		Log.d("data sizee", ""+data.getCount());
			data.moveToFirst();
			urls.put("gift_image",getActivity().getString(R.string.urlString)+"userdata/gift_gallery/"+data.getString(data.getColumnIndex(DB.Table.gift_master.gift_image.toString())));
			urls.put(DB.Table.gift_master.gift_title.toString(),data.getString(data.getColumnIndex(DB.Table.gift_master.gift_title.toString())));
			
		}
			else{
				urls.put("gift_image","http://www.ogbongefriends.com/assets/img/coin.png");
				urls.put(DB.Table.gift_master.gift_title.toString(),"Send points as gift");
			}
		
		data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.sender_master_id.toString()))+"'", null, null);
		if(data.getCount()>0){
			data.moveToFirst();
			urls.put("User_Image_url", getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString())));
			urls.put("user_name", data.getString(data.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+data.getString(data.getColumnIndex(DB.Table.user_master.last_name.toString())));
			urls.put(DB.Table.user_master.uuid.toString(), giftData.getString(giftData.getColumnIndex(DB.Table.gift_sent_to_user.sender_master_id.toString())));
			
		}
		

		giftData.moveToNext();
		
		data_map.add(i, urls);
	}
	db.close();
}
	gifts_list.setAdapter(new UserGiftAdapter(_ctx, data_map,gift_to_redeem) {
		
		@Override
		protected void onItemLongClick(View v, String string) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void onItemClick(View v, String string) {
			// TODO Auto-generated method stub
			
		}
		
	});
		
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
