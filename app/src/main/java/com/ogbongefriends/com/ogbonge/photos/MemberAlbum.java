package com.ogbongefriends.com.ogbonge.photos;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.confirm_pointsApi;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.api.getTransactionApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class MemberAlbum extends Fragment implements Runnable,MyLocationListener,OnClickListener,OnCheckedChangeListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;
private Context _ctx;
private CustomLoader p;
private DB db;
private String del_item="";
private View rootView;
private AlbumAdapter albumAdapter;

	public static ArrayList<PhotoOfYouVO>myPhotoVo=new ArrayList<PhotoOfYouVO>();
/*public static ArrayList<String>myPhoto=new ArrayList<String>();
public static ArrayList<Integer>myPhoto_id=new ArrayList<Integer>();
public static ArrayList<Boolean>checkStatus=new ArrayList<Boolean>();
public static ArrayList<String>myPhotoFullUrl=new ArrayList<String>();*/

public static ArrayList<Integer>myPhoto_type=new ArrayList<Integer>();

private TextView photo_of_user,private_photos_text;
private String server_id,userName;
private ProgressBar p1;
private Gallery gallery;
	private user_profile_api user_profile_info;
public JsonObject _jsondata;
private confirm_pointsApi confirm_points_api;
private ImageView image;
private int points,item_points;
	private getTransactionApi get_transaction_api;
public static int myPhoto_count=0, backstage_purchase_status=0;
private LinearLayout horizontal_scroll;
private MemberAlbumAdapter circle_gallery_adapter;
	AnimationDrawable animPlaceholder;
public static int screenSize ;


private getAlbumApi getAlbumApi;

	
	public MemberAlbum(Context ctx) {
		_ctx=ctx;
	}
	public MemberAlbum() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
		 pref=new Preferences(_ctx);

			db=new DB(_ctx);
			 _jsondata=new JsonObject();
		p.show();
		myPhotoVo.clear();
		 myPhoto_type.clear();
		Cursor c;
		if(pref.get("images_of").length()>3){
			Log.d("pref******", ""+pref.get("images_of"));
			c=db.findCursor(Table.Name.user_master, Table.user_master.uuid+" = "+"'"+ pref.get("images_of") +"'", null, null);
			Log.d("pref******", ""+c.getCount());
		}
		else{
			
			 c=db.findCursor(Table.Name.user_master, Table.user_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'", null, null);
			 Log.d("pref******", ""+c.getCount());
		}
			c.moveToFirst();
			 server_id=c.getString(c.getColumnIndex(Table.user_master.server_id.toString()));
			 userName=c.getString(c.getColumnIndex(Table.user_master.first_name.toString()))+" "+c.getString(c.getColumnIndex(Table.user_master.last_name.toString()));
			 item_points=Integer.parseInt(c.getString(c.getColumnIndex(Table.user_master.backstage_album_cost.toString())));
			 DrawerActivity.account_setting.setText("Photo Of "+userName);
	rootView = inflater.inflate(R.layout.member_album, container, false);
	 horizontal_scroll=(LinearLayout)rootView.findViewById(R.id.horizontal_scroll);
	gallery=(Gallery)rootView.findViewById(R.id.gallery1);
	image=(ImageView)rootView.findViewById(R.id.image1);
	p1=(ProgressBar)rootView.findViewById(R.id.pb1);
	image.setScaleType(ScaleType.FIT_XY);
	

	 screenSize = getResources().getConfiguration().screenLayout &
	        Configuration.SCREENLAYOUT_SIZE_MASK;

		

	circle_gallery_adapter = new MemberAlbumAdapter(getActivity(), myPhotoVo,screenSize) {

				@Override
				protected void onItemClick(View v, int string) {
					// TODO Auto-generated method stub
					
				//	imageLoader.displayImage(myPhotoFullUrl.get(string), image);
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					// TODO Auto-generated method stub
					
					imageLoader.displayImage(myPhotoVo.get(position).getfullPhotoUrl(), image);
					return super.getView(position, convertView, parent);
					
				}
				};
				
				gallery.setAdapter(circle_gallery_adapter);	
				
				
				albumAdapter=new AlbumAdapter(_ctx,myPhotoVo,server_id) {
			
			@Override
			protected void onItemClick(View v, int string) {
				// TODO Auto-generated method stub
				pref.set(Constants.SelectedImage, string);
				pref.commit();	
				((DrawerActivity) getActivity()).displayView(36);
			}

					@Override
					protected void onlongClick(View v, int string) {

					}
				};
	//	my_photos.setAdapter(albumAdapter);
		
		//ViewGroup.LayoutParams layoutParams = my_photos.getLayoutParams();
		//layoutParams.height = 50*urls.length; //this is in pixels
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
		
	//	my_photos.setLayoutParams(layoutParams);
		getAlbums();
		
	return 	rootView;
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

			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				//bindImagesOnScrollView();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						backstage_purchase_status = 1;
						bindImagesOnScrollView();
					}
				});

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
			myPhotoVo.clear();
					myPhoto_type.clear();
				
				if(getAlbumApi.photos_of_you.size()>0){
					myPhoto_count=getAlbumApi.photos_of_you.size();
					for(int i=0;i<getAlbumApi.photos_of_you.size();i++){
						PhotoOfYouVO photoOfYouVO=new PhotoOfYouVO(getAlbumApi.photos_of_you.get(i).getAsJsonObject());
						photoOfYouVO.setfullPhotoUrl(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+server_id+"/photos_of_you/"+photoOfYouVO.getPhoto_pic());
						myPhotoVo.add(photoOfYouVO);

						//myPhoto.add();
						//myPhoto_id.add(json.get("id").getAsInt());


						myPhoto_type.add(1);
					}
				}
				
				
				if(getAlbumApi.backstage_photos.size()>0){
					for(int i=0;i<getAlbumApi.backstage_photos.size();i++){
					/*	JsonObject json=getAlbumApi.backstage_photos.get(i).getAsJsonObject();

						JsonObject json=getAlbumApi.photos_of_you.get(i).getAsJsonObject();*/
						PhotoOfYouVO photoOfYouVO=new PhotoOfYouVO(getAlbumApi.backstage_photos.get(i).getAsJsonObject());
						photoOfYouVO.setfullPhotoUrl(getActivity().getString(R.string.urlString) + "userdata/image_gallery/" + server_id + "/backstage_photos/" + photoOfYouVO.getPhoto_pic());
						myPhotoVo.add(photoOfYouVO);


						/*myPhoto.add(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+server_id+"/backstage_photos/"+json.get("photo_pic").getAsString());
					myPhoto_id.add(json.get("id").getAsInt());*/

						if(i==0){
					myPhoto_type.add(2);
					}
					else{
						myPhoto_type.add(3);
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

			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				
				super.updateUI();
				p.cancel();
				bindImagesOnScrollView();

			}
			
		};
		HashMap<String, String>keydata=new HashMap<String, String>();
		keydata.put("uuid", pref.get(Constants.KeyUUID));
		keydata.put("auth_token", pref.get(Constants.kAuthT));
		keydata.put("time_stamp", "");
		if(pref.get("images_of").length()>3){
			keydata.put("other_uuid", pref.get("images_of"));	
		//	AddMorePhoto.setVisibility(View.GONE);
			//del_photos.setVisibility(View.GONE);
		}
		else{
			keydata.put("other_uuid", "");
		//	AddMorePhoto.setVisibility(View.VISIBLE);
			//del_photos.setVisibility(View.VISIBLE);
		}
		
		getAlbumApi.setPostData(keydata);
		callApi(getAlbumApi);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
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
	     map.put("other_uuid","");  
	     map.put("time_stamp",""); 
	     map.put("type","1"); 
	    
	   
	     confirm_points_api.setPostData(map);
	     callApi(confirm_points_api);
		
	}
	
	
public void	showPrompt(JsonObject jsn){
		points=Integer.parseInt(jsn.get("points").getAsString());
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
					/* HashMap<String, String>map=new HashMap<String, String>();
				     map.put("uuid", pref.get(Constants.KeyUUID));
				     map.put("auth_token", pref.get(Constants.kAuthT));
				     map.put("other_uuid","");  
				     map.put("time_stamp",""); 
				     map.put("type","1"); 
				     backstage_purchase_status=1;
				     bindImagesOnScrollView();
				     dialog.dismiss();*/

					HashMap<String, String>map=new HashMap<String, String>();
					map.put("uuid", pref.get(Constants.KeyUUID));
					map.put("auth_token", pref.get(Constants.kAuthT));
					map.put("other_uuid",pref.get("images_of"));
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

					((DrawerActivity) getActivity()).displayView(11);
					dialog.dismiss();
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
		final ProgressBar pb=new ProgressBar(getActivity());
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 180);
		
		if(myPhoto_type.get(i)==1){
		parent.setLayoutParams(parent_param);
		parent.setTag(String.valueOf(i));
		parent.setTag(R.id.account_setting_text, i);
		if(i==0){
			animPlaceholder = (AnimationDrawable)_ctx.getResources().getDrawable(R.drawable.image_loading_animation);
			animPlaceholder.start();
			//imageLoader.displayImage(myPhoto.get(0), image);
			Glide.with(_ctx).load(myPhotoVo.get(0).getfullPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(.01f)
					.into(image);
		}
		
		
		
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myPhoto_type.get(Integer.parseInt((String) parent.getTag())) == 1) {

//			pref.set(Constants.SelectedImage, Integer.parseInt((String) parent.getTag()));
//				pref.commit();	
//				((DrawerActivity) getActivity()).displayView(39);

					Glide.with(_ctx).load(myPhotoVo.get(Integer.parseInt((String) parent.getTag())).getfullPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(.01f)
							.into(image);
					p1.setVisibility(View.GONE);

					/*	imageLoader.displayImage(myPhoto.get(Integer.parseInt((String) parent.getTag())), image,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					p1.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					p1.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					p1.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					p1.setVisibility(View.GONE);
				}
			});*/

				}
//			else{
//				if(backstage_purchase_status==0){
//				getPointInfo();
//				}
//				else{
//					((DrawerActivity) getActivity()).displayView(39);
//				}
//			}


//		//	if(myPhoto_type.get(Integer.parseInt((String) parent.getTag()))==1){
//				pref.set(Constants.SelectedImage, Integer.parseInt((String) parent.getTag()));
//				pref.commit();	
//				((DrawerActivity) getActivity()).displayView(39);
//			//}
////			else{
////				if(backstage_purchase_status==0){
////				getPointInfo();
////				}
////				else{
////					((DrawerActivity) getActivity()).displayView(39);
////				}
////			}
			}
		});

		String url=myPhotoVo.get(i).getfullPhotoUrl();


				

		imagView.setScaleType(ScaleType.FIT_XY);

		horizontal_scroll.setOrientation(LinearLayout.HORIZONTAL);

	

		  lp.setMargins(5, 0, 0, 0);
		    imagView.setLayoutParams(lp);
		  //  imagView.setTag(url);
		    imagView.setImageResource(R.drawable.profile);
			pb.setVisibility(View.GONE);
			Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).into(imagView);

		  /*  imageLoader.displayImage(url, imagView, new ImageLoadingListener() {


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
	
			imagView.setScaleType(ScaleType.FIT_XY);
			imagView.setImageResource(R.drawable.lock_backstage);
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


				Log.d("Photo Url", "" + myPhotoVo.get(Integer.parseInt((String) parent.getTag())).getfullPhotoUrl());
				Glide.with(_ctx).load(myPhotoVo.get(Integer.parseInt((String) parent.getTag())).getfullPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(.01f).into(image);

			}
		});

		String url=myPhotoVo.get(i).getfullPhotoUrl();

		Log.d("Photo Url", "" + url);
				

		imagView.setScaleType(ScaleType.FIT_XY);

		horizontal_scroll.setOrientation(LinearLayout.HORIZONTAL);

	

		  lp.setMargins(5, 0, 0, 0);
		    imagView.setLayoutParams(lp);
		    //imagView.setTag(url);
		    imagView.setImageResource(R.drawable.profile);
		pb.setVisibility(View.GONE);
		animPlaceholder = (AnimationDrawable)_ctx.getResources().getDrawable(R.drawable.image_loading_animation);
		animPlaceholder.start();
		Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).into(imagView);

		   /* imageLoader.displayImage(url, imagView, new ImageLoadingListener() {


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
	



	
}


