package com.ogbongefriends.com.ogbonge.profile;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.ogbonge.Vos.UserVO;
import com.ogbongefriends.com.ogbonge.backstage_album.BackstageAdapter;
import com.ogbongefriends.com.ogbonge.fragment.Get_favourites_API;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.Block_Unblock_Api;
import com.ogbongefriends.com.api.confirm_pointsApi;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.api.getTransactionApi;
import com.ogbongefriends.com.api.sendFriendRequestApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint({ "NewApi", "ResourceAsColor" }) public class full_users_profile extends Fragment implements OnClickListener{

	private View rootView;
	//private LinearLayout ll_user;
	private DB db;
	private ImageLoader imageLoader;
	private Cursor data;
	private ImageView user_profile_image,favorite_image;
	private user_profile_api user_profile_info;
	private Preferences pref;
	private ProgressBar pb,pb1,pb2;
	private String otherUser_uuid="",server_id;
	private CustomLoader p;
	private Context _ctx;
	private LinearLayout giftLayout,horizontal_scroll,starc1,starc2;
	
	private Get_favourites_API getfavoriteApi;
	private LinearLayout favorities_container;
	private RelativeLayout block_parent;
	 private DisplayImageOptions options,option_rounded;
	 private int current_gallery_pic=0,gender_type=0,friend_status=0,block_status=0;
	 private com.ogbongefriends.com.api.getAlbumApi getAlbumApi;
	 private ImageView r1,r2,r3,r4,r5,snc1,snc2,like_her_image;
	private TextView body_type,height,weight,edu_status,job_status,looking_for,block_text,_for,age_range,name_age,about_myself,location,last_visit,about_me,rate,snc1_text,snc2_text,like_text,no_backstage,followers1,followers2;
	private int UserRating=0;
	private int points,item_points;
	private BackstageAdapter backstageAdapter;
	public static int myPhoto_count=0, backstage_purchase_status=0,backstage_comment_status=0;

	public static ArrayList<PhotoOfYouVO>myPhotoVo=new ArrayList<PhotoOfYouVO>();
	/*public static ArrayList<Integer>myPhoto_id=new ArrayList<Integer>();
	public static ArrayList<String>myPhoto=new ArrayList<String>();*/

	public static ArrayList<Integer>myPhoto_type=new ArrayList<Integer>();
	public static ArrayList<Boolean>backstage_photo_comment_status=new ArrayList<Boolean>();
	private confirm_pointsApi confirm_points_api;
	public JsonObject _jsondata;
	private Block_Unblock_Api blockApi;
	private getTransactionApi get_transaction_api;
	private sendFriendRequestApi sendFriendRequest;
	AnimationDrawable animPlaceholder;
	LinearLayout.LayoutParams lp;
	public full_users_profile(Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView==null){
	 rootView = inflater.inflate(R.layout.full_user_profile, container, false);
	 //ll_user=(LinearLayout)rootView.findViewById(R.id.parent_user_images);
	 p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
	 pref=new Preferences(getActivity());
			myPhotoVo.clear();
	 myPhoto_type.clear();
	 backstage_photo_comment_status.clear();
	 _jsondata=new JsonObject();
	 name_age=(TextView)rootView.findViewById(R.id.user_name_age);
	 about_myself=(TextView)rootView.findViewById(R.id.about_myself);
	 location=(TextView)rootView.findViewById(R.id.User_location);
	 last_visit=(TextView)rootView.findViewById(R.id.user_last_visit);
	 about_me=(TextView)rootView.findViewById(R.id.user_about_me);
	 no_backstage=(TextView)rootView.findViewById(R.id.textView5);
	 giftLayout=(LinearLayout)rootView.findViewById(R.id.giftlayout);
	 user_profile_image=(ImageView)rootView.findViewById(R.id.user_profile);
	 like_her_image=(ImageView)rootView.findViewById(R.id.imageView1);
	 horizontal_scroll=(LinearLayout)rootView.findViewById(R.id.horizontal_scroll);
	 favorities_container=(LinearLayout)rootView.findViewById(R.id.favorities_container);
	 favorite_image=(ImageView)rootView.findViewById(R.id.favorite_image);
	 block_text=(TextView)rootView.findViewById(R.id.block_text);
	 starc1=(LinearLayout)rootView.findViewById(R.id.starc1);
	 starc2=(LinearLayout)rootView.findViewById(R.id.starc2);
	 block_parent=(RelativeLayout)rootView.findViewById(R.id.block_parent);
	 pb=(ProgressBar)rootView.findViewById(R.id.progressBar1);
	 pb1=(ProgressBar)rootView.findViewById(R.id.progressBar2);
	 pb2=(ProgressBar)rootView.findViewById(R.id.progressBar3);
	 r1=(ImageView)rootView.findViewById(R.id.imageView3);
	r2=(ImageView)rootView.findViewById(R.id.imageView4);
	r3=(ImageView)rootView.findViewById(R.id.imageView5);
	r4=(ImageView)rootView.findViewById(R.id.imageView6);
	r5=(ImageView)rootView.findViewById(R.id.imageView7);
		
	snc1=(ImageView)rootView.findViewById(R.id.snc1);
	snc2=(ImageView)rootView.findViewById(R.id.snc2);
	 db=new DB(getActivity());
	snc1_text=(TextView)rootView.findViewById(R.id.snc1_text);
	 snc2_text=(TextView)rootView.findViewById(R.id.snc2_text);
	 followers2=(TextView)rootView.findViewById(R.id.followers2);
	 followers1=(TextView)rootView.findViewById(R.id.followers1);
	rate=(TextView)rootView.findViewById(R.id.rate);
	body_type=(TextView)rootView.findViewById(R.id.body_type);
	 height=(TextView)rootView.findViewById(R.id.height);
	 weight=(TextView)rootView.findViewById(R.id.weight);
	 edu_status=(TextView)rootView.findViewById(R.id.edu_status);
	 job_status=(TextView)rootView.findViewById(R.id.job_status);
	 looking_for=(TextView)rootView.findViewById(R.id.looking_for);
	 _for=(TextView)rootView.findViewById(R.id._for);
	 like_text=(TextView)rootView.findViewById(R.id.like_text);
	 age_range=(TextView)rootView.findViewById(R.id.age_range);



	 
	 giftLayout.setOnClickListener(this);
	 
	 
	 
	 like_her_image.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			if(friend_status==0){
				p.show();
			sendFriendRequest=new sendFriendRequestApi(getActivity(), db, p){

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
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
				
				}

				@SuppressLint("ResourceAsColor") @Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
					//showuser();
					getActivity().runOnUiThread(new Runnable() {
						@SuppressLint("ResourceAsColor") public void run() {
							if(sendFriendRequest.resCode==1){
								if(friend_status==0){
									friend_status=1;
									if(gender_type==1){
										like_text.setText("I Liked Him");
										like_text.setTextColor(R.color.green_btn);
										}
										else{
											like_text.setText("I Liked her");
											like_text.setTextColor(R.color.green_btn);
										}
								}
							else{
								friend_status=0;
								if(gender_type==1){
									like_text.setText("I Like Him");
									}
									else{
										like_text.setText("I Like her");
									}
							}
							
						}
						}
						
					});
				}
				
			};
			
			
			 HashMap<String, String>map=new HashMap<String, String>();
		     map.put("uuid", pref.get(Constants.KeyUUID));
		     map.put("auth_token", pref.get(Constants.kAuthT));
		     map.put("receiver_uuid",otherUser_uuid);  
		     map.put("type",(friend_status==0)?String.valueOf(1):String.valueOf(2)); 
		     sendFriendRequest.setPostData(map);
		     callApi(sendFriendRequest);
				
		}
		}
	});
	 
	 blockApi=new Block_Unblock_Api(getActivity(), db, p){

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
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				//showuser();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(blockApi.resCode==1){
							if(block_status==0){
								block_status=1;
									block_text.setText("Blocked");
						Utils.same_id("Message", "Member Blocked Successfully", _ctx);
							}
							
						else{
							block_status=0;
							block_text.setText("Block");
							Utils.same_id("Message", "Member Unblocked Successfully", _ctx);
								}
						
					}
						else{
							Utils.same_id("Message", blockApi.resMsg, _ctx);
						}
					}
					
				});
			}
			
		};
	 
	 block_parent.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			String str="";
			//	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);

				final Dialog dialog = new Dialog(_ctx);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	            dialog.setContentView(R.layout.message_to_block);
	           TextView title=(TextView)dialog.findViewById(R.id.title);
	           
	           final EditText msg=(EditText)dialog.findViewById(R.id.editText1);
				
	           Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
				Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
				title.setText("Confirmation..");
				if(block_status==1){
					msg.setVisibility(View.GONE);
					 str="Are you sure want to Unblock ?";
				}
				else{
					msg.setVisibility(View.VISIBLE);
					str="Are you sure want to Block ?";
				}
				title.setText(str);
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
						
						if(block_status!=1){
						if(msg.getText().length()>0 ){
						
						dialog.dismiss();
						p.show();
						 HashMap<String, String>map=new HashMap<String, String>();
					     map.put("uuid", pref.get(Constants.KeyUUID));
					     map.put("auth_token", pref.get(Constants.kAuthT));
					     map.put("other_uuid",otherUser_uuid);  
					     map.put("message",msg.getText().toString()); 
					     map.put("time_stamp",""); 
					     map.put("type",(block_status==0)?String.valueOf(1):String.valueOf(2)); 
					     blockApi.setPostData(map);
					     callApi(blockApi);
					     
						}
						else{
							Utils.same_id("Message", "message should not be blank", _ctx);
						}
						}
						else{
							dialog.dismiss();
							p.show();
							 HashMap<String, String>map=new HashMap<String, String>();
						     map.put("uuid", pref.get(Constants.KeyUUID));
						     map.put("auth_token", pref.get(Constants.kAuthT));
						     map.put("other_uuid",otherUser_uuid);  
						     map.put("message",msg.getText().toString()); 
						     map.put("time_stamp",""); 
						     map.put("type",(block_status==0)?String.valueOf(1):String.valueOf(2)); 
						     blockApi.setPostData(map);
						     callApi(blockApi);
						}
					     
					}
				});
			
				// show it
	            dialog.show();
				
			
					}
			
		});
	 
	 
	 File cacheDir = StorageUtils.getCacheDirectory(getActivity());
	 		
	 option_rounded = new DisplayImageOptions.Builder()
     .resetViewBeforeLoading(false)  // default
     .delayBeforeLoading(1000)
     .displayer(new RoundedBitmapDisplayer(100))
     .cacheInMemory(true) // default
     .cacheOnDisk(true) // default
     .considerExifParams(false) // default
     .build();
	 
	 options = new DisplayImageOptions.Builder()
     .resetViewBeforeLoading(false)  // default
     .delayBeforeLoading(1000)
     .displayer(new RoundedBitmapDisplayer(15))
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
			
			ImageLoader.getInstance().init(config);
	 		
	 	 imageLoader = ImageLoader.getInstance();
	 
	
	 p.show();
	 otherUser_uuid=pref.get(Constants.OtherUser);
	 db.open();
	 getUserInfoFromDB();

	 pref.set(Constants.OtherUserName, name_age.getText().toString());
	 pref.commit();
	 com.ogbongefriends.com.common.Log.d("arv", "arv otherUUIOd" + otherUser_uuid);
	 getUserProfile();

	 backstageAdapter=new BackstageAdapter(_ctx,myPhotoVo,server_id) {

			@Override
			protected void onItemClick(View v, int string, String path) {
				// TODO Auto-generated method stub
				pref.set(Constants.SelectedImage, string);
				
				pref.commit();	
				((DrawerActivity) getActivity()).displayView(35);
			}

			@Override
			protected void onLongItemClick(View v, int string, String path) {
				// TODO Auto-generated method stub
//			Log.d("Arv", "clicked on "+path);
//			del_photo_id=path;
//			callPropmtForDelete();
			}
			
			
		};
	 
		
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
			
			
			
			
			
		
		
	 favorities_container.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	
			
		}
	});
	 

		}
		return rootView;
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
				p.cancel();

				backstage_purchase_status=1;


			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				bindImagesOnScrollView();


				
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
	
	public void getAlbums(){
		
		getAlbumApi=new getAlbumApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				p.cancel();
				getUserInfoFromDB();
				myPhotoVo.clear();
				myPhoto_type.clear();
				backstage_photo_comment_status.clear();

				if(getAlbumApi.photos_of_you.size()>0){
					myPhoto_count=getAlbumApi.photos_of_you.size();
					for(int i=0;i<getAlbumApi.photos_of_you.size();i++){

						//JsonObject json=getAlbumApi.photos_of_you.get(i).getAsJsonObject();
						PhotoOfYouVO photoOfYouVO=new PhotoOfYouVO(getAlbumApi.photos_of_you.get(i).getAsJsonObject());
						photoOfYouVO.setfullPhotoUrl(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+server_id+"/photos_of_you/"+photoOfYouVO.getPhoto_pic());
					myPhotoVo.add(photoOfYouVO);

					myPhoto_type.add(1);
					backstage_photo_comment_status.add(false);
					}
				}


				if(getAlbumApi.backstage_photos.size()>0){
					for(int i=0;i<getAlbumApi.backstage_photos.size();i++){
						PhotoOfYouVO photoOfYouVO=new PhotoOfYouVO(getAlbumApi.backstage_photos.get(i).getAsJsonObject());
						photoOfYouVO.setfullPhotoUrl(getActivity().getString(R.string.urlString) + "userdata/image_gallery/" + server_id + "/backstage_photos/" + photoOfYouVO.getPhoto_pic());
						myPhotoVo.add(photoOfYouVO);
					if(i==0){
					myPhoto_type.add(2);
					backstage_photo_comment_status.add(false);
					}
					else{
						myPhoto_type.add(3);
						backstage_photo_comment_status.add(false);
					}
					}
				}

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
				p.cancel();

				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (getAlbumApi.backstage_photos.size() > 0) {
							no_backstage.setText("Backstage photos");
						} else {
							no_backstage.setText("No Backstage Photo");
						}

					}
				});
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub

				super.updateUI();

				bindImagesOnScrollView();

				
			}
			
		};
		HashMap<String, String>keydata=new HashMap<String, String>();
		keydata.put("uuid", pref.get(Constants.KeyUUID));
		keydata.put("auth_token", pref.get(Constants.kAuthT));
		keydata.put("other_uuid", otherUser_uuid);
		keydata.put("time_stamp", "");
		
		getAlbumApi.setPostData(keydata);
		callApi(getAlbumApi);
	}
	
	public void getPointInfo(){
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
				p.cancel();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						showPrompt(_jsondata);
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
	     map.put("other_uuid",otherUser_uuid);  
	     map.put("time_stamp",""); 
	     map.put("type","1"); 
	    
	   
	     confirm_points_api.setPostData(map);
	     callApi(confirm_points_api);
		
	}
	
		public void	showPrompt(JsonObject jsn){
		
		Log.d("arv", "arv"+jsn.get("points").getAsString()+"  "+jsn.get("item_points").getAsString());
		points=Integer.parseInt(jsn.get("points").getAsString());
		//item_points=Integer.parseInt(jsn.get("item_points").getAsString());
		
		
		if(points>item_points){
			final Dialog dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
            dialog.setContentView(R.layout.confirmation_dialog);
            TextView title=(TextView)dialog.findViewById(R.id.title);
            TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			ok_btn.setText("Continue");
			cancel_btn.setText(getString(R.string.cancel));
			title.setText(getString(R.string.app_name));
			String str="You have "+points+ " points, "+ item_points +" points will be deducted from your account";
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
					 HashMap<String, String>map=new HashMap<String, String>();
				     map.put("uuid", pref.get(Constants.KeyUUID));
				     map.put("auth_token", pref.get(Constants.kAuthT));
				     map.put("other_uuid",otherUser_uuid);  
				     map.put("time_stamp",""); 
				     map.put("type","3"); 
				     dialog.dismiss();
				     get_transaction_api.setPostData(map);
					p.show();
				  callApi(get_transaction_api);
				   
				}
			});
            
		
		
		// show it
            dialog.show();
		
		}
		
		else{
			
			
			
			final Dialog dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
            dialog.setContentView(R.layout.confirmation_dialog);
           TextView title=(TextView)dialog.findViewById(R.id.title);
           TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			ok_btn.setText("Continue");
			cancel_btn.setText(getString(R.string.cancel));
			title.setText(getString(R.string.app_name));
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
					((DrawerActivity) getActivity()).displayView(55);
				}
			});
			dialog.show();
		}
		}





private void bindImagesOnScrollView(){
	horizontal_scroll.removeAllViews();
	
	for(int i=0;i<myPhotoVo.size();i++)
	{	
 	RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final RelativeLayout parent = new RelativeLayout(getActivity());
		ImageView imagView = new ImageView(getActivity());
		 ProgressBar pb=new ProgressBar(getActivity());
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
		
		if(myPhoto_type.get(i)==1){
		
		parent.setLayoutParams(parent_param);
		parent.setTag(String.valueOf(i));
		parent.setTag(R.id.account_setting_text, i);
		parent.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		//	if(myPhoto_type.get(Integer.parseInt((String) parent.getTag()))==1){
				pref.set(Constants.SelectedImage, Integer.parseInt((String) parent.getTag()));
				pref.commit();
				((DrawerActivity) getActivity()).displayView(39);
			//}
//			else{
//				if(backstage_purchase_status==0){
//				getPointInfo();
//				}
//				else{
//					((DrawerActivity) getActivity()).displayView(39);
//				}
//			}
		}
		});

		String url=myPhotoVo.get(i).getfullPhotoUrl();

		
				

		imagView.setScaleType(ScaleType.FIT_XY);

		horizontal_scroll.setOrientation(LinearLayout.HORIZONTAL);

	

		  lp.setMargins(5, 0, 0, 0);
		    imagView.setLayoutParams(lp);
		  //  imagView.setTag(url);
		    imagView.setImageResource(R.drawable.profile);
			AnimationDrawable   animPlaceholder = (AnimationDrawable)getActivity().getResources().getDrawable(R.drawable.image_loading_animation);
			animPlaceholder.start();

			Glide.with(_ctx).load(url).listener(new RequestListener<String, GlideDrawable>() {
				@Override
				public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
					Log.e("Ready***************",model);
					//e.printStackTrace();
					return false;
				}

				@Override
				public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
					Log.e("Ready***************","ready");
						return false;
				}
			}).placeholder(animPlaceholder).into(imagView);


		    /*imageLoader.displayImage(url, imagView, options,new ImageLoadingListener() {


		@Override

		public void onLoadingStarted(String arg0, View arg1) {

		}


		@Override

		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

		pb.setVisibility(View.GONE);

		}


		@Override

		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

		// TODO Auto-generated method stub

			pb.setVisibility(View.GONE);

		}


		@Override

		public void onLoadingCancelled(String arg0, View arg1) {

		// TODO Auto-generated method stub

			pb.setVisibility(View.GONE);

		}

		});*/
		    parent.addView(imagView);
		    horizontal_scroll.addView(parent);
	}
	
		
		
		else{	
	if(backstage_purchase_status==0){
	RelativeLayout.LayoutParams parent_paramLast = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);



	final RelativeLayout parent_last = new RelativeLayout(getActivity());

	parent_last.setLayoutParams(parent_paramLast);
	// imagView = new ImageView(getActivity());
	
			
			imagView.setImageResource(R.drawable.lock_backstage);
			imagView.setScaleType(ScaleType.FIT_XY);
			//imagView.setBackgroundResource(R.drawable.lock_backstage);
			parent_last.addView(imagView);
			horizontal_scroll.addView(parent_last);
			
			parent_last.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					p.show();
					getPointInfo();
				}
			});
			
//			parent_last.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//
//				public void onClick(View v) {
//
//						if(backstage_purchase_status==0){
//						getPointInfo();
//						}
//						else{
//							((DrawerActivity) getActivity()).displayView(39);
//						}
//					
//
//				}
//
//				});
			
			break;
	}
	else{
		parent.setLayoutParams(parent_param);
		parent.setTag(String.valueOf(i));
		parent.setTag(R.id.account_setting_text, i);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//			if(myPhoto_type.get(Integer.parseInt((String) parent.getTag()))==1){
//				pref.set(Constants.SelectedImage, Integer.parseInt((String) parent.getTag()));
//				pref.commit();	
//				((DrawerActivity) getActivity()).displayView(39);
//			}
//			else{
//				if(backstage_purchase_status==0){
//				getPointInfo();
//				}
//				else{
				pref.set(Constants.SelectedImage, Integer.parseInt((String) parent.getTag()));
				pref.commit();
				((DrawerActivity) getActivity()).displayView(39);
				//	}
				//	}
			}
		});

		String url=myPhotoVo.get(i).getfullPhotoUrl();

		
				

		imagView.setScaleType(ScaleType.FIT_XY);

		horizontal_scroll.setOrientation(LinearLayout.HORIZONTAL);


		lp.setMargins(5, 0, 0, 0);
		    imagView.setLayoutParams(lp);
		   // imagView.setTag(url);
		    imagView.setImageResource(R.drawable.profile);
		AnimationDrawable   animPlaceholder = (AnimationDrawable)getActivity().getResources().getDrawable(R.drawable.image_loading_animation);
		animPlaceholder.start();

		Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
			@Override
			public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				Log.e("Ready***************", "ready");
				return false;
			}
		}).placeholder(animPlaceholder).into(imagView);

		/*
		    imageLoader.displayImage(url, imagView, new ImageLoadingListener() {


		@Override

		public void onLoadingStarted(String arg0, View arg1) {

		}


		@Override

		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

		pb.setVisibility(View.GONE);

		}


		@Override

		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

		// TODO Auto-generated method stub

			pb.setVisibility(View.GONE);

		}


		@Override

		public void onLoadingCancelled(String arg0, View arg1) {

		// TODO Auto-generated method stub

			pb.setVisibility(View.GONE);

		}

		});*/
		    parent.addView(imagView);
		    horizontal_scroll.addView(parent);
	}
		}
	}
}


	private void getUserProfile(){
		
		 
	     user_profile_info=new user_profile_api(getActivity(), db, p){

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
				getAlbums();
				getUserInfoFromDB();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				
				super.updateUI();

				showuser(otherUser_uuid);
			}
	    	 
	     };
	     
	     
	     HashMap<String, String>map=new HashMap<String, String>();
	     
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid", otherUser_uuid);
	     map.put("time_stamp", "");  
	     user_profile_info.setPostData(map);
	     callApi(user_profile_info);
	     
	    
	}

	private void getUserInfoFromDB(){

		data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+ otherUser_uuid +"'", null, null);
		while(data != null &&data.moveToNext())
		{

			server_id=data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()));
			try
			{
				Log.d("arv", "arvi"+data.getString(data.getColumnIndex(DB.Table.user_master.backstage_album_cost.toString())));
				item_points=Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_master.backstage_album_cost.toString())));

				data.notify();
			}
			catch(Exception e)
			{
				//Log.d("Ogbonge", e);

			}
		}
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

	@SuppressLint("ResourceAsColor") private void showuser(String uuid){
		// TODO Auto-generated method stub
	db.open();
	Cursor temp;
		data = db.findCursor(DB.Table.Name.user_master," uuid="+"'"+uuid+"'",null, null);
	Log.d("data sizee", ""+data.getCount());
		data.moveToFirst();
		
		try{
			about_myself.setText(data.getString(data.getColumnIndex(DB.Table.user_master.handle_description.toString())));
		 about_me.setText(data.getString(data.getColumnIndex(DB.Table.user_master.about_myself.toString())));
		 
		 
		 //age=CelUtils.getAgeStrFromDOB(data.getString(data.getColumnIndex(Table.user_master.date_of_birth.toString())));
		// profile_user_name.setText(data.getString(data.getColumnIndex(Table.user_master.first_name.toString()))+" "+data.getString(data.getColumnIndex(Table.user_master.last_name.toString()))+", "+age);
		
		 name_age.setText(data.getString(data.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+data.getString(data.getColumnIndex(DB.Table.user_master.last_name.toString()))+","+ CelUtils.getAgeStrFromDOB(data.getString(data.getColumnIndex(DB.Table.user_master.date_of_birth.toString()))));
		
		 
		 age_range.setText("("+data.getString(data.getColumnIndex(DB.Table.user_master.meet_min_age.toString()))+"-"+data.getString(data.getColumnIndex(DB.Table.user_master.meet_max_age.toString()))+") years");
		location.setText(""+data.getString(data.getColumnIndex(DB.Table.user_master.city.toString())));
	
		
		if(data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString())).length()>0){
			last_visit.setVisibility(View.VISIBLE);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    df.setTimeZone(TimeZone.getTimeZone("GMT"));
		    Date timestamp = null;
		    try {
				timestamp = df.parse(data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString())));
				timestamp.setHours(timestamp.getHours()+4);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    timestamp.getTime();
		    
		    String parsed = df.format(timestamp);
		    long tim = 0;
		    try {
			 tim=df.parse(parsed).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    System.out.println("New = " + parsed);
		    long milli = 
		    Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
		    
		    Log.d("ARVI", ""+(milli-tim)/(1000*60)+"min");
		    long millis=milli-tim;
		    
		    String hms="";

			if(TimeUnit.MILLISECONDS.toDays(millis)>0){

				if(TimeUnit.MILLISECONDS.toDays(millis)>14){
					hms = String.format("%02d weeks ", TimeUnit.MILLISECONDS.toDays(millis));
					last_visit.setText("more than 2 weeks ago");

				}
				else{
					if(TimeUnit.MILLISECONDS.toDays(millis)>1) {
						hms = String.format("%02d days ", TimeUnit.MILLISECONDS.toDays(millis));
					}
					else{
						hms = String.format("%02d day ", TimeUnit.MILLISECONDS.toDays(millis));
					}
					last_visit.setText("Last visit "+hms+" ago");
				}
			}
			else{
				if((TimeUnit.MILLISECONDS.toHours(millis)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)))>0){
					hms=	String.format("%02d hour ", TimeUnit.MILLISECONDS.toHours(millis)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)));
				}
				else{
					if((TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))>0){
						hms=	String.format("%02d min ", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
					}
					else{
						if((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))>0){
							hms=	String.format("%02d sec", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
						}
					}
				}
				last_visit.setText("Last visit "+hms+" ago");
			}
	}
		
		
		
		//last_visit.setText("Last visit "+CelUtils.dateDifrence(CelUtils.StringToDate(Constants.kTimeStampFormat, data.getString(data.getColumnIndex(Table.user_master.last_seen.toString()))).getTime()/1000));
		
		
		UserRating=Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_master.rate_count.toString())));
		gender_type=data.getInt(data.getColumnIndex(DB.Table.user_master.gender.toString()));
		friend_status=data.getInt(data.getColumnIndex(DB.Table.user_master.friend_status.toString()));
		
		
if(data.getInt(data.getColumnIndex(DB.Table.user_master.gender.toString()))==1){
	if(friend_status==0){
		like_text.setText("I Like Him");
	}else{
		like_text.setText("I Liked Him");
		like_text.setTextColor(R.color.green_btn);
	}
}
else{
if(friend_status==0){
	like_text.setText("I Like Her");
	}else{
		like_text.setText("I Liked Her");
		like_text.setTextColor(R.color.green_btn);
	}
}
		
		rate.setText(""+UserRating);
		if(UserRating>0 && UserRating<=1){
			r1.setImageResource(R.drawable.rating);
			r2.setImageResource(R.drawable.no_rating);
			r3.setImageResource(R.drawable.no_rating);
			r4.setImageResource(R.drawable.no_rating);
			r5.setImageResource(R.drawable.no_rating);
		}
if(1<UserRating && UserRating<=2){
		r1.setImageResource(R.drawable.rating);
		r2.setImageResource(R.drawable.rating);
		r3.setImageResource(R.drawable.no_rating);
		r4.setImageResource(R.drawable.no_rating);
		r5.setImageResource(R.drawable.no_rating);
		}

if(2<UserRating && UserRating<=3){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}

if(3<UserRating && UserRating<=4){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.rating);
	r5.setImageResource(R.drawable.no_rating);
}

if(4<UserRating && UserRating<=5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.rating);
	r5.setImageResource(R.drawable.rating);
}
		
		
		 if(data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString())).length()>3){
			 String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
			 Log.d("arv Image URL", "arv image"+url);

			/* AnimationDrawable   animPlaceholder = (AnimationDrawable)getActivity().getResources().getDrawable(R.drawable.image_loading_animation);
			 animPlaceholder.start();
			 Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(0.01f).into(user_profile_image);
*/
		 imageLoader.displayImage(url, user_profile_image,options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
		});
		 
		 }
		 
		 
		 temp=db.findCursor(DB.Table.Name.bodytype_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.bodytype_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 body_type.setText(temp.getString(temp.getColumnIndex(DB.Table.bodytype_master.bodytype_content.toString())));
		 }
		 
		 Log.d("arv", "arv"+data.getString(data.getColumnIndex(DB.Table.user_master.height_master_id.toString())));
		 temp=db.findCursor(DB.Table.Name.height_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.height_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 height.setText(temp.getString(temp.getColumnIndex(DB.Table.height_master.length.toString())));
		 }
		 Log.d("arv", "arv"+data.getString(data.getColumnIndex(DB.Table.user_master.weight_master_id.toString())));
		 temp=db.findCursor(DB.Table.Name.weight_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.weight_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 weight.setText(temp.getString(temp.getColumnIndex(DB.Table.weight_master.weight.toString())));
		 }
		 
		 Log.d("11111", ""+data.getString(data.getColumnIndex(DB.Table.user_master.education_master_id.toString())));
		 Log.d("2222222", ""+data.getString(data.getColumnIndex(DB.Table.user_master.job_master_id.toString())));
		 
		 temp=db.findCursor(DB.Table.Name.education_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.education_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 edu_status.setText(temp.getString(temp.getColumnIndex(DB.Table.education_master.name.toString())));
		 }
		 
		 temp=db.findCursor(DB.Table.Name.job_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.job_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 job_status.setText(temp.getString(temp.getColumnIndex(DB.Table.job_master.name.toString())));
		 }
		 
		 temp=db.findCursor(DB.Table.Name.interestedin_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.interestedin_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
		 looking_for.setText(temp.getString(temp.getColumnIndex(DB.Table.interestedin_master.interestedin_content.toString())));
		 }
		 
		 temp=db.findCursor(DB.Table.Name.interestedin_purpose_master,"id="+data.getString(data.getColumnIndex(DB.Table.user_master.interestedin_purpose_master_id.toString())),null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
			// Log.d("arv", "arv"+)
		 _for.setText(temp.getString(temp.getColumnIndex(DB.Table.interestedin_purpose_master.purpose.toString())));
		 }
		 
		 
		 temp = db.findCursor(DB.Table.Name.user_starsandclubs, DB.Table.user_starsandclubs.user_master_id+" = "+"'"+uuid+"'",null, null);
		 if(temp.getCount()>0){
			 temp.moveToFirst();
			 
			 followers1.setText(temp.getString(temp.getColumnIndex(DB.Table.user_starsandclubs.followers.toString())));
			 Cursor snc_data1=db.findCursor(DB.Table.Name.starsandclubs_master, "id = "+temp.getString(temp.getColumnIndex(DB.Table.user_starsandclubs.starsandclubs_master_id.toString())), null, null);
			 if(snc_data1.getCount()>0){
				 snc_data1.moveToFirst();
			 snc1_text.setText(snc_data1.getString(snc_data1.getColumnIndex(DB.Table.starsandclubs_master.sac_title.toString())));
			
			 Log.d("URL si", getActivity().getString(R.string.urlString)+"userdata/stars_and_clubs/"+snc_data1.getString(snc_data1.getColumnIndex(DB.Table.starsandclubs_master.sac_image.toString())));
			 
			 imageLoader.displayImage(getActivity().getString(R.string.urlString)+"userdata/stars_and_clubs/"+snc_data1.getString(snc_data1.getColumnIndex(DB.Table.starsandclubs_master.sac_image.toString())), snc1,option_rounded, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pb1.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					pb1.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					pb1.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pb1.setVisibility(View.GONE);
				}
			});
			 
			 
			 
			 if(temp.moveToNext()){
			 
			 
			 snc_data1=db.findCursor(DB.Table.Name.starsandclubs_master, "id = "+temp.getString(temp.getColumnIndex(DB.Table.user_starsandclubs.starsandclubs_master_id.toString())), null, null);
			
				 snc_data1.moveToFirst();
				 followers2.setText(temp.getString(temp.getColumnIndex(DB.Table.user_starsandclubs.followers.toString())));
			 snc2_text.setText(snc_data1.getString(snc_data1.getColumnIndex(DB.Table.starsandclubs_master.sac_title.toString())));
			 imageLoader.displayImage(getActivity().getString(R.string.urlString)+"userdata/stars_and_clubs/"+snc_data1.getString(snc_data1.getColumnIndex(DB.Table.starsandclubs_master.sac_image.toString())), snc2,option_rounded, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pb2.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					pb2.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					pb2.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pb2.setVisibility(View.GONE);
				}
			});
			 
			 }
			 else{
				 starc2.setVisibility(View.GONE);
			 }
			 }
			 else{
				 starc1.setVisibility(View.GONE);
				 starc2.setVisibility(View.GONE);
			 }
		 }
		 
		 temp=db.findCursor(DB.Table.Name.user_block_master, DB.Table.user_block_master.user_master_id.toString()+" = '"+pref.get(Constants.KeyUUID)+"' AND "+ DB.Table.user_block_master.other_user_master_id.toString()+" = '"+otherUser_uuid+"'", null, null);
		 temp.moveToFirst();
		 block_status=temp.getInt(temp.getColumnIndex(DB.Table.user_block_master.status.toString()));
		 
		 if(block_status==0){
			block_text.setText("Block"); 
		 }
		 else{
			 block_text.setText("Blocked"); 
		 }
//		 
//		 if(temp.getCount()==2){
//			 temp.moveToFirst();
//			 
//		 _for.setText(temp.getString(temp.getColumnIndex(Table.interestedin_purpose_master.purpose.toString())));
//		 }
		  
		 
		 
		}
		catch(Exception e){
			e.printStackTrace();
		}
			finally{
			p.cancel();
			}
		}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.giftlayout:
		
		Log.d("arv", "arv "+otherUser_uuid);
		pref.set(Constants.OtherUser, otherUser_uuid);
		pref.commit();
		((DrawerActivity) getActivity()).displayView(48);
		
		break;

	default:
		break;
	}
	
}

}
