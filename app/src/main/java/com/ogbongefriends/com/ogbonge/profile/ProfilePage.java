package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.GetUserGiftApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.NotificationType;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi")
public class ProfilePage extends Fragment implements Runnable ,OnClickListener,AnimationListener{

	// private ListView listView;
	private ArrayList<HashMap<String, String>> feedData;
	private ArrayList<HashMap<String, String>> placeData;
	// private EventAdapter eventAdapter;
	private ArrayList<HashMap<String, String>> eventsData;
	
	public ProfilePage(){
		
	}
	
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Animation slideUp, slideDown;
	private float UserRating=0;
	private LinearLayout super_parent;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	private CustomLoader p;
	private ImageView profileImage,rating_larger,gift_larger;
	private LinearLayout like_con,prof_con,photo_icon;
	private RelativeLayout top_portion,gift_con;
	private LinearLayout red_bubble;
	
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	// EventAdapter showfollowerlist;
	ArrayList<HashMap<String, String>> followerdata;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	Cursor placedatacursor;
	View rootView;
	private Boolean bottom_visibility=false;
	Notification nt;
	int count = 0;
	private TextView loading,rating_text,gift_text,user_gift_notification;
	public TextView myStatus;
	private DB db;
	private ImageView user_profile;
	//private Gallery userGallery;
	private Context _ctx;
	private Preferences pref;
	private GetUserGiftApi getUserGift;
	//private String[] listData={"Basic Information","Contact Details","About Me","Interested In","Personal Information"};

	
	public ProfilePage(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.profilepage, container, false);
		//userGallery=(Gallery)rootView.findViewById(R.id.userGallery);
				rating_larger=(ImageView)rootView.findViewById(R.id.ImageView01);
			//	photo_larger=(ImageView)rootView.findViewById(R.id.ImageView02);
				gift_larger=(ImageView)rootView.findViewById(R.id.imageView03);
				photo_icon=(LinearLayout)rootView.findViewById(R.id.photo_icon);
				gift_con=(RelativeLayout)rootView.findViewById(R.id.gift_con);
				myStatus=(TextView)rootView.findViewById(R.id.user_profile_status);
				user_profile=(ImageView)rootView.findViewById(R.id.user_profile);
				rating_text=(TextView)rootView.findViewById(R.id.rating_text);
				gift_text=(TextView)rootView.findViewById(R.id.gift_text);
				like_con=(LinearLayout)rootView.findViewById(R.id.like_con);
				prof_con=(LinearLayout)rootView.findViewById(R.id.prof_con);
				red_bubble=(LinearLayout)rootView.findViewById(R.id.red_bubble);
				top_portion=(RelativeLayout)rootView.findViewById(R.id.top_portion);
				super_parent=(LinearLayout)rootView.findViewById(R.id.super_parent);
				loading=(TextView)rootView.findViewById(R.id.loding_text);
				user_gift_notification=(TextView)rootView.findViewById(R.id.user_gift_notification);
				slideUp = AnimationUtils.loadAnimation(getActivity(),
						R.anim.slide_up);
				slideDown=AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
				p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
				db=new DB(_ctx);
				
				slideUp.setAnimationListener(this);
				slideDown.setAnimationListener(this);
				
				rating_larger.setOnClickListener(this);	
			//	photo_larger.setOnClickListener(this);
				gift_larger.setOnClickListener(this);
				//favourite_con.setOnClickListener(this);
				gift_con.setOnClickListener(this);
				like_con.setOnClickListener(this);
				//add_peo_con.setOnClickListener(this);
				prof_con.setOnClickListener(this);
				photo_icon.setOnClickListener(this);

		pref=new Preferences(_ctx);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fragmentManager = getFragmentManager();
		followerdata = new ArrayList<HashMap<String, String>>();
		

		if(NotificationType.someone_send_gift_notification>0){
			red_bubble.setVisibility(View.VISIBLE);
			user_gift_notification.setText(String.valueOf(NotificationType.someone_send_gift_notification));

		}
		else{
			red_bubble.setVisibility(View.GONE);
		}

		
		
		getuserGifts();
		
	return 	rootView;
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.ImageView01:
			
			//Toast.makeText(getActivity(), "Rating Button", Toast.LENGTH_LONG).show();
			break;

		case R.id.ImageView02:
			//Toast.makeText(getActivity(), "Photo Button", Toast.LENGTH_LONG).show();			
			break;
			
		case R.id.imageView03:
			//Toast.makeText(getActivity(), "Gift Button", Toast.LENGTH_LONG).show();		
			break;
			
//		case R.id.favourite_con:
//			Toast.makeText(getActivity(), "Favourite Container", Toast.LENGTH_LONG).show();		
//			break;
			
		case R.id.gift_con:
		//	Toast.makeText(getActivity(), "Gift Container", Toast.LENGTH_LONG).show();		
			pref.set(Constants.OtherUser, "");
			pref.commit();
			red_bubble.setVisibility(View.GONE);
			
			db.delete(Table.Name.notification_master, Table.notification_master.type+" =8 ", null);
			
			((DrawerActivity) getActivity()).displayView(48);
			
			
			break;
			
		case R.id.like_con:
			//Toast.makeText(getActivity(), "Like Container", Toast.LENGTH_LONG).show();	
			((DrawerActivity)getActivity()).displayView(11);
			
			break;
			
//		case R.id.add_peo_con:
//			Toast.makeText(getActivity(), "Add People Container", Toast.LENGTH_LONG).show();		
//			break;


			
			
		case R.id.photo_icon:
			pref.set("images_of", "");
			pref.commit();
			((DrawerActivity) getActivity()).displayView(44);
			
			break;
			
			
		case R.id.prof_con:

			((DrawerActivity) getActivity()).displayView(45);	
			
			break;
			
		default:
			break;
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}	
	
	
public void getuserGifts(){
		p.show();
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
						p.cancel();
						//if(getAlbumApi.photos_of_you.size()>0){
							
							gift_text.setText(""+getUserGift.userdata.size());
							getUser();
						//}
						
					}
				});
				
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
				//getAllUsersFroBanner();
				
			}
			
		};
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_uuid","");  
	     map.put("time_stamp",""); 
	     map.put("type","1"); 
	     getUserGift.setPostData(map);
	     callApi(getUserGift);
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
				Utils.same_id("Error", getString(R.string.no_internet),getActivity());
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
	
	public void getUser(){
		
		db.open();
		Cursor temp;
			data = db.findCursor(DB.Table.Name.user_master," uuid="+"'"+pref.get(Constants.KeyUUID)+"'",null, null);
		Log.d("data sizee", ""+data.getCount());
			data.moveToFirst();
			
			try{
			 if(data.getString(data.getColumnIndex(Table.user_master.profile_pic.toString())).length()>3){
				 String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(Table.user_master.profile_pic.toString()));
			Log.d("arv Image URL", "arv image"+url+"   "+data.getColumnIndex(Table.user_master.about_myself.toString()));
			myStatus.setText(""+data.getString(data.getColumnIndex(Table.user_master.handle_description.toString())));
			rating_text.setText(data.getString(data.getColumnIndex(Table.user_master.photo_count.toString())));
			
			//rating_text.setText(new DecimalFormat("##.##").format(Double.parseDouble(data.getString(data.getColumnIndex(Table.user_master.rating.toString())))));

				 Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(.01f)
						 .into(user_profile);
			
			/*imageLoader.displayImage(url, user_profile,options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					loading.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					loading.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					loading.setVisibility(View.GONE);
				}
			});*/
			 
			 }
		
	}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	
	
}
